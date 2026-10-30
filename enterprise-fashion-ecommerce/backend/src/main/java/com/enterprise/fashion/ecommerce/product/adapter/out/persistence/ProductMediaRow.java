package com.enterprise.fashion.ecommerce.product.adapter.out.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "product_media", schema = "product")
record ProductMediaRow(@Id String mediaId, String productId, String variantId, String assetRevision,
        String url, String alternativeText, Integer presentationOrder) {
    @Override
    public String toString() { return "ProductMediaRow[internal]"; }
}
