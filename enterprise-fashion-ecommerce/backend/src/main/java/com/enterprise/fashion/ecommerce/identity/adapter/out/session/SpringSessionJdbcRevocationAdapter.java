package com.enterprise.fashion.ecommerce.identity.adapter.out.session;

import com.enterprise.fashion.ecommerce.identity.application.SessionRevocationOutcome;
import com.enterprise.fashion.ecommerce.identity.application.port.out.IdentitySessionRevocationPort;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;

@Component
public final class SpringSessionJdbcRevocationAdapter implements IdentitySessionRevocationPort {

    private final SessionRepository<? extends Session> sessionRepository;

    public SpringSessionJdbcRevocationAdapter(SessionRepository<? extends Session> sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public SessionRevocationOutcome revoke(String sessionIdentifier) {
        try {
            sessionRepository.deleteById(sessionIdentifier);

            // This is a point-in-time authoritative observation, not proof that this
            // invocation deleted an existing Session or that the identifier cannot reappear.
            return sessionRepository.findById(sessionIdentifier) == null
                    ? SessionRevocationOutcome.CONFIRMED_ABSENT
                    : SessionRevocationOutcome.UNCERTAIN;
        }
        catch (RuntimeException exception) {
            return SessionRevocationOutcome.UNCERTAIN;
        }
    }
}
