package com.enterprise.fashion.ecommerce.product.application.api;

/**
 * Product-owned internal Contract 1.0.0; initial consumer Pricing (not wired in this slice).
 * Facts apply only to one coherent observation. No publication, sellability, Inventory,
 * authorization or commercial Pricing grant. A revision is not continuing freshness.
 * Exports no Product Domain, persistence or framework types.
 */
public interface ProductVariantEvidence {
    Result observe(String productReference, String variantReference);

    enum Lifecycle { DRAFT, PUBLISHED, ARCHIVED }
    enum InvalidReason { INVALID_INPUT, INCONSISTENT_EVIDENCE, INTERNAL_FAILURE }
    sealed interface Result permits ConfirmedAssociation, ProductAbsent, VariantNotAssociated, Unavailable, Invalid { }

    /** Membership is confirmed by this outcome, not by a caller-supplied boolean. */
    record ConfirmedAssociation(String productReference, String variantReference,
            Lifecycle lifecycle, String representationRevision) implements Result {
        public ConfirmedAssociation {
            if (productReference == null || !productReference.matches("[A-Za-z0-9_-]{1,128}")
                    || variantReference == null || !variantReference.matches("[A-Za-z0-9_-]{1,128}")
                    || lifecycle == null || representationRevision == null || representationRevision.isBlank()) {
                throw new IllegalArgumentException("Complete Product association evidence required");
            }
        }
    }
    record ProductAbsent() implements Result { }
    /** Does not disclose whether the Variant exists under a different Product. */
    record VariantNotAssociated() implements Result { }
    record Unavailable() implements Result { }
    record Invalid(InvalidReason reason) implements Result {
        public Invalid {
            if (reason == null) { throw new IllegalArgumentException("Safe failure reason required"); }
        }
    }
}
