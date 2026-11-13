package com.enterprise.fashion.ecommerce.identity.application.staff;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Clock;
import java.time.Duration;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;
import com.enterprise.fashion.ecommerce.identity.application.port.out.StaffOidcAuthorizationTransactionPort;
import com.enterprise.fashion.ecommerce.identity.application.port.out.StaffOidcAuthorizationTransactionPort.Consumption;
import com.enterprise.fashion.ecommerce.identity.application.port.out.StaffOidcAuthorizationTransactionPort.StoreUnavailableException;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionOperations;

/**
 * Dormant Identity-owned OIDC correlation safeguards. This service performs no redirect,
 * token exchange, evidence verification, Authentication, SecurityContext or Session issuance.
 */
public final class StaffOidcAuthorizationTransactions {
    public enum BeginOutcome { STARTED, REJECTED, UNAVAILABLE }
    public enum ConsumeOutcome { CONSUMED, REJECTED, REPLAYED, EXPIRED, SUPERSEDED, CORRUPT, UNAVAILABLE }

    public record Beginning(BeginOutcome outcome, StaffOidcAuthorizationTransaction transaction) {
        public Beginning {
            if ((outcome == BeginOutcome.STARTED) != (transaction != null)) {
                throw new IllegalArgumentException("Invalid authorization-transaction beginning");
            }
        }
        @Override public String toString() { return "StaffOidcBeginning[" + outcome + "]"; }
    }

    public record Result(ConsumeOutcome outcome, StaffOidcAuthorizationTransaction transaction) {
        public Result {
            if ((outcome == ConsumeOutcome.CONSUMED) != (transaction != null)) {
                throw new IllegalArgumentException("Invalid authorization-transaction result");
            }
        }
        @Override public String toString() { return "StaffOidcConsumption[" + outcome + "]"; }
    }

    private static final Base64.Encoder BASE64_URL = Base64.getUrlEncoder().withoutPadding();
    private static final int RANDOM_BYTES = 32;

    private final StaffOidcAuthorizationTransactionPort store;
    private final TransactionOperations transactions;
    private final SecureRandom random;
    private final Clock clock;
    private final Duration lifetime;
    private final Duration clockTolerance;
    private final String registrationId;

    public StaffOidcAuthorizationTransactions(StaffOidcAuthorizationTransactionPort store,
            TransactionOperations transactions, SecureRandom random, Clock clock,
            Duration lifetime, Duration clockTolerance, String registrationId) {
        this.store = Objects.requireNonNull(store);
        this.transactions = Objects.requireNonNull(transactions);
        this.random = Objects.requireNonNull(random);
        this.clock = Objects.requireNonNull(clock);
        this.lifetime = positive(lifetime, "Transaction lifetime");
        this.clockTolerance = nonNegative(clockTolerance, "Clock tolerance");
        this.registrationId = bounded(registrationId, 128, "Registration identity");
    }

    public Beginning begin(String initiatingSessionId) {
        if (!validSession(initiatingSessionId)) {
            return new Beginning(BeginOutcome.REJECTED, null);
        }
        var createdAt = clock.instant();
        String state = randomValue();
        String nonce = randomValue();
        String verifier = randomValue();
        var candidate = new StaffOidcAuthorizationTransaction(randomUuid(), state, nonce,
                verifier, deriveS256(verifier), initiatingSessionId, registrationId,
                createdAt, createdAt.plus(lifetime), StaffOidcAuthorizationTransaction.Status.ACTIVE);
        try {
            var stored = transactions.execute(status -> store.supersedeAndStore(candidate));
            if (stored == StaffOidcAuthorizationTransactionPort.BeginOutcome.STORED) {
                return new Beginning(BeginOutcome.STARTED, candidate);
            }
            return new Beginning(BeginOutcome.REJECTED, null);
        } catch (StoreUnavailableException | TransactionException unavailable) {
            return new Beginning(BeginOutcome.UNAVAILABLE, null);
        }
    }

    public Result consume(String returnedState, String initiatingSessionId) {
        if (!validSecret(returnedState) || !validSession(initiatingSessionId)) {
            return new Result(ConsumeOutcome.REJECTED, null);
        }
        try {
            Consumption consumption = transactions.execute(status -> store.consume(returnedState,
                    initiatingSessionId, registrationId, clock.instant(), clockTolerance));
            if (consumption == null) {
                return new Result(ConsumeOutcome.UNAVAILABLE, null);
            }
            return switch (consumption.outcome()) {
                case CONSUMED -> new Result(ConsumeOutcome.CONSUMED, consumption.transaction());
                case REJECTED -> new Result(ConsumeOutcome.REJECTED, null);
                case REPLAYED -> new Result(ConsumeOutcome.REPLAYED, null);
                case EXPIRED -> new Result(ConsumeOutcome.EXPIRED, null);
                case SUPERSEDED -> new Result(ConsumeOutcome.SUPERSEDED, null);
                case CORRUPT -> new Result(ConsumeOutcome.CORRUPT, null);
            };
        } catch (StoreUnavailableException | TransactionException unavailable) {
            return new Result(ConsumeOutcome.UNAVAILABLE, null);
        }
    }

    static String deriveS256(String verifier) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(verifier.getBytes(StandardCharsets.US_ASCII));
            return BASE64_URL.encodeToString(digest);
        } catch (NoSuchAlgorithmException impossibleOnJava21) {
            throw new IllegalStateException("Required SHA-256 implementation unavailable", impossibleOnJava21);
        }
    }

    private String randomValue() {
        byte[] value = new byte[RANDOM_BYTES];
        random.nextBytes(value);
        return BASE64_URL.encodeToString(value);
    }

    private UUID randomUuid() {
        return new UUID(random.nextLong(), random.nextLong());
    }

    private static boolean validSecret(String value) {
        return value != null && value.length() >= 43 && value.length() <= 128
                && value.chars().allMatch(character -> Character.isLetterOrDigit(character)
                        || character == '-' || character == '_');
    }

    private static boolean validSession(String value) {
        return value != null && !value.isBlank() && value.length() <= 100
                && value.codePoints().noneMatch(Character::isISOControl);
    }

    private static Duration positive(Duration value, String label) {
        if (value == null || value.isZero() || value.isNegative()) {
            throw new IllegalArgumentException(label + " must be positive");
        }
        return value;
    }

    private static Duration nonNegative(Duration value, String label) {
        if (value == null || value.isNegative()) {
            throw new IllegalArgumentException(label + " must not be negative");
        }
        return value;
    }

    private static String bounded(String value, int maximum, String label) {
        if (value == null || value.isBlank() || value.length() > maximum
                || value.codePoints().anyMatch(Character::isISOControl)) {
            throw new IllegalArgumentException("Invalid " + label);
        }
        return value;
    }
}
