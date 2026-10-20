package com.enterprise.fashion.ecommerce.identity.application.port.out;

import com.enterprise.fashion.ecommerce.identity.application.SessionObservation;

public interface IdentitySessionObservationPort {
    /** Reads authoritative state without mutation or disclosure of Session contents. */
    SessionObservation observe(String sessionIdentifier);
}
