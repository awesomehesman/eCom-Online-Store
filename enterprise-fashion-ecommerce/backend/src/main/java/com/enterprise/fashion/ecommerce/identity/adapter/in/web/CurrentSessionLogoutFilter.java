package com.enterprise.fashion.ecommerce.identity.adapter.in.web;

import java.io.IOException;
import java.util.List;
import com.enterprise.fashion.ecommerce.identity.application.SessionRevocationOutcome;
import com.enterprise.fashion.ecommerce.identity.application.usecase.RevokeIdentitySession;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.DeferredCsrfToken;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.filter.OncePerRequestFilter;

/** Terminal HTTP adapter; never forwards into MVC or any Session-loading component. */
public final class CurrentSessionLogoutFilter extends OncePerRequestFilter {
    private enum Outcome { CONFIRMED_ABSENT, INVALID_REQUEST, UNCERTAIN, TOKEN_ISSUED }

    private static final String OUTCOME = CurrentSessionLogoutFilter.class.getName() + ".outcome";
    static final String FORBIDDEN_QUERY = CurrentSessionLogoutFilter.class.getName() + ".forbiddenQuery";
    private final RevokeIdentitySession revocations;
    private final HttpSessionIdResolver resolver;
    private final CookieCsrfTokenRepository csrf;
    private final LogoutResponses responses;

    public CurrentSessionLogoutFilter(RevokeIdentitySession revocations, HttpSessionIdResolver resolver,
            CookieCsrfTokenRepository csrf, LogoutResponses responses) {
        this.revocations = revocations;
        this.resolver = resolver;
        this.csrf = csrf;
        this.responses = responses;
    }

    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return false;
    }

    @Override
    protected void doFilterNestedErrorDispatch(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        doFilterInternal(request, response, chain);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain unused)
            throws IOException, ServletException {
        boolean logout = "POST".equals(request.getMethod());
        try {
            if (request.getDispatcherType() == jakarta.servlet.DispatcherType.ERROR) {
                // Preserve only an outcome already established on this same request.
                // No new Session resolution, CSRF issuance or revocation on error dispatch.
                Object established = request.getAttribute(OUTCOME);
                if (established == Outcome.CONFIRMED_ABSENT) {
                    response.setStatus(204);
                }
                else if (established == Outcome.INVALID_REQUEST) {
                    responses.problem(response, 400);
                }
                else if (established == Outcome.UNCERTAIN) {
                    responses.problem(response, 503);
                }
                else {
                    // Unexpected failure, or interrupted acquisition: never manufacture
                    // Authentication/CSRF rejection or expose the error-dispatch attributes.
                    responses.problem(response, 500);
                }
                return;
            }
            if (request.getAttribute(FORBIDDEN_QUERY) != null
                    || request.getQueryString() != null || request.getInputStream().read() != -1) {
                request.setAttribute(OUTCOME, Outcome.INVALID_REQUEST);
                responses.problem(response, 400);
                return;
            }
            if (!logout) {
                // Reissue even an existing cookie, as required by the acquisition response Contract.
                CsrfToken masked = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
                String token = masked.getToken();
                DeferredCsrfToken raw = (DeferredCsrfToken) request.getAttribute(DeferredCsrfToken.class.getName());
                if (!raw.isGenerated()) {
                    csrf.saveToken(raw.get(), request, response);
                }
                request.setAttribute(OUTCOME, Outcome.TOKEN_ISSUED);
                responses.token(response, token);
                return;
            }
            List<String> ids;
            try {
                ids = resolver.resolveSessionIds(request);
            }
            catch (LogoutSessionIdResolver.InvalidCredential exception) {
                request.setAttribute(OUTCOME, Outcome.INVALID_REQUEST);
                responses.problem(response, 400);
                return;
            }
            if (ids.size() > 1 || (!ids.isEmpty() && invalid(ids.getFirst()))) {
                request.setAttribute(OUTCOME, Outcome.INVALID_REQUEST);
                responses.problem(response, 400);
                return;
            }
            if (ids.isEmpty() || revocations.revoke(ids.getFirst()) == SessionRevocationOutcome.CONFIRMED_ABSENT) {
                request.setAttribute(OUTCOME, Outcome.CONFIRMED_ABSENT);
                response.setStatus(204);
            }
            else {
                request.setAttribute(OUTCOME, Outcome.UNCERTAIN);
                responses.problem(response, 503);
            }
        }
        catch (DataAccessException | CannotCreateTransactionException exception) {
            request.setAttribute(OUTCOME, Outcome.UNCERTAIN);
            responses.problem(response, 503);
        }
        finally {
            if (logout) {
                SecurityContextHolder.clearContext();
            }
        }
    }

    private static boolean invalid(String reference) {
        return reference == null || reference.isBlank() || reference.length() > 36
                || reference.codePoints().anyMatch(c -> Character.isWhitespace(c)
                        || Character.isSpaceChar(c) || Character.isISOControl(c));
    }
}
