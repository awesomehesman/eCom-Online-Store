package com.enterprise.fashion.ecommerce.identity.config;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.Duration;
import java.util.Base64;
import java.util.List;

import com.enterprise.fashion.ecommerce.product.application.administration.GetProductForAdministration;
import com.enterprise.fashion.ecommerce.product.application.administration.ListProductsForAdministration;
import com.enterprise.fashion.ecommerce.product.application.usecase.CreateDraftProduct;
import com.enterprise.fashion.ecommerce.product.application.usecase.UpdateDraftProduct;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.session.jdbc.JdbcIndexedSessionRepository;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** BIDN-REQ-011, 012, 021, 024–026: non-operational boundary evidence, not Staff authentication. */
@Tag("integration")
@SpringBootTest(properties = "product.catalogue.cursor-secret=AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
@AutoConfigureMockMvc
@Testcontainers
@Import(IdentityStaffAuthenticationBoundaryIntegrationTest.ProbeConfiguration.class)
class IdentityStaffAuthenticationBoundaryIntegrationTest {
    private static final String PROBE = "/test-only/identity/principal-boundary";
    @Container @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:18");
    @Autowired private MockMvc http;
    @Autowired private JdbcIndexedSessionRepository sessions;
    @MockitoSpyBean private CreateDraftProduct create;
    @MockitoSpyBean private UpdateDraftProduct update;
    @MockitoSpyBean private GetProductForAdministration get;
    @MockitoSpyBean private ListProductsForAdministration list;

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    void restoredCustomerContextAndUntrustedStaffClaimsNeverGrantProductAuthority() throws Exception {
        SessionRepository<Session> repository = (SessionRepository) sessions;
        Session session = repository.createSession();
        session.setMaxInactiveInterval(Duration.ofHours(1));
        // Synthetic authenticated Customer-labelled fixture, not an executed Customer login or Staff assignment.
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                new SecurityContextImpl(UsernamePasswordAuthenticationToken.authenticated(
                        "synthetic-customer", null, List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER")))));
        repository.save(session);
        var cookie = new Cookie("SESSION", Base64.getEncoder().encodeToString(session.getId().getBytes(StandardCharsets.UTF_8)));
        try {
            // Positive control: real persisted context is restored by the existing ordinary Identity chain.
            http.perform(get(PROBE).cookie(cookie)).andExpect(status().isOk())
                    .andExpect(content().string("synthetic-customer"));
            var access = repository.findById(session.getId()).getLastAccessedTime();
            var result = http.perform(post("/api/v1/admin/products").cookie(cookie)
                    .header("X-Staff-Id", "claimed-staff").header("X-Role", "STAFF_ADMIN")
                    .header("X-MFA", "true").header("X-Correlation-ID", "boundary-test")
                    .contentType("application/json").content("{\"staffAuthenticated\":true}"))
                    .andExpect(status().isUnauthorized())
                    .andExpect(header().string("Cache-Control", "no-store"))
                    .andExpect(header().string("X-Correlation-ID", "boundary-test"))
                    .andExpect(header().doesNotExist("Set-Cookie")).andReturn();
            assertThat(result.getRequest().getSession(false)).isNull();
            assertThat(repository.findById(session.getId()).getLastAccessedTime()).isEqualTo(access);
            assertThat(result.getResponse().getContentAsString()).doesNotContain("synthetic-customer", "claimed-staff", "STAFF_ADMIN");
            verifyNoInteractions(create, update, get, list);
        } finally {
            repository.deleteById(session.getId());
        }
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    void callerStaffAndAssuranceClaimsCannotEstablishAuthentication() throws Exception {
        var result = http.perform(get(PROBE).header("X-Staff-Id", "claimed-staff")
                .header("X-Role", "STAFF_ADMIN").header("X-MFA", "true")
                .header("Authorization", "Bearer untrusted-assertion"))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist("Location")).andReturn();
        assertThat(result.getRequest().getUserPrincipal()).isNull();
        // Existing request-cache behavior may create an unauthenticated Session; it is not Staff evidence.
        Cookie issued = result.getResponse().getCookie("SESSION");
        assertThat(issued).isNotNull();
        String identifier = new String(Base64.getDecoder().decode(issued.getValue()), StandardCharsets.UTF_8);
        SessionRepository<Session> repository = (SessionRepository) sessions;
        Session saved = repository.findById(identifier);
        assertThat(saved).isNotNull();
        try {
            Object context = saved.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
            assertThat(context).isNull();
            var replay = new Cookie("SESSION", Base64.getEncoder().encodeToString(saved.getId().getBytes(StandardCharsets.UTF_8)));
            var retried = http.perform(get(PROBE).cookie(replay)).andExpect(status().isForbidden()).andReturn();
            assertThat(retried.getRequest().getUserPrincipal()).isNull();
            verifyNoInteractions(create, update, get, list);
        } finally {
            sessions.deleteById(saved.getId());
        }
    }

    @TestConfiguration(proxyBeanMethods = false)
    static class ProbeConfiguration {
        @Bean PrincipalProbe principalProbe() { return new PrincipalProbe(); }
    }
    @RestController
    static class PrincipalProbe {
        @GetMapping(PROBE)
        String current(Principal principal) { return principal.getName(); }
    }
}
