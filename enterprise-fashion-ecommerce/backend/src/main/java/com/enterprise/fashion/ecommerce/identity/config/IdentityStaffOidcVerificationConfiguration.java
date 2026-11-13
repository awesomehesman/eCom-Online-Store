package com.enterprise.fashion.ecommerce.identity.config;

import com.enterprise.fashion.ecommerce.identity.adapter.out.staff.UnavailableStaffOidcAuthenticationVerifier;
import com.enterprise.fashion.ecommerce.identity.application.port.out.StaffOidcAuthenticationVerifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Fail-closed verifier composition only. The orchestration remains unwired until the V16 service
 * has admitted application-owned registration and timing configuration.
 */
@Configuration(proxyBeanMethods = false)
public class IdentityStaffOidcVerificationConfiguration {
    @Bean StaffOidcAuthenticationVerifier staffOidcAuthenticationVerifier() {
        return new UnavailableStaffOidcAuthenticationVerifier();
    }
}
