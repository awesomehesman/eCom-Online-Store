package com.enterprise.fashion.ecommerce.identity.application.port.out;

import java.time.Duration;
import java.time.Instant;
import com.enterprise.fashion.ecommerce.identity.application.staff.StaffOidcAuthorizationTransaction;

/** Internal persistence contract; every operation requires an application-owned transaction. */
public interface StaffOidcAuthorizationTransactionPort {
    enum BeginOutcome { STORED, SESSION_UNAVAILABLE }
    enum ConsumeOutcome { CONSUMED, REJECTED, REPLAYED, EXPIRED, SUPERSEDED, CORRUPT }

    record Consumption(ConsumeOutcome outcome, StaffOidcAuthorizationTransaction transaction) {
        public Consumption {
            if ((outcome == ConsumeOutcome.CONSUMED) != (transaction != null)) {
                throw new IllegalArgumentException("Invalid authorization-transaction consumption");
            }
        }
    }

    BeginOutcome supersedeAndStore(StaffOidcAuthorizationTransaction transaction);

    Consumption consume(String returnedState, String initiatingSessionId,
            String registrationId, Instant observedAt, Duration clockTolerance);

    final class StoreUnavailableException extends RuntimeException {
        public StoreUnavailableException(String message, Throwable cause) { super(message, cause); }
    }
}
