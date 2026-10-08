package com.enterprise.fashion.ecommerce.identity.adapter.out.credential.source;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/** Immutable stored publication. Provenance depends on controlled writes, not row shape. */
@Table(name = "credential_publication", schema = "identity")
record CredentialPublicationRow(@Id UUID fact, UUID subject, String context,
        String verifier, UUID supersedes) {
    @Override
    public String toString() { return "CredentialPublicationRow[redacted]"; }
}
