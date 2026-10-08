package com.enterprise.fashion.ecommerce.identity.application.credential;

import java.util.Objects;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.identity.application.port.out.CredentialSourcePort.Observation;
import com.enterprise.fashion.ecommerce.identity.domain.model.CredentialSubject;
import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Sensitive source mapping used only within Identity verification. It is not an acceptance
 * capability or a reusable currentness claim. Callers cannot submit it to the composition API.
 * Only the trusted source Port supplies observations; final reliance requires revalidation.
 */
public final class CredentialVerifierObservation {
    public static final String LOCAL_PASSWORD_CONTEXT = "LOCAL_CUSTOMER_PASSWORD";

    private final Observation observation;
    private final CredentialSubject subject;
    private final UUID fact;
    private final String context;
    private final String verifier;
    private final UUID supersedes;

    @JsonCreator(mode = JsonCreator.Mode.DISABLED)
    private CredentialVerifierObservation(Observation observation, CredentialSubject subject,
            UUID fact, String context, String verifier, UUID supersedes) {
        this.observation = Objects.requireNonNull(observation);
        this.subject = subject;
        this.fact = fact;
        this.context = context;
        this.verifier = verifier;
        this.supersedes = supersedes;
    }

    /** Mapping factory for the trusted Adapter, not upstream credential acceptance. */
    public static CredentialVerifierObservation applicable(CredentialSubject subject, UUID fact,
            String context, String verifier, UUID supersedes) {
        return new CredentialVerifierObservation(Observation.BOUND_APPLICABLE,
                subject, fact, context, verifier, supersedes);
    }

    public static CredentialVerifierObservation nonConfirming(Observation observation) {
        if (observation == Observation.BOUND_APPLICABLE) {
            throw new IllegalArgumentException("Applicable observation requires source binding");
        }
        return new CredentialVerifierObservation(observation, null, null, null, null, null);
    }

    public Observation observation() { return observation; }

    boolean boundTo(CredentialSubject requestedSubject, UUID requestedFact) {
        return observation == Observation.BOUND_APPLICABLE
                && requestedSubject != null && requestedSubject.equals(subject)
                && requestedFact != null && requestedFact.equals(fact)
                && LOCAL_PASSWORD_CONTEXT.equals(context)
                && verifier != null && !verifier.isBlank();
    }

    boolean sameBinding(CredentialVerifierObservation other) {
        return other != null && boundTo(other.subject, other.fact)
                && other.boundTo(subject, fact) && verifier.equals(other.verifier)
                && Objects.equals(supersedes, other.supersedes);
    }

    CredentialSubject subject() { return subject; }
    UUID fact() { return fact; }
    String verifier() { return verifier; }

    @Override
    public String toString() { return "CredentialVerifierObservation[redacted]"; }
}
