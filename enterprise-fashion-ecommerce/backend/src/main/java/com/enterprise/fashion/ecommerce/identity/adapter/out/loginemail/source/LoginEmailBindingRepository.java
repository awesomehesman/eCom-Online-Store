package com.enterprise.fashion.ecommerce.identity.adapter.out.loginemail.source;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;

/** No general caller-facing save, update or delete operation. */
interface LoginEmailBindingRepository extends Repository<LoginEmailBindingRow, UUID> {
    Optional<LoginEmailBindingRow> findById(UUID fact);

    @Query("SELECT * FROM identity.login_email_binding WHERE subject = :subject")
    List<LoginEmailBindingRow> forSubject(UUID subject);
}
