package com.enterprise.fashion.ecommerce.identity.application.staff;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Base64;
import com.enterprise.fashion.ecommerce.identity.application.port.out.StaffOidcAuthorizationTransactionPort;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.TransactionSystemException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StaffOidcAuthorizationTransactionsTest {
    private static final Instant NOW = Instant.parse("2026-11-13T10:15:30Z");
    private static final String SESSION = "00000000-0000-0000-0000-000000000001";
    private static final String REGISTRATION = "approved-registration-reference";
    private static final TransactionOperations TRANSACTIONS = new TransactionOperations() {
        @Override public <T> T execute(TransactionCallback<T> callback) {
            return callback.doInTransaction(null);
        }
    };

    @AfterEach void clearSecurityContext() { SecurityContextHolder.clearContext(); }

    @Test
    void beginGeneratesIndependentOpaqueValuesAndStandardsCompliantS256Challenge() throws Exception {
        var store = new InMemoryStore();
        var service = service(store);

        var beginning = service.begin(SESSION);

        assertThat(beginning.outcome()).isEqualTo(StaffOidcAuthorizationTransactions.BeginOutcome.STARTED);
        var transaction = beginning.transaction();
        assertThat(transaction.state()).hasSize(43);
        assertThat(transaction.nonce()).hasSize(43);
        assertThat(transaction.pkceVerifier()).hasSize(43);
        assertThat(java.util.List.of(transaction.state(), transaction.nonce(), transaction.pkceVerifier()))
                .doesNotHaveDuplicates();
        String expected = Base64.getUrlEncoder().withoutPadding().encodeToString(
                MessageDigest.getInstance("SHA-256")
                        .digest(transaction.pkceVerifier().getBytes(StandardCharsets.US_ASCII)));
        assertThat(transaction.pkceChallenge()).isEqualTo(expected).doesNotContain("=");
        assertThat(transaction.registrationId()).isEqualTo(REGISTRATION);
        assertThat(transaction.initiatingSessionId()).isEqualTo(SESSION);
        assertThat(transaction.createdAt()).isEqualTo(NOW);
        assertThat(transaction.expiresAt()).isEqualTo(NOW.plusSeconds(300));
    }

    @Test
    void diagnosticsNeverExposeProtocolSecrets() {
        var beginning = service(new InMemoryStore()).begin(SESSION);
        var transaction = beginning.transaction();

        assertThat(transaction.toString()).doesNotContain(transaction.state(), transaction.nonce(),
                transaction.pkceVerifier(), transaction.pkceChallenge(), transaction.initiatingSessionId());
        assertThat(beginning.toString()).doesNotContain(transaction.state(), transaction.nonce(),
                transaction.pkceVerifier(), transaction.pkceChallenge());
    }

    @Test
    void malformedStateAndSessionAreRejectedWithoutConsultingStorage() {
        var store = new InMemoryStore();
        var service = service(store);

        assertThat(service.begin(" ").outcome()).isEqualTo(StaffOidcAuthorizationTransactions.BeginOutcome.REJECTED);
        assertThat(service.consume("short", SESSION).outcome())
                .isEqualTo(StaffOidcAuthorizationTransactions.ConsumeOutcome.REJECTED);
        assertThat(service.consume("A".repeat(43), "bad\nsession").outcome())
                .isEqualTo(StaffOidcAuthorizationTransactions.ConsumeOutcome.REJECTED);
        assertThat(store.calls).isZero();
    }

    @Test
    void unavailableStorageCannotStartOrConsumeAuthenticationState() {
        StaffOidcAuthorizationTransactionPort unavailable = new InMemoryStore() {
            @Override public BeginOutcome supersedeAndStore(StaffOidcAuthorizationTransaction transaction) {
                throw new StoreUnavailableException("synthetic unavailable", null);
            }
            @Override public Consumption consume(String state, String sessionId, String registrationId,
                    Instant observedAt, Duration tolerance) {
                throw new StoreUnavailableException("synthetic unavailable", null);
            }
        };
        var service = service(unavailable);

        assertThat(service.begin(SESSION).outcome())
                .isEqualTo(StaffOidcAuthorizationTransactions.BeginOutcome.UNAVAILABLE);
        assertThat(service.consume("A".repeat(43), SESSION).outcome())
                .isEqualTo(StaffOidcAuthorizationTransactions.ConsumeOutcome.UNAVAILABLE);
    }

    @Test
    void transactionFailureCannotReturnStartedOrConsumedState() {
        TransactionOperations failingTransactions = new TransactionOperations() {
            @Override public <T> T execute(TransactionCallback<T> callback) {
                callback.doInTransaction(null);
                throw new TransactionSystemException("synthetic commit failure");
            }
        };
        var service = new StaffOidcAuthorizationTransactions(new InMemoryStore(), failingTransactions,
                new SecureRandom(), Clock.fixed(NOW, ZoneOffset.UTC), Duration.ofMinutes(5),
                Duration.ofSeconds(10), REGISTRATION);

        assertThat(service.begin(SESSION).outcome())
                .isEqualTo(StaffOidcAuthorizationTransactions.BeginOutcome.UNAVAILABLE);
        assertThat(service.consume("A".repeat(43), SESSION).outcome())
                .isEqualTo(StaffOidcAuthorizationTransactions.ConsumeOutcome.UNAVAILABLE);
    }

    @Test
    void lifecycleHasNoAuthenticationOrAuthorizationSideEffects() {
        var authentication = UsernamePasswordAuthenticationToken.authenticated(
                "synthetic-customer", null, java.util.List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var service = service(new InMemoryStore());

        var beginning = service.begin(SESSION);
        var result = service.consume(beginning.transaction().state(), SESSION);

        assertThat(result.outcome()).isEqualTo(StaffOidcAuthorizationTransactions.ConsumeOutcome.CONSUMED);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isSameAs(authentication);
        assertThat(VerifiedStaffAuthentication.class.getConstructors()).isEmpty();
        assertThat(result.transaction().toString()).doesNotContain("authenticated", "authority", "role");
    }

    @Test
    void expiryAndClockToleranceMustBeExplicitValidInputs() {
        var store = new InMemoryStore();
        assertThatThrownBy(() -> service(store, Duration.ZERO, Duration.ZERO))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> service(store, Duration.ofMinutes(5), Duration.ofSeconds(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static StaffOidcAuthorizationTransactions service(StaffOidcAuthorizationTransactionPort store) {
        return service(store, Duration.ofMinutes(5), Duration.ofSeconds(10));
    }

    private static StaffOidcAuthorizationTransactions service(StaffOidcAuthorizationTransactionPort store,
            Duration lifetime, Duration tolerance) {
        return new StaffOidcAuthorizationTransactions(store, TRANSACTIONS, new SecureRandom(),
                Clock.fixed(NOW, ZoneOffset.UTC), lifetime, tolerance, REGISTRATION);
    }

    private static class InMemoryStore implements StaffOidcAuthorizationTransactionPort {
        private StaffOidcAuthorizationTransaction current;
        private int calls;

        @Override public BeginOutcome supersedeAndStore(StaffOidcAuthorizationTransaction transaction) {
            calls++;
            current = transaction;
            return BeginOutcome.STORED;
        }

        @Override public Consumption consume(String state, String sessionId, String registrationId,
                Instant observedAt, Duration tolerance) {
            calls++;
            if (current == null || !current.state().equals(state)
                    || !current.initiatingSessionId().equals(sessionId)
                    || !current.registrationId().equals(registrationId)) {
                return new Consumption(ConsumeOutcome.REJECTED, null);
            }
            return new Consumption(ConsumeOutcome.CONSUMED,
                    current.withStatus(StaffOidcAuthorizationTransaction.Status.CONSUMED));
        }
    }
}
