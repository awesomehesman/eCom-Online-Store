package com.enterprise.fashion.ecommerce.identity.application.usecase;

import com.enterprise.fashion.ecommerce.identity.application.SessionObservation;
import com.enterprise.fashion.ecommerce.identity.application.SessionRevocationOutcome;
import com.enterprise.fashion.ecommerce.identity.application.port.out.IdentitySessionObservationPort;
import com.enterprise.fashion.ecommerce.identity.application.port.out.IdentitySessionRevocationPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class RevokeIdentitySessionTest {
    @ParameterizedTest
    @EnumSource(SessionObservation.class)
    void confirmsOnlyObservedAbsenceAfterDeletion(SessionObservation observation) {
        var deletions = mock(IdentitySessionRevocationPort.class);
        var observations = mock(IdentitySessionObservationPort.class);
        when(deletions.revoke("known-session")).thenReturn(true);
        when(observations.observe("known-session")).thenReturn(observation);
        var command = new RevokeIdentitySession(deletions, observations);
        var expected = observation == SessionObservation.ABSENT
                ? SessionRevocationOutcome.CONFIRMED_ABSENT : SessionRevocationOutcome.UNCERTAIN;
        assertThat(command.revoke("known-session")).isEqualTo(expected);
        assertThat(command.revoke("known-session")).isEqualTo(expected);
        var order = inOrder(deletions, observations);
        for (int i = 0; i < 2; i++) {
            order.verify(deletions).revoke("known-session");
            order.verify(observations).observe("known-session");
        }
        verifyNoMoreInteractions(deletions, observations);
    }

    @Test
    void failedDeletionCannotBeConvertedToSuccess() {
        var deletions = mock(IdentitySessionRevocationPort.class);
        var observations = mock(IdentitySessionObservationPort.class);
        assertThat(new RevokeIdentitySession(deletions, observations).revoke("known-session"))
                .isEqualTo(SessionRevocationOutcome.UNCERTAIN);
        verify(deletions).revoke("known-session");
        verifyNoInteractions(observations);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "leading ", " line", "bad\nreference", "bad\tref",
            "1234567890123456789012345678901234567"})
    void invalidInputNeverReachesEitherPort(String reference) {
        var deletions = mock(IdentitySessionRevocationPort.class);
        var observations = mock(IdentitySessionObservationPort.class);
        assertThatIllegalArgumentException().isThrownBy(() ->
                new RevokeIdentitySession(deletions, observations).revoke(reference));
        verifyNoInteractions(deletions, observations);
    }

    @Test
    void doesNotSwallowProgrammingErrors() {
        var deletions = mock(IdentitySessionRevocationPort.class);
        var observations = mock(IdentitySessionObservationPort.class);
        when(deletions.revoke("known-session")).thenThrow(new IllegalStateException("bug"));
        assertThatIllegalStateException().isThrownBy(() ->
                new RevokeIdentitySession(deletions, observations).revoke("known-session"));
        verifyNoInteractions(observations);
        doReturn(true).when(deletions).revoke("known-session");
        when(observations.observe("known-session")).thenThrow(new IllegalStateException("bug"));
        assertThatIllegalStateException().isThrownBy(() ->
                new RevokeIdentitySession(deletions, observations).revoke("known-session"));
    }
}
