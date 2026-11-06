package com.enterprise.fashion.ecommerce.product.adapter.out.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "product_variant", schema = "product")
record ProductVariantRow(@Id String variantId, String productId, String family,
        String attributeStandardVersion, Boolean sizeApplicable, Boolean colourApplicable,
        String sizeCode, String colourCode, String fit, String material) {
    @Override
    public String toString() { return "ProductVariantRow[redacted]"; }
}
