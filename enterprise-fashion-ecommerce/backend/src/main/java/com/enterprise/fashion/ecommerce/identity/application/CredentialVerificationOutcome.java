package com.enterprise.fashion.ecommerce.identity.application;

/**
 * Identity-owned credential evidence only; no outcome represents Authentication,
 * Principal, Session, association, ownership, or Authorization authority.
 */
public enum CredentialVerificationOutcome {
    MATCH,
    NO_MATCH,
    REJECTED
}
