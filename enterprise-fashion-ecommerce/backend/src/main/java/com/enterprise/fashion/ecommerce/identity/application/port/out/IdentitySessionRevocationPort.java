package com.enterprise.fashion.ecommerce.identity.application.port.out;

import com.enterprise.fashion.ecommerce.identity.application.SessionRevocationOutcome;

public interface IdentitySessionRevocationPort {

    /**
     * Attempts revocation and reports the authoritative post-operation observation.
     * Confirmed absence does not attribute deletion or guarantee permanent absence.
     */
    SessionRevocationOutcome revoke(String sessionIdentifier);
}
