package com.enterprise.fashion.ecommerce.product.config;

import java.time.Clock;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.application.port.out.ProductImageAssociationBindingPort;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductPersistencePort;
import com.enterprise.fashion.ecommerce.product.application.usecase.BindProductImageAssociation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration(proxyBeanMethods = false)
public class ProductMediaAssociationBindingConfiguration {
    @Bean
    BindProductImageAssociation bindProductImageAssociation(ProductPersistencePort products,
            ProductImageAssociationBindingPort bindings, PlatformTransactionManager manager) {
        return new BindProductImageAssociation(products, bindings,
                transaction(manager, true), transaction(manager, false), Clock.systemUTC(), UUID::randomUUID);
    }

    private static TransactionTemplate transaction(PlatformTransactionManager manager, boolean readOnly) {
        var transaction = new TransactionTemplate(manager);
        transaction.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        transaction.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        transaction.setReadOnly(readOnly);
        return transaction;
    }
}
