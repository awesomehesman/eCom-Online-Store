package com.enterprise.fashion.ecommerce.product.config;

import com.enterprise.fashion.ecommerce.product.application.evidence.ProductEvidenceService;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductEvidenceObservationPort;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductPersistencePort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration(proxyBeanMethods = false)
public class ProductEvidenceConfiguration {
    @Bean
    ProductEvidenceService productEvidenceService(ProductPersistencePort products,
            ProductEvidenceObservationPort evidence, PlatformTransactionManager manager) {
        var reads = new TransactionTemplate(manager);
        reads.setReadOnly(true);
        reads.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        return new ProductEvidenceService(products, evidence, reads);
    }
}
