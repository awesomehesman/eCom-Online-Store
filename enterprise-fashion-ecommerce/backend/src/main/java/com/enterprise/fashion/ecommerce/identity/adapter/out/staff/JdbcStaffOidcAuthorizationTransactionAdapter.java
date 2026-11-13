package com.enterprise.fashion.ecommerce.identity.adapter.out.staff;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import com.enterprise.fashion.ecommerce.identity.application.port.out.StaffOidcAuthorizationTransactionPort;
import com.enterprise.fashion.ecommerce.identity.application.staff.StaffOidcAuthorizationTransaction;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/** PostgreSQL coordination for dormant Staff OIDC transactions; no provider or login is configured. */
public final class JdbcStaffOidcAuthorizationTransactionAdapter
        implements StaffOidcAuthorizationTransactionPort {
    private final JdbcClient jdbc;

    public JdbcStaffOidcAuthorizationTransactionAdapter(JdbcClient jdbc) {
        this.jdbc = Objects.requireNonNull(jdbc);
    }

    @Override
    public BeginOutcome supersedeAndStore(StaffOidcAuthorizationTransaction transaction) {
        requireTransaction();
        Objects.requireNonNull(transaction);
        try {
            boolean sessionExists = jdbc.sql("""
                    SELECT session_id FROM identity.spring_session
                    WHERE session_id = :sessionId FOR UPDATE
                    """).param("sessionId", transaction.initiatingSessionId())
                    .query(String.class).optional().isPresent();
            if (!sessionExists) {
                return BeginOutcome.SESSION_UNAVAILABLE;
            }
            Instant supersededAt = transaction.createdAt();
            jdbc.sql("""
                    UPDATE identity.staff_oidc_authorization_transaction
                    SET status = 'SUPERSEDED', superseded_at = :supersededAt
                    WHERE session_id = :sessionId AND registration_id = :registrationId
                      AND status = 'ACTIVE'
                    """).param("supersededAt", Timestamp.from(supersededAt))
                    .param("sessionId", transaction.initiatingSessionId())
                    .param("registrationId", transaction.registrationId()).update();
            jdbc.sql("""
                    INSERT INTO identity.staff_oidc_authorization_transaction
                        (transaction_id, state_sha256, nonce, pkce_verifier, pkce_challenge,
                         session_id, registration_id, created_at, expires_at, status)
                    VALUES
                        (:transactionId, :stateSha256, :nonce, :pkceVerifier, :pkceChallenge,
                         :sessionId, :registrationId, :createdAt, :expiresAt, 'ACTIVE')
                    """).param("transactionId", transaction.transactionId())
                    .param("stateSha256", digest(transaction.state()))
                    .param("nonce", transaction.nonce())
                    .param("pkceVerifier", transaction.pkceVerifier())
                    .param("pkceChallenge", transaction.pkceChallenge())
                    .param("sessionId", transaction.initiatingSessionId())
                    .param("registrationId", transaction.registrationId())
                    .param("createdAt", Timestamp.from(transaction.createdAt()))
                    .param("expiresAt", Timestamp.from(transaction.expiresAt())).update();
            return BeginOutcome.STORED;
        } catch (DataAccessException unavailable) {
            throw new StoreUnavailableException("Staff OIDC transaction storage unavailable", unavailable);
        }
    }

    @Override
    public Consumption consume(String state, String sessionId, String registrationId,
            Instant now, Duration clockTolerance) {
        requireTransaction();
        Objects.requireNonNull(state);
        Objects.requireNonNull(sessionId);
        Objects.requireNonNull(registrationId);
        Objects.requireNonNull(now);
        Objects.requireNonNull(clockTolerance);
        try {
            boolean sessionExists = jdbc.sql("""
                    SELECT session_id FROM identity.spring_session
                    WHERE session_id = :sessionId FOR KEY SHARE
                    """).param("sessionId", sessionId).query(String.class).optional().isPresent();
            if (!sessionExists) {
                return rejected(ConsumeOutcome.REJECTED);
            }
            Optional<Row> found = jdbc.sql("""
                    SELECT transaction_id, nonce, pkce_verifier, pkce_challenge, session_id,
                           registration_id, created_at, expires_at, status
                    FROM identity.staff_oidc_authorization_transaction
                    WHERE state_sha256 = :stateSha256
                    FOR UPDATE
                    """).param("stateSha256", digest(state)).query((result, rowNumber) -> new Row(
                            result.getObject("transaction_id", UUID.class),
                            result.getString("nonce"),
                            result.getString("pkce_verifier"),
                            result.getString("pkce_challenge"),
                            result.getString("session_id").trim(),
                            result.getString("registration_id"),
                            result.getTimestamp("created_at").toInstant(),
                            result.getTimestamp("expires_at").toInstant(),
                            result.getString("status"))).optional();
            if (found.isEmpty()) {
                return rejected(ConsumeOutcome.REJECTED);
            }
            Row row = found.get();
            if (!row.sessionId().equals(sessionId) || !row.registrationId().equals(registrationId)) {
                return rejected(ConsumeOutcome.REJECTED);
            }
            if (row.status().equals("CONSUMED")) {
                return rejected(ConsumeOutcome.REPLAYED);
            }
            if (row.status().equals("SUPERSEDED")) {
                return rejected(ConsumeOutcome.SUPERSEDED);
            }
            if (row.status().equals("EXPIRED")) {
                return rejected(ConsumeOutcome.EXPIRED);
            }
            if (row.status().equals("CORRUPT")) {
                return rejected(ConsumeOutcome.CORRUPT);
            }
            if (!row.status().equals("ACTIVE")) {
                return rejected(ConsumeOutcome.CORRUPT);
            }
            StaffOidcAuthorizationTransaction active;
            try {
                active = row.transaction(state, StaffOidcAuthorizationTransaction.Status.ACTIVE);
            } catch (IllegalArgumentException corrupt) {
                transition(row.transactionId(), "CORRUPT", "corrupt_at", now);
                return rejected(ConsumeOutcome.CORRUPT);
            }
            if (now.plus(clockTolerance).isBefore(row.createdAt())) {
                transition(row.transactionId(), "CORRUPT", "corrupt_at", now);
                return rejected(ConsumeOutcome.CORRUPT);
            }
            if (now.isAfter(row.expiresAt().plus(clockTolerance))) {
                transition(row.transactionId(), "EXPIRED", "expired_at", now);
                return rejected(ConsumeOutcome.EXPIRED);
            }
            transition(row.transactionId(), "CONSUMED", "consumed_at", now);
            return new Consumption(ConsumeOutcome.CONSUMED,
                    active.withStatus(StaffOidcAuthorizationTransaction.Status.CONSUMED));
        } catch (DataAccessException unavailable) {
            throw new StoreUnavailableException("Staff OIDC transaction storage unavailable", unavailable);
        }
    }

    private void transition(UUID transactionId, String status, String timestampColumn, Instant at) {
        String sql = "UPDATE identity.staff_oidc_authorization_transaction SET status = :status, "
                + timestampColumn + " = :at WHERE transaction_id = :id AND status = 'ACTIVE'";
        int updated = jdbc.sql(sql).param("status", status).param("at", Timestamp.from(at))
                .param("id", transactionId).update();
        if (updated != 1) {
            throw new StoreUnavailableException("Staff OIDC transaction transition unavailable", null);
        }
    }

    private static Consumption rejected(ConsumeOutcome outcome) {
        return new Consumption(outcome, null);
    }

    private static byte[] digest(String value) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.US_ASCII));
        } catch (NoSuchAlgorithmException impossibleOnJava21) {
            throw new IllegalStateException("Required SHA-256 implementation unavailable", impossibleOnJava21);
        }
    }

    private static void requireTransaction() {
        if (!TransactionSynchronizationManager.isActualTransactionActive()
                || !Integer.valueOf(TransactionDefinition.ISOLATION_READ_COMMITTED).equals(
                        TransactionSynchronizationManager.getCurrentTransactionIsolationLevel())) {
            throw new IllegalStateException("Staff OIDC transaction requires its application transaction");
        }
    }

    private record Row(UUID transactionId, String nonce, String pkceVerifier, String pkceChallenge,
            String sessionId, String registrationId, Instant createdAt, Instant expiresAt, String status) {
        StaffOidcAuthorizationTransaction transaction(String state,
                StaffOidcAuthorizationTransaction.Status lifecycle) {
            return new StaffOidcAuthorizationTransaction(transactionId, state, nonce, pkceVerifier,
                    pkceChallenge, sessionId, registrationId, createdAt, expiresAt, lifecycle);
        }
    }
}
