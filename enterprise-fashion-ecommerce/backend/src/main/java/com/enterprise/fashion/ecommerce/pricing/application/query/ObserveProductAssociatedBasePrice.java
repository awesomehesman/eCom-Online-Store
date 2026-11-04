package com.enterprise.fashion.ecommerce.pricing.application.query;

import java.util.Objects;
import com.enterprise.fashion.ecommerce.product.application.api.ProductVariantEvidence;
import com.enterprise.fashion.ecommerce.pricing.domain.model.SelectVariantBasePrice;
import com.enterprise.fashion.ecommerce.pricing.domain.model.VariantBasePriceAssignment.Subject;

/**
 * Product evidence first, then an independent Pricing snapshot. Each call re-observes
 * Product; neither snapshot establishes one shared instant or continuing freshness.
 * Even conditional validation establishes no publication, structural sellability,
 * Inventory, Staff/assignment authorization, commercial applicability or Checkout eligibility.
 * Uses only Product's exported Contract, never its Domain or persistence implementation.
 */
public final class ObserveProductAssociatedBasePrice {
    private final ProductVariantEvidence products;
    private final ObserveVariantBasePrice pricing;

    public ObserveProductAssociatedBasePrice(ProductVariantEvidence products, ObserveVariantBasePrice pricing) {
        this.products = Objects.requireNonNull(products);
        this.pricing = Objects.requireNonNull(pricing);
    }

    /** No enclosing transaction, caching, fallback, retries or fabricated failure observations. */
    public Result observe(String productReference, String variantReference) {
        var product = Objects.requireNonNull(products.observe(productReference, variantReference), "Missing Product observation");
        if (!(product instanceof ProductVariantEvidence.ConfirmedAssociation association)) {
            return new ProductNotConfirmed(product);
        }
        if (!Objects.equals(productReference, association.productReference())
                || !Objects.equals(variantReference, association.variantReference())) {
            return new ReferenceMismatch();
        }
        var subject = new Subject(productReference, variantReference);
        var price = Objects.requireNonNull(pricing.observe(subject), "Missing Pricing observation");
        if (price instanceof SelectVariantBasePrice.Selected selected) {
            if (!subject.equals(selected.assignment().subject())) { return new ReferenceMismatch(); }
            return new ConditionallyValidatedStoredObservation(association, selected);
        }
        return new PricingNotSelected(association, price);
    }

    public sealed interface Result permits ProductNotConfirmed, PricingNotSelected,
            ConditionallyValidatedStoredObservation, ReferenceMismatch { }

    /** Preserves Product absence, non-membership, unavailability and safe invalid reason. Pricing was not read. */
    public record ProductNotConfirmed(ProductVariantEvidence.Result product) implements Result {
        public ProductNotConfirmed {
            if (product == null || product instanceof ProductVariantEvidence.ConfirmedAssociation) {
                throw new IllegalArgumentException("Non-confirmed Product observation required");
            }
        }
    }
    /** Preserves Missing, Historical (obsolete/withdrawn), Conflict and Invalid independently of Product absence. */
    public record PricingNotSelected(ProductVariantEvidence.ConfirmedAssociation product,
            SelectVariantBasePrice.Result pricing) implements Result {
        public PricingNotSelected {
            if (product == null || pricing == null || pricing instanceof SelectVariantBasePrice.Selected) {
                throw new IllegalArgumentException("Confirmed Product and non-selected Pricing observations required");
            }
        }
    }
    /** Two independently observed matching subjects; not an authoritative commercial Price or freshness guarantee. */
    public record ConditionallyValidatedStoredObservation(ProductVariantEvidence.ConfirmedAssociation product,
            SelectVariantBasePrice.Selected pricing) implements Result {
        public ConditionallyValidatedStoredObservation {
            if (product == null || pricing == null
                    || !product.productReference().equals(pricing.assignment().subject().productReference())
                    || !product.variantReference().equals(pricing.assignment().subject().variantReference())) {
                throw new IllegalArgumentException("Matching Product and Pricing observations required");
            }
        }
    }
    public record ReferenceMismatch() implements Result { }
}
