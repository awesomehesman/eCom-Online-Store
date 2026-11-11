package com.enterprise.fashion.ecommerce.product.config;

import java.time.Clock;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.application.media.ProductImageTechnicalInspector;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductImageSecurityCheckPort;
import com.enterprise.fashion.ecommerce.product.application.usecase.CheckQuarantinedProductImage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration(proxyBeanMethods = false)
public class ProductMediaSecurityCheckingConfiguration {
    @Bean
    CheckQuarantinedProductImage checkQuarantinedProductImage(ProductImageSecurityCheckPort storage,
            PlatformTransactionManager manager) {
        var transaction = new TransactionTemplate(manager);
        transaction.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        return new CheckQuarantinedProductImage(storage, transaction,
                ProductImageTechnicalInspector.Limits.conservativeDefaults(), Clock.systemUTC(), UUID::randomUUID);
    }
}
