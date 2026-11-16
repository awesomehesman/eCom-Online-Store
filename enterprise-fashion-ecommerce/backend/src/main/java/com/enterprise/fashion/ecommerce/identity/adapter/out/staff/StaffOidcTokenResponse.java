package com.enterprise.fashion.ecommerce.identity.adapter.out.staff;

import java.util.Objects;

/** Internal unverified ID-token transport value, not Staff authentication evidence. */
record StaffOidcTokenResponse(String idToken) {
    StaffOidcTokenResponse {
        Objects.requireNonNull(idToken, "ID token is required");
        if (idToken.isBlank() || idToken.codePoints().anyMatch(Character::isISOControl)) {
            throw new IllegalArgumentException("ID token is invalid");
        }
    }

    @Override public String toString() { return "StaffOidcTokenResponse[redacted]"; }
}
