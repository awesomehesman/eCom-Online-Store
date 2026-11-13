package com.enterprise.fashion.ecommerce.identity.config;

import com.enterprise.fashion.ecommerce.identity.adapter.out.staff.UnavailableStaffOidcAuthenticationVerifier;
import com.enterprise.fashion.ecommerce.identity.application.port.out.StaffOidcAuthenticationVerifier;
import com.enterprise.fashion.ecommerce.identity.application.staff.VerifyStaffOidcAuthentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Fail-closed composition only; no provider registration, client, endpoint or evidence issuer. */
@Configuration(proxyBeanMethods = false)
public class IdentityStaffOidcVerificationConfiguration {
    @Bean StaffOidcAuthenticationVerifier staffOidcAuthenticationVerifier() {
        return new UnavailableStaffOidcAuthenticationVerifier();
    }

    @Bean VerifyStaffOidcAuthentication verifyStaffOidcAuthentication(
            StaffOidcAuthenticationVerifier verifier) {
        return new VerifyStaffOidcAuthentication(verifier);
    }
}
