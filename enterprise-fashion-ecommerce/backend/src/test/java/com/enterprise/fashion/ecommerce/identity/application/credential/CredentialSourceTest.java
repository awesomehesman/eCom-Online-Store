package com.enterprise.fashion.ecommerce.identity.application.credential;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.identity.application.port.out.CredentialSourcePort;
import com.enterprise.fashion.ecommerce.identity.domain.model.CredentialSubject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionOperations;

class CredentialSourceTest {
    private final CredentialSourcePort port = mock(CredentialSourcePort.class);
    private final CredentialSubject subject = new CredentialSubject(UUID.randomUUID());
    private final UUID fact = UUID.randomUUID();
    // A fake transaction tests mapping only, not PostgreSQL visibility or production provenance.
    private final TransactionOperations local = new TransactionOperations() {
        @Override
        public <T> T execute(TransactionCallback<T> action) {
            return action.doInTransaction(mock(TransactionStatus.class));
        }
    };
    private final CredentialSource source = new CredentialSource(port, local);

    @Test
    void sourceOutcomeMappingNeverInventsAbsenceOrPasswordMatch() {
        var observations = CredentialSourcePort.Observation.values();
        var expected = new CredentialEvidence[]{CredentialEvidence.CONFIRMED_APPLICABLE,
                CredentialEvidence.NON_CURRENT, CredentialEvidence.CONFLICT, CredentialEvidence.UNCERTAIN};
        for (int index = 0; index < observations.length; index++) {
            when(port.observe(subject, fact)).thenReturn(observations[index]);
            assertThat(source.confirm(subject, fact)).isEqualTo(expected[index]);
        }
        assertThat(CredentialEvidence.values()).containsExactly(expected);
    }

    @Test
    void incompleteAndFailedSourceWithholdConfirmationWithoutLeakingErrorDetails() {
        when(port.observe(subject, fact)).thenReturn(null);
        assertThat(source.confirm(subject, fact)).isEqualTo(CredentialEvidence.UNCERTAIN);
        when(port.observe(subject, fact)).thenThrow(new IllegalStateException("sensitive-verifier"));
        assertThat(source.confirm(subject, fact)).isEqualTo(CredentialEvidence.UNCERTAIN);
    }

    @Test
    void commitFailureDoesNotEscapeAsPublicationSuccessOrPositiveConfirmation() {
        TransactionOperations failedCommit = new TransactionOperations() {
            @Override
            public <T> T execute(TransactionCallback<T> action) {
                action.doInTransaction(mock(TransactionStatus.class));
                throw new IllegalStateException("sensitive commit details");
            }
        };
        CredentialSource service = new CredentialSource(port, failedCommit);
        var publication = SyntheticCredentialPublications.fact(fact, subject, "test-verifier", null);
        when(port.publish(publication)).thenReturn(CredentialPublicationResult.PUBLISHED);
        when(port.observe(subject, fact)).thenReturn(CredentialSourcePort.Observation.BOUND_APPLICABLE);
        assertThat(service.publish(publication)).isEqualTo(CredentialPublicationResult.UNCERTAIN);
        assertThat(service.confirm(subject, fact)).isEqualTo(CredentialEvidence.UNCERTAIN);
    }

    @Test
    void missingInputsDoNotReachSource() {
        assertThat(source.publish(null)).isEqualTo(CredentialPublicationResult.UNCERTAIN);
        assertThat(source.confirm(null, fact)).isEqualTo(CredentialEvidence.UNCERTAIN);
        assertThat(source.confirm(subject, null)).isEqualTo(CredentialEvidence.UNCERTAIN);
        verifyNoInteractions(port);
    }

    @Test
    void internalReferencesDoNotAcceptEmailOrSessionAuthorityAndRedactDiagnostics() {
        assertThatThrownBy(() -> new CredentialSubject(null)).isInstanceOf(NullPointerException.class);
        assertThat(new CredentialSubject(subject.value())).isEqualTo(subject);
        assertThat(subject.toString()).doesNotContain(subject.value().toString());
        var publication = SyntheticCredentialPublications.fact(fact, subject, "sensitive-verifier", null);
        assertThat(publication.toString()).doesNotContain("sensitive-verifier", fact.toString());
        assertThatThrownBy(() -> SyntheticCredentialPublications.fact(fact, subject, "", null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> SyntheticCredentialPublications.fact(fact, subject, "x".repeat(129), null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> SyntheticCredentialPublications.fact(fact, subject, "test", fact))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void jacksonCannotDeserializeAcceptedAuthorityFromOtherwiseValidFields() throws Exception {
        ObjectMapper mapper = new ObjectMapper().registerModule(new ParameterNamesModule());
        String json = mapper.writeValueAsString(validCallerFields());
        assertThatThrownBy(() -> mapper.readValue(json, AcceptedCredentialPublication.class))
                .isInstanceOf(InvalidDefinitionException.class);
    }

    @Test
    void springJacksonCannotConvertCallerFieldsIntoAcceptedAuthority() {
        ObjectMapper mapper = Jackson2ObjectMapperBuilder.json().build();
        assertThatThrownBy(() -> mapper.convertValue(validCallerFields(), AcceptedCredentialPublication.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasCauseInstanceOf(InvalidDefinitionException.class);
    }

    private Map<String, Object> validCallerFields() {
        // The same inputs remain constructible by the isolated test-only mechanics helper.
        var synthetic = SyntheticCredentialPublications.fact(
                fact, subject, SyntheticCredentialPublications.VERIFIER, null);
        assertThat(synthetic.fact()).isEqualTo(fact);
        assertThat(synthetic.subject()).isEqualTo(subject);
        return Map.of("fact", fact.toString(),
                "subject", Map.of("value", subject.value().toString()),
                "verifier", SyntheticCredentialPublications.VERIFIER);
    }

    @Test
    void capabilityHasNoCallerConstructorFactoryOrExtensibleSubclass() {
        assertThat(Modifier.isFinal(AcceptedCredentialPublication.class.getModifiers())).isTrue();
        assertThat(Arrays.stream(AcceptedCredentialPublication.class.getDeclaredConstructors()))
                .allMatch(constructor -> Modifier.isPrivate(constructor.getModifiers()));
        assertThat(Arrays.stream(AcceptedCredentialPublication.class.getDeclaredMethods()))
                .noneMatch(method -> method.getReturnType() == AcceptedCredentialPublication.class);
    }
}
