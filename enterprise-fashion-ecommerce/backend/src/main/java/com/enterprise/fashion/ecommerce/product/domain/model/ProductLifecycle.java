package com.enterprise.fashion.ecommerce.product.domain.model;

/** Canonical lifecycle vocabulary only; state is not publication or visibility evidence. */
public enum ProductLifecycle {
    DRAFT,
    PUBLISHED,
    ARCHIVED;

    /** No state alone satisfies current content, media, validation and authorization requirements. */
    public boolean grantsCustomerVisibility() {
        return false;
    }
}
