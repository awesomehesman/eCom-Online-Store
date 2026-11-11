package com.enterprise.fashion.ecommerce.product.application.usecase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

import com.enterprise.fashion.ecommerce.product.application.media.QuarantinedProductImageAsset;
import com.enterprise.fashion.ecommerce.product.application.media.ProductImageTechnicalInspector;
import com.enterprise.fashion.ecommerce.product.application.media.QuarantinedProductImageAsset.Format;
import com.enterprise.fashion.ecommerce.product.application.media.QuarantinedProductImageAsset.Status;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductImageQuarantinePort;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductImageQuarantinePort.Payload;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductPersistencePort;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Internal write boundary for original Product image bytes. Successful ingestion means private quarantine only;
 * it creates no Product media association, security evidence, acceptance, publication or public URL.
 */
public final class IngestProductImage {
    private final ProductPersistencePort products;
    private final ProductImageQuarantinePort quarantine;
    private final TransactionOperations writes;
    private final Limits limits;
    private final Clock clock;
    private final Supplier<UUID> identifiers;

    public IngestProductImage(ProductPersistencePort products, ProductImageQuarantinePort quarantine,
            TransactionOperations writes, Limits limits, Clock clock, Supplier<UUID> identifiers) {
        this.products = Objects.requireNonNull(products, "products");
        this.quarantine = Objects.requireNonNull(quarantine, "quarantine");
        this.writes = Objects.requireNonNull(writes, "writes");
        this.limits = Objects.requireNonNull(limits, "limits");
        this.clock = Objects.requireNonNull(clock, "clock");
        this.identifiers = Objects.requireNonNull(identifiers, "identifiers");
    }

    public QuarantinedProductImageAsset ingest(ProductId productId, byte[] suppliedBytes) {
        Objects.requireNonNull(productId, "productId");
        requireOwnTransaction();
        byte[] bytes = copyAndCheckSize(suppliedBytes);
        ProductImageTechnicalInspector.Inspection image;
        try {
            image = ProductImageTechnicalInspector.inspect(bytes, new ProductImageTechnicalInspector.Limits(
                    limits.maximumBytes(), limits.maximumWidth(), limits.maximumHeight(), limits.maximumPixels()));
        } catch (ProductImageTechnicalInspector.Failure failure) {
            throw new Failure(switch (failure.reason()) {
                case MALFORMED_CONTENT -> Reason.MALFORMED_CONTENT;
                case UNSUPPORTED_FORMAT -> Reason.UNSUPPORTED_FORMAT;
                case RESOURCE_LIMIT_EXCEEDED -> Reason.IMAGE_RESOURCE_LIMIT_EXCEEDED;
            });
        }
        UUID assetId = Objects.requireNonNull(identifiers.get(), "Generated asset identity");
        var asset = new QuarantinedProductImageAsset(assetId, productId, digest(bytes), image.format(),
                bytes.length, image.width(), image.height(), Status.QUARANTINED, Instant.now(clock));
        return Objects.requireNonNull(writes.execute(status -> {
            if (products.load(productId).isEmpty()) {
                throw new Failure(Reason.PRODUCT_NOT_FOUND);
            }
            quarantine.store(new Payload(asset, bytes));
            return asset;
        }), "Image-ingestion transaction returned no outcome");
    }

    private byte[] copyAndCheckSize(byte[] suppliedBytes) {
        if (suppliedBytes == null || suppliedBytes.length == 0) {
            throw new Failure(Reason.MALFORMED_CONTENT);
        }
        if (suppliedBytes.length > limits.maximumBytes()) {
            throw new Failure(Reason.UPLOAD_TOO_LARGE);
        }
        return suppliedBytes.clone();
    }


    private static String digest(byte[] bytes) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes));
        } catch (NoSuchAlgorithmException impossible) {
            throw new IllegalStateException("SHA-256 unavailable", impossible);
        }
    }

    private static void requireOwnTransaction() {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException("Product image ingestion must own its transaction");
        }
    }

    /** Implementation-local resource ceilings, not a Product or Architecture policy decision. */
    public record Limits(int maximumBytes, int maximumWidth, int maximumHeight, long maximumPixels) {
        public Limits {
            if (maximumBytes < 1 || maximumWidth < 1 || maximumHeight < 1 || maximumPixels < 1
                    || maximumPixels > (long) maximumWidth * maximumHeight) {
                throw new IllegalArgumentException("Invalid Product image-ingestion limits");
            }
        }

        public static Limits conservativeLocalDefaults() {
            return new Limits(8 * 1024 * 1024, 4096, 4096, 16_000_000L);
        }
    }

    public enum Reason {
        MALFORMED_CONTENT,
        UNSUPPORTED_FORMAT,
        UPLOAD_TOO_LARGE,
        IMAGE_RESOURCE_LIMIT_EXCEEDED,
        PRODUCT_NOT_FOUND
    }

    public static final class Failure extends RuntimeException {
        private final Reason reason;

        public Failure(Reason reason) {
            super("Product image ingestion rejected");
            this.reason = Objects.requireNonNull(reason, "reason");
        }

        public Reason reason() { return reason; }
    }
}
