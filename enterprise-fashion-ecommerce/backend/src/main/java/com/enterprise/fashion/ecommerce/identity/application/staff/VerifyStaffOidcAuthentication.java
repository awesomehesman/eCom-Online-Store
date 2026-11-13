package com.enterprise.fashion.ecommerce.identity.application.staff;

import java.util.Objects;
import com.enterprise.fashion.ecommerce.identity.application.port.out.StaffOidcAuthenticationVerifier;

/**
 * Dormant, negative-only OIDC verification orchestration. This boundary owns V16 consumption and
 * passes only its direct CONSUMED result to the verifier. A valid provider rejection consumes the
 * transaction before returning REJECTED_CALLBACK so failed callbacks cannot leave state reusable.
 * Missing callback input is rejected before consumption. No outcome is authenticated Staff evidence.
 */
public final class VerifyStaffOidcAuthentication {
    private final StaffOidcAuthorizationTransactions authorizationTransactions;
    private final StaffOidcAuthenticationVerifier verifier;

    public VerifyStaffOidcAuthentication(StaffOidcAuthorizationTransactions authorizationTransactions,
            StaffOidcAuthenticationVerifier verifier) {
        this.authorizationTransactions = Objects.requireNonNull(authorizationTransactions);
        this.verifier = Objects.requireNonNull(verifier);
    }

    public StaffOidcAuthenticationVerification verify(String returnedState, String initiatingSessionId,
            StaffOidcAuthorizationCallback callback) {
        if (callback == null) {
            return result(StaffOidcAuthenticationVerification.Outcome.REJECTED_CALLBACK);
        }
        StaffOidcAuthorizationTransactions.Result consumption;
        try {
            consumption = authorizationTransactions.consume(returnedState, initiatingSessionId);
        } catch (RuntimeException unavailable) {
            return result(StaffOidcAuthenticationVerification.Outcome.TRANSACTION_UNAVAILABLE);
        }
        if (consumption == null) {
            return result(StaffOidcAuthenticationVerification.Outcome.TRANSACTION_UNAVAILABLE);
        }
        if (consumption.outcome() != StaffOidcAuthorizationTransactions.ConsumeOutcome.CONSUMED) {
            return result(map(consumption.outcome()));
        }
        StaffOidcAuthorizationTransaction consumedTransaction = consumption.transaction();
        if (consumedTransaction == null
                || consumedTransaction.status() != StaffOidcAuthorizationTransaction.Status.CONSUMED) {
            return result(StaffOidcAuthenticationVerification.Outcome.INVALID_TRANSACTION);
        }
        if (callback.kind() == StaffOidcAuthorizationCallback.Kind.REJECTED) {
            return result(StaffOidcAuthenticationVerification.Outcome.REJECTED_CALLBACK);
        }
        try {
            StaffOidcAuthenticationVerification verification = verifier.verify(consumedTransaction, callback);
            return verification == null
                    ? result(StaffOidcAuthenticationVerification.Outcome.INDETERMINATE)
                    : verification;
        } catch (RuntimeException unavailable) {
            return result(StaffOidcAuthenticationVerification.Outcome.INDETERMINATE);
        }
    }

    private static StaffOidcAuthenticationVerification.Outcome map(
            StaffOidcAuthorizationTransactions.ConsumeOutcome outcome) {
        return switch (outcome) {
            case CONSUMED -> StaffOidcAuthenticationVerification.Outcome.INVALID_TRANSACTION;
            case REJECTED -> StaffOidcAuthenticationVerification.Outcome.TRANSACTION_REJECTED;
            case REPLAYED -> StaffOidcAuthenticationVerification.Outcome.TRANSACTION_REPLAYED;
            case EXPIRED -> StaffOidcAuthenticationVerification.Outcome.TRANSACTION_EXPIRED;
            case SUPERSEDED -> StaffOidcAuthenticationVerification.Outcome.TRANSACTION_SUPERSEDED;
            case CORRUPT -> StaffOidcAuthenticationVerification.Outcome.TRANSACTION_CORRUPT;
            case UNAVAILABLE -> StaffOidcAuthenticationVerification.Outcome.TRANSACTION_UNAVAILABLE;
        };
    }

    private static StaffOidcAuthenticationVerification result(
            StaffOidcAuthenticationVerification.Outcome outcome) {
        return new StaffOidcAuthenticationVerification(outcome);
    }
}
