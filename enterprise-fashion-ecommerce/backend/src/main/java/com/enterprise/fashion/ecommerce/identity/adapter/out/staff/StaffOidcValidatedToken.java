package com.enterprise.fashion.ecommerce.identity.adapter.out.staff;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;

/** Internal validated claims, not authenticated Staff evidence or an authorization grant. */
record StaffOidcValidatedToken(String issuer, String subject, Set<String> audience,
        Instant issuedAt, Instant expiresAt, Instant authenticatedAt) {
    StaffOidcValidatedToken {
        Objects.requireNonNull(issuer);
        Objects.requireNonNull(subject);
        audience = Set.copyOf(Objects.requireNonNull(audience));
        Objects.requireNonNull(issuedAt);
        Objects.requireNonNull(expiresAt);
        Objects.requireNonNull(authenticatedAt);
    }

    @Override public String toString() { return "StaffOidcValidatedToken[redacted]"; }
}
