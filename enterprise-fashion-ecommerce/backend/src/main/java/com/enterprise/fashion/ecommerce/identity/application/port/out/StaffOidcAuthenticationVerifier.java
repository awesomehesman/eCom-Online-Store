package com.enterprise.fashion.ecommerce.identity.application.port.out;

import com.enterprise.fashion.ecommerce.identity.application.staff.StaffOidcAuthenticationVerification;
import com.enterprise.fashion.ecommerce.identity.application.staff.StaffOidcAuthorizationCallback;
import com.enterprise.fashion.ecommerce.identity.application.staff.StaffOidcAuthorizationTransaction;

/**
 * Future provider adapter boundary. An approved implementation must perform server-side code
 * exchange and PKCE S256 correlation, verify an admitted registration's signature/key, exact
 * issuer, audience/authorized party, nonce and temporal claims, and establish stable subject,
 * provider-specific assurance and authentication freshness. Malformed, ambiguous or untrusted
 * evidence must fail closed. This negative-only milestone cannot issue verified Staff evidence.
 */
public interface StaffOidcAuthenticationVerifier {
    StaffOidcAuthenticationVerification verify(StaffOidcAuthorizationTransaction consumedTransaction,
            StaffOidcAuthorizationCallback callback);
}
