package com.enterprise.fashion.ecommerce.identity.adapter.out.session;

import com.enterprise.fashion.ecommerce.identity.application.SessionRevocationOutcome;
import org.junit.jupiter.api.Test;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SpringSessionJdbcRevocationAdapterTest {

    @Test
    void confirmsAbsenceOnlyAfterDeletionAndAuthoritativeLookup() {
        SessionRepository<Session> repository = repository();
        when(repository.findById("known-session")).thenReturn(null);
        SpringSessionJdbcRevocationAdapter adapter = new SpringSessionJdbcRevocationAdapter(repository);

        SessionRevocationOutcome outcome = adapter.revoke("known-session");

        assertThat(outcome).isEqualTo(SessionRevocationOutcome.CONFIRMED_ABSENT);
        var ordered = inOrder(repository);
        ordered.verify(repository).deleteById("known-session");
        ordered.verify(repository).findById("known-session");
        ordered.verifyNoMoreInteractions();
    }

    @Test
    void reportsUncertaintyWhenConfirmationStillObservesTheSession() {
        SessionRepository<Session> repository = repository();
        when(repository.findById("known-session")).thenReturn(mock(Session.class));
        SpringSessionJdbcRevocationAdapter adapter = new SpringSessionJdbcRevocationAdapter(repository);

        SessionRevocationOutcome outcome = adapter.revoke("known-session");

        assertThat(outcome).isEqualTo(SessionRevocationOutcome.UNCERTAIN);
    }

    @Test
    void reportsUncertaintyWhenDeletionFails() {
        SessionRepository<Session> repository = repository();
        org.mockito.Mockito.doThrow(new IllegalStateException("repository unavailable"))
                .when(repository).deleteById("known-session");
        SpringSessionJdbcRevocationAdapter adapter = new SpringSessionJdbcRevocationAdapter(repository);

        SessionRevocationOutcome outcome = adapter.revoke("known-session");

        assertThat(outcome).isEqualTo(SessionRevocationOutcome.UNCERTAIN);
    }

    @Test
    void reportsUncertaintyWhenConfirmationFails() {
        SessionRepository<Session> repository = repository();
        when(repository.findById("known-session"))
                .thenThrow(new IllegalStateException("confirmation unavailable"));
        SpringSessionJdbcRevocationAdapter adapter = new SpringSessionJdbcRevocationAdapter(repository);

        SessionRevocationOutcome outcome = adapter.revoke("known-session");

        assertThat(outcome).isEqualTo(SessionRevocationOutcome.UNCERTAIN);
    }

    @SuppressWarnings("unchecked")
    private SessionRepository<Session> repository() {
        return mock(SessionRepository.class);
    }
}
