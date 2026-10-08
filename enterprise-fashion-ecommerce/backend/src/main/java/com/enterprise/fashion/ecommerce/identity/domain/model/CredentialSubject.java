package com.enterprise.fashion.ecommerce.identity.domain.model;

import java.util.Objects;
import java.util.UUID;

/** Internal stable reference only: construction proves neither existence nor association. */
public record CredentialSubject(UUID value) {
    public CredentialSubject {
        Objects.requireNonNull(value, "Credential subject is required");
    }

    @Override
    public String toString() {
        return "CredentialSubject[redacted]";
    }
}
