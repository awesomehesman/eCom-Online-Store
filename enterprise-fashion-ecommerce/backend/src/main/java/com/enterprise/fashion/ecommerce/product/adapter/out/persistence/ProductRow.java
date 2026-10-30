package com.enterprise.fashion.ecommerce.product.adapter.out.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "product", schema = "product")
record ProductRow(@Id String productId, String lifecycle, String family,
        String attributeStandardVersion, Boolean sizeApplicable, Boolean colourApplicable,
        String contentName, String contentDescription, String representationRevision) {
    @Override
    public String toString() { return "ProductRow[redacted]"; }
}
