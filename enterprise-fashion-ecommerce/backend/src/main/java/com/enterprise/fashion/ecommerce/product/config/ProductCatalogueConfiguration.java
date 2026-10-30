package com.enterprise.fashion.ecommerce.product.config;

import com.enterprise.fashion.ecommerce.product.application.catalogue.ProductCatalogueQueryService;
import com.enterprise.fashion.ecommerce.product.adapter.in.web.ProductCatalogueSessionIsolationFilter;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import com.enterprise.fashion.ecommerce.product.application.persistence.ProductPersistenceService;
import com.enterprise.fashion.ecommerce.product.application.usecase.AssessProductVisibilityReadiness;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.RequestDispatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.savedrequest.NullRequestCache;

@Configuration(proxyBeanMethods = false)
public class ProductCatalogueConfiguration {
    @Bean
    ProductCatalogueQueryService productCatalogueQueryService(ProductPersistenceService products) {
        return new ProductCatalogueQueryService(products, new AssessProductVisibilityReadiness(products));
    }

    @Bean
    @Order(2)
    SecurityFilterChain productCatalogueSecurityFilterChain(HttpSecurity http, ObjectMapper json) throws Exception {
        http.securityMatcher(request -> {
            if (!"GET".equals(request.getMethod())) {
                return false;
            }
            String uri = request.getRequestURI();
            if (request.getDispatcherType() == DispatcherType.ERROR
                    && request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI) instanceof String original) {
                uri = original;
            }
            String path = uri.substring(request.getContextPath().length());
            String base = "/api/v1/products";
            return base.equals(path) || (path.startsWith(base + "/")
                    && path.length() > base.length() + 1 && path.indexOf('/', base.length() + 1) < 0);
        })
                .securityContext(context -> context.securityContextRepository(new RequestAttributeSecurityContextRepository()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .requestCache(cache -> cache.requestCache(new NullRequestCache()))
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                .logout(AbstractHttpConfigurer::disable)
                .addFilterBefore(new ProductCatalogueSessionIsolationFilter(json), SecurityContextHolderFilter.class);
        return http.build();
    }
}
