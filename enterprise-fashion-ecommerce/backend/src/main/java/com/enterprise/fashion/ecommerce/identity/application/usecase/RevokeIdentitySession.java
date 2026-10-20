package com.enterprise.fashion.ecommerce.identity.application.usecase;

import java.util.Objects;

import com.enterprise.fashion.ecommerce.identity.application.SessionObservation;
import com.enterprise.fashion.ecommerce.identity.application.SessionRevocationOutcome;
import com.enterprise.fashion.ecommerce.identity.application.port.out.IdentitySessionObservationPort;
import com.enterprise.fashion.ecommerce.identity.application.port.out.IdentitySessionRevocationPort;

public final class RevokeIdentitySession {
    private final IdentitySessionRevocationPort revocations;
    private final IdentitySessionObservationPort observations;

    public RevokeIdentitySession(IdentitySessionRevocationPort revocations,
            IdentitySessionObservationPort observations) {
        this.revocations = Objects.requireNonNull(revocations);
        this.observations = Objects.requireNonNull(observations);
    }

    public SessionRevocationOutcome revoke(String sessionIdentifier) {
        SessionReferences.validate(sessionIdentifier);
        if (!revocations.revoke(sessionIdentifier)) {
            return SessionRevocationOutcome.UNCERTAIN;
        }
        // Point-in-time absence does not prove this invocation deleted an existing
        // record or prevent later stale work from restoring Session state.
        return observations.observe(sessionIdentifier) == SessionObservation.ABSENT
                ? SessionRevocationOutcome.CONFIRMED_ABSENT : SessionRevocationOutcome.UNCERTAIN;
    }
}
