package com.enterprise.fashion.ecommerce.inventory.domain.model;

/**
 * Inventory-owned reference representation; Product remains authoritative for Product
 * and Variant identity. Construction proves neither Product nor Variant existence or
 * membership, and establishes no Product lifecycle, visibility or sellability.
 * It establishes no Stock existence, quantity, Stock Location, availability,
 * reservation or adjustment authorization.
 */
public record InventoryResourceReference(String productReference, String variantReference) {
    public InventoryResourceReference {
        if (!validReference(productReference) || !validReference(variantReference)) {
            throw new IllegalArgumentException("Invalid Product or Variant reference");
        }
    }

    private static boolean validReference(String value) {
        return value != null && value.matches("[A-Za-z0-9_-]{1,128}");
    }
}
