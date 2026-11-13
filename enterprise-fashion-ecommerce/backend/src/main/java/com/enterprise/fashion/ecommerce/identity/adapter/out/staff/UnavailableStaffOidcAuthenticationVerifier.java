package com.enterprise.fashion.ecommerce.identity.adapter.out.staff;

import com.enterprise.fashion.ecommerce.identity.application.port.out.StaffOidcAuthenticationVerifier;
import com.enterprise.fashion.ecommerce.identity.application.staff.StaffOidcAuthenticationVerification;
import com.enterprise.fashion.ecommerce.identity.application.staff.StaffOidcAuthorizationCallback;
import com.enterprise.fashion.ecommerce.identity.application.staff.StaffOidcAuthorizationTransaction;

/** Default production verifier: performs no protocol, network, token or evidence operation. */
public final class UnavailableStaffOidcAuthenticationVerifier implements StaffOidcAuthenticationVerifier {
    @Override
    public StaffOidcAuthenticationVerification verify(StaffOidcAuthorizationTransaction consumedTransaction,
            StaffOidcAuthorizationCallback callback) {
        return new StaffOidcAuthenticationVerification(
                StaffOidcAuthenticationVerification.Outcome.VERIFICATION_UNAVAILABLE);
    }
}
