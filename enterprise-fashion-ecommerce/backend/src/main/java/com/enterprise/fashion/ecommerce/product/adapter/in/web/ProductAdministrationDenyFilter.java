package com.enterprise.fashion.ecommerce.product.adapter.in.web;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Terminal security perimeter, not an authorization provider or operational administration API.
 * POST creation, PUT modification and GET reads all remain denied: no governed Staff action
 * assignments or trusted mutation audit implementation exists. Never parses protected input,
 * invokes application capabilities, fabricates actor attribution or interprets authority labels.
 * GenericFilterBean deliberately applies to repeated/error/forward dispatches as well.
 */
public final class ProductAdministrationDenyFilter extends GenericFilterBean {
    private final ObjectMapper json;
    private final AuthenticationTrustResolverImpl trust = new AuthenticationTrustResolverImpl();

    public ProductAdministrationDenyFilter(ObjectMapper json) { this.json = json; }

    @Override
    public void doFilter(ServletRequest input, ServletResponse output, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) input;
        HttpServletResponse response = (HttpServletResponse) output;
        if (response.isCommitted()) { return; }
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        int status = authentication != null && authentication.isAuthenticated() && !trust.isAnonymous(authentication) ? 403 : 401;
        var values = Collections.list(request.getHeaders("X-Correlation-ID"));
        String correlation = values.size() == 1 && values.getFirst().length() <= 128
                && values.getFirst().matches("[A-Za-z0-9._-]{1,128}") ? values.getFirst() : UUID.randomUUID().toString();
        // Discard any uncommitted error/forward body and headers from an earlier dispatch.
        response.reset();
        response.setStatus(status);
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("X-Correlation-ID", correlation);
        response.setContentType("application/problem+json");
        json.writeValue(response.getOutputStream(), new Problem("about:blank",
                status == 401 ? "Unauthorized" : "Forbidden", status, correlation));
        // No downstream filter, MVC dispatch, session lookup or protected capability is reached.
    }

    private record Problem(String type, String title, int status, String correlationId) { }
}
