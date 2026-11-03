package com.enterprise.fashion.ecommerce.pricing.config;

import com.enterprise.fashion.ecommerce.pricing.application.persistence.BasePricePersistenceService;
import com.enterprise.fashion.ecommerce.pricing.application.query.ObserveVariantBasePrice;
import com.enterprise.fashion.ecommerce.pricing.application.port.out.BasePricePersistencePort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration(proxyBeanMethods = false)
public class PricingPersistenceConfiguration {
    @Bean
    ObserveVariantBasePrice observeVariantBasePrice(BasePricePersistenceService persistence) {
        return new ObserveVariantBasePrice(persistence);
    }

    @Bean
    BasePricePersistenceService basePricePersistenceService(BasePricePersistencePort persistence, PlatformTransactionManager manager) {
        var writes = new TransactionTemplate(manager);
        writes.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        var reads = new TransactionTemplate(manager);
        reads.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        reads.setReadOnly(true);
        return new BasePricePersistenceService(persistence, writes, reads);
    }
}
