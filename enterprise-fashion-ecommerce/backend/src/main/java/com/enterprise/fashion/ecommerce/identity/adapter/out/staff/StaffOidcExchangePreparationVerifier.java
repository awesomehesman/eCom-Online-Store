package com.enterprise.fashion.ecommerce.identity.adapter.out.staff;

import java.util.Objects;
import com.enterprise.fashion.ecommerce.identity.application.port.out.StaffOidcAuthenticationVerifier;
import com.enterprise.fashion.ecommerce.identity.application.staff.StaffOidcAuthenticationVerification;
import com.enterprise.fashion.ecommerce.identity.application.staff.StaffOidcAuthorizationCallback;
import com.enterprise.fashion.ecommerce.identity.application.staff.StaffOidcAuthorizationTransaction;

/**
 * Dormant negative-only binding from the direct V16 verification path to exact-registration
 * exchange preparation. This adapter does not independently prove consumption provenance and must
 * be invoked only through the application-owned orchestration. It performs no credential, network,
 * token-validation, Staff-evidence or Session operation.
 */
final class StaffOidcExchangePreparationVerifier implements StaffOidcAuthenticationVerifier {
    private final StaffOidcAuthorizationCodeExchangePreparer preparer;

    StaffOidcExchangePreparationVerifier(StaffOidcProviderRegistrationResolver registrations) {
        this(new StaffOidcAuthorizationCodeExchangePreparer(
                Objects.requireNonNull(registrations, "Provider registrations are required")));
    }

    StaffOidcExchangePreparationVerifier(StaffOidcAuthorizationCodeExchangePreparer preparer) {
        this.preparer = Objects.requireNonNull(preparer, "Exchange preparer is required");
    }

    @Override
    public StaffOidcAuthenticationVerification verify(
            StaffOidcAuthorizationTransaction consumedTransaction,
            StaffOidcAuthorizationCallback callback) {
        try {
            StaffOidcAuthorizationCodeExchangePreparer.Result preparation =
                    preparer.prepare(consumedTransaction, callback);
            if (preparation == null) {
                return result(StaffOidcAuthenticationVerification.Outcome.INDETERMINATE);
            }
            if (!preparation.prepared()) {
                return result(preparation.failure() == null
                        ? StaffOidcAuthenticationVerification.Outcome.INDETERMINATE
                        : preparation.failure());
            }
            return result(StaffOidcAuthenticationVerification.Outcome.VERIFICATION_UNAVAILABLE);
        } catch (RuntimeException unavailable) {
            return result(StaffOidcAuthenticationVerification.Outcome.INDETERMINATE);
        }
    }

    @Override public String toString() {
        return "StaffOidcExchangePreparationVerifier[redacted]";
    }

    private static StaffOidcAuthenticationVerification result(
            StaffOidcAuthenticationVerification.Outcome outcome) {
        return new StaffOidcAuthenticationVerification(outcome);
    }
}
