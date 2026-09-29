package com.enterprise.fashion.ecommerce.identity.application.usecase;

import java.util.Objects;

import com.enterprise.fashion.ecommerce.identity.application.SessionRevocationOutcome;
import com.enterprise.fashion.ecommerce.identity.application.port.out.IdentitySessionRevocationPort;

public final class RevokeIdentitySession {

    private final IdentitySessionRevocationPort revocationPort;

    public RevokeIdentitySession(IdentitySessionRevocationPort revocationPort) {
        this.revocationPort = Objects.requireNonNull(revocationPort);
    }

    public SessionRevocationOutcome revoke(String sessionIdentifier) {
        return revocationPort.revoke(sessionIdentifier);
    }
}
