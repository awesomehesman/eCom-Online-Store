package com.enterprise.fashion.ecommerce.identity.application.staff;

import java.util.Objects;
import com.enterprise.fashion.ecommerce.identity.application.port.out.StaffOidcAuthenticationVerifier;

/**
 * Dormant, negative-only OIDC verification orchestration. A CONSUMED value is required, but the
 * current public transaction constructor means the value alone is not proof of persistence-backed
 * V16 consumption. Positive verification therefore remains deliberately impossible until a future
 * reviewed callback boundary passes the result of atomic V16 consumption directly to an approved
 * verifier and controlled evidence issuer.
 */
public final class VerifyStaffOidcAuthentication {
    private final StaffOidcAuthenticationVerifier verifier;

    public VerifyStaffOidcAuthentication(StaffOidcAuthenticationVerifier verifier) {
        this.verifier = Objects.requireNonNull(verifier);
    }

    public StaffOidcAuthenticationVerification verify(
            StaffOidcAuthorizationTransaction consumedTransaction,
            StaffOidcAuthorizationCallback callback) {
        if (consumedTransaction == null
                || consumedTransaction.status() != StaffOidcAuthorizationTransaction.Status.CONSUMED) {
            return result(StaffOidcAuthenticationVerification.Outcome.INVALID_TRANSACTION);
        }
        if (callback == null || callback.kind() == StaffOidcAuthorizationCallback.Kind.REJECTED) {
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

    private static StaffOidcAuthenticationVerification result(
            StaffOidcAuthenticationVerification.Outcome outcome) {
        return new StaffOidcAuthenticationVerification(outcome);
    }
}
