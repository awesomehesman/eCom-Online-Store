package com.enterprise.fashion.ecommerce.product.config;

import com.enterprise.fashion.ecommerce.product.application.persistence.ProductPersistenceService;
import com.enterprise.fashion.ecommerce.product.application.usecase.CreateDraftProduct;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductPersistencePort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration(proxyBeanMethods = false)
public class ProductPersistenceConfiguration {
    @Bean
    CreateDraftProduct createDraftProduct(ProductPersistenceService persistence) {
        return new CreateDraftProduct(persistence);
    }

    @Bean
    ProductPersistenceService productPersistenceService(ProductPersistencePort persistence, PlatformTransactionManager manager) {
        TransactionTemplate writes = new TransactionTemplate(manager);
        writes.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        writes.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        TransactionTemplate reads = new TransactionTemplate(manager);
        reads.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        reads.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        reads.setReadOnly(true);
        return new ProductPersistenceService(persistence, writes, reads);
    }
}
