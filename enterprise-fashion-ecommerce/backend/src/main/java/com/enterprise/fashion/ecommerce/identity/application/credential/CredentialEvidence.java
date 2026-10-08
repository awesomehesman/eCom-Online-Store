package com.enterprise.fashion.ecommerce.identity.application.credential;

/**
 * Result of a bounded source-confirmation call, not a reusable currentness/authorization token.
 * Currentness is established only at that call's locked observation boundary. A later operation
 * must re-establish applicability within its own reviewed reliance boundary. Contains no verifier.
 * Confirmed absence is intentionally not representable.
 */
public enum CredentialEvidence {
    CONFIRMED_APPLICABLE, NON_CURRENT, CONFLICT, UNCERTAIN
}
