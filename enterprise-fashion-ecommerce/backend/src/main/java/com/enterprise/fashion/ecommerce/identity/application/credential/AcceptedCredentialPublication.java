package com.enterprise.fashion.ecommerce.identity.application.credential;

import java.util.Objects;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.identity.domain.model.CredentialSubject;
import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Capability for an already-accepted local Customer password fact, not an acceptance request.
 * Deliberately has NO production constructor/factory/issuer accessible to callers.
 * Future issuance requires separately reviewed upstream policy and confirmation integration.
 * In particular, confirmed effects must participate in the same publication/observation
 * transaction protocol; confirming an effect elsewhere and publishing it later is unsupported.
 */
public final class AcceptedCredentialPublication {
    private final UUID fact;
    private final CredentialSubject subject;
    private final String verifier;
    private final UUID supersedes;

    // Caller-supplied serialized fields cannot reconstruct upstream acceptance.
    @JsonCreator(mode = JsonCreator.Mode.DISABLED)
    private AcceptedCredentialPublication(UUID fact, CredentialSubject subject,
            String verifier, UUID supersedes) {
        this.fact = Objects.requireNonNull(fact, "Fact is required");
        this.subject = Objects.requireNonNull(subject, "Subject is required");
        if (verifier == null || verifier.isBlank() || verifier.length() > 128) {
            throw new IllegalArgumentException("Invalid bounded verifier representation");
        }
        if (fact.equals(supersedes)) {
            throw new IllegalArgumentException("A fact cannot supersede itself");
        }
        this.verifier = verifier;
        this.supersedes = supersedes;
    }

    public UUID fact() { return fact; }
    public CredentialSubject subject() { return subject; }
    /** Sensitive internal persistence handoff; never ordinary evidence or diagnostics. */
    public String verifier() { return verifier; }
    public UUID supersedes() { return supersedes; }

    @Override
    public String toString() { return "AcceptedCredentialPublication[redacted]"; }
}
