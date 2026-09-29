package com.enterprise.fashion.ecommerce.identity.application.usecase;

import java.util.concurrent.atomic.AtomicReference;

import com.enterprise.fashion.ecommerce.identity.application.SessionRevocationOutcome;
import com.enterprise.fashion.ecommerce.identity.application.port.out.IdentitySessionRevocationPort;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RevokeIdentitySessionTest {

    @Test
    void delegatesConfirmedAbsenceThroughTheProjectOwnedPort() {
        AtomicReference<String> delegatedIdentifier = new AtomicReference<>();
        IdentitySessionRevocationPort port = sessionIdentifier -> {
            delegatedIdentifier.set(sessionIdentifier);
            return SessionRevocationOutcome.CONFIRMED_ABSENT;
        };

        SessionRevocationOutcome outcome = new RevokeIdentitySession(port).revoke("known-session");

        assertThat(outcome).isEqualTo(SessionRevocationOutcome.CONFIRMED_ABSENT);
        assertThat(delegatedIdentifier).hasValue("known-session");
    }

    @Test
    void propagatesUncertainAuthorityThroughTheProjectOwnedPort() {
        IdentitySessionRevocationPort port = sessionIdentifier -> SessionRevocationOutcome.UNCERTAIN;

        SessionRevocationOutcome outcome = new RevokeIdentitySession(port).revoke("known-session");

        assertThat(outcome).isEqualTo(SessionRevocationOutcome.UNCERTAIN);
    }
}
