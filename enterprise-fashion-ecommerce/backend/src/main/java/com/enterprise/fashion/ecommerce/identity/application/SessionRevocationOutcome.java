package com.enterprise.fashion.ecommerce.identity.application;

public enum SessionRevocationOutcome {
    /** Authoritative absence was observed after the revocation attempt. */
    CONFIRMED_ABSENT,

    /** Authoritative absence could not be established with sufficient certainty. */
    UNCERTAIN
}
