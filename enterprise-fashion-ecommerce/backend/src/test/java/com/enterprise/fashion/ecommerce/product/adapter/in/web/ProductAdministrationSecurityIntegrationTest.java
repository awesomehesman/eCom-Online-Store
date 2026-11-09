package com.enterprise.fashion.ecommerce.product.adapter.in.web;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.enterprise.fashion.ecommerce.product.application.administration.GetProductForAdministration;
import com.enterprise.fashion.ecommerce.product.application.administration.ListProductsForAdministration;
import com.enterprise.fashion.ecommerce.product.application.usecase.CreateDraftProduct;
import com.enterprise.fashion.ecommerce.product.application.usecase.UpdateDraftProduct;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.RequestDispatcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.session.jdbc.JdbcIndexedSessionRepository;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;

@Tag("integration")
@SpringBootTest(properties = "product.catalogue.cursor-secret=AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
@AutoConfigureMockMvc
@Testcontainers
class ProductAdministrationSecurityIntegrationTest {
    @Container @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:18");
    @Autowired private MockMvc http;
    @Autowired private ObjectMapper json;
    @Autowired private JdbcIndexedSessionRepository sessions;
    @MockitoSpyBean private CreateDraftProduct create;
    @MockitoSpyBean private UpdateDraftProduct update;
    @MockitoSpyBean private GetProductForAdministration get;
    @MockitoSpyBean private ListProductsForAdministration list;

    private MockHttpServletRequestBuilder route(String method, String path) {
        return switch (method) { case "POST" -> post(path); case "PUT" -> put(path); default -> get(path); };
    }

    private void denied(MvcResult result, int status) throws Exception {
        var response = result.getResponse();
        assertThat(response.getStatus()).isEqualTo(status);
        assertThat(response.getContentType()).startsWith("application/problem+json");
        assertThat(response.getHeader("Cache-Control")).isEqualTo("no-store");
        String correlation = response.getHeader("X-Correlation-ID");
        assertThat(correlation).matches("[A-Za-z0-9._-]{1,128}");
        var body = json.readTree(response.getContentAsString());
        var fields = new ArrayList<String>();
        body.fieldNames().forEachRemaining(fields::add);
        assertThat(fields).containsExactlyInAnyOrder("type", "title", "status", "correlationId");
        assertThat(body.get("correlationId").asText()).isEqualTo(correlation);
        assertThat(response.getContentAsString()).doesNotContain("PRIVATE", "actor", "evidence", "Exception", "SQL", "staff-secret");
        assertThat(response.getHeaders("Set-Cookie")).isEmpty();
        assertThat(result.getRequest().getSession(false)).isNull();
        verifyNoInteractions(create, update, get, list);
    }

    @ParameterizedTest
    @CsvSource({"POST,/api/v1/admin/products", "PUT,/api/v1/admin/products/PRIVATE-id",
            "GET,/api/v1/admin/products/PRIVATE-id", "GET,/api/v1/admin/products"})
    void anonymousCannotReachAnyProtectedCapability(String method, String path) throws Exception {
        denied(http.perform(route(method, path)).andReturn(), 401);
    }

    @ParameterizedTest
    @CsvSource({"POST,/api/v1/admin/products", "PUT,/api/v1/admin/products/PRIVATE-id",
            "GET,/api/v1/admin/products/PRIVATE-id", "GET,/api/v1/admin/products"})
    void authenticationOrClaimedRolesNeverGrantStaffAuthority(String method, String path) throws Exception {
        for (String role : List.of("CUSTOMER", "USER", "STAFF", "ADMIN")) {
            denied(http.perform(route(method, path).with(user("test-identity").roles(role))).andReturn(), 403);
        }
    }

    @Test
    void claimedPermissionsActorAndAuditReadinessCannotActivateMutations() throws Exception {
        String body = "{\"id\":\"PRIVATE\",\"role\":\"ADMIN\",\"actor\":\"staff-secret\",\"auditReady\":true,\"published\":true}";
        denied(http.perform(post("/api/v1/admin/products").with(user("customer").authorities(() -> "UNAPPROVED_PRODUCT_CREATE_CLAIM"))
                .header("X-Staff-Id", "staff-secret").header("X-Audit-Ready", "true")
                .contentType("application/json").content(body)).andReturn(), 403);
        denied(http.perform(put("/api/v1/admin/products/PRIVATE-id").with(user("customer").roles("ADMIN"))
                .header("If-Match", "forged-revision").contentType("application/json").content("malformed PRIVATE JSON")).andReturn(), 403);
        denied(http.perform(post("/api/v1/admin/products").header("X-Staff-Id", "staff-secret")
                .header("Authorization", "Bearer caller-assertion")).andReturn(), 401);
    }

    @ParameterizedTest
    @CsvSource({"ERROR,jakarta.servlet.error.request_uri", "FORWARD,jakarta.servlet.forward.request_uri",
            "INCLUDE,jakarta.servlet.include.request_uri"})
    void alternateDispatchCannotEnterPublicOrErrorHandlers(String dispatcher, String attribute) throws Exception {
        denied(http.perform(get("/api/v1/products").with(request -> {
            request.setDispatcherType(DispatcherType.valueOf(dispatcher)); return request;
        }).requestAttr(attribute, "/api/v1/admin/products/PRIVATE-id")
                .requestAttr(RequestDispatcher.ERROR_EXCEPTION, new IllegalStateException("PRIVATE diagnostic"))).andReturn(), 401);
    }

    @Test
    void errorRouteAndIdentityRouteCannotEscapeAdministrationDenial() throws Exception {
        for (String target : List.of("/error", "/api/v1/identity/csrf")) {
            denied(http.perform(get(target).with(request -> {
                request.setDispatcherType(DispatcherType.ERROR); return request;
            }).requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "/api/v1/admin/products/PRIVATE-id"))
                    .andReturn(), 401);
        }
        var one = http.perform(get("/api/v1/admin/products/PRIVATE-id").with(user("customer"))
                .header("X-Correlation-ID", "same-correlation")).andReturn();
        var two = http.perform(get("/api/v1/admin/products/does-not-exist").with(user("customer"))
                .header("X-Correlation-ID", "same-correlation")).andReturn();
        denied(one, 403);
        denied(two, 403);
        assertThat(one.getResponse().getContentAsString()).isEqualTo(two.getResponse().getContentAsString());
    }

    @Test
    void correlationIsBoundedAndNoAdministrationValidationLeaksBeforeAuthorization() throws Exception {
        var valid = http.perform(get("/api/v1/admin/products").header("X-Correlation-ID", "safe-correlation")).andReturn();
        denied(valid, 401);
        assertThat(valid.getResponse().getHeader("X-Correlation-ID")).isEqualTo("safe-correlation");
        for (String value : List.of("bad~value", "x".repeat(129))) {
            var invalid = http.perform(get("/api/v1/admin/products").header("X-Correlation-ID", value)
                    .param("cursor", "PRIVATE-invalid").param("limit", "-1")).andReturn();
            denied(invalid, 401);
            assertThat(invalid.getResponse().getHeader("X-Correlation-ID")).isNotEqualTo(value);
        }
        var repeated = http.perform(get("/api/v1/admin/products").header("X-Correlation-ID", "first", "second")).andReturn();
        denied(repeated, 401);
        assertThat(repeated.getResponse().getHeader("X-Correlation-ID")).isNotIn("first", "second");
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    void realSessionCookieIsNotObservedOrRenewed() throws Exception {
        SessionRepository<Session> repository = (SessionRepository) sessions;
        Session session = repository.createSession();
        session.setMaxInactiveInterval(Duration.ofHours(2));
        Instant access = Instant.now().minusSeconds(60);
        session.setLastAccessedTime(access);
        repository.save(session);
        Instant stored = repository.findById(session.getId()).getLastAccessedTime();
        String cookie = java.util.Base64.getEncoder().encodeToString(session.getId().getBytes(java.nio.charset.StandardCharsets.UTF_8));
        denied(http.perform(get("/api/v1/admin/products").cookie(new jakarta.servlet.http.Cookie("SESSION", cookie))).andReturn(), 401);
        assertThat(repository.findById(session.getId()).getLastAccessedTime()).isEqualTo(stored);
    }

    private record PersistedAuthentication(String id, String cookie, Instant lastAccess) { }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private PersistedAuthentication persistAuthenticatedSession(String authority) {
        // Adversarial labels only: no operational Staff Role/Permission is selected.
        SessionRepository<Session> repository = (SessionRepository) sessions;
        Session session = repository.createSession();
        session.setMaxInactiveInterval(Duration.ofHours(2));
        session.setLastAccessedTime(Instant.now().minusSeconds(60));
        var authentication = UsernamePasswordAuthenticationToken.authenticated(
                "synthetic-principal", null, List.of(new SimpleGrantedAuthority(authority)));
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                new SecurityContextImpl(authentication));
        repository.save(session);
        // Read back through JDBC: prove serialized authentication exists, not just request state.
        Session stored = repository.findById(session.getId());
        assertThat(stored).isNotNull();
        SecurityContext context = stored.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        assertThat(context.getAuthentication().isAuthenticated()).isTrue();
        assertThat(context.getAuthentication().getName()).isEqualTo("synthetic-principal");
        assertThat(context.getAuthentication().getAuthorities()).extracting("authority").containsExactly(authority);
        String cookie = java.util.Base64.getEncoder().encodeToString(
                session.getId().getBytes(java.nio.charset.StandardCharsets.UTF_8));
        return new PersistedAuthentication(session.getId(), cookie, stored.getLastAccessedTime());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void assertPersistedAuthenticationUnchanged(PersistedAuthentication persisted, String authority) {
        SessionRepository<Session> repository = (SessionRepository) sessions;
        Session stored = repository.findById(persisted.id());
        assertThat(stored).isNotNull();
        assertThat(stored.getLastAccessedTime()).isEqualTo(persisted.lastAccess());
        SecurityContext context = stored.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        assertThat(context.getAuthentication().isAuthenticated()).isTrue();
        assertThat(context.getAuthentication().getAuthorities()).extracting("authority").containsExactly(authority);
    }

    @ParameterizedTest
    @ValueSource(strings = {"GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"})
    void persistedAuthenticationCannotActivateAdministrationForAnyMethod(String method) throws Exception {
        for (String authority : List.of("PRODUCT_ADMIN", "STAFF_ADMIN")) {
            var persisted = persistAuthenticatedSession(authority);
            try {
                for (String path : List.of("/api/v1/admin/products", "/api/v1/admin/products/PRIVATE-id")) {
                    // Only a real persisted Session cookie: no user()/securityContext() postprocessor.
                    var result = http.perform(request(HttpMethod.valueOf(method), path)
                            .cookie(new jakarta.servlet.http.Cookie("SESSION", persisted.cookie()))
                            .header("X-Correlation-ID", "persisted-context-test")).andReturn();
                    // The stateless perimeter deliberately does not restore authentication from Session.
                    denied(result, 401);
                    assertThat(result.getResponse().getHeader("X-Correlation-ID")).isEqualTo("persisted-context-test");
                    assertPersistedAuthenticationUnchanged(persisted, authority);
                }
            } finally {
                sessions.deleteById(persisted.id());
            }
        }
    }

    @ParameterizedTest
    @CsvSource({"ERROR,jakarta.servlet.error.request_uri", "FORWARD,jakarta.servlet.forward.request_uri",
            "INCLUDE,jakarta.servlet.include.request_uri"})
    void persistedAuthenticationCannotBypassAlternateDispatch(String dispatcher, String attribute) throws Exception {
        for (String authority : List.of("PRODUCT_ADMIN", "STAFF_ADMIN")) {
            var persisted = persistAuthenticatedSession(authority);
            try {
                var result = http.perform(get("/api/v1/products").with(request -> {
                    request.setDispatcherType(DispatcherType.valueOf(dispatcher)); return request;
                }).requestAttr(attribute, "/api/v1/admin/products/PRIVATE-id")
                        .cookie(new jakarta.servlet.http.Cookie("SESSION", persisted.cookie()))
                        .header("X-Correlation-ID", "invalid~correlation")
                        .requestAttr(RequestDispatcher.ERROR_EXCEPTION, new IllegalStateException("PRIVATE diagnostic")))
                        .andReturn();
                denied(result, 401);
                assertThat(result.getResponse().getHeader("X-Correlation-ID")).isNotEqualTo("invalid~correlation");
                assertPersistedAuthenticationUnchanged(persisted, authority);
            } finally {
                sessions.deleteById(persisted.id());
            }
        }
    }

    @Test
    void publicCatalogueRemainsAnonymousAndFailClosed() throws Exception {
        var response = http.perform(get("/api/v1/products")).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getHeader("Cache-Control")).isEqualTo("no-store");
        assertThat(response.getContentAsString()).isEqualTo("{\"items\":[],\"pageSize\":20,\"nextCursor\":null}");
        verifyNoInteractions(create, update, get, list);
    }
}
