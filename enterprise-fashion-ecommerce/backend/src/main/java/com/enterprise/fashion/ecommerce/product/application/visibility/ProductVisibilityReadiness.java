package com.enterprise.fashion.ecommerce.product.application.visibility;

/**
 * Internal readiness categories, never positive visibility or a public existence response.
 * A future public boundary must preserve concealment and map failures under its Contract.
 * No Product content, identifier or underlying failure detail is carried by these outcomes.
 */
public enum ProductVisibilityReadiness {
    NOT_FOUND,
    NOT_VISIBLE,
    EVIDENCE_UNCONFIRMED,
    AUTHORITY_UNAVAILABLE,
    ERROR
}
