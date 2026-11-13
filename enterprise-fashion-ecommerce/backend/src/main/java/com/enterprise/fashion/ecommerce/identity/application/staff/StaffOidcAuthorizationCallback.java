package com.enterprise.fashion.ecommerce.identity.application.staff;

import java.util.Objects;

/**
 * Opaque, unverified authorization response input. Shape validation establishes no OIDC trust.
 * Code and provider error detail are deliberately excluded from diagnostic representation.
 */
public final class StaffOidcAuthorizationCallback {
    public enum Kind { AUTHORIZATION_CODE, REJECTED }

    private static final int MAXIMUM_VALUE_LENGTH = 4096;

    private final Kind kind;
    private final String value;

    private StaffOidcAuthorizationCallback(Kind kind, String value) {
        this.kind = Objects.requireNonNull(kind, "Callback kind is required");
        this.value = bounded(value);
    }

    public static StaffOidcAuthorizationCallback authorizationCode(String code) {
        return new StaffOidcAuthorizationCallback(Kind.AUTHORIZATION_CODE, code);
    }

    public static StaffOidcAuthorizationCallback rejected(String providerError) {
        return new StaffOidcAuthorizationCallback(Kind.REJECTED, providerError);
    }

    public Kind kind() { return kind; }

    /** Secret-bearing protocol value for the future trusted verifier only. */
    public String value() { return value; }

    @Override
    public String toString() {
        return "StaffOidcAuthorizationCallback[" + kind + "]";
    }

    private static String bounded(String value) {
        if (value == null || value.isBlank() || value.length() > MAXIMUM_VALUE_LENGTH
                || value.codePoints().anyMatch(Character::isISOControl)) {
            throw new IllegalArgumentException("Invalid authorization callback");
        }
        return value;
    }
}
