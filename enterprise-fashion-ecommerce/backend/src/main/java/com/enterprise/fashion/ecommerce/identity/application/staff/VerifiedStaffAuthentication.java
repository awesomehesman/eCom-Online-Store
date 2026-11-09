package com.enterprise.fashion.ecommerce.identity.application.staff;

import java.util.Objects;
import com.enterprise.fashion.ecommerce.identity.domain.model.StaffIdentityReference;
import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Reserved capability for accepted Staff authentication, not a wrapper for caller claims.
 * No production constructor, factory, issuer or deserialization path exists.
 * Adding an issuer requires separate review of authentication authority, subject binding,
 * freshness, replay, assurance and revocation before operational reliance. This class
 * does not verify an external assertion, Customer credential, header, cookie or token.
 */
public final class VerifiedStaffAuthentication {
    private final StaffIdentityReference identity;

    @JsonCreator(mode = JsonCreator.Mode.DISABLED)
    private VerifiedStaffAuthentication(StaffIdentityReference identity) {
        this.identity = Objects.requireNonNull(identity, "Staff authentication evidence requires an identity");
    }
    StaffIdentityReference identity() { return identity; }
    @Override public String toString() { return "VerifiedStaffAuthentication[redacted]"; }
}
