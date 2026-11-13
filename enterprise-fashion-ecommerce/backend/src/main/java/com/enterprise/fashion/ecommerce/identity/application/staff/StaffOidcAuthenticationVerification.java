package com.enterprise.fashion.ecommerce.identity.application.staff;

import java.util.Objects;

/** Negative-only verification result. No outcome is verified Staff evidence. */
public record StaffOidcAuthenticationVerification(Outcome outcome) {
    public enum Outcome {
        REJECTED_CALLBACK,
        INVALID_TRANSACTION,
        EXCHANGE_REJECTED,
        INVALID_TOKEN,
        ISSUER_MISMATCH,
        AUDIENCE_MISMATCH,
        NONCE_MISMATCH,
        INSUFFICIENT_ASSURANCE,
        STALE_AUTHENTICATION,
        UNTRUSTED_SUBJECT,
        PROVIDER_UNAVAILABLE,
        VERIFICATION_UNAVAILABLE,
        INDETERMINATE
    }

    public StaffOidcAuthenticationVerification {
        Objects.requireNonNull(outcome, "Verification outcome is required");
    }

    @Override
    public String toString() {
        return "StaffOidcAuthenticationVerification[" + outcome + "]";
    }
}
