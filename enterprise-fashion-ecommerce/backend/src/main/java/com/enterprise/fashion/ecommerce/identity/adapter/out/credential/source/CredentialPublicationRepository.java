package com.enterprise.fashion.ecommerce.identity.adapter.out.credential.source;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;

/** Deliberately exposes no save/update/delete operation. */
interface CredentialPublicationRepository extends Repository<CredentialPublicationRow, UUID> {
    Optional<CredentialPublicationRow> findById(UUID fact);

    @Query("SELECT * FROM identity.credential_publication WHERE subject = :subject AND context = :context")
    List<CredentialPublicationRow> forSubject(UUID subject, String context);
}
