package com.enterprise.fashion.ecommerce.identity.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.enterprise.fashion.ecommerce.identity.application.credential.AcceptedCredentialPublication;
import com.enterprise.fashion.ecommerce.identity.application.credential.CredentialEvidence;
import com.enterprise.fashion.ecommerce.identity.application.credential.CredentialPublicationResult;
import com.enterprise.fashion.ecommerce.identity.application.credential.CredentialSource;
import com.enterprise.fashion.ecommerce.identity.application.credential.SyntheticCredentialPublications;
import com.enterprise.fashion.ecommerce.identity.application.port.out.CredentialSourcePort;
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

/** Synthetic capabilities prove persistence mechanics, never lawful production establishment. */
@Tag("integration")
@SpringBootTest
@Testcontainers
class IdentityCredentialSourceIntegrationTest {
    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:18");

    @Autowired private CredentialSource source;
    @Autowired private CredentialSourcePort port;
    @Autowired private JdbcTemplate jdbc;
    @Autowired private PlatformTransactionManager manager;
    @Autowired private Flyway flyway;

    @Test
    void migrationAndSyntheticPublicationPreserveBindingAndDuplicates() {
        flyway.validate();
        assertThat(flyway.info().applied()).anySatisfy(migration ->
                assertThat(migration.getVersion().getVersion()).isEqualTo("2"));
        var publication = synthetic(null);
        assertThat(source.publish(publication)).isEqualTo(CredentialPublicationResult.PUBLISHED);
        assertThat(source.publish(publication)).isEqualTo(CredentialPublicationResult.DUPLICATE);
        assertThat(source.confirm(publication.subject(), publication.fact()))
                .isEqualTo(CredentialEvidence.CONFIRMED_APPLICABLE);
        assertThat(jdbc.queryForObject("SELECT verifier FROM identity.credential_publication WHERE fact = ?",
                String.class, publication.fact())).isEqualTo(publication.verifier());
        assertThat(jdbc.queryForObject("SELECT count(*) FROM identity.credential_publication WHERE fact = ?",
                Integer.class, publication.fact())).isOne();
        var mismatched = SyntheticCredentialPublications.fact(publication.fact(), publication.subject(),
                SyntheticCredentialPublications.OTHER_VERIFIER, null);
        assertThat(source.publish(mismatched)).isEqualTo(CredentialPublicationResult.CONFLICT);
        var otherSubject = new CredentialSubject(UUID.randomUUID());
        assertThat(source.confirm(otherSubject, publication.fact())).isEqualTo(CredentialEvidence.UNCERTAIN);
        assertThat(source.publish(SyntheticCredentialPublications.fact(publication.fact(), otherSubject,
                publication.verifier(), null))).isEqualTo(CredentialPublicationResult.CONFLICT);
    }

    @Test
    void supersessionIsAttributableAndReplayCannotRestorePriorApplicability() {
        var old = synthetic(null);
        source.publish(old);
        var replacement = SyntheticCredentialPublications.fact(UUID.randomUUID(), old.subject(),
                SyntheticCredentialPublications.OTHER_VERIFIER, old.fact());
        assertThat(source.publish(replacement)).isEqualTo(CredentialPublicationResult.PUBLISHED);
        assertThat(source.confirm(old.subject(), old.fact())).isEqualTo(CredentialEvidence.NON_CURRENT);
        assertThat(source.publish(old)).isEqualTo(CredentialPublicationResult.DUPLICATE);
        assertThat(source.confirm(old.subject(), old.fact())).isEqualTo(CredentialEvidence.NON_CURRENT);
        assertThat(source.confirm(old.subject(), replacement.fact()))
                .isEqualTo(CredentialEvidence.CONFIRMED_APPLICABLE);
    }

    @Test
    void unrelatedFactsConflictWithoutArrivalOrderWinner() {
        var first = synthetic(null);
        source.publish(first);
        var second = SyntheticCredentialPublications.fact(UUID.randomUUID(), first.subject(), SyntheticCredentialPublications.OTHER_VERIFIER, null);
        source.publish(second);
        assertThat(source.confirm(first.subject(), first.fact())).isEqualTo(CredentialEvidence.CONFLICT);
        assertThat(source.confirm(first.subject(), second.fact())).isEqualTo(CredentialEvidence.CONFLICT);
    }

    @Test
    void duplicateCannotChangeItsSupersessionBindingAndForksHaveNoWinner() {
        var original = synthetic(null);
        source.publish(original);
        var first = SyntheticCredentialPublications.fact(UUID.randomUUID(), original.subject(),
                SyntheticCredentialPublications.OTHER_VERIFIER, original.fact());
        source.publish(first);
        var changedRelationship = SyntheticCredentialPublications.fact(first.fact(), first.subject(),
                first.verifier(), null);
        assertThat(source.publish(changedRelationship)).isEqualTo(CredentialPublicationResult.CONFLICT);
        var fork = SyntheticCredentialPublications.fact(UUID.randomUUID(), original.subject(),
                SyntheticCredentialPublications.OTHER_VERIFIER, original.fact());
        source.publish(fork);
        assertThat(source.confirm(original.subject(), original.fact())).isEqualTo(CredentialEvidence.NON_CURRENT);
        assertThat(source.confirm(first.subject(), first.fact())).isEqualTo(CredentialEvidence.CONFLICT);
        assertThat(source.confirm(fork.subject(), fork.fact())).isEqualTo(CredentialEvidence.CONFLICT);
    }

    @Test
    void missingOrUnrelatedPredecessorNeverBecomesAcceptedSupersession() {
        var missing = synthetic(UUID.randomUUID());
        assertThat(source.publish(missing)).isEqualTo(CredentialPublicationResult.UNCERTAIN);
        assertThat(source.confirm(missing.subject(), missing.fact())).isEqualTo(CredentialEvidence.UNCERTAIN);
        var other = synthetic(null);
        source.publish(other);
        var wrongSubject = synthetic(other.fact());
        assertThat(source.publish(wrongSubject)).isEqualTo(CredentialPublicationResult.CONFLICT);
        assertThat(source.confirm(wrongSubject.subject(), wrongSubject.fact()))
                .isEqualTo(CredentialEvidence.UNCERTAIN);
    }

    @Test
    void emptyObservationDoesNotCreateSourceOrConfirmAbsence() {
        var subject = new CredentialSubject(UUID.randomUUID());
        assertThat(source.confirm(subject, UUID.randomUUID())).isEqualTo(CredentialEvidence.UNCERTAIN);
        assertThat(jdbc.queryForObject("SELECT count(*) FROM identity.credential_source_scope WHERE subject = ?",
                Integer.class, subject.value())).isZero();
        assertThatThrownBy(() -> port.observe(subject, UUID.randomUUID()))
                .isInstanceOf(IllegalStateException.class).hasMessageNotContaining(subject.value().toString());
    }

    @Test
    void earlierObservationCannotBeReusedAfterConcurrentConfirmedSyntheticEffect() throws Exception {
        var old = synthetic(null);
        source.publish(old);
        assertThat(source.confirm(old.subject(), old.fact())).isEqualTo(CredentialEvidence.CONFIRMED_APPLICABLE);
        var replacement = SyntheticCredentialPublications.fact(UUID.randomUUID(), old.subject(), SyntheticCredentialPublications.OTHER_VERIFIER, old.fact());
        CountDownLatch inserted = new CountDownLatch(1);
        CountDownLatch allowCommit = new CountDownLatch(1);
        CountDownLatch attemptingRead = new CountDownLatch(1);
        try (var executor = Executors.newFixedThreadPool(2)) {
            var writer = executor.submit(() -> transaction().execute(status -> {
                assertThat(port.publish(replacement)).isEqualTo(CredentialPublicationResult.PUBLISHED);
                inserted.countDown();
                await(allowCommit);
                return true;
            }));
            try {
                assertThat(inserted.await(10, TimeUnit.SECONDS)).isTrue();
                var reader = executor.submit(() -> {
                    attemptingRead.countDown();
                    return source.confirm(old.subject(), old.fact());
                });
                assertThat(attemptingRead.await(10, TimeUnit.SECONDS)).isTrue();
                awaitReaderLock();
                allowCommit.countDown();
                assertThat(writer.get(10, TimeUnit.SECONDS)).isTrue();
                assertThat(reader.get(10, TimeUnit.SECONDS)).isEqualTo(CredentialEvidence.NON_CURRENT);
                assertThat(source.publish(old)).isEqualTo(CredentialPublicationResult.DUPLICATE);
                assertThat(source.confirm(old.subject(), old.fact())).isEqualTo(CredentialEvidence.NON_CURRENT);
            } finally {
                allowCommit.countDown();
            }
        }
    }

    @Test
    void failedDatabaseAccessWithholdsEvidence() {
        // A transaction-local statement failure exercises the actual Adapter failure path.
        var publication = synthetic(null);
        source.publish(publication);
        CredentialSourcePort failing = new CredentialSourcePort() {
            @Override
            public CredentialPublicationResult publish(AcceptedCredentialPublication fact) {
                jdbc.execute("SELECT 1 / 0");
                return port.publish(fact);
            }
            @Override
            public Observation observe(CredentialSubject subject, UUID fact) {
                jdbc.execute("SELECT 1 / 0");
                return port.observe(subject, fact);
            }
        };
        var service = new CredentialSource(failing, transaction());
        assertThat(service.confirm(publication.subject(), publication.fact()))
                .isEqualTo(CredentialEvidence.UNCERTAIN);
        assertThat(service.publish(publication)).isEqualTo(CredentialPublicationResult.UNCERTAIN);
    }

    @Test
    void unsupportedProfileAndAmbientTransactionWithholdConfirmation() {
        var unsupported = SyntheticCredentialPublications.fact(UUID.randomUUID(),
                new CredentialSubject(UUID.randomUUID()), "unsupported", null);
        assertThat(source.publish(unsupported)).isEqualTo(CredentialPublicationResult.UNCERTAIN);
        var publication = synthetic(null);
        source.publish(publication);
        transaction().executeWithoutResult(status -> {
            assertThat(source.confirm(publication.subject(), publication.fact()))
                    .isEqualTo(CredentialEvidence.UNCERTAIN);
            assertThat(source.publish(publication)).isEqualTo(CredentialPublicationResult.UNCERTAIN);
        });
        // Simulated damaged privileged storage is not accepted merely because its row exists.
        // Identically forged *valid* rows cannot be recognized: write/access controls remain required.
        jdbc.update("UPDATE identity.credential_publication SET verifier = ? WHERE fact = ?",
                "unsupported", publication.fact());
        assertThat(source.confirm(publication.subject(), publication.fact()))
                .isEqualTo(CredentialEvidence.UNCERTAIN);
    }

    @Test
    void failedPublicationTransactionRollsBackBothSlotAndFact() {
        var publication = synthetic(null);
        transaction().executeWithoutResult(status -> {
            assertThat(port.publish(publication)).isEqualTo(CredentialPublicationResult.PUBLISHED);
            status.setRollbackOnly();
        });
        assertThat(source.confirm(publication.subject(), publication.fact()))
                .isEqualTo(CredentialEvidence.UNCERTAIN);
        assertThat(jdbc.queryForObject("SELECT count(*) FROM identity.credential_source_scope WHERE subject = ?",
                Integer.class, publication.subject().value())).isZero();
    }

    private void awaitReaderLock() {
        // Synchronize on an observed PostgreSQL lock wait, not a scheduling delay or sleep.
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
        while (System.nanoTime() < deadline) {
            Boolean waiting = jdbc.queryForObject("""
                    SELECT EXISTS (SELECT 1 FROM pg_stat_activity
                        WHERE datname = current_database() AND wait_event_type = 'Lock'
                          AND query LIKE '%credential_source_scope%'
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

    private static AcceptedCredentialPublication synthetic(UUID supersedes) {
        return SyntheticCredentialPublications.fact(UUID.randomUUID(),
                new CredentialSubject(UUID.randomUUID()), SyntheticCredentialPublications.VERIFIER, supersedes);
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
