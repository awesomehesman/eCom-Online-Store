package com.enterprise.fashion.ecommerce.product.domain.model;

/**
 * Immutable Variant entity. Construction does not establish accepted membership or visibility.
 * The owning Product must validate configuration and membership before accepting this entity.
 */
public final class ProductVariant {
    private final ProductVariantId id;
    private final ProductId productId;
    private final VariantConfiguration configuration;
    private final String fit;
    private final String material;

    public ProductVariant(ProductVariantId id, ProductId productId, VariantConfiguration configuration,
            String fit, String material) {
        if (id == null || productId == null || configuration == null) {
            throw new IllegalArgumentException("Variant identity, owner and configuration are required");
        }
        this.id = id;
        this.productId = productId;
        this.configuration = configuration;
        this.fit = fit;
        this.material = material;
    }

    public ProductVariantId id() { return id; }
    public ProductId productId() { return productId; }
    public VariantConfiguration configuration() { return configuration; }

    /** Optional descriptive content; no vocabulary or publication acceptance is implied. */
    public String fit() { return fit; }
    public String material() { return material; }

    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof ProductVariant variant
                && id.equals(variant.id) && productId.equals(variant.productId);
    }

    @Override
    public int hashCode() {
        return 31 * productId.hashCode() + id.hashCode();
    }
}
