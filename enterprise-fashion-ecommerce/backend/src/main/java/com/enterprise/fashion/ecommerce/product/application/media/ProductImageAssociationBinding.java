package com.enterprise.fashion.ecommerce.product.application.media;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductRevision;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariantId;

/**
 * Immutable structural binding between one quarantined asset and one Product Media association.
 * It is neither media acceptance nor publication evidence and grants no visibility or authority.
 */
public record ProductImageAssociationBinding(UUID bindingId, ProductId productId,
        ProductRevision productRevision, String mediaAssociationId,
        ProductRevision associationRevision, UUID assetId, String assetRevision,
        String originalSha256, ProductVariantId variantId, Instant boundAt,
        boolean authoritativeAcceptanceEligible) {
    public ProductImageAssociationBinding {
        Objects.requireNonNull(bindingId, "bindingId");
        Objects.requireNonNull(productId, "productId");
        Objects.requireNonNull(productRevision, "productRevision");
        Objects.requireNonNull(associationRevision, "associationRevision");
        Objects.requireNonNull(assetId, "assetId");
        Objects.requireNonNull(boundAt, "boundAt");
        if (mediaAssociationId == null || !mediaAssociationId.matches("[A-Za-z0-9_-]{1,128}")) {
            throw new IllegalArgumentException("Invalid Product Media association identity");
        }
        if (originalSha256 == null || !originalSha256.matches("[0-9a-f]{64}")
                || !QuarantinedProductImageAsset.revisionOf(originalSha256).equals(assetRevision)) {
            throw new IllegalArgumentException("Invalid quarantined asset revision binding");
        }
        if (!productRevision.equals(associationRevision)) {
            throw new IllegalArgumentException("Association revision must be the Product representation revision");
        }
        if (authoritativeAcceptanceEligible) {
            throw new IllegalArgumentException("Structural media binding cannot establish authoritative acceptance");
        }
    }

    @Override
    public String toString() {
        return "ProductImageAssociationBinding[internal]";
    }
}
