package com.enterprise.fashion.ecommerce.identity.application.port.out;

public interface IdentitySessionRevocationPort {
    /**
     * Attempts deletion of only the supplied Session. True means the persistence call
     * completed, not that a record existed or absence was confirmed. False means
     * infrastructure uncertainty. Programming errors are not infrastructure outcomes.
     */
    boolean revoke(String sessionIdentifier);
}
