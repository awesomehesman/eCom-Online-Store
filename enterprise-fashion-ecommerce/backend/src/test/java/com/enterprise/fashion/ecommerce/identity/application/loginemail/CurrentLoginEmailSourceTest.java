package com.enterprise.fashion.ecommerce.identity.application.loginemail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.identity.application.port.out.CurrentLoginEmailSourcePort;
import com.enterprise.fashion.ecommerce.identity.domain.model.CredentialSubject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.jupiter.api.Test;
import org.springframework.core.ResolvableType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.validation.DataBinder;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionOperations;

class CurrentLoginEmailSourceTest {
    private final CurrentLoginEmailSourcePort port = mock(CurrentLoginEmailSourcePort.class);
    private final CredentialSubject subject = new CredentialSubject(UUID.randomUUID());
    private final UUID fact = UUID.randomUUID();
    private final String email = "private@example.test";
    private final TransactionOperations local = new TransactionOperations() {
        @Override
        public <T> T execute(TransactionCallback<T> action) {
            return action.doInTransaction(mock(TransactionStatus.class));
        }
    };
    private final CurrentLoginEmailSource source = new CurrentLoginEmailSource(port, local);

    @Test
    void onlySourceBackedExactSubjectAndFactCanReturnCurrentBinding() {
        when(port.observe(subject, fact)).thenReturn(CurrentLoginEmailObservation.current(subject, fact, email));
        var result = source.observe(subject, fact);
        assertThat(result.outcome()).isEqualTo(CurrentLoginEmailObservation.Outcome.CURRENT);
        assertThat(result.boundTo(subject, fact)).isTrue();
        assertThat(result.boundTo(subject, UUID.randomUUID())).isFalse();
        assertThat(result.boundTo(new CredentialSubject(UUID.randomUUID()), fact)).isFalse();
        assertThat(result.email()).isEqualTo(email);
        assertThat(result.toString()).doesNotContain(email, fact.toString(), subject.value().toString());
    }

    @Test
    void mismatchedOrUncertainSourceNeverBecomesCurrent() {
        when(port.observe(subject, fact)).thenReturn(CurrentLoginEmailObservation.current(subject, UUID.randomUUID(), email));
        assertThat(source.observe(subject, fact).outcome()).isEqualTo(CurrentLoginEmailObservation.Outcome.UNCERTAIN);
        when(port.observe(subject, fact)).thenReturn(null);
        assertThat(source.observe(subject, fact).outcome()).isEqualTo(CurrentLoginEmailObservation.Outcome.UNCERTAIN);
        when(port.observe(subject, fact)).thenThrow(new IllegalStateException(email));
        assertThat(source.observe(subject, fact).toString()).doesNotContain(email);
    }

    @Test
    void nonConfirmingOutcomesCarryNoEmailOrVerificationAuthority() {
        for (var outcome : new CurrentLoginEmailObservation.Outcome[]{
                CurrentLoginEmailObservation.Outcome.SUPERSEDED,
                CurrentLoginEmailObservation.Outcome.CONFLICT,
                CurrentLoginEmailObservation.Outcome.UNCERTAIN}) {
            when(port.observe(subject, fact)).thenReturn(CurrentLoginEmailObservation.nonConfirming(outcome));
            var result = source.observe(subject, fact);
            assertThat(result.outcome()).isEqualTo(outcome);
            assertThat(result.email()).isNull();
            assertThat(result.boundTo(subject, fact)).isFalse();
        }
        assertThat(Arrays.stream(CurrentLoginEmailObservation.Outcome.values()).map(Enum::name))
                .doesNotContain("VERIFIED", "AUTHENTICATED", "ABSENT");
    }

    @Test
    void missingInputsAndCommitFailureWithholdSourceAuthority() {
        assertThat(source.observe(null, fact).outcome()).isEqualTo(CurrentLoginEmailObservation.Outcome.UNCERTAIN);
        assertThat(source.observe(subject, null).outcome()).isEqualTo(CurrentLoginEmailObservation.Outcome.UNCERTAIN);
        assertThat(source.publish(null)).isEqualTo(LoginEmailPublicationResult.UNCERTAIN);
        verifyNoInteractions(port);
        TransactionOperations failedCommit = new TransactionOperations() {
            @Override
            public <T> T execute(TransactionCallback<T> action) {
                action.doInTransaction(mock(TransactionStatus.class));
                throw new IllegalStateException(email);
            }
        };
        var failed = new CurrentLoginEmailSource(port, failedCommit);
        var synthetic = SyntheticLoginEmailBindings.fact(fact, subject, email, null);
        when(port.publish(synthetic)).thenReturn(LoginEmailPublicationResult.PUBLISHED);
        when(port.observe(subject, fact)).thenReturn(CurrentLoginEmailObservation.current(subject, fact, email));
        assertThat(failed.publish(synthetic)).isEqualTo(LoginEmailPublicationResult.UNCERTAIN);
        assertThat(failed.observe(subject, fact).outcome()).isEqualTo(CurrentLoginEmailObservation.Outcome.UNCERTAIN);
    }

    @Test
    void testOnlyBindingCannotBeMadeByOrdinaryCallerOrFramework() throws Exception {
        assertThat(Modifier.isFinal(AcceptedLoginEmailBinding.class.getModifiers())).isTrue();
        assertThat(Arrays.stream(AcceptedLoginEmailBinding.class.getDeclaredConstructors()))
                .allMatch(constructor -> Modifier.isPrivate(constructor.getModifiers()));
        assertThat(Arrays.stream(AcceptedLoginEmailBinding.class.getDeclaredMethods()))
                .noneMatch(method -> method.getReturnType() == AcceptedLoginEmailBinding.class);
        var synthetic = SyntheticLoginEmailBindings.fact(fact, subject, email, null);
        assertThat(synthetic.toString()).doesNotContain(email, fact.toString());
        Map<String, Object> fields = Map.of("fact", fact.toString(),
                "subject", Map.of("value", subject.value().toString()), "email", email);
        ObjectMapper mapper = new ObjectMapper().registerModule(new ParameterNamesModule());
        assertThatThrownBy(() -> mapper.readValue(mapper.writeValueAsString(fields), AcceptedLoginEmailBinding.class))
                .isInstanceOf(InvalidDefinitionException.class);
        ObjectMapper spring = Jackson2ObjectMapperBuilder.json().build();
        assertThatThrownBy(() -> spring.convertValue(fields, AcceptedLoginEmailBinding.class))
                .isInstanceOf(IllegalArgumentException.class).hasCauseInstanceOf(InvalidDefinitionException.class);
        assertThatThrownBy(() -> SyntheticLoginEmailBindings.fact(fact, subject, "", null))
                .isInstanceOf(IllegalArgumentException.class).hasMessageNotContaining(email);
        assertThatThrownBy(() -> SyntheticLoginEmailBindings.fact(fact, subject, email, fact))
                .isInstanceOf(IllegalArgumentException.class).hasMessageNotContaining(email);
    }

    @Test
    void springConstructorBindingCannotIssueAcceptedLoginEmailBinding() {
        for (Object suppliedKey : new Object[]{null, new Object()}) {
            Map<String, Object> fields = new java.util.HashMap<>();
            fields.put("fact", fact);
            fields.put("subject", subject);
            fields.put("email", email);
            fields.put("supersedes", null);
            fields.put("issuanceKey", suppliedKey);
            DataBinder binder = new DataBinder(null);
            binder.setTargetType(ResolvableType.forClass(AcceptedLoginEmailBinding.class));
            try {
                binder.construct(new DataBinder.ValueResolver() {
                    @Override
                    public Object resolveValue(String name, Class<?> type) {
                        return fields.get(name);
                    }

                    @Override
                    public Set<String> getNames() {
                        return fields.keySet();
                    }
                });
            } catch (RuntimeException rejected) {
                // A rejected constructor may surface directly or as a binding error.
            }
            assertThat(binder.getTarget()).isNull();
        }
    }
}
