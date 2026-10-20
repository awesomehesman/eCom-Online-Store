package com.enterprise.fashion.ecommerce.identity.application.usecase;

import java.util.Objects;

import com.enterprise.fashion.ecommerce.identity.application.SessionObservation;
import com.enterprise.fashion.ecommerce.identity.application.port.out.IdentitySessionObservationPort;

public final class ObserveIdentitySession {
    private final IdentitySessionObservationPort observations;

    public ObserveIdentitySession(IdentitySessionObservationPort observations) {
        this.observations = Objects.requireNonNull(observations);
    }

    public SessionObservation observe(String sessionIdentifier) {
        SessionReferences.validate(sessionIdentifier);
        return observations.observe(sessionIdentifier);
    }
}
