package com.enterprise.fashion.ecommerce.identity.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.enterprise.fashion.ecommerce.identity.application.loginemail.AcceptedLoginEmailBinding;
import com.enterprise.fashion.ecommerce.identity.application.loginemail.CurrentLoginEmailObservation;
import com.enterprise.fashion.ecommerce.identity.application.loginemail.CurrentLoginEmailSource;
import com.enterprise.fashion.ecommerce.identity.application.loginemail.LoginEmailPublicationResult;
import com.enterprise.fashion.ecommerce.identity.application.loginemail.SyntheticLoginEmailBindings;
import com.enterprise.fashion.ecommerce.identity.application.port.out.CurrentLoginEmailSourcePort;
import com.enterprise.fashion.ecommerce.identity.domain.model.CredentialSubject;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/** Synthetic facts test mechanics only. No production current-email issuer or verifier exists. */
@Tag("integration")
@SpringBootTest
@Testcontainers
class IdentityLoginEmailSourceIntegrationTest {
    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:18");

    @Autowired private CurrentLoginEmailSource source;
    @Autowired private CurrentLoginEmailSourcePort port;
    @Autowired private JdbcTemplate jdbc;
    @Autowired private PlatformTransactionManager manager;
    @Autowired private Flyway flyway;

    @Test
    void migrationSyntheticFactAndDuplicatePreserveExactBindingAndIdentity() {
        flyway.validate();
        assertThat(flyway.info().applied()).anySatisfy(migration ->
                assertThat(migration.getVersion().getVersion()).isEqualTo("3"));
        var binding = newFact(newSubject(), "a@example.test", null);
        assertThat(source.publish(binding)).isEqualTo(LoginEmailPublicationResult.PUBLISHED);
        assertThat(source.publish(binding)).isEqualTo(LoginEmailPublicationResult.DUPLICATE);
        var current = source.observe(binding.subject(), binding.fact());
        assertThat(current.outcome()).isEqualTo(CurrentLoginEmailObservation.Outcome.CURRENT);
        assertThat(current.boundTo(binding.subject(), binding.fact())).isTrue();
        assertThat(current.email()).isEqualTo(binding.email());
        assertThat(current.toString()).doesNotContain(binding.email());
        assertThat(jdbc.queryForObject("SELECT count(*) FROM identity.login_email_binding WHERE fact = ?",
                Integer.class, binding.fact())).isOne();
        assertThat(source.observe(newSubject(), binding.fact()).outcome())
                .isEqualTo(CurrentLoginEmailObservation.Outcome.UNCERTAIN);
        assertThat(source.publish(SyntheticLoginEmailBindings.fact(binding.fact(), newSubject(),
                binding.email(), null))).isEqualTo(LoginEmailPublicationResult.CONFLICT);
        assertThat(source.publish(SyntheticLoginEmailBindings.fact(binding.fact(), binding.subject(),
                "different@example.test", null))).isEqualTo(LoginEmailPublicationResult.CONFLICT);
    }

    @Test
    void returningToEqualTextNeverReactivatesTheEarlierFact() {
        var subject = newSubject();
        var firstA = newFact(subject, "a@example.test", null);
        var b = newFact(subject, "b@example.test", firstA.fact());
        var secondA = newFact(subject, "a@example.test", b.fact());
        assertThat(source.publish(firstA)).isEqualTo(LoginEmailPublicationResult.PUBLISHED);
        assertThat(source.publish(b)).isEqualTo(LoginEmailPublicationResult.PUBLISHED);
        assertThat(source.publish(secondA)).isEqualTo(LoginEmailPublicationResult.PUBLISHED);
        assertThat(source.observe(subject, firstA.fact()).outcome())
                .isEqualTo(CurrentLoginEmailObservation.Outcome.SUPERSEDED);
        assertThat(source.observe(subject, b.fact()).outcome())
                .isEqualTo(CurrentLoginEmailObservation.Outcome.SUPERSEDED);
        assertThat(source.observe(subject, secondA.fact()).outcome())
                .isEqualTo(CurrentLoginEmailObservation.Outcome.CURRENT);
        assertThat(source.publish(firstA)).isEqualTo(LoginEmailPublicationResult.DUPLICATE);
        assertThat(source.observe(subject, firstA.fact()).outcome())
                .isEqualTo(CurrentLoginEmailObservation.Outcome.SUPERSEDED);
    }

    @Test
    void incompatibleCurrentFactsAndForksHaveNoWinner() {
        var subject = newSubject();
        var first = newFact(subject, "a@example.test", null);
        var unrelated = newFact(subject, "b@example.test", null);
        source.publish(first);
        source.publish(unrelated);
        assertThat(source.observe(subject, first.fact()).outcome())
                .isEqualTo(CurrentLoginEmailObservation.Outcome.CONFLICT);
        assertThat(source.observe(subject, unrelated.fact()).outcome())
                .isEqualTo(CurrentLoginEmailObservation.Outcome.CONFLICT);
        var otherSubject = newSubject();
        var root = newFact(otherSubject, "a@example.test", null);
        source.publish(root);
        var branchOne = newFact(otherSubject, "b@example.test", root.fact());
        var branchTwo = newFact(otherSubject, "c@example.test", root.fact());
        source.publish(branchOne);
        source.publish(branchTwo);
        assertThat(source.observe(otherSubject, branchOne.fact()).outcome())
                .isEqualTo(CurrentLoginEmailObservation.Outcome.CONFLICT);
        assertThat(source.observe(otherSubject, branchTwo.fact()).outcome())
                .isEqualTo(CurrentLoginEmailObservation.Outcome.CONFLICT);
    }

    @Test
    void missingPredecessorOrEmptySourceDoesNotConfirmAbsenceOrVerification() {
        var subject = newSubject();
        var missing = newFact(subject, "a@example.test", UUID.randomUUID());
        assertThat(source.publish(missing)).isEqualTo(LoginEmailPublicationResult.UNCERTAIN);
        assertThat(source.observe(subject, missing.fact()).outcome())
                .isEqualTo(CurrentLoginEmailObservation.Outcome.UNCERTAIN);
        assertThat(jdbc.queryForObject("SELECT count(*) FROM identity.login_email_binding_scope WHERE subject = ?",
                Integer.class, subject.value())).isOne();
        var empty = newSubject();
        assertThat(source.observe(empty, UUID.randomUUID()).outcome())
                .isEqualTo(CurrentLoginEmailObservation.Outcome.UNCERTAIN);
        assertThat(jdbc.queryForObject("SELECT count(*) FROM identity.login_email_binding_scope WHERE subject = ?",
                Integer.class, empty.value())).isZero();
        assertThatThrownBy(() -> port.observe(empty, UUID.randomUUID()))
                .isInstanceOf(IllegalStateException.class).hasMessageNotContaining("@")
                .hasMessageNotContaining(empty.value().toString());
        assertThat(CurrentLoginEmailObservation.Outcome.values()).hasSize(4);
    }

    @Test
    void staleObservationCannotOverrideConcurrentCommittedSupersession() throws Exception {
        var subject = newSubject();
        var first = newFact(subject, "a@example.test", null);
        var replacement = newFact(subject, "b@example.test", first.fact());
        source.publish(first);
        assertThat(source.observe(subject, first.fact()).outcome())
                .isEqualTo(CurrentLoginEmailObservation.Outcome.CURRENT);
        CountDownLatch inserted = new CountDownLatch(1);
        CountDownLatch allowCommit = new CountDownLatch(1);
        CountDownLatch attemptingRead = new CountDownLatch(1);
        try (var executor = Executors.newFixedThreadPool(2)) {
            var writer = executor.submit(() -> transaction().execute(status -> {
                assertThat(port.publish(replacement)).isEqualTo(LoginEmailPublicationResult.PUBLISHED);
                inserted.countDown();
                await(allowCommit);
                return true;
            }));
            try {
                assertThat(inserted.await(10, TimeUnit.SECONDS)).isTrue();
                var reader = executor.submit(() -> {
                    attemptingRead.countDown();
                    return source.observe(subject, first.fact()).outcome();
                });
                assertThat(attemptingRead.await(10, TimeUnit.SECONDS)).isTrue();
                awaitReaderLock();
                allowCommit.countDown();
                assertThat(writer.get(10, TimeUnit.SECONDS)).isTrue();
                assertThat(reader.get(10, TimeUnit.SECONDS))
                        .isEqualTo(CurrentLoginEmailObservation.Outcome.SUPERSEDED);
            } finally {
                allowCommit.countDown();
            }
        }
    }

    @Test
    void databaseFailureAndMalformedStateWithholdCurrentObservation() {
        var binding = newFact(newSubject(), "a@example.test", null);
        source.publish(binding);
        CurrentLoginEmailSourcePort failing = new CurrentLoginEmailSourcePort() {
            @Override
            public LoginEmailPublicationResult publish(AcceptedLoginEmailBinding fact) {
                jdbc.execute("SELECT 1 / 0");
                return port.publish(fact);
            }
            @Override
            public CurrentLoginEmailObservation observe(CredentialSubject subject, UUID fact) {
                jdbc.execute("SELECT 1 / 0");
                return port.observe(subject, fact);
            }
        };
        var failed = new CurrentLoginEmailSource(failing, transaction());
        assertThat(failed.observe(binding.subject(), binding.fact()).outcome())
                .isEqualTo(CurrentLoginEmailObservation.Outcome.UNCERTAIN);
        assertThat(failed.publish(binding)).isEqualTo(LoginEmailPublicationResult.UNCERTAIN);
        // Privileged corruption is not a lawful production issuer. Even malformed rows fail closed.
        jdbc.update("UPDATE identity.login_email_binding SET email = ? WHERE fact = ?", " ", binding.fact());
        assertThat(source.observe(binding.subject(), binding.fact()).outcome())
                .isEqualTo(CurrentLoginEmailObservation.Outcome.UNCERTAIN);
    }

    @Test
    void rolledBackPublicationLeavesNoCurrentFact() {
        var binding = newFact(newSubject(), "a@example.test", null);
        transaction().executeWithoutResult(status -> {
            assertThat(port.publish(binding)).isEqualTo(LoginEmailPublicationResult.PUBLISHED);
            status.setRollbackOnly();
        });
        assertThat(source.observe(binding.subject(), binding.fact()).outcome())
                .isEqualTo(CurrentLoginEmailObservation.Outcome.UNCERTAIN);
    }

    private void awaitReaderLock() {
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
        while (System.nanoTime() < deadline) {
            Boolean waiting = jdbc.queryForObject("""
                    SELECT EXISTS (SELECT 1 FROM pg_stat_activity
                        WHERE datname = current_database() AND wait_event_type = 'Lock'
                          AND query LIKE '%login_email_binding_scope%'
                          AND cardinality(pg_blocking_pids(pid)) > 0)
                    """, Boolean.class);
            if (Boolean.TRUE.equals(waiting)) {
                return;
            }
            Thread.yield();
        }
        throw new AssertionError("Reader did not reach the PostgreSQL coordination lock");
    }

    private TransactionTemplate transaction() {
        var transaction = new TransactionTemplate(manager);
        transaction.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transaction.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return transaction;
    }

    private static AcceptedLoginEmailBinding newFact(CredentialSubject subject, String email, UUID supersedes) {
        return SyntheticLoginEmailBindings.fact(UUID.randomUUID(), subject, email, supersedes);
    }

    private static CredentialSubject newSubject() {
        return new CredentialSubject(UUID.randomUUID());
    }

    private static void await(CountDownLatch latch) {
        try {
            if (!latch.await(10, TimeUnit.SECONDS)) {
                throw new AssertionError("Timed out waiting for concurrent source effect");
            }
        } catch (InterruptedException failure) {
            Thread.currentThread().interrupt();
            throw new AssertionError("Interrupted while waiting for source effect", failure);
        }
    }
}
