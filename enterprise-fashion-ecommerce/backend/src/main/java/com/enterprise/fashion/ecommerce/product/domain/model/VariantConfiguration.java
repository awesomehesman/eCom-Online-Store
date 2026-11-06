package com.enterprise.fashion.ecommerce.product.domain.model;

/**
 * Immutable canonical defining values, not a Variant identity or publication capability.
 * Applicability must be supplied explicitly by Product-owned context. A null value means
 * confirmed non-applicability only when its corresponding applicability flag is false.
 * Product-level consistency, supported half sizes, membership and uniqueness enforcement
 * belong to the future aggregate. Fit and Material have no role in this value's equality.
 */
public record VariantConfiguration(
        ProductFamily family,
        ProductAttributeStandard standard,
        boolean sizeApplicable,
        boolean colourApplicable,
        ProductSize size,
        ProductColour colour) {
    public VariantConfiguration {
        if (family == null || standard == null
                || sizeApplicable != (size != null)
                || colourApplicable != (colour != null)
                || (size != null && !family.equals(size.family()))) {
            throw new IllegalArgumentException("Invalid Variant configuration");
        }
    }
}
