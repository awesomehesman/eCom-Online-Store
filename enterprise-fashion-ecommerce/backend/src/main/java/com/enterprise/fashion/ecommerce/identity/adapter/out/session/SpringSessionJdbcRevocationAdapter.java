package com.enterprise.fashion.ecommerce.identity.adapter.out.session;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.CannotCreateTransactionException;
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
    public boolean revoke(String sessionIdentifier) {
        try {
            sessionRepository.deleteById(sessionIdentifier);

            return true;
        }
        catch (DataAccessException | CannotCreateTransactionException exception) {
            return false;
        }
    }
}
