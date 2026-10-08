package com.enterprise.fashion.ecommerce.identity.application.credential;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.identity.application.CredentialVerificationOutcome;
import com.enterprise.fashion.ecommerce.identity.application.port.out.CredentialSourcePort;
import com.enterprise.fashion.ecommerce.identity.application.port.out.CustomerPasswordVerificationPort;
import com.enterprise.fashion.ecommerce.identity.domain.model.CredentialSubject;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionOperations;

class VerifySourceBoundCustomerPasswordTest {
    private final CredentialSubject subject = new CredentialSubject(UUID.randomUUID());
    private final UUID fact = UUID.randomUUID();
    private final CredentialSourcePort port = mock(CredentialSourcePort.class);
    private final CustomerPasswordVerificationPort passwords = mock(CustomerPasswordVerificationPort.class);
    // Unit-only transaction stub; PostgreSQL tests separately prove actual lock boundaries.
    private final TransactionOperations transactions = new TransactionOperations() {
        @Override
        public <T> T execute(TransactionCallback<T> action) {
            return action.doInTransaction(mock(TransactionStatus.class));
        }
    };
    private final VerifySourceBoundCustomerPassword service = new VerifySourceBoundCustomerPassword(
            new CredentialSource(port, transactions), passwords);

    @Test
    void matchRequiresTwoCoherentObservationsOfExactlyTheSameBinding() {
        when(port.observeVerifier(subject, fact)).thenReturn(applicable());
        when(passwords.verify("input", SyntheticCredentialPublications.VERIFIER))
                .thenReturn(CredentialVerificationOutcome.MATCH);
        assertThat(service.verify(subject, fact, "input")).isEqualTo(SourceBoundPasswordVerificationResult.VERIFIED);
        verify(port, times(2)).observeVerifier(subject, fact);
        verify(passwords).verify("input", SyntheticCredentialPublications.VERIFIER);
    }

    @Test
    void nonMatchAndRejectionNeverBecomeSuccessOrRequestFinalRevalidation() {
        for (var outcome : List.of(CredentialVerificationOutcome.NO_MATCH, CredentialVerificationOutcome.REJECTED)) {
            reset(port, passwords);
            when(port.observeVerifier(subject, fact)).thenReturn(applicable());
            when(passwords.verify(any(), any())).thenReturn(outcome);
            assertThat(service.verify(subject, fact, "input")).isEqualTo(
                    outcome == CredentialVerificationOutcome.NO_MATCH
                            ? SourceBoundPasswordVerificationResult.NO_MATCH
                            : SourceBoundPasswordVerificationResult.REJECTED);
            verify(port).observeVerifier(subject, fact);
        }
    }

    @Test
    void initialNonConfirmingEvidenceNeverInvokesPasswordVerification() {
        var observations = List.of(CredentialSourcePort.Observation.SUPERSEDED,
                CredentialSourcePort.Observation.INCOMPATIBLE, CredentialSourcePort.Observation.INCOMPLETE);
        var results = List.of(SourceBoundPasswordVerificationResult.SOURCE_NON_CURRENT,
                SourceBoundPasswordVerificationResult.SOURCE_CONFLICT, SourceBoundPasswordVerificationResult.SOURCE_UNCERTAIN);
        for (int index = 0; index < observations.size(); index++) {
            when(port.observeVerifier(subject, fact))
                    .thenReturn(CredentialVerifierObservation.nonConfirming(observations.get(index)));
            assertThat(service.verify(subject, fact, "input")).isEqualTo(results.get(index));
        }
        when(port.observeVerifier(subject, fact)).thenReturn(null);
        assertThat(service.verify(subject, fact, "input")).isEqualTo(SourceBoundPasswordVerificationResult.SOURCE_UNCERTAIN);
        when(port.observeVerifier(subject, fact)).thenThrow(new IllegalStateException("secret source details"));
        assertThat(service.verify(subject, fact, "input")).isEqualTo(SourceBoundPasswordVerificationResult.SOURCE_UNCERTAIN);
        verifyNoInteractions(passwords);
    }

    @Test
    void substitutedInitialSubjectFactOrContextNeverReachesPasswordVerifier() {
        for (var observation : List.of(
                bound(new CredentialSubject(UUID.randomUUID()), fact, "LOCAL_CUSTOMER_PASSWORD", "secret", null),
                bound(subject, UUID.randomUUID(), "LOCAL_CUSTOMER_PASSWORD", "secret", null),
                bound(subject, fact, "OTHER_CONTEXT", "secret", null),
                bound(subject, fact, "LOCAL_CUSTOMER_PASSWORD", null, null))) {
            when(port.observeVerifier(subject, fact)).thenReturn(observation);
            assertThat(service.verify(subject, fact, "input")).isEqualTo(SourceBoundPasswordVerificationResult.SOURCE_UNCERTAIN);
        }
        verifyNoInteractions(passwords);
    }

    @Test
    void finalBindingSubstitutionCannotReuseMatch() {
        for (var changed : List.of(
                bound(new CredentialSubject(UUID.randomUUID()), fact, "LOCAL_CUSTOMER_PASSWORD", SyntheticCredentialPublications.VERIFIER, null),
                bound(subject, UUID.randomUUID(), "LOCAL_CUSTOMER_PASSWORD", SyntheticCredentialPublications.VERIFIER, null),
                bound(subject, fact, "OTHER_CONTEXT", SyntheticCredentialPublications.VERIFIER, null),
                bound(subject, fact, "LOCAL_CUSTOMER_PASSWORD", SyntheticCredentialPublications.OTHER_VERIFIER, null),
                bound(subject, fact, "LOCAL_CUSTOMER_PASSWORD", SyntheticCredentialPublications.VERIFIER, UUID.randomUUID()))) {
            when(port.observeVerifier(subject, fact)).thenReturn(applicable(), changed);
            when(passwords.verify(any(), any())).thenReturn(CredentialVerificationOutcome.MATCH);
            assertThat(service.verify(subject, fact, "input")).isEqualTo(SourceBoundPasswordVerificationResult.SOURCE_UNCERTAIN);
        }
    }

    @Test
    void finalNonCurrentConflictOrUncertaintyWithholdsMatch() {
        var observations = List.of(CredentialSourcePort.Observation.SUPERSEDED,
                CredentialSourcePort.Observation.INCOMPATIBLE, CredentialSourcePort.Observation.INCOMPLETE);
        var results = List.of(SourceBoundPasswordVerificationResult.SOURCE_NON_CURRENT,
                SourceBoundPasswordVerificationResult.SOURCE_CONFLICT, SourceBoundPasswordVerificationResult.SOURCE_UNCERTAIN);
        for (int index = 0; index < observations.size(); index++) {
            when(port.observeVerifier(subject, fact)).thenReturn(applicable(),
                    CredentialVerifierObservation.nonConfirming(observations.get(index)));
            when(passwords.verify(any(), any())).thenReturn(CredentialVerificationOutcome.MATCH);
            assertThat(service.verify(subject, fact, "input")).isEqualTo(results.get(index));
        }
        when(port.observeVerifier(subject, fact)).thenReturn(applicable()).thenThrow(new IllegalStateException("secret"));
        assertThat(service.verify(subject, fact, "input")).isEqualTo(SourceBoundPasswordVerificationResult.SOURCE_UNCERTAIN);
    }

    @Test
    void verificationNormalizesNfcWithoutProspectiveLengthCaseOrWhitespacePolicy() {
        for (String input : List.of("", "short", "x".repeat(65), "  E\u0301\t ")) {
            reset(port, passwords);
            when(port.observeVerifier(subject, fact)).thenReturn(applicable());
            when(passwords.verify(any(), any())).thenReturn(CredentialVerificationOutcome.MATCH);
            assertThat(service.verify(subject, fact, input)).isEqualTo(SourceBoundPasswordVerificationResult.VERIFIED);
            verify(passwords).verify(java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFC),
                    SyntheticCredentialPublications.VERIFIER);
        }
    }

    @Test
    void malformedPasswordAndVerifierFailuresAreRejectedWithoutRepairOrLeakage() {
        when(port.observeVerifier(subject, fact)).thenReturn(applicable());
        for (String input : new String[]{null, "\uD800", "\uDC00"}) {
            assertThat(service.verify(subject, fact, input)).isEqualTo(SourceBoundPasswordVerificationResult.REJECTED);
        }
        verifyNoInteractions(passwords);
        when(passwords.verify(any(), any())).thenThrow(new IllegalStateException("secret verifier"));
        assertThat(service.verify(subject, fact, "input")).isEqualTo(SourceBoundPasswordVerificationResult.REJECTED);
    }

    @Test
    void ordinaryResultsAndHandoffDiagnosticsContainNoSecretsOrAbsence() {
        assertThat(applicable().toString()).isEqualTo("CredentialVerifierObservation[redacted]");
        assertThat(SourceBoundPasswordVerificationResult.values()).containsExactly(
                SourceBoundPasswordVerificationResult.VERIFIED, SourceBoundPasswordVerificationResult.NO_MATCH,
                SourceBoundPasswordVerificationResult.REJECTED, SourceBoundPasswordVerificationResult.SOURCE_NON_CURRENT,
                SourceBoundPasswordVerificationResult.SOURCE_CONFLICT, SourceBoundPasswordVerificationResult.SOURCE_UNCERTAIN);
    }

    private CredentialVerifierObservation applicable() {
        return bound(subject, fact, "LOCAL_CUSTOMER_PASSWORD", SyntheticCredentialPublications.VERIFIER, null);
    }

    private CredentialVerifierObservation bound(CredentialSubject owner, UUID publication, String context,
            String verifier, UUID supersedes) {
        return CredentialVerifierObservation.applicable(owner, publication, context, verifier, supersedes);
    }
}
