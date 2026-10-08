package com.enterprise.fashion.ecommerce.identity.adapter.out.loginemail.source;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/** Immutable source fact; the row itself does not prove lawful Identity publication. */
@Table(name = "login_email_binding", schema = "identity")
record LoginEmailBindingRow(@Id UUID fact, UUID subject, String email, UUID supersedes) {
    @Override
    public String toString() { return "LoginEmailBindingRow[redacted]"; }
}
