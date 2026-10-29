package com.enterprise.fashion.ecommerce.product.domain.model;

/** Product-owned Attribute context under DEC-0019, not Category taxonomy. */
public record ProductFamily(String value) {
    public static final ProductFamily APPAREL = new ProductFamily("Apparel");
    public static final ProductFamily FOOTWEAR = new ProductFamily("Footwear");
    public static final ProductFamily ACCESSORIES = new ProductFamily("Accessories");

    public ProductFamily {
        if (!"Apparel".equals(value) && !"Footwear".equals(value) && !"Accessories".equals(value)) {
            throw new IllegalArgumentException("Unsupported Product family");
        }
    }
}
