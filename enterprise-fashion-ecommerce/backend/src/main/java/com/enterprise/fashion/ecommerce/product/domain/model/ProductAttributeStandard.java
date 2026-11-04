package com.enterprise.fashion.ecommerce.product.domain.model;

/** Interpretation version, retained with accepted values; no fallback to another version. */
public record ProductAttributeStandard(String version) {
    public static final ProductAttributeStandard INITIAL = new ProductAttributeStandard("1.0.0");

    public ProductAttributeStandard {
        if (!"1.0.0".equals(version)) {
            throw new IllegalArgumentException("Unsupported Product Attribute standard");
        }
    }
}
