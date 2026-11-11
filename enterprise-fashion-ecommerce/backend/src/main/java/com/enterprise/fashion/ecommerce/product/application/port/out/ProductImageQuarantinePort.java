package com.enterprise.fashion.ecommerce.product.application.port.out;

import java.util.Objects;

import com.enterprise.fashion.ecommerce.product.application.media.QuarantinedProductImageAsset;

/** Write-only private quarantine boundary. It provides no read, serving, acceptance or publication operation. */
@FunctionalInterface
public interface ProductImageQuarantinePort {
    void store(Payload payload);

    record Payload(QuarantinedProductImageAsset asset, byte[] originalBytes) {
        public Payload {
            Objects.requireNonNull(asset, "asset");
            if (originalBytes == null || originalBytes.length != asset.byteLength()) {
                throw new IllegalArgumentException("Quarantine bytes do not match asset metadata");
            }
            originalBytes = originalBytes.clone();
        }

        @Override
        public byte[] originalBytes() { return originalBytes.clone(); }

        @Override
        public String toString() { return "ProductImageQuarantinePort.Payload[private]"; }
    }
}
