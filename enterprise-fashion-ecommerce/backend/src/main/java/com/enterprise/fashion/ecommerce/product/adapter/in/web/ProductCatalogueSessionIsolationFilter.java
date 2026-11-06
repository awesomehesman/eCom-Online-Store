package com.enterprise.fashion.ecommerce.product.adapter.in.web;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.DispatcherType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Product safe reads must not observe or renew a Session, including incidental MVC diagnostic
 * lookups. Applied only inside the Product GET security chain, after Spring Session's wrapper.
 */
public final class ProductCatalogueSessionIsolationFilter extends OncePerRequestFilter {
    private final ObjectMapper json;

    public ProductCatalogueSessionIsolationFilter(ObjectMapper json) { this.json = json; }

    @Override
    protected boolean shouldNotFilterErrorDispatch() { return false; }

    @Override
    protected void doFilterNestedErrorDispatch(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        doFilterInternal(request, response, chain);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        if (request.getDispatcherType() == DispatcherType.ERROR) {
            // Never delegate Product error dispatch to session-backed authentication or the
            // generic /error representation, which may contain path/exception diagnostics.
            if (!response.isCommitted()) {
                var problem = ProductCatalogueController.problem(500, ProductCatalogueController.correlation(request));
                response.resetBuffer();
                response.setStatus(500);
                problem.getHeaders().forEach((name, values) -> response.setHeader(name, values.getFirst()));
                json.writeValue(response.getOutputStream(), problem.getBody());
            }
            return;
        }
        chain.doFilter(new HttpServletRequestWrapper(request) {
            @Override
            public HttpSession getSession(boolean create) {
                if (create) {
                    throw new IllegalStateException("Product catalogue reads cannot create Sessions");
                }
                return null;
            }

            @Override
            public HttpSession getSession() {
                throw new IllegalStateException("Product catalogue reads cannot create Sessions");
            }

            @Override
            public String changeSessionId() {
                throw new IllegalStateException("Product catalogue reads cannot change Sessions");
            }

            @Override
            public String getRequestedSessionId() { return null; }

            @Override
            public boolean isRequestedSessionIdValid() { return false; }

            @Override
            public boolean isRequestedSessionIdFromCookie() { return false; }

            @Override
            public boolean isRequestedSessionIdFromURL() { return false; }
        }, response);
    }
}
