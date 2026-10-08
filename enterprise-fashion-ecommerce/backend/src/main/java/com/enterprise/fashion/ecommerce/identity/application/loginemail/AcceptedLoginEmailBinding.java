package com.enterprise.fashion.ecommerce.identity.application.loginemail;

import java.util.Objects;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.identity.domain.model.CredentialSubject;
import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Capability for an already-authorized Identity login-email binding fact.
 * No production issuer exists. Construction or storage cannot establish lawful population.
 */
public final class AcceptedLoginEmailBinding {
    private static final Object ISSUANCE_KEY = new Object();
    private final UUID fact;
    private final CredentialSubject subject;
    private final String email;
    private final UUID supersedes;

    @JsonCreator(mode = JsonCreator.Mode.DISABLED)
    private AcceptedLoginEmailBinding(Object issuanceKey, UUID fact, CredentialSubject subject,
            String email, UUID supersedes) {
        if (issuanceKey != ISSUANCE_KEY) {
            throw new IllegalArgumentException("Invalid accepted login-email binding");
        }
        this.fact = Objects.requireNonNull(fact, "Binding fact is required");
        this.subject = Objects.requireNonNull(subject, "Identity reference is required");
        if (email == null || email.isBlank() || fact.equals(supersedes)) {
            throw new IllegalArgumentException("Invalid accepted login-email binding");
        }
        this.email = email;
        this.supersedes = supersedes;
    }

    public UUID fact() { return fact; }
    public CredentialSubject subject() { return subject; }
    /** Sensitive Identity-only handoff; never an ordinary response or diagnostic. */
    public String email() { return email; }
    public UUID supersedes() { return supersedes; }

    @Override
    public String toString() { return "AcceptedLoginEmailBinding[redacted]"; }
}
