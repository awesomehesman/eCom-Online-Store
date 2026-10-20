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
        // V1 stores opaque Session identifiers in CHAR(36). Do not normalize credentials.
        if (sessionIdentifier == null || sessionIdentifier.isBlank() || sessionIdentifier.length() > 36
                || sessionIdentifier.codePoints().anyMatch(c -> Character.isWhitespace(c)
                        || Character.isSpaceChar(c) || Character.isISOControl(c))) {
            throw new IllegalArgumentException("Invalid Session reference");
        }
        return observations.observe(sessionIdentifier);
    }
}
