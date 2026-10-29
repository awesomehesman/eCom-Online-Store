package com.enterprise.fashion.ecommerce.product.domain.model;

/** Stable opaque public reference; construction proves no existence, membership or authority. */
public record ProductVariantId(String value) {
    public ProductVariantId {
        if (value == null || !value.matches("[A-Za-z0-9_-]{1,128}")) {
            throw new IllegalArgumentException("Invalid Product Variant identifier");
        }
    }
}
