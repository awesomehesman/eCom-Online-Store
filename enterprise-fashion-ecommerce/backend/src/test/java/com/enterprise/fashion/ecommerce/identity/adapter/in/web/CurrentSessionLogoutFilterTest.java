package com.enterprise.fashion.ecommerce.identity.adapter.in.web;

import com.enterprise.fashion.ecommerce.identity.application.SessionObservation;
import com.enterprise.fashion.ecommerce.identity.application.usecase.RevokeIdentitySession;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.session.web.http.HttpSessionIdResolver;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

class CurrentSessionLogoutFilterTest {
    @ParameterizedTest
    @EnumSource(SessionObservation.class)
    void onlyAuthoritativeAbsenceBecomesSuccess(SessionObservation state) throws Exception {
        var resolver = mock(HttpSessionIdResolver.class);
        when(resolver.resolveSessionIds(any())).thenReturn(List.of("opaque-session"));
        var command = new RevokeIdentitySession(id -> true, id -> state);
        var response = invoke(command, resolver);
        assertThat(response.getStatus()).isEqualTo(state == SessionObservation.ABSENT ? 204 : 503);
    }

    @Test
    void failedDeletionNeverBecomesSuccessEvenWhenObservationCouldBeAbsent() throws Exception {
        var resolver = mock(HttpSessionIdResolver.class);
        when(resolver.resolveSessionIds(any())).thenReturn(List.of("opaque-session"));
        var observations = new AtomicInteger();
        var command = new RevokeIdentitySession(id -> false, id -> {
            observations.incrementAndGet();
            return SessionObservation.ABSENT;
        });
        assertThat(invoke(command, resolver).getStatus()).isEqualTo(503);
        assertThat(observations).hasValue(0);
    }

    @Test
    void resolverFailureIsUncertainAndProgrammingErrorsRemainVisible() throws Exception {
        var resolver = mock(HttpSessionIdResolver.class);
        when(resolver.resolveSessionIds(any())).thenThrow(new DataAccessResourceFailureException("private"));
        var command = new RevokeIdentitySession(id -> { throw new AssertionError("must not delete"); },
                id -> SessionObservation.ABSENT);
        var response = invoke(command, resolver);
        assertThat(response.getStatus()).isEqualTo(503);
        assertThat(response.getContentAsString()).doesNotContain("private");
        org.mockito.Mockito.doThrow(new IllegalStateException("bug")).when(resolver).resolveSessionIds(any());
        assertThatIllegalStateException().isThrownBy(() -> invoke(command, resolver));
    }

    @Test
    void exceptionBeforeOutcomeDispatchesAsUnexpectedFailureWithoutRepeatingWork() throws Exception {
        var resolver = mock(HttpSessionIdResolver.class);
        when(resolver.resolveSessionIds(any())).thenReturn(List.of("opaque-session"));
        var attempts = new AtomicInteger();
        var command = new RevokeIdentitySession(id -> {
            attempts.incrementAndGet();
            throw new IllegalStateException("private backend failure");
        }, id -> SessionObservation.ABSENT);
        var filter = new CurrentSessionLogoutFilter(command, resolver, new CookieCsrfTokenRepository(),
                new LogoutResponses(new ObjectMapper()));
        var request = new MockHttpServletRequest("POST", "/api/v1/identity/logout");
        var initial = new MockHttpServletResponse();
        assertThatIllegalStateException().isThrownBy(() -> filter.doFilter(request, initial, (r, s) -> {
            throw new AssertionError("must not forward");
        }));
        assertThat(request.getAttribute(CurrentSessionLogoutFilter.class.getName() + ".outcome")).isNull();
        request.setDispatcherType(jakarta.servlet.DispatcherType.ERROR);
        request.setAttribute(jakarta.servlet.RequestDispatcher.ERROR_REQUEST_URI, request.getRequestURI());
        var dispatched = new MockHttpServletResponse();
        filter.doFilter(request, dispatched, (r, s) -> { throw new AssertionError("must not forward"); });
        assertThat(dispatched.getStatus()).isEqualTo(500);
        assertThat(dispatched.getContentType()).startsWith("application/problem+json");
        assertThat(dispatched.getContentAsString()).doesNotContain("private", "opaque-session");
        assertThat(attempts).hasValue(1);
        org.mockito.Mockito.verify(resolver).resolveSessionIds(any());
        assertThat(request.getSession(false)).isNull();
    }

    @Test
    void bodyReadFailureDoesNotRecordDefault200OrBecomeSecurityRejection() throws Exception {
        var resolver = mock(HttpSessionIdResolver.class);
        var command = new RevokeIdentitySession(id -> { throw new AssertionError("must not delete"); },
                id -> SessionObservation.ABSENT);
        var filter = new CurrentSessionLogoutFilter(command, resolver, new CookieCsrfTokenRepository(),
                new LogoutResponses(new ObjectMapper()));
        var request = new MockHttpServletRequest("POST", "/api/v1/identity/logout") {
            @Override
            public jakarta.servlet.ServletInputStream getInputStream() {
                return new org.springframework.mock.web.DelegatingServletInputStream(new java.io.InputStream() {
                    @Override
                    public int read() throws java.io.IOException {
                        throw new java.io.IOException("private read failure");
                    }
                });
            }
        };
        var response = new MockHttpServletResponse();
        org.assertj.core.api.Assertions.assertThatIOException().isThrownBy(() ->
                filter.doFilter(request, response, (r, s) -> { throw new AssertionError("must not forward"); }));
        assertThat(request.getAttribute(CurrentSessionLogoutFilter.class.getName() + ".outcome")).isNull();
        request.setDispatcherType(jakarta.servlet.DispatcherType.ERROR);
        request.setAttribute(jakarta.servlet.RequestDispatcher.ERROR_REQUEST_URI, request.getRequestURI());
        filter.doFilter(request, response, (r, s) -> { throw new AssertionError("must not forward"); });
        assertThat(response.getStatus()).isEqualTo(500);
        assertThat(response.getContentAsString()).doesNotContain("private read failure");
        org.mockito.Mockito.verifyNoInteractions(resolver);
        assertThat(request.getSession(false)).isNull();
    }

    @Test
    void defaultOrPreviouslySuccessfulHttpStatusIsNotAnEstablishedOutcome() throws Exception {
        var resolver = mock(HttpSessionIdResolver.class);
        var command = new RevokeIdentitySession(id -> { throw new AssertionError("must not delete"); },
                id -> SessionObservation.ABSENT);
        var filter = new CurrentSessionLogoutFilter(command, resolver, new CookieCsrfTokenRepository(),
                new LogoutResponses(new ObjectMapper()));
        for (int status : new int[] {200, 204, 403}) {
            var request = new MockHttpServletRequest("POST", "/api/v1/identity/logout");
            request.setDispatcherType(jakarta.servlet.DispatcherType.ERROR);
            request.setAttribute(jakarta.servlet.RequestDispatcher.ERROR_REQUEST_URI, request.getRequestURI());
            var response = new MockHttpServletResponse();
            response.setStatus(status);
            filter.doFilter(request, response, (r, s) -> { throw new AssertionError("must not forward"); });
            assertThat(response.getStatus()).isEqualTo(500);
        }
        org.mockito.Mockito.verifyNoInteractions(resolver);
    }

    private MockHttpServletResponse invoke(RevokeIdentitySession command, HttpSessionIdResolver resolver)
            throws Exception {
        var filter = new CurrentSessionLogoutFilter(command, resolver, new CookieCsrfTokenRepository(),
                new LogoutResponses(new ObjectMapper()));
        var request = new MockHttpServletRequest("POST", "/api/v1/identity/logout");
        var response = new MockHttpServletResponse();
        filter.doFilter(request, response, (r, s) -> { throw new AssertionError("must not forward"); });
        assertThat(request.getSession(false)).isNull();
        assertThat(org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication()).isNull();
        return response;
    }
}
