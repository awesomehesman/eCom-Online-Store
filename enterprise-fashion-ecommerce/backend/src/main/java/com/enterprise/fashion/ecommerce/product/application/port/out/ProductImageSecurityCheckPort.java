package com.enterprise.fashion.ecommerce.product.application.port.out;

import java.util.Objects;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.application.media.ProductMediaSecurityCheckResult;
import com.enterprise.fashion.ecommerce.product.application.media.QuarantinedProductImageAsset;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;

/** Narrow internal port for checker-only quarantine retrieval and append-only technical results. */
public interface ProductImageSecurityCheckPort {
    RetrievedAsset retrieve(UUID assetId, ProductId expectedOwner);
    void append(ProductMediaSecurityCheckResult result);

    record RetrievedAsset(QuarantinedProductImageAsset asset, byte[] originalBytes) {
        public RetrievedAsset {
            Objects.requireNonNull(asset, "asset");
            if (originalBytes == null || originalBytes.length != asset.byteLength()) throw new IllegalArgumentException("Invalid bytes");
            originalBytes = originalBytes.clone();
        }
        @Override public byte[] originalBytes() { return originalBytes.clone(); }
    }
    enum FailureReason { MISSING_ASSET, WRONG_PRODUCT_OWNER, DIGEST_MISMATCH, CORRUPT_ASSET }
    final class Failure extends RuntimeException {
        private final FailureReason reason;
        public Failure(FailureReason reason) { super("Quarantined Product image is unavailable for checking"); this.reason = reason; }
        public FailureReason reason() { return reason; }
    }
}
