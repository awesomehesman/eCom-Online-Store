package com.enterprise.fashion.ecommerce.identity.application.staff;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Provider-neutral, pre-authentication protocol state. Possession or consumption of this value
 * establishes no Identity, Staff association, Authentication, Session or Authorization.
 */
public final class StaffOidcAuthorizationTransaction {
    private static final Pattern BASE64_URL = Pattern.compile("[A-Za-z0-9_-]{43,128}");
    private static final Pattern PKCE_VERIFIER = Pattern.compile("[A-Za-z0-9._~-]{43,128}");

    public enum Status { ACTIVE, CONSUMED, SUPERSEDED, EXPIRED, CORRUPT }

    private final UUID transactionId;
    private final String state;
    private final String nonce;
    private final String pkceVerifier;
    private final String pkceChallenge;
    private final String initiatingSessionId;
    private final String registrationId;
    private final Instant createdAt;
    private final Instant expiresAt;
    private final Status status;

    public StaffOidcAuthorizationTransaction(UUID transactionId, String state, String nonce,
            String pkceVerifier, String pkceChallenge, String initiatingSessionId,
            String registrationId, Instant createdAt, Instant expiresAt, Status status) {
        this.transactionId = Objects.requireNonNull(transactionId, "Transaction identity is required");
        this.state = secret(state, BASE64_URL, "state");
        this.nonce = secret(nonce, BASE64_URL, "nonce");
        this.pkceVerifier = secret(pkceVerifier, PKCE_VERIFIER, "PKCE verifier");
        this.pkceChallenge = secret(pkceChallenge, BASE64_URL, "PKCE challenge");
        this.initiatingSessionId = bounded(initiatingSessionId, 100, "initiating Session");
        this.registrationId = bounded(registrationId, 128, "registration");
        this.createdAt = Objects.requireNonNull(createdAt, "Creation time is required");
        this.expiresAt = Objects.requireNonNull(expiresAt, "Expiration time is required");
        this.status = Objects.requireNonNull(status, "Lifecycle state is required");
        if (!expiresAt.isAfter(createdAt)) {
            throw new IllegalArgumentException("Expiration must follow creation");
        }
        if (!StaffOidcAuthorizationTransactions.deriveS256(pkceVerifier).equals(pkceChallenge)) {
            throw new IllegalArgumentException("PKCE material is inconsistent");
        }
    }

    public UUID transactionId() { return transactionId; }
    public String state() { return state; }
    public String nonce() { return nonce; }
    public String pkceVerifier() { return pkceVerifier; }
    public String pkceChallenge() { return pkceChallenge; }
    public String initiatingSessionId() { return initiatingSessionId; }
    public String registrationId() { return registrationId; }
    public Instant createdAt() { return createdAt; }
    public Instant expiresAt() { return expiresAt; }
    public Status status() { return status; }

    public StaffOidcAuthorizationTransaction withStatus(Status replacement) {
        return new StaffOidcAuthorizationTransaction(transactionId, state, nonce, pkceVerifier,
                pkceChallenge, initiatingSessionId, registrationId, createdAt, expiresAt, replacement);
    }

    @Override
    public String toString() {
        return "StaffOidcAuthorizationTransaction[id=" + transactionId + ", status=" + status + "]";
    }

    private static String secret(String value, Pattern pattern, String label) {
        if (value == null || !pattern.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid " + label);
        }
        return value;
    }

    private static String bounded(String value, int maximum, String label) {
        if (value == null || value.isBlank() || value.length() > maximum
                || value.codePoints().anyMatch(Character::isISOControl)) {
            throw new IllegalArgumentException("Invalid " + label);
        }
        return value;
    }
}
