package com.enterprise.fashion.ecommerce.identity.config;

import java.util.Base64;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.session.jdbc.JdbcIndexedSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "identity.logout.trusted-origin=https://shop.example")
@org.springframework.context.annotation.Import(IdentityLogoutIntegrationTest.ErrorDispatchEvidence.class)
@AutoConfigureMockMvc
@Testcontainers
class IdentityLogoutIntegrationTest {
    private static final String CSRF = "/api/v1/identity/csrf";
    private static final String LOGOUT = "/api/v1/identity/logout";
    @Container @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:18");
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired org.springframework.boot.test.web.client.TestRestTemplate http;
    @Autowired JdbcTemplate jdbc;
    @Autowired org.springframework.session.web.http.HttpSessionIdResolver resolver;
    @Autowired org.springframework.session.web.http.SessionRepositoryFilter<?> sessionFilter;
    @MockitoSpyBean JdbcIndexedSessionRepository sessions;
    @Autowired ErrorDispatchProbe errorProbe;

    @AfterEach
    void restoreRepository() {
        reset(sessions);
    }

    @Test
    void realAnonymousAcquisitionAndRepeatedLogoutNeverAccessSessions() throws Exception {
        assertThat(org.springframework.test.util.ReflectionTestUtils.getField(sessionFilter, "httpSessionIdResolver"))
                .isSameAs(resolver);
        var token = acquire();
        assertThat(token.token()).isNotEqualTo(token.cookie().getValue());
        assertThat(token.cookie().getName()).isEqualTo("__Host-XSRF-TOKEN");
        assertThat(token.cookie().getSecure()).isTrue();
        assertThat(token.cookie().isHttpOnly()).isTrue();
        assertThat(token.cookie().getPath()).isEqualTo("/");
        assertThat(token.cookie().getDomain()).isNull();
        var reacquired = mvc.perform(origin(get(CSRF)).cookie(token.cookie())).andReturn();
        assertThat(reacquired.getResponse().getStatus()).isEqualTo(200);
        for (int i = 0; i < 2; i++) assertStatus(mvc.perform(logout(token)).andReturn(), 204);
        noSessionLifecycle();
    }

    @Test
    void csrfPrecedesBodyAndCredentialProcessing() throws Exception {
        var token = acquire();
        assertStatus(mvc.perform(origin(post(LOGOUT)).content("bad")).andReturn(), 403);
        assertStatus(mvc.perform(origin(post(LOGOUT)).cookie(token.cookie())
                .header("X-XSRF-TOKEN", "wrong")).andReturn(), 403);
        assertStatus(mvc.perform(origin(post(LOGOUT)).cookie(token.cookie())
                .header("X-XSRF-TOKEN", token.cookie().getValue())).andReturn(), 403);
        verify(sessions, never()).deleteById(anyString());
        noSessionLifecycle();
    }

    @Test
    void validatesOriginBeforeIssuanceAndBodylessRequestShape() throws Exception {
        var rejected = mvc.perform(get(CSRF).header("Origin", "https://evil.example")).andReturn();
        assertStatus(rejected, 403);
        assertThat(rejected.getResponse().getHeader("Set-Cookie")).isNull();
        assertStatus(mvc.perform(get(CSRF)).andReturn(), 403);
        assertStatus(mvc.perform(get(CSRF).header("Referer", "https://shop.example/page")).andReturn(), 200);
        var token = acquire();
        for (String type : new String[] {"application/json", "application/xml"}) {
            assertStatus(mvc.perform(logout(token).contentType(type)).andReturn(), 204);
            assertStatus(mvc.perform(logout(token).contentType(type).content("null")).andReturn(), 400);
        }
        assertStatus(mvc.perform(logout(token).queryParam("sessionId", "untrusted")).andReturn(), 400);
        assertStatus(mvc.perform(origin(get(CSRF)).content(" ")).andReturn(), 400);
        assertStatus(mvc.perform(origin(get(CSRF)).queryParam("any", "value")).andReturn(), 400);
        noSessionLifecycle();
    }

    @Test
    void rejectsMalformedAndAmbiguousCredentialsWithoutDeletion() throws Exception {
        var token = acquire();
        assertStatus(mvc.perform(logout(token).cookie(new Cookie("SESSION", "%%%"))).andReturn(), 400);
        assertStatus(mvc.perform(logout(token).cookie(sessionCookie("first"), sessionCookie("second"))).andReturn(), 400);
        assertStatus(mvc.perform(logout(token).cookie(sessionCookie(" "))).andReturn(), 400);
        verify(sessions, never()).deleteById(anyString());
        noSessionLifecycle();
    }

    @Test
    void revokesOnlyTargetAndNeverResavesItAtResponseCompletion() throws Exception {
        Session target = store().createSession();
        target.setAttribute("private", "secret-must-not-escape");
        store().save(target);
        String primaryId = (String) row(target.getId()).get("primary_id");
        Session other = store().createSession();
        other.setAttribute("keep", "unchanged");
        store().save(other);
        Map<String, Object> before = row(other.getId());
        var attributesBefore = attributes(other.getId());
        clearInvocations(sessions);
        var token = acquire();
        var result = mvc.perform(logout(token).cookie(sessionCookie(target.getId()))).andReturn();
        result.getResponse().flushBuffer();
        assertStatus(result, 204);
        assertThat(count(target.getId())).isZero();
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM identity.spring_session_attributes WHERE session_primary_id = ?",
                Integer.class, primaryId)).isZero();
        assertThat(row(other.getId())).isEqualTo(before);
        assertThat(attributes(other.getId())).isEqualTo(attributesBefore);
        assertThat(count(target.getId())).isZero();
        noSessionLifecycle();
    }

    @Test
    void deletionFailureRemains503WithoutSavingOrLeakingDetails() throws Exception {
        Session target = store().createSession();
        store().save(target);
        var before = row(target.getId());
        clearInvocations(sessions);
        doThrow(new DataAccessResourceFailureException("sensitive-database-detail"))
                .when(sessions).deleteById(target.getId());
        var result = mvc.perform(logout(acquire()).cookie(sessionCookie(target.getId()))).andReturn();
        assertStatus(result, 503);
        assertThat(row(target.getId())).isEqualTo(before);
        assertThat(result.getResponse().getContentAsString()).doesNotContain(target.getId(), "sensitive-database-detail");
        noSessionLifecycle();
    }

    @Test
    @org.junit.jupiter.api.extension.ExtendWith(org.springframework.boot.test.system.OutputCaptureExtension.class)
    void rejectedQueryAndTokenDoNotLeakIntoLogs(org.springframework.boot.test.system.CapturedOutput logs)
            throws Exception {
        String secret = "must-not-log-session-target-xyz";
        String token = "must-not-log-csrf-token-xyz";
        var result = mvc.perform(origin(post(LOGOUT)).queryParam("sessionId", secret)
                .header("X-XSRF-TOKEN", token)).andReturn();
        assertStatus(result, 403);
        assertThat(logs.getAll()).doesNotContain(secret, token);
        noSessionLifecycle();
    }

    @Test
    void embeddedContainerSerializesRequiredCookieAndCacheAttributes() {
        var headers = new org.springframework.http.HttpHeaders();
        headers.set("Origin", "https://shop.example");
        var result = http.exchange(CSRF, org.springframework.http.HttpMethod.GET,
                new org.springframework.http.HttpEntity<>(headers), String.class);
        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getHeaders().getFirst("Set-Cookie"))
                .contains("__Host-XSRF-TOKEN=", "Path=/", "Secure", "HttpOnly", "SameSite=Strict")
                .doesNotContain("Domain=");
        assertThat(result.getHeaders().getCacheControl()).isEqualTo("no-store");
        noSessionLifecycle();
    }

    @Test
    void realContainerDispatchesUnexpectedFailureWithoutMisclassificationOrRepeatedRevocation() throws Exception {
        Session target = store().createSession();
        target.setAttribute("private", "must-not-expose-attribute");
        store().save(target);
        var before = row(target.getId());
        var attributesBefore = attributes(target.getId());
        clearInvocations(sessions);
        errorProbe.dispatches.set(0);
        errorProbe.originalPath.set(null);
        doThrow(new IllegalStateException("unexpected internal failure"))
                .when(sessions).deleteById(target.getId());

        var headers = new org.springframework.http.HttpHeaders();
        headers.set("Origin", "https://shop.example");
        var acquisition = http.exchange(CSRF, org.springframework.http.HttpMethod.GET,
                new org.springframework.http.HttpEntity<>(headers), String.class);
        assertThat(acquisition.getStatusCode().value()).isEqualTo(200);
        String masked = json.readTree(acquisition.getBody()).get("token").asText();
        String csrfCookie = acquisition.getHeaders().getFirst("Set-Cookie").split(";", 2)[0];
        headers.set("Cookie", csrfCookie + "; SESSION=" + sessionCookie(target.getId()).getValue());
        headers.set("X-XSRF-TOKEN", masked);
        var result = http.exchange(LOGOUT, org.springframework.http.HttpMethod.POST,
                new org.springframework.http.HttpEntity<>(headers), String.class);

        assertThat(errorProbe.dispatches).hasValue(1);
        assertThat(errorProbe.originalPath).hasValue(LOGOUT);
        assertThat(result.getStatusCode().value()).isEqualTo(500);
        assertThat(result.getHeaders().getContentType().toString()).startsWith("application/problem+json");
        assertThat(result.getHeaders().getCacheControl()).isEqualTo("no-store");
        assertThat(json.readTree(result.getBody()).get("status").asInt()).isEqualTo(500);
        assertThat(result.getBody()).doesNotContain(target.getId(), masked, csrfCookie,
                "unexpected internal failure", "must-not-expose-attribute", "IllegalStateException");
        assertThat(row(target.getId())).isEqualTo(before);
        assertThat(attributes(target.getId())).isEqualTo(attributesBefore);
        verify(sessions).deleteById(target.getId());
        noSessionLifecycle();
    }

    @Test
    void unrelatedRoutesKeepExistingSecurity() throws Exception {
        assertThat(mvc.perform(get("/unrelated")).andReturn().getResponse().getStatus()).isEqualTo(403);
        assertThat(mvc.perform(post("/logout")).andReturn().getResponse().getStatus()).isEqualTo(403);
    }

    private Token acquire() throws Exception {
        var result = mvc.perform(origin(get(CSRF))).andReturn();
        assertStatus(result, 200);
        assertThat(result.getResponse().getCookie("__Host-XSRF-TOKEN").getAttribute("SameSite")).isEqualTo("Strict");
        return new Token(result.getResponse().getCookie("__Host-XSRF-TOKEN"),
                json.readTree(result.getResponse().getContentAsString()).get("token").asText());
    }

    private MockHttpServletRequestBuilder logout(Token token) {
        return origin(post(LOGOUT)).cookie(token.cookie()).header("X-XSRF-TOKEN", token.token());
    }

    private MockHttpServletRequestBuilder origin(MockHttpServletRequestBuilder request) {
        return request.secure(true).header("Origin", "https://shop.example");
    }

    private Cookie sessionCookie(String id) {
        return new Cookie("SESSION", Base64.getEncoder().encodeToString(id.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
    }

    private void noSessionLifecycle() {
        verify(sessions, never()).createSession();
        verify(sessions, never()).findById(anyString());
        verify(store(), never()).save(any());
    }

    private void assertStatus(MvcResult result, int status) throws Exception {
        assertThat(result.getRequest().getAttribute(SessionRepository.class.getName() + ".CURRENT_SESSION")).isNull();
        assertThat(result.getResponse().getStatus()).isEqualTo(status);
        assertThat(result.getResponse().getHeader("Cache-Control")).isEqualTo("no-store");
        assertThat(result.getResponse().getHeader("X-Correlation-ID")).isNotBlank();
        if (status >= 400) {
            assertThat(result.getResponse().getContentType()).startsWith("application/problem+json");
            assertThat(json.readTree(result.getResponse().getContentAsString()).get("status").asInt()).isEqualTo(status);
        }
    }

    private Map<String, Object> row(String id) {
        return jdbc.queryForMap("SELECT * FROM identity.spring_session WHERE session_id = ?", id);
    }

    private java.util.List<String> attributes(String id) {
        return jdbc.queryForList("SELECT attribute_name || ':' || encode(attribute_bytes, 'hex') "
                + "FROM identity.spring_session_attributes WHERE session_primary_id = "
                + "(SELECT primary_id FROM identity.spring_session WHERE session_id = ?) ORDER BY attribute_name",
                String.class, id);
    }

    private int count(String id) {
        return jdbc.queryForObject("SELECT COUNT(*) FROM identity.spring_session WHERE session_id = ?", Integer.class, id);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private SessionRepository<Session> store() {
        return (SessionRepository) sessions;
    }

    static final class ErrorDispatchProbe implements jakarta.servlet.Filter {
        final java.util.concurrent.atomic.AtomicInteger dispatches = new java.util.concurrent.atomic.AtomicInteger();
        final java.util.concurrent.atomic.AtomicReference<String> originalPath = new java.util.concurrent.atomic.AtomicReference<>();

        @Override
        public void doFilter(jakarta.servlet.ServletRequest request, jakarta.servlet.ServletResponse response,
                jakarta.servlet.FilterChain chain) throws java.io.IOException, jakarta.servlet.ServletException {
            dispatches.incrementAndGet();
            originalPath.set((String) request.getAttribute(jakarta.servlet.RequestDispatcher.ERROR_REQUEST_URI));
            chain.doFilter(request, response);
        }
    }

    @org.springframework.boot.test.context.TestConfiguration(proxyBeanMethods = false)
    static class ErrorDispatchEvidence {
        @org.springframework.context.annotation.Bean
        ErrorDispatchProbe errorDispatchProbe() {
            return new ErrorDispatchProbe();
        }

        @org.springframework.context.annotation.Bean
        org.springframework.boot.web.servlet.FilterRegistrationBean<ErrorDispatchProbe> errorDispatchRegistration(
                ErrorDispatchProbe probe) {
            var registration = new org.springframework.boot.web.servlet.FilterRegistrationBean<>(probe);
            registration.setDispatcherTypes(DispatcherType.ERROR);
            registration.setOrder(Integer.MIN_VALUE);
            return registration;
        }
    }

    private record Token(Cookie cookie, String token) { }
}
