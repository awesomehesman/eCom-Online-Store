package com.enterprise.fashion.ecommerce.identity.application.usecase;

import com.enterprise.fashion.ecommerce.identity.application.SessionObservation;
import com.enterprise.fashion.ecommerce.identity.application.port.out.IdentitySessionObservationPort;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class ObserveIdentitySessionTest {
    @ParameterizedTest
    @EnumSource(SessionObservation.class)
    void preservesEvidenceLimitedOutcome(SessionObservation observation) {
        var port = mock(IdentitySessionObservationPort.class);
        when(port.observe("opaque-session")).thenReturn(observation);
        assertThat(new ObserveIdentitySession(port).observe("opaque-session")).isEqualTo(observation);
        verify(port).observe("opaque-session");
        verifyNoMoreInteractions(port);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", " leading", "trailing ", "line\nbreak", "tab\tvalue",
            "1234567890123456789012345678901234567"})
    void rejectsInvalidReferenceBeforePersistence(String identifier) {
        var port = mock(IdentitySessionObservationPort.class);
        assertThatIllegalArgumentException().isThrownBy(() -> new ObserveIdentitySession(port).observe(identifier))
                .withMessage("Invalid Session reference");
        verifyNoInteractions(port);
    }
}
