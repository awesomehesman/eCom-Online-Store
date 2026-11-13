package com.enterprise.fashion.ecommerce.identity.config;

import com.enterprise.fashion.ecommerce.identity.adapter.out.staff.UnavailableStaffOidcAuthenticationVerifier;
import com.enterprise.fashion.ecommerce.identity.application.port.out.StaffOidcAuthenticationVerifier;
import com.enterprise.fashion.ecommerce.identity.application.staff.VerifyStaffOidcAuthentication;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.*;

class IdentityStaffOidcVerificationConfigurationTest {
    @Test void compositionContainsOnlyTheUnavailableVerifierAndOrchestrator() {
        try (var context = new AnnotationConfigApplicationContext(
                IdentityStaffOidcVerificationConfiguration.class)) {
            assertThat(context.getBeansOfType(StaffOidcAuthenticationVerifier.class)).hasSize(1);
            assertThat(context.getBean(StaffOidcAuthenticationVerifier.class))
                    .isExactlyInstanceOf(UnavailableStaffOidcAuthenticationVerifier.class);
            assertThat(context.getBeansOfType(VerifyStaffOidcAuthentication.class)).hasSize(1);
        }
    }
}
