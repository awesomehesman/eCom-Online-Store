package com.enterprise.fashion.ecommerce.inventory.application.query;

import java.util.Objects;

import com.enterprise.fashion.ecommerce.inventory.domain.model.InventoryResourceReference;
import com.enterprise.fashion.ecommerce.product.application.api.ProductVariantEvidence;

/**
 * Internal association observation under Product's Approved Contract 1.1.0.
 * Confirmation applies only to the Product observation, not permanent membership,
 * Stock existence, quantity, Stock Location, availability, reservation eligibility
 * or adjustment authorization. No transaction, cache, retry or fallback is introduced.
 */
public final class ObserveInventoryProductAssociation {
    private final ProductVariantEvidence products;

    public ObserveInventoryProductAssociation(ProductVariantEvidence products) {
        this.products = Objects.requireNonNull(products);
    }

    public Result observe(InventoryResourceReference reference) {
        Objects.requireNonNull(reference, "Inventory resource reference is required");
        var product = Objects.requireNonNull(
                products.observe(reference.productReference(), reference.variantReference()),
                "Missing Product observation");
        if (!(product instanceof ProductVariantEvidence.ConfirmedAssociation association)) {
            return new NotConfirmed(product);
        }
        if (!reference.productReference().equals(association.productReference())
                || !reference.variantReference().equals(association.variantReference())) {
            return new ReferenceMismatch();
        }
        return new Confirmed(association);
    }

    public sealed interface Result permits Confirmed, NotConfirmed, ReferenceMismatch { }

    /** Retains Product evidence only; does not establish operational Inventory authority. */
    public record Confirmed(ProductVariantEvidence.ConfirmedAssociation product) implements Result {
        public Confirmed {
            Objects.requireNonNull(product, "Confirmed Product observation is required");
        }
    }

    public record NotConfirmed(ProductVariantEvidence.Result product) implements Result {
        public NotConfirmed {
            if (product == null || product instanceof ProductVariantEvidence.ConfirmedAssociation) {
                throw new IllegalArgumentException("Non-confirmed Product observation is required");
            }
        }
    }

    public record ReferenceMismatch() implements Result { }
}
