package com.enterprise.fashion.ecommerce.product.config;

import java.time.Clock;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.application.port.out.ProductImageQuarantinePort;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductPersistencePort;
import com.enterprise.fashion.ecommerce.product.application.usecase.IngestProductImage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration(proxyBeanMethods = false)
public class ProductMediaIngestionConfiguration {
    @Bean
    IngestProductImage ingestProductImage(ProductPersistencePort products,
            ProductImageQuarantinePort quarantine, PlatformTransactionManager manager) {
        var writes = new TransactionTemplate(manager);
        writes.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        writes.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        return new IngestProductImage(products, quarantine, writes,
                IngestProductImage.Limits.conservativeLocalDefaults(), Clock.systemUTC(), UUID::randomUUID);
    }
}
