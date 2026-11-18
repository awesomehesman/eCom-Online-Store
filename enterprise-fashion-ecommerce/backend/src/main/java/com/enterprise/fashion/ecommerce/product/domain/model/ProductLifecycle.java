package com.enterprise.fashion.ecommerce.product.domain.model;

/** Canonical lifecycle vocabulary only; state is not publication or visibility evidence. */
public enum ProductLifecycle {
    DRAFT,
    PUBLISHED,
    ARCHIVED;

    /**
     * State-machine predicate for publication eligibility.
     * Only DRAFT Products may transition forward to PUBLISHED.
     * This predicate does not establish authorization, readiness or visibility.
     */
    public boolean canPublish() {
        return this == DRAFT;
    }

    /** No state alone satisfies current content, media, validation and authorization requirements. */
    public boolean grantsCustomerVisibility() {
        return false;
    }
}
