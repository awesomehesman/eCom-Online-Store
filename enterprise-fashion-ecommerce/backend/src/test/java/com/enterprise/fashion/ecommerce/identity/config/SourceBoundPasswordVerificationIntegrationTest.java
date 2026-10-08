package com.enterprise.fashion.ecommerce.identity.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.enterprise.fashion.ecommerce.identity.application.credential.AcceptedCredentialPublication;
import com.enterprise.fashion.ecommerce.identity.application.credential.CredentialPublicationResult;
import com.enterprise.fashion.ecommerce.identity.application.credential.CredentialSource;
import com.enterprise.fashion.ecommerce.identity.application.credential.SourceBoundPasswordVerificationResult;
import com.enterprise.fashion.ecommerce.identity.application.credential.SyntheticCredentialPublications;
import com.enterprise.fashion.ecommerce.identity.application.credential.VerifySourceBoundCustomerPassword;
import com.enterprise.fashion.ecommerce.identity.application.port.out.CustomerPasswordVerificationPort;
import com.enterprise.fashion.ecommerce.identity.domain.model.CredentialSubject;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/** Synthetic publication tests mechanics; no production establishment or Authentication is asserted. */
@Tag("integration")
@SpringBootTest
@Testcontainers
class SourceBoundPasswordVerificationIntegrationTest {
    private static final String PASSWORD = "  É\t ";
    private static final String VERIFIER = new Argon2PasswordEncoder(16, 32, 4, 65_536, 3).encode(PASSWORD);

    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:18");

    @Autowired private CredentialSource source;
    @Autowired private VerifySourceBoundCustomerPassword service;
    @Autowired private CustomerPasswordVerificationPort passwords;
    @Autowired private JdbcTemplate jdbc;

    @Test
    void realArgon2AndSourceVerifyNormalizedShortPasswordWithoutEstablishmentPolicy() {
        var publication = publish(null, null);
        assertThat(service.verify(publication.subject(), publication.fact(), "  E\u0301\t "))
                .isEqualTo(SourceBoundPasswordVerificationResult.VERIFIED);
        assertThat(service.verify(publication.subject(), publication.fact(), "incorrect"))
                .isEqualTo(SourceBoundPasswordVerificationResult.NO_MATCH);
        assertThat(jdbc.queryForObject("SELECT verifier FROM identity.credential_publication WHERE fact = ?",
                String.class, publication.fact())).isEqualTo(VERIFIER).doesNotContain(PASSWORD);
        assertThat(jdbc.queryForObject("SELECT count(*) FROM identity.spring_session", Integer.class)).isZero();
    }

    @Test
    void absentWrongSubjectWrongPublicationAndUnsupportedRowsNeverInvokeArgon2() {
        var publication = publish(null, null);
        AtomicInteger calls = new AtomicInteger();
        var recording = new VerifySourceBoundCustomerPassword(source, (password, verifier) -> {
            calls.incrementAndGet();
            return passwords.verify(password, verifier);
        });
        assertThat(recording.verify(new CredentialSubject(UUID.randomUUID()), publication.fact(), PASSWORD))
                .isEqualTo(SourceBoundPasswordVerificationResult.SOURCE_UNCERTAIN);
        assertThat(recording.verify(publication.subject(), UUID.randomUUID(), PASSWORD))
                .isEqualTo(SourceBoundPasswordVerificationResult.SOURCE_UNCERTAIN);
        jdbc.update("UPDATE identity.credential_publication SET verifier = ? WHERE fact = ?", "unsupported", publication.fact());
        assertThat(recording.verify(publication.subject(), publication.fact(), PASSWORD))
                .isEqualTo(SourceBoundPasswordVerificationResult.SOURCE_UNCERTAIN);
        assertThat(calls).hasValue(0);
    }

    @Test
    void supersessionCommitsDuringPasswordWorkAndFinalRevalidationWithholdsSuccess() throws Exception {
        var original = publish(null, null);
        var replacement = SyntheticCredentialPublications.fact(UUID.randomUUID(), original.subject(), VERIFIER, original.fact());
        CountDownLatch comparing = new CountDownLatch(1);
        CountDownLatch finishComparison = new CountDownLatch(1);
        var controlled = new VerifySourceBoundCustomerPassword(source, (password, verifier) -> {
            assertThat(TransactionSynchronizationManager.isActualTransactionActive()).isFalse();
            comparing.countDown();
            await(finishComparison);
            return passwords.verify(password, verifier);
        });
        try (var executor = Executors.newFixedThreadPool(2)) {
            var verification = executor.submit(() -> controlled.verify(original.subject(), original.fact(), PASSWORD));
            try {
                assertThat(comparing.await(10, TimeUnit.SECONDS)).isTrue();
                var publication = executor.submit(() -> source.publish(replacement));
                // Completes BEFORE password work is released: neither DB lock nor transaction spans it.
                assertThat(publication.get(10, TimeUnit.SECONDS)).isEqualTo(CredentialPublicationResult.PUBLISHED);
            } finally {
                finishComparison.countDown();
            }
            assertThat(verification.get(10, TimeUnit.SECONDS)).isEqualTo(SourceBoundPasswordVerificationResult.SOURCE_NON_CURRENT);
        }
        assertThat(source.publish(original)).isEqualTo(CredentialPublicationResult.DUPLICATE);
        assertThat(service.verify(original.subject(), original.fact(), PASSWORD))
                .isEqualTo(SourceBoundPasswordVerificationResult.SOURCE_NON_CURRENT);
    }

    @Test
    void conflictAppearingDuringComparisonCannotTurnMatchIntoSuccess() {
        var original = publish(null, null);
        var controlled = new VerifySourceBoundCustomerPassword(source, (password, verifier) -> {
            assertThat(TransactionSynchronizationManager.isActualTransactionActive()).isFalse();
            publish(original.subject(), null);
            return passwords.verify(password, verifier);
        });
        assertThat(controlled.verify(original.subject(), original.fact(), PASSWORD))
                .isEqualTo(SourceBoundPasswordVerificationResult.SOURCE_CONFLICT);
    }

    @Test
    void changedVerifierUnderSameFactCannotReuseEarlierMatch() {
        var original = publish(null, null);
        var controlled = new VerifySourceBoundCustomerPassword(source, (password, verifier) -> {
            // Simulate detectable privileged corruption, not an authorized production write path.
            jdbc.update("UPDATE identity.credential_publication SET verifier = ? WHERE fact = ?",
                    SyntheticCredentialPublications.OTHER_VERIFIER, original.fact());
            return passwords.verify(password, verifier);
        });
        assertThat(controlled.verify(original.subject(), original.fact(), PASSWORD))
                .isEqualTo(SourceBoundPasswordVerificationResult.SOURCE_UNCERTAIN);
    }

    private AcceptedCredentialPublication publish(CredentialSubject subject, UUID supersedes) {
        var publication = SyntheticCredentialPublications.fact(UUID.randomUUID(),
                subject == null ? new CredentialSubject(UUID.randomUUID()) : subject, VERIFIER, supersedes);
        assertThat(source.publish(publication)).isEqualTo(CredentialPublicationResult.PUBLISHED);
        return publication;
    }

    private static void await(CountDownLatch latch) {
        try {
            if (!latch.await(10, TimeUnit.SECONDS)) {
                throw new IllegalStateException("Test coordination timed out");
            }
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Test interrupted");
        }
    }
}
