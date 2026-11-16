package com.enterprise.fashion.ecommerce.identity.adapter.out.staff;

import java.util.Objects;
import com.enterprise.fashion.ecommerce.identity.application.staff.StaffOidcAuthenticationVerification;
import com.enterprise.fashion.ecommerce.identity.application.staff.StaffOidcAuthorizationTransaction;

/**
 * Dormant binding from a consumed authorization transaction to one exact provider-registration
 * revision's response limits and token trust. This component performs no exchange, Authentication,
 * evidence issuance, SecurityContext mutation or Session work.
 */
final class StaffOidcRegistrationTokenVerifier {
    record Result(StaffOidcValidatedToken validatedToken,
            StaffOidcAuthenticationVerification.Outcome failure) {
        Result {
            if ((validatedToken == null) == (failure == null)) {
                throw new IllegalArgumentException("Exactly one registration-token result is required");
            }
        }

        static Result accepted(StaffOidcValidatedToken token) {
            return new Result(Objects.requireNonNull(token), null);
        }

        static Result rejected(StaffOidcAuthenticationVerification.Outcome failure) {
            return new Result(null, Objects.requireNonNull(failure));
        }

        boolean accepted() { return validatedToken != null; }

        @Override public String toString() {
            return accepted() ? "StaffOidcRegistrationTokenVerification[internal-success]"
                    : "StaffOidcRegistrationTokenVerification[" + failure + "]";
        }
    }

    private final StaffOidcProviderRegistrationResolver registrations;

    StaffOidcRegistrationTokenVerifier(StaffOidcProviderRegistrationResolver registrations) {
        this.registrations = Objects.requireNonNull(registrations, "Provider registrations are required");
    }

    Result verify(StaffOidcAuthorizationTransaction consumedTransaction, int statusCode,
            String contentType, byte[] responseBody) {
        if (consumedTransaction == null
                || consumedTransaction.status() != StaffOidcAuthorizationTransaction.Status.CONSUMED) {
            return rejected(StaffOidcAuthenticationVerification.Outcome.INVALID_TRANSACTION);
        }

        StaffOidcProviderRegistrationResolver.Resolution resolution;
        try {
            resolution = registrations.resolve(consumedTransaction.registrationId(),
                    consumedTransaction.registrationRevision());
        } catch (RuntimeException unavailable) {
            return rejected(StaffOidcAuthenticationVerification.Outcome.INDETERMINATE);
        }
        if (resolution == null) {
            return rejected(StaffOidcAuthenticationVerification.Outcome.INDETERMINATE);
        }
        StaffOidcProviderRegistrationResolver.Outcome resolutionOutcome = resolution.outcome();
        if (resolutionOutcome == null) {
            return rejected(StaffOidcAuthenticationVerification.Outcome.INDETERMINATE);
        }
        if (resolutionOutcome != StaffOidcProviderRegistrationResolver.Outcome.RESOLVED) {
            return rejected(StaffOidcAuthenticationVerification.Outcome.VERIFICATION_UNAVAILABLE);
        }

        StaffOidcProviderRegistration registration = resolution.registration();
        if (registration == null
                || !registration.registrationId().equals(consumedTransaction.registrationId())
                || registration.revision() != consumedTransaction.registrationRevision()) {
            return rejected(StaffOidcAuthenticationVerification.Outcome.INDETERMINATE);
        }
        try {
            StaffOidcTokenResponseParser.Result parsed = new StaffOidcTokenResponseParser(
                    registration.maximumResponseSize()).parse(statusCode, contentType, responseBody);
            if (parsed == null) {
                return rejected(StaffOidcAuthenticationVerification.Outcome.INDETERMINATE);
            }
            if (!parsed.parsed()) {
                return rejected(parsed.failure());
            }
            StaffOidcIdTokenValidator.Result validated = new StaffOidcIdTokenValidator(
                    registration.tokenTrust()).validate(parsed.tokenResponse().idToken(),
                            consumedTransaction.nonce());
            if (validated == null) {
                return rejected(StaffOidcAuthenticationVerification.Outcome.INDETERMINATE);
            }
            return validated.accepted()
                    ? Result.accepted(validated.validatedToken())
                    : rejected(validated.failure());
        } catch (RuntimeException unavailable) {
            return rejected(StaffOidcAuthenticationVerification.Outcome.INDETERMINATE);
        }
    }

    @Override public String toString() {
        return "StaffOidcRegistrationTokenVerifier[redacted]";
    }

    private static Result rejected(StaffOidcAuthenticationVerification.Outcome failure) {
        return Result.rejected(failure);
    }
}
