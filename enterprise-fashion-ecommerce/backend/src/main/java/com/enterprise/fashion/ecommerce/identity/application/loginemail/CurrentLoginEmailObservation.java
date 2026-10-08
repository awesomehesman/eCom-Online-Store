package com.enterprise.fashion.ecommerce.identity.application.loginemail;

import java.util.Objects;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.identity.domain.model.CredentialSubject;

/** Source observation of an already-authorized binding, never email verification or Login success. */
public final class CurrentLoginEmailObservation {
    public enum Outcome { CURRENT, SUPERSEDED, CONFLICT, UNCERTAIN }

    private final Outcome outcome;
    private final CredentialSubject subject;
    private final UUID fact;
    private final String email;

    private CurrentLoginEmailObservation(Outcome outcome, CredentialSubject subject, UUID fact, String email) {
        this.outcome = Objects.requireNonNull(outcome);
        this.subject = subject;
        this.fact = fact;
        this.email = email;
    }

    /** For the trusted source Adapter only. This does not accept an external authority claim. */
    public static CurrentLoginEmailObservation current(CredentialSubject subject, UUID fact, String email) {
        Objects.requireNonNull(subject);
        Objects.requireNonNull(fact);
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Invalid source binding");
        }
        return new CurrentLoginEmailObservation(Outcome.CURRENT, subject, fact, email);
    }

    public static CurrentLoginEmailObservation nonConfirming(Outcome outcome) {
        if (outcome == null || outcome == Outcome.CURRENT) {
            throw new IllegalArgumentException("A non-confirming source outcome is required");
        }
        return new CurrentLoginEmailObservation(outcome, null, null, null);
    }

    public Outcome outcome() { return outcome; }
    public CredentialSubject subject() { return subject; }
    public UUID fact() { return fact; }
    /** Sensitive value only for a current binding; never expose in ordinary outputs. */
    public String email() { return email; }

    public boolean boundTo(CredentialSubject requestedSubject, UUID requestedFact) {
        return outcome == Outcome.CURRENT && Objects.equals(subject, requestedSubject)
                && Objects.equals(fact, requestedFact) && email != null && !email.isBlank();
    }

    @Override
    public String toString() { return "CurrentLoginEmailObservation[" + outcome + ", redacted]"; }
}
