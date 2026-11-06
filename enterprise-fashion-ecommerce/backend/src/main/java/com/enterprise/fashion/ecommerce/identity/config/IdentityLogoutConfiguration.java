package com.enterprise.fashion.ecommerce.identity.config;

import com.enterprise.fashion.ecommerce.identity.adapter.in.web.CurrentSessionLogoutFilter;
import com.enterprise.fashion.ecommerce.identity.adapter.in.web.LogoutOriginFilter;
import com.enterprise.fashion.ecommerce.identity.adapter.in.web.LogoutOriginPolicy;
import com.enterprise.fashion.ecommerce.identity.adapter.in.web.LogoutResponses;
import com.enterprise.fashion.ecommerce.identity.adapter.in.web.LogoutSessionIdResolver;
import com.enterprise.fashion.ecommerce.identity.application.usecase.RevokeIdentitySession;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.function.Supplier;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.CsrfToken;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.session.web.http.HttpSessionIdResolver;

@Configuration(proxyBeanMethods = false)
public class IdentityLogoutConfiguration {
    @Bean
    HttpSessionIdResolver identityHttpSessionIdResolver(
            @Value("${server.servlet.session.cookie.name:SESSION}") String cookieName) {
        return new LogoutSessionIdResolver(cookieName);
    }

    @Bean
    @Order(1)
    SecurityFilterChain identityLogoutSecurityFilterChain(HttpSecurity http, RevokeIdentitySession revocations,
            HttpSessionIdResolver resolver, ObjectMapper json,
            @Value("${identity.logout.trusted-origin:}") String trustedOrigin) throws Exception {
        var csrf = new CookieCsrfTokenRepository();
        csrf.setCookieName("__Host-XSRF-TOKEN");
        csrf.setCookiePath("/");
        csrf.setCookieCustomizer(cookie -> cookie.secure(true).httpOnly(true).path("/").sameSite("Strict"));
        var responses = new LogoutResponses(json);
        http.securityMatcher(request -> {
            String path = request.getRequestURI();
            if (request.getDispatcherType() == jakarta.servlet.DispatcherType.ERROR
                    && request.getAttribute(jakarta.servlet.RequestDispatcher.ERROR_REQUEST_URI) instanceof String original) {
                path = original;
            }
            String context = request.getContextPath();
            return ("GET".equals(request.getMethod()) && (context + "/api/v1/identity/csrf").equals(path))
                    || ("POST".equals(request.getMethod()) && (context + "/api/v1/identity/logout").equals(path));
        })
                .securityContext(context -> context.securityContextRepository(new RequestAttributeSecurityContextRepository()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .requestCache(cache -> cache.requestCache(new NullRequestCache()))
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                .csrf(config -> config.csrfTokenRepository(csrf)
                        .csrfTokenRequestHandler(new CsrfTokenRequestHandler() {
                            private final XorCsrfTokenRequestAttributeHandler xor = new XorCsrfTokenRequestAttributeHandler();

                            @Override
                            public void handle(HttpServletRequest request, HttpServletResponse response,
                                    Supplier<CsrfToken> token) {
                                xor.handle(request, response, token);
                            }

                            @Override
                            public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken token) {
                                // Header-only Contract: never parse form content or query tokens.
                                if (Collections.list(request.getHeaders(token.getHeaderName())).size() != 1) {
                                    return null;
                                }
                                return xor.resolveCsrfTokenValue(request, token);
                            }
                        }))
                .exceptionHandling(config -> config.accessDeniedHandler(
                        (request, response, exception) -> responses.problem(response, 403)))
                .logout(AbstractHttpConfigurer::disable)
                .addFilterBefore(new LogoutOriginFilter(new LogoutOriginPolicy(trustedOrigin), responses), CsrfFilter.class)
                .addFilterAfter(new CurrentSessionLogoutFilter(revocations, resolver, csrf, responses), CsrfFilter.class);
        return http.build();
    }
}
