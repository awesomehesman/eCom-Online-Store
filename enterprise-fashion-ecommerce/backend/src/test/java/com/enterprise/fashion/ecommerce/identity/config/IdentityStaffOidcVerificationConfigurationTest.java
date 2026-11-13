package com.enterprise.fashion.ecommerce.identity.config;

import com.enterprise.fashion.ecommerce.identity.adapter.out.staff.UnavailableStaffOidcAuthenticationVerifier;
import com.enterprise.fashion.ecommerce.identity.application.port.out.StaffOidcAuthenticationVerifier;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.*;

class IdentityStaffOidcVerificationConfigurationTest {
    @Test void compositionContainsOnlyTheUnavailableVerifierAndLeavesOrchestrationUnwired() {
        try (var context = new AnnotationConfigApplicationContext(
                IdentityStaffOidcVerificationConfiguration.class)) {
            assertThat(context.getBeansOfType(StaffOidcAuthenticationVerifier.class)).hasSize(1);
            assertThat(context.getBean(StaffOidcAuthenticationVerifier.class))
                    .isExactlyInstanceOf(UnavailableStaffOidcAuthenticationVerifier.class);
            assertThat(context.getBeanDefinitionNames()).noneMatch(name -> name.contains("verifyStaffOidc"));
        }
    }
}
