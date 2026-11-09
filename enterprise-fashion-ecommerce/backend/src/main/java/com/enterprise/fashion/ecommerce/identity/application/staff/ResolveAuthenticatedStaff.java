package com.enterprise.fashion.ecommerce.identity.application.staff;

import java.util.Objects;
import java.util.UUID;
import com.enterprise.fashion.ecommerce.identity.domain.model.StaffAssociation;

/**
 * Isolated association-resolution contract. No production evidence issuer or caller exists.
 * A resolved reference is an observation, not a Spring Principal, Session, assignment or
 * Authorization grant. Future use must govern evidence freshness and withdrawal at final
 * reliance; this lookup does not establish atomic authentication/association consistency.
 */
public final class ResolveAuthenticatedStaff {
    public enum Outcome { RESOLVED, REJECTED, UNAVAILABLE }
    public record Resolution(Outcome outcome, UUID staffPrincipalId) {
        public Resolution {
            Objects.requireNonNull(outcome);
            if ((outcome == Outcome.RESOLVED) != (staffPrincipalId != null)) {
                throw new IllegalArgumentException("Invalid Staff resolution outcome");
            }
        }
        @Override public String toString() { return "StaffResolution[" + outcome + "]"; }
    }
    private final StaffAssociations associations;
    public ResolveAuthenticatedStaff(StaffAssociations associations) {
        this.associations = Objects.requireNonNull(associations);
    }
    public Resolution resolve(VerifiedStaffAuthentication evidence) {
        if (evidence == null) return new Resolution(Outcome.REJECTED, null);
        try {
            var association = associations.active(evidence.identity());
            return association.filter(value -> value.identity().equals(evidence.identity())
                    && value.status() == StaffAssociation.Status.ACTIVE)
                    .map(value -> new Resolution(Outcome.RESOLVED, value.staffPrincipalId()))
                    .orElseGet(() -> new Resolution(Outcome.REJECTED, null));
        } catch (RuntimeException unavailable) {
            // No protected identity/existence or dependency diagnostics escape this boundary.
            return new Resolution(Outcome.UNAVAILABLE, null);
        }
    }
}
