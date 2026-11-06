package com.enterprise.fashion.ecommerce.product.domain.model;

/** Contracted content structure only; nonblank text is not accuracy or accessibility acceptance. */
public record ProductContent(String name, String description) {
    public ProductContent {
        if (name == null || name.isBlank() || description == null || description.isBlank()
                || name.indexOf('\0') >= 0 || description.indexOf('\0') >= 0
                || !java.nio.charset.StandardCharsets.UTF_8.newEncoder().canEncode(name)
                || !java.nio.charset.StandardCharsets.UTF_8.newEncoder().canEncode(description)) {
            throw new IllegalArgumentException("Product name and description are required");
        }
    }

    @Override
    public String toString() { return "ProductContent[internal]"; }
}
