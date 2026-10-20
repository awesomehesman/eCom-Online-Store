package com.enterprise.fashion.ecommerce.identity.adapter.out.session;

import org.junit.jupiter.api.Test;
import com.enterprise.fashion.ecommerce.identity.application.SessionRevocationOutcome;
import com.enterprise.fashion.ecommerce.identity.application.port.out.IdentitySessionObservationPort;
import com.enterprise.fashion.ecommerce.identity.application.usecase.RevokeIdentitySession;
import org.springframework.transaction.CannotCreateTransactionException;
import static org.mockito.Mockito.verifyNoInteractions;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.session.SessionRepository;
import org.springframework.session.Session;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class SpringSessionJdbcRevocationAdapterTest {
    @Test
    void deletesOnlyTheSuppliedSessionWithoutConfirmationReads() {
        @SuppressWarnings("unchecked")
        SessionRepository<Session> repository = mock(SessionRepository.class);
        assertThat(new SpringSessionJdbcRevocationAdapter(repository).revoke("known-session")).isTrue();
        verify(repository).deleteById("known-session");
        verifyNoMoreInteractions(repository);
    }

    @Test
    void infrastructureFailureIsUncertain() {
        @SuppressWarnings("unchecked")
        SessionRepository<Session> repository = mock(SessionRepository.class);
        doThrow(new DataAccessResourceFailureException("unavailable")).when(repository).deleteById("known-session");
        assertThat(new SpringSessionJdbcRevocationAdapter(repository).revoke("known-session")).isFalse();
        verify(repository).deleteById("known-session");
        verifyNoMoreInteractions(repository);
    }

    @Test
    void transactionStartFailureReturnsUncertaintyWithoutObservation() {
        @SuppressWarnings("unchecked")
        SessionRepository<Session> repository = mock(SessionRepository.class);
        var observations = mock(IdentitySessionObservationPort.class);
        doThrow(new CannotCreateTransactionException("connection unavailable"))
                .when(repository).deleteById("known-session");
        var command = new RevokeIdentitySession(new SpringSessionJdbcRevocationAdapter(repository), observations);

        assertThat(command.revoke("known-session")).isEqualTo(SessionRevocationOutcome.UNCERTAIN);
        verify(repository).deleteById("known-session");
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(observations);
    }

    @Test
    void programmingErrorsAreNotReportedAsInfrastructureUncertainty() {
        @SuppressWarnings("unchecked")
        SessionRepository<Session> repository = mock(SessionRepository.class);
        doThrow(new IllegalArgumentException("bug")).when(repository).deleteById("known-session");
        assertThatIllegalArgumentException().isThrownBy(() ->
                new SpringSessionJdbcRevocationAdapter(repository).revoke("known-session"));
    }
}
