package com.enterprise.fashion.ecommerce.product.domain.model;

/** Stable opaque public reference; construction proves no existence, membership or authority. */
public record ProductId(String value) {
    public ProductId {
        if (value == null || !value.matches("[A-Za-z0-9_-]{1,128}")) {
            throw new IllegalArgumentException("Invalid Product identifier");
        }
    }
}
