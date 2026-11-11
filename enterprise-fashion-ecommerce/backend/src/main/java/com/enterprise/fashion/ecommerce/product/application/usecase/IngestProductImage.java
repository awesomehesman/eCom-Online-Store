package com.enterprise.fashion.ecommerce.product.application.usecase;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.MemoryCacheImageInputStream;

import com.enterprise.fashion.ecommerce.product.application.media.QuarantinedProductImageAsset;
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
        InspectedImage image = inspect(bytes);
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

    private InspectedImage inspect(byte[] bytes) {
        Format expected = signature(bytes);
        try (var input = new MemoryCacheImageInputStream(new ByteArrayInputStream(bytes))) {
            Iterator<ImageReader> candidates = ImageIO.getImageReaders(input);
            ImageReader reader = null;
            while (candidates.hasNext()) {
                ImageReader candidate = candidates.next();
                if (matches(expected, candidate.getFormatName())) {
                    reader = candidate;
                    break;
                }
                candidate.dispose();
            }
            if (reader == null) {
                throw new Failure(Reason.MALFORMED_CONTENT);
            }
            try {
                reader.setInput(input, false, true);
                int width = reader.getWidth(0);
                int height = reader.getHeight(0);
                long pixels = Math.multiplyExact((long) width, (long) height);
                if (width < 1 || height < 1 || width > limits.maximumWidth()
                        || height > limits.maximumHeight() || pixels > limits.maximumPixels()) {
                    throw new Failure(Reason.IMAGE_RESOURCE_LIMIT_EXCEEDED);
                }
                BufferedImage decoded = reader.read(0);
                if (decoded == null || decoded.getWidth() != width || decoded.getHeight() != height
                        || reader.getNumImages(true) != 1) {
                    throw new Failure(Reason.MALFORMED_CONTENT);
                }
                decoded.flush();
                return new InspectedImage(expected, width, height);
            } finally {
                reader.dispose();
            }
        } catch (Failure known) {
            throw known;
        } catch (IOException | RuntimeException invalid) {
            throw new Failure(Reason.MALFORMED_CONTENT);
        }
    }

    private static Format signature(byte[] bytes) {
        byte[] png = {(byte) 0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a};
        if (startsWith(bytes, png)) {
            return Format.PNG;
        }
        if (bytes.length >= 3 && bytes[0] == (byte) 0xff && bytes[1] == (byte) 0xd8
                && bytes[2] == (byte) 0xff) {
            return Format.JPEG;
        }
        if (startsWith(bytes, "GIF87a".getBytes(java.nio.charset.StandardCharsets.US_ASCII))
                || startsWith(bytes, "GIF89a".getBytes(java.nio.charset.StandardCharsets.US_ASCII))
                || (bytes.length >= 12 && startsWith(bytes, "RIFF".getBytes(java.nio.charset.StandardCharsets.US_ASCII))
                && bytes[8] == 'W' && bytes[9] == 'E' && bytes[10] == 'B' && bytes[11] == 'P')) {
            throw new Failure(Reason.UNSUPPORTED_FORMAT);
        }
        throw new Failure(Reason.MALFORMED_CONTENT);
    }

    private static boolean startsWith(byte[] bytes, byte[] prefix) {
        if (bytes.length < prefix.length) {
            return false;
        }
        for (int index = 0; index < prefix.length; index++) {
            if (bytes[index] != prefix[index]) {
                return false;
            }
        }
        return true;
    }

    private static boolean matches(Format expected, String readerFormat) {
        return expected == Format.PNG ? "png".equalsIgnoreCase(readerFormat)
                : "jpeg".equalsIgnoreCase(readerFormat) || "jpg".equalsIgnoreCase(readerFormat);
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

    private record InspectedImage(Format format, int width, int height) { }

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
