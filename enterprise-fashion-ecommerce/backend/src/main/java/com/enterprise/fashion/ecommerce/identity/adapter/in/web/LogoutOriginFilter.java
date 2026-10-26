package com.enterprise.fashion.ecommerce.identity.adapter.in.web;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.web.filter.OncePerRequestFilter;

public final class LogoutOriginFilter extends OncePerRequestFilter {
    private final LogoutOriginPolicy policy;
    private final LogoutResponses responses;

    public LogoutOriginFilter(LogoutOriginPolicy policy, LogoutResponses responses) {
        this.policy = policy;
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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        responses.headers(request, response);
        if (!policy.allows(request)) {
            responses.problem(response, 403);
            return;
        }
        // CsrfFilter diagnostics may include the URL. Preserve only a shape flag,
        // not rejected query contents, in the URL seen by downstream diagnostics.
        if (request.getQueryString() != null) {
            request.setAttribute(CurrentSessionLogoutFilter.FORBIDDEN_QUERY, Boolean.TRUE);
            request = new HttpServletRequestWrapper(request) {
                @Override
                public String getQueryString() {
                    return null;
                }
            };
        }
        chain.doFilter(request, response);
    }
}
