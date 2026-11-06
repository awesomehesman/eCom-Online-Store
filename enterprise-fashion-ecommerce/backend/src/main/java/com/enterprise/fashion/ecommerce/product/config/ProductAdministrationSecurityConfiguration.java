package com.enterprise.fashion.ecommerce.product.config;

import com.enterprise.fashion.ecommerce.product.adapter.in.web.ProductAdministrationDenyFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.RequestDispatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.savedrequest.NullRequestCache;

/**
 * Reserves the requested administration paths with default denial. This does not establish a
 * success/request wire Contract: the existing catalogue Contract explicitly excludes admin APIs.
 * No controller or permissive fallback may activate commands before Staff/audit/Contract gates.
 */
@Configuration(proxyBeanMethods = false)
public class ProductAdministrationSecurityConfiguration {
    @Bean
    @Order(0)
    SecurityFilterChain productAdministrationSecurityFilterChain(HttpSecurity http, ObjectMapper json) throws Exception {
        http.securityMatcher(request -> matches(request.getRequestURI(), request.getContextPath())
                || matches(request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI), request.getContextPath())
                || matches(request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI), request.getContextPath())
                || matches(request.getAttribute(RequestDispatcher.INCLUDE_REQUEST_URI), request.getContextPath()))
                .securityContext(context -> context.securityContextRepository(new RequestAttributeSecurityContextRepository()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .requestCache(cache -> cache.requestCache(new NullRequestCache()))
                .authorizeHttpRequests(authorize -> authorize.anyRequest().denyAll())
                .logout(AbstractHttpConfigurer::disable)
                // Deny before CSRF materialization or body parsing; do not disable CSRF safeguards.
                .addFilterBefore(new ProductAdministrationDenyFilter(json), CsrfFilter.class);
        return http.build();
    }

    private static boolean matches(Object candidate, String contextPath) {
        if (!(candidate instanceof String uri) || !uri.startsWith(contextPath)) { return false; }
        String path = uri.substring(contextPath.length());
        return path.equals("/api/v1/admin/products") || path.startsWith("/api/v1/admin/products/");
    }
}
