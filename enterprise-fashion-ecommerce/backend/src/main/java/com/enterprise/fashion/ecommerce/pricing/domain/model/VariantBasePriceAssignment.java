package com.enterprise.fashion.ecommerce.pricing.domain.model;

/**
 * Immutable revision representation under Accepted DEC-0043, not a Price aggregate.
 * Construction/restoration proves neither authorized commitment nor external Product
 * membership. The owning application must obtain those facts from trusted authorities.
 * No transition methods are provided: historical revisions must be retained by their owner.
 */
// Revision references are opaque provenance tokens, never precedence or time evidence.
public record VariantBasePriceAssignment(
        Subject subject, String revision, Money money, State state) {

    public VariantBasePriceAssignment {
        if (subject == null || revision == null || revision.isBlank() || money == null || state == null) {
            throw new IllegalArgumentException("Base-price assignment requires subject, revision, money and state");
        }
        if (!Currency.ZAR.equals(money.currency()) || money.amount().signum() <= 0) {
            throw new IllegalArgumentException("Base price requires strictly positive ZAR Money");
        }
    }

    /** Currentness classification only; no Product or publication lifecycle is modeled. */
    public enum State { CURRENT, OBSOLETE, WITHDRAWN }

    /**
     * Pricing-owned copies of the existing opaque Product identity reference contract.
     * Syntax validation does not establish existence, ownership, visibility or sellability.
     * Keeping references here avoids depending on another module's internal domain model.
     */
    public record Subject(String productReference, String variantReference) {
        public Subject {
            if (!validReference(productReference) || !validReference(variantReference)) {
                throw new IllegalArgumentException("Invalid Product or Variant reference");
            }
        }

        private static boolean validReference(String value) {
            return value != null && value.matches("[A-Za-z0-9_-]{1,128}");
        }
    }
}
