package com.enterprise.fashion.ecommerce.product.application.media;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;

/** Immutable private asset metadata. QUARANTINED is not acceptance, safety evidence or public eligibility. */
public record QuarantinedProductImageAsset(UUID assetId, ProductId productId, String sha256,
        Format format, long byteLength, int pixelWidth, int pixelHeight, Status status, Instant createdAt) {
    public enum Format { PNG, JPEG }
    public enum Status { QUARANTINED }

    public QuarantinedProductImageAsset {
        Objects.requireNonNull(assetId, "assetId");
        Objects.requireNonNull(productId, "productId");
        Objects.requireNonNull(format, "format");
        Objects.requireNonNull(status, "status");
        Objects.requireNonNull(createdAt, "createdAt");
        if (sha256 == null || !sha256.matches("[0-9a-f]{64}") || byteLength < 1
                || pixelWidth < 1 || pixelHeight < 1) {
            throw new IllegalArgumentException("Invalid quarantined Product image metadata");
        }
    }

    /** Content-derived immutable revision reference suitable for a later governed association. */
    public String assetRevision() {
        return "sha256_" + sha256;
    }

    @Override
    public String toString() { return "QuarantinedProductImageAsset[private]"; }
}
