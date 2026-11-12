package com.enterprise.fashion.ecommerce.product.config;

import com.enterprise.fashion.ecommerce.product.application.port.out.ProductMediaSecurityEvidenceCorrelationPort;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductPersistencePort;
import com.enterprise.fashion.ecommerce.product.application.usecase.CorrelateProductMediaSecurityEvidence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration(proxyBeanMethods = false)
public class ProductMediaSecurityEvidenceCorrelationConfiguration {
    @Bean
    CorrelateProductMediaSecurityEvidence correlateProductMediaSecurityEvidence(ProductPersistencePort products,
            ProductMediaSecurityEvidenceCorrelationPort evidence, PlatformTransactionManager manager) {
        var reads = new TransactionTemplate(manager);
        reads.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        reads.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        reads.setReadOnly(true);
        return new CorrelateProductMediaSecurityEvidence(products, evidence, reads);
    }
}
