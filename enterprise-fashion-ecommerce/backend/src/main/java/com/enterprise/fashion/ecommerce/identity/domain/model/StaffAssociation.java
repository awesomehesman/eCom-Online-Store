package com.enterprise.fashion.ecommerce.identity.domain.model;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/** Identity-local Staff principal reference; distinct from a Customer credential subject or Staff access grant. */
public record StaffAssociation(UUID staffPrincipalId, StaffIdentityReference identity,
        Instant createdAt, Instant revokedAt) {
    public enum Status { ACTIVE, REVOKED }
    public StaffAssociation {
        Objects.requireNonNull(staffPrincipalId);
        Objects.requireNonNull(identity);
        Objects.requireNonNull(createdAt);
        if (revokedAt != null && revokedAt.isBefore(createdAt)) {
            throw new IllegalArgumentException("Invalid Staff association chronology");
        }
    }
    public Status status() { return revokedAt == null ? Status.ACTIVE : Status.REVOKED; }
    @Override public String toString() { return "StaffAssociation[redacted]"; }
}
