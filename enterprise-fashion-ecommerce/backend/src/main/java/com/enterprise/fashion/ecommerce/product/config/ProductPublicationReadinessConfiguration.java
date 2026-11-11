package com.enterprise.fashion.ecommerce.product.config;

import com.enterprise.fashion.ecommerce.product.adapter.out.publication.UnavailableProductPublicationEvidenceAdapter;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidencePolicy;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductPublicationEvidencePort;
import com.enterprise.fashion.ecommerce.product.application.usecase.AssessProductPublicationReadiness;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Fail-closed composition seam. An authoritative adapter and approved policy may replace the defaults;
 * their presence alone still grants no publication, authorization, lifecycle transition or visibility.
 */
@Configuration(proxyBeanMethods = false)
public class ProductPublicationReadinessConfiguration {
    @Bean
    @ConditionalOnMissingBean(ProductPublicationEvidencePort.class)
    ProductPublicationEvidencePort unavailableProductPublicationEvidencePort() {
        return new UnavailableProductPublicationEvidenceAdapter();
    }

    @Bean
    @ConditionalOnMissingBean(ProductPublicationEvidencePolicy.class)
    ProductPublicationEvidencePolicy rejectAllProductPublicationEvidencePolicy() {
        return (kind, source) -> false;
    }

    @Bean
    AssessProductPublicationReadiness assessProductPublicationReadiness(ProductPublicationEvidencePort evidence,
            ProductPublicationEvidencePolicy policy) {
        return new AssessProductPublicationReadiness(evidence, policy);
    }
}
