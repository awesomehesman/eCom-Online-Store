package com.enterprise.fashion.ecommerce.identity.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enterprise.fashion.ecommerce.EnterpriseFashionEcommerceApplication;

@WebMvcTest(
        controllers = IdentitySecurityConfigurationTest.ProtectedTestController.class,
        excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class)
@Import(IdentitySecurityConfiguration.class)
class IdentitySecurityConfigurationTest {

    private static final String PROTECTED_RESOURCE = "/test-only/protected";

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void loadsProjectOwnedSecurityWithoutBootsGeneratedUser() {
        assertThat(applicationContext.getBeansOfType(SecurityFilterChain.class))
                .containsKey("identitySecurityFilterChain");
        assertThat(applicationContext.getBeansOfType(UserDetailsService.class)).isEmpty();
        assertThat(applicationContext.getBeansOfType(InMemoryUserDetailsManager.class)).isEmpty();

        SpringBootApplication application = AnnotatedElementUtils.findMergedAnnotation(
                EnterpriseFashionEcommerceApplication.class, SpringBootApplication.class);

        assertThat(application).isNotNull();
        assertThat(application.exclude()).contains(UserDetailsServiceAutoConfiguration.class);
    }

    @Test
    void deniesUnauthenticatedRequestsWithoutFormLoginOrHttpBasic() throws Exception {
        mockMvc.perform(get(PROTECTED_RESOURCE))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist("Location"))
                .andExpect(header().doesNotExist("WWW-Authenticate"));

        assertThat(filterTypesFor(PROTECTED_RESOURCE))
                .doesNotContain(UsernamePasswordAuthenticationFilter.class, BasicAuthenticationFilter.class);
    }

    @Test
    void preservesCsrfProtectionForUnsafeRequests() throws Exception {
        assertThat(springSecurityFilterChain.getFilters(PROTECTED_RESOURCE))
                .anyMatch(CsrfFilter.class::isInstance);

        mockMvc.perform(post(PROTECTED_RESOURCE))
                .andExpect(status().isForbidden());
    }

    @Test
    void doesNotExposeImplicitLogoutHandling() throws Exception {
        assertThat(filterTypesFor("/logout")).doesNotContain(LogoutFilter.class);

        mockMvc.perform(post("/logout").with(csrf()))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist("Location"));
    }

    private List<Class<?>> filterTypesFor(String path) {
        return springSecurityFilterChain.getFilters(path).stream()
                .map(Object::getClass)
                .toList();
    }

    @RestController
    static class ProtectedTestController {

        @GetMapping(PROTECTED_RESOURCE)
        String read() {
            return "protected";
        }

        @PostMapping(PROTECTED_RESOURCE)
        String write() {
            return "protected";
        }
    }
}
