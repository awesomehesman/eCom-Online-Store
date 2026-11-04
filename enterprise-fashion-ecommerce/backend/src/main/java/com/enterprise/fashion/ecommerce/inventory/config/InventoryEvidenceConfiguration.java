package com.enterprise.fashion.ecommerce.inventory.config;

import com.enterprise.fashion.ecommerce.inventory.application.query.ObserveInventoryProductAssociation;
import com.enterprise.fashion.ecommerce.product.application.api.ProductVariantEvidence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class InventoryEvidenceConfiguration {
    @Bean
    ObserveInventoryProductAssociation observeInventoryProductAssociation(ProductVariantEvidence products) {
        return new ObserveInventoryProductAssociation(products);
    }
}
