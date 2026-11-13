package com.enterprise.fashion.ecommerce.identity.application.staff;

import java.security.SecureRandom;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import com.enterprise.fashion.ecommerce.identity.adapter.out.staff.JdbcStaffOidcAuthorizationTransactionAdapter;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.session.jdbc.JdbcIndexedSessionRepository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("integration")
@SpringBootTest(properties = "product.catalogue.cursor-secret=AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
@Testcontainers
class StaffOidcAuthorizationTransactionsIntegrationTest {
    private static final Instant NOW = Instant.parse("2026-11-13T10:15:30Z");
    private static final Duration LIFETIME = Duration.ofMinutes(5);
    private static final Duration TOLERANCE = Duration.ofSeconds(10);
    private static final String REGISTRATION = "synthetic-approved-registration";

    @Container @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:18");

    @Autowired private JdbcClient jdbc;
    @Autowired private JdbcIndexedSessionRepository sessions;
    @Autowired private PlatformTransactionManager transactionManager;
    @Autowired private Flyway flyway;

    @Test
    void migrationStoresOnlyStateDigestAndExactSessionBoundConsumptionIsSingleUse() {
        flyway.validate();
        assertThat(flyway.info().applied()).anySatisfy(migration ->
                assertThat(migration.getVersion().getVersion()).isEqualTo("16"));
        String sessionId = session();
        String otherSessionId = session();
        var service = service(NOW);
        var authentication = UsernamePasswordAuthenticationToken.authenticated(
                "synthetic-customer", null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var beginning = service.begin(sessionId);
        var transaction = beginning.transaction();
        assertThat(beginning.outcome()).isEqualTo(StaffOidcAuthorizationTransactions.BeginOutcome.STARTED);
        assertThat(jdbc.sql("""
                SELECT encode(state_sha256, 'hex') FROM identity.staff_oidc_authorization_transaction
                WHERE transaction_id = :id
                """).param("id", transaction.transactionId()).query(String.class).single())
                .doesNotContain(transaction.state());
        assertThat(service.consume(transaction.state(), otherSessionId).outcome())
                .isEqualTo(StaffOidcAuthorizationTransactions.ConsumeOutcome.REJECTED);
        assertThat(service.consume("A".repeat(43), sessionId).outcome())
                .isEqualTo(StaffOidcAuthorizationTransactions.ConsumeOutcome.REJECTED);

        var consumed = service.consume(transaction.state(), sessionId);

        assertThat(consumed.outcome()).isEqualTo(StaffOidcAuthorizationTransactions.ConsumeOutcome.CONSUMED);
        assertThat(consumed.transaction().status()).isEqualTo(StaffOidcAuthorizationTransaction.Status.CONSUMED);
        assertThat(service.consume(transaction.state(), sessionId).outcome())
                .isEqualTo(StaffOidcAuthorizationTransactions.ConsumeOutcome.REPLAYED);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isSameAs(authentication);
        assertThat(sessionAttributes(sessionId)).isZero();
        SecurityContextHolder.clearContext();
    }

    @Test
    void newerTransactionSupersedesPriorStateWithoutAuthenticatingEitherOne() {
        String sessionId = session();
        var service = service(NOW);
        var first = service.begin(sessionId).transaction();
        var second = service.begin(sessionId).transaction();

        assertThat(service.consume(first.state(), sessionId).outcome())
                .isEqualTo(StaffOidcAuthorizationTransactions.ConsumeOutcome.SUPERSEDED);
        assertThat(service.consume(second.state(), sessionId).outcome())
                .isEqualTo(StaffOidcAuthorizationTransactions.ConsumeOutcome.CONSUMED);
        assertThat(jdbc.sql("""
                SELECT status FROM identity.staff_oidc_authorization_transaction
                WHERE session_id = :sessionId ORDER BY created_at, transaction_id
                """).param("sessionId", sessionId).query(String.class).list())
                .containsExactlyInAnyOrder("SUPERSEDED", "CONSUMED");
    }

    @Test
    void expirationIncludesOnlyTheExplicitClockToleranceBoundary() {
        String sessionId = session();
        var started = service(NOW).begin(sessionId).transaction();

        assertThat(service(NOW.plus(LIFETIME).plus(TOLERANCE)).consume(started.state(), sessionId).outcome())
                .isEqualTo(StaffOidcAuthorizationTransactions.ConsumeOutcome.CONSUMED);

        String anotherSession = session();
        var expired = service(NOW).begin(anotherSession).transaction();
        assertThat(service(NOW.plus(LIFETIME).plus(TOLERANCE).plusNanos(1))
                .consume(expired.state(), anotherSession).outcome())
                .isEqualTo(StaffOidcAuthorizationTransactions.ConsumeOutcome.EXPIRED);
        assertThat(service(NOW).consume(expired.state(), anotherSession).outcome())
                .isEqualTo(StaffOidcAuthorizationTransactions.ConsumeOutcome.EXPIRED);
    }

    @Test
    void concurrentConsumptionAcrossTransactionsAllowsExactlyOneWinner() throws Exception {
        String sessionId = session();
        var service = service(NOW);
        var transaction = service.begin(sessionId).transaction();
        var start = new CountDownLatch(1);
        try (var workers = Executors.newFixedThreadPool(2)) {
            java.util.concurrent.Callable<StaffOidcAuthorizationTransactions.ConsumeOutcome> consume = () -> {
                if (!start.await(10, TimeUnit.SECONDS)) {
                    throw new IllegalStateException("Concurrent test start timed out");
                }
                return service.consume(transaction.state(), sessionId).outcome();
            };
            var first = workers.submit(consume);
            var second = workers.submit(consume);
            start.countDown();
            assertThat(List.of(first.get(20, TimeUnit.SECONDS), second.get(20, TimeUnit.SECONDS)))
                    .containsExactlyInAnyOrder(
                            StaffOidcAuthorizationTransactions.ConsumeOutcome.CONSUMED,
                            StaffOidcAuthorizationTransactions.ConsumeOutcome.REPLAYED);
        }
        assertThat(jdbc.sql("""
                SELECT count(*) FROM identity.staff_oidc_authorization_transaction
                WHERE transaction_id = :id AND status = 'CONSUMED'
                """).param("id", transaction.transactionId()).query(Integer.class).single()).isOne();
    }

    @Test
    void corruptActiveTransactionIsTerminalAndCannotLaterBeConsumed() {
        String sessionId = session();
        var service = service(NOW);
        var transaction = service.begin(sessionId).transaction();
        try {
            jdbc.sql("ALTER TABLE identity.staff_oidc_authorization_transaction DISABLE TRIGGER "
                    + "protect_staff_oidc_authorization_transaction").update();
            jdbc.sql("""
                    UPDATE identity.staff_oidc_authorization_transaction
                    SET pkce_challenge = :corrupt WHERE transaction_id = :id
                    """).param("corrupt", "B".repeat(43)).param("id", transaction.transactionId()).update();
        } finally {
            jdbc.sql("ALTER TABLE identity.staff_oidc_authorization_transaction ENABLE TRIGGER "
                    + "protect_staff_oidc_authorization_transaction").update();
        }

        assertThat(service.consume(transaction.state(), sessionId).outcome())
                .isEqualTo(StaffOidcAuthorizationTransactions.ConsumeOutcome.CORRUPT);
        assertThat(jdbc.sql("""
                SELECT status FROM identity.staff_oidc_authorization_transaction
                WHERE transaction_id = :id
                """).param("id", transaction.transactionId()).query(String.class).single()).isEqualTo("CORRUPT");
        assertThat(service.consume(transaction.state(), sessionId).outcome())
                .isEqualTo(StaffOidcAuthorizationTransactions.ConsumeOutcome.CORRUPT);
    }

    @Test
    void sessionRemovedBeforeConsumptionRejectsAndCascadesTransaction() {
        String sessionId = session();
        var service = service(NOW);
        var transaction = service.begin(sessionId).transaction();

        sessions.deleteById(sessionId);

        assertThat(service.consume(transaction.state(), sessionId).outcome())
                .isEqualTo(StaffOidcAuthorizationTransactions.ConsumeOutcome.REJECTED);
        assertThat(jdbc.sql("""
                SELECT count(*) FROM identity.staff_oidc_authorization_transaction
                WHERE transaction_id = :id
                """).param("id", transaction.transactionId()).query(Integer.class).single()).isZero();
    }

    @Test
    void sessionDeletionRacingConsumptionIsSerializedByPostgresqlSessionLock() throws Exception {
        String sessionId = session();
        var service = service(NOW);
        var authorization = service.begin(sessionId).transaction();
        var rowLocked = new CountDownLatch(1);
        var releaseRow = new CountDownLatch(1);
        try (var workers = Executors.newFixedThreadPool(3)) {
            var locker = workers.submit(() -> transaction().execute(status -> {
                jdbc.sql("""
                        SELECT transaction_id FROM identity.staff_oidc_authorization_transaction
                        WHERE transaction_id = :id FOR UPDATE
                        """).param("id", authorization.transactionId()).query(java.util.UUID.class).single();
                rowLocked.countDown();
                await(releaseRow, "Authorization-row release timed out");
                return null;
            }));
            assertThat(rowLocked.await(10, TimeUnit.SECONDS)).isTrue();

            var consumption = workers.submit(() -> service.consume(authorization.state(), sessionId).outcome());
            awaitBlockedQuery("state_sha256");
            var deletion = workers.submit(() -> transaction().execute(status -> jdbc.sql("""
                    DELETE FROM identity.spring_session WHERE session_id = :sessionId
                    """).param("sessionId", sessionId).update()));
            awaitBlockedQuery("DELETE FROM identity.spring_session");

            releaseRow.countDown();
            assertThat(consumption.get(20, TimeUnit.SECONDS))
                    .isEqualTo(StaffOidcAuthorizationTransactions.ConsumeOutcome.CONSUMED);
            assertThat(deletion.get(20, TimeUnit.SECONDS)).isOne();
            locker.get(20, TimeUnit.SECONDS);
        }
        assertThat(jdbc.sql("""
                SELECT count(*) FROM identity.spring_session WHERE session_id = :sessionId
                """).param("sessionId", sessionId).query(Integer.class).single()).isZero();
        assertThat(jdbc.sql("""
                SELECT count(*) FROM identity.staff_oidc_authorization_transaction
                WHERE transaction_id = :id
                """).param("id", authorization.transactionId()).query(Integer.class).single()).isZero();
    }

    @Test
    void missingServerSideSessionCannotReceiveAuthorizationState() {
        var result = service(NOW).begin("00000000-0000-0000-0000-000000000099");
        assertThat(result.outcome()).isEqualTo(StaffOidcAuthorizationTransactions.BeginOutcome.REJECTED);
        assertThat(result.transaction()).isNull();
    }

    private StaffOidcAuthorizationTransactions service(Instant instant) {
        return new StaffOidcAuthorizationTransactions(
                new JdbcStaffOidcAuthorizationTransactionAdapter(jdbc), transaction(),
                new SecureRandom(), Clock.fixed(instant, ZoneOffset.UTC),
                LIFETIME, TOLERANCE, REGISTRATION);
    }

    private TransactionTemplate transaction() {
        var transaction = new TransactionTemplate(transactionManager);
        transaction.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        return transaction;
    }

    private void awaitBlockedQuery(String queryFragment) {
        Instant deadline = Instant.now().plusSeconds(10);
        while (Instant.now().isBefore(deadline)) {
            boolean blocked = jdbc.sql("""
                    SELECT EXISTS (
                        SELECT 1 FROM pg_stat_activity
                        WHERE pid <> pg_backend_pid()
                          AND wait_event_type = 'Lock'
                          AND position(:fragment in query) > 0
                    )
                    """).param("fragment", queryFragment).query(Boolean.class).single();
            if (blocked) {
                return;
            }
            Thread.onSpinWait();
        }
        throw new AssertionError("Expected blocked PostgreSQL query was not observed");
    }

    private static void await(CountDownLatch latch, String message) {
        try {
            if (!latch.await(10, TimeUnit.SECONDS)) {
                throw new IllegalStateException(message);
            }
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(message, interrupted);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private String session() {
        SessionRepository repository = sessions;
        Session session = repository.createSession();
        repository.save(session);
        return session.getId();
    }

    private int sessionAttributes(String sessionId) {
        return jdbc.sql("""
                SELECT count(*) FROM identity.spring_session_attributes attributes
                JOIN identity.spring_session sessions ON sessions.primary_id = attributes.session_primary_id
                WHERE sessions.session_id = :sessionId
                """).param("sessionId", sessionId).query(Integer.class).single();
    }
}
