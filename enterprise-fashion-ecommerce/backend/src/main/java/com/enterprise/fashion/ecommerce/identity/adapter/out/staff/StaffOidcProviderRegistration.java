package com.enterprise.fashion.ecommerce.identity.adapter.out.staff;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Objects;

/**
 * Dormant, explicitly supplied Staff OIDC provider-registration metadata. A valid instance is not
 * provider admission and cannot perform discovery, transport, credential loading or Authentication.
 */
final class StaffOidcProviderRegistration {
    enum ClientAuthenticationMethod {
        CLIENT_SECRET_BASIC,
        PRIVATE_KEY_JWT
    }

    private static final int MAXIMUM_REGISTRATION_ID_LENGTH = 128;
    private static final int MAXIMUM_CLIENT_ID_LENGTH = 256;
    private static final int MAXIMUM_OWNER_REFERENCE_LENGTH = 256;
    private static final Duration MAXIMUM_CONNECTION_TIMEOUT = Duration.ofSeconds(60);
    private static final Duration MAXIMUM_RESPONSE_TIMEOUT = Duration.ofMinutes(2);
    private static final int MAXIMUM_RESPONSE_SIZE = 1024 * 1024;

    private final String registrationId;
    private final long revision;
    private final URI issuer;
    private final String clientId;
    private final URI redirectUri;
    private final URI tokenEndpointUri;
    private final ClientAuthenticationMethod clientAuthenticationMethod;
    private final StaffOidcTokenTrust tokenTrust;
    private final Duration connectionTimeout;
    private final Duration responseTimeout;
    private final int maximumResponseSize;
    private final boolean enabled;
    private final String ownerReference;

    StaffOidcProviderRegistration(String registrationId, long revision, URI issuer, String clientId,
            URI redirectUri, URI tokenEndpointUri,
            ClientAuthenticationMethod clientAuthenticationMethod, StaffOidcTokenTrust tokenTrust,
            Duration connectionTimeout, Duration responseTimeout, int maximumResponseSize,
            boolean enabled, String ownerReference) {
        this.registrationId = bounded(registrationId, MAXIMUM_REGISTRATION_ID_LENGTH,
                "registration identifier");
        if (revision <= 0) throw new IllegalArgumentException("Registration revision is invalid");
        this.revision = revision;
        this.issuer = httpsUri(issuer, false, "issuer");
        this.clientId = bounded(clientId, MAXIMUM_CLIENT_ID_LENGTH, "client identifier");
        this.redirectUri = httpsUri(redirectUri, true, "redirect URI");
        this.tokenEndpointUri = httpsUri(tokenEndpointUri, false, "token endpoint URI");
        this.clientAuthenticationMethod = Objects.requireNonNull(clientAuthenticationMethod,
                "Client authentication method is required");
        this.tokenTrust = Objects.requireNonNull(tokenTrust, "Token trust is required");
        if (!this.issuer.toASCIIString().equals(tokenTrust.issuer())) {
            throw new IllegalArgumentException("Registration issuer and token trust must match exactly");
        }
        if (!this.clientId.equals(tokenTrust.clientId())) {
            throw new IllegalArgumentException("Registration client and token trust must match exactly");
        }
        this.connectionTimeout = positiveBounded(connectionTimeout, MAXIMUM_CONNECTION_TIMEOUT,
                "Connection timeout");
        this.responseTimeout = positiveBounded(responseTimeout, MAXIMUM_RESPONSE_TIMEOUT,
                "Response timeout");
        if (maximumResponseSize <= 0 || maximumResponseSize > MAXIMUM_RESPONSE_SIZE) {
            throw new IllegalArgumentException("Maximum response size is invalid");
        }
        this.maximumResponseSize = maximumResponseSize;
        this.enabled = enabled;
        this.ownerReference = bounded(ownerReference, MAXIMUM_OWNER_REFERENCE_LENGTH,
                "owner reference");
    }

    String registrationId() { return registrationId; }
    long revision() { return revision; }
    URI issuer() { return issuer; }
    String clientId() { return clientId; }
    URI redirectUri() { return redirectUri; }
    URI tokenEndpointUri() { return tokenEndpointUri; }
    ClientAuthenticationMethod clientAuthenticationMethod() { return clientAuthenticationMethod; }
    StaffOidcTokenTrust tokenTrust() { return tokenTrust; }
    Duration connectionTimeout() { return connectionTimeout; }
    Duration responseTimeout() { return responseTimeout; }
    int maximumResponseSize() { return maximumResponseSize; }
    boolean enabled() { return enabled; }
    String ownerReference() { return ownerReference; }

    @Override public String toString() {
        return "StaffOidcProviderRegistration[registrationId=" + registrationId
                + ", revision=" + revision + ", enabled=" + enabled + "]";
    }

    private static String bounded(String value, int maximum, String label) {
        if (value == null || value.isBlank() || value.length() > maximum
                || value.codePoints().anyMatch(Character::isISOControl)) {
            throw new IllegalArgumentException("Invalid " + label);
        }
        return value;
    }

    private static URI httpsUri(URI value, boolean queryAllowed, String label) {
        Objects.requireNonNull(value, label + " is required");
        try {
            value.parseServerAuthority();
        } catch (URISyntaxException invalidAuthority) {
            throw new IllegalArgumentException("Invalid " + label);
        }
        String host = value.getHost();
        String rawAuthority = value.getRawAuthority();
        if (!value.isAbsolute() || !"https".equals(value.getScheme()) || host == null || host.isBlank()
                || rawAuthority == null || rawAuthority.indexOf('%') >= 0 || value.getUserInfo() != null
                || value.getFragment() != null || !queryAllowed && value.getQuery() != null
                || value.getPort() < -1 || value.getPort() > 65535 || rawAuthority.endsWith(":")
                || !value.normalize().equals(value)
                || host.codePoints().anyMatch(character -> character > 0x7f || Character.isISOControl(character))) {
            throw new IllegalArgumentException("Invalid " + label);
        }
        return value;
    }

    private static Duration positiveBounded(Duration value, Duration maximum, String label) {
        Objects.requireNonNull(value, label + " is required");
        if (value.isZero() || value.isNegative() || value.compareTo(maximum) > 0) {
            throw new IllegalArgumentException(label + " is invalid");
        }
        return value;
    }
}
