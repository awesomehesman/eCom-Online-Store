package com.enterprise.fashion.ecommerce.product.application.media;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.MemoryCacheImageInputStream;

import com.enterprise.fashion.ecommerce.product.application.media.QuarantinedProductImageAsset.Format;

/** Deterministic content inspection shared by ingestion and later quarantine checking. */
public final class ProductImageTechnicalInspector {
    private ProductImageTechnicalInspector() { }

    public static Inspection inspect(byte[] suppliedBytes, Limits limits) {
        if (suppliedBytes == null || suppliedBytes.length == 0) throw new Failure(Reason.MALFORMED_CONTENT);
        if (suppliedBytes.length > limits.maximumBytes()) throw new Failure(Reason.RESOURCE_LIMIT_EXCEEDED);
        byte[] bytes = suppliedBytes.clone();
        Format expected = signature(bytes);
        try (var input = new MemoryCacheImageInputStream(new ByteArrayInputStream(bytes))) {
            Iterator<ImageReader> candidates = ImageIO.getImageReaders(input);
            ImageReader reader = null;
            while (candidates.hasNext()) {
                ImageReader candidate = candidates.next();
                if (matches(expected, candidate.getFormatName())) { reader = candidate; break; }
                candidate.dispose();
            }
            if (reader == null) throw new Failure(Reason.MALFORMED_CONTENT);
            try {
                reader.setInput(input, false, true);
                int width = reader.getWidth(0);
                int height = reader.getHeight(0);
                long pixels = Math.multiplyExact((long) width, height);
                if (width < 1 || height < 1 || width > limits.maximumWidth()
                        || height > limits.maximumHeight() || pixels > limits.maximumPixels()) {
                    throw new Failure(Reason.RESOURCE_LIMIT_EXCEEDED);
                }
                BufferedImage decoded = reader.read(0);
                if (decoded == null || decoded.getWidth() != width || decoded.getHeight() != height
                        || reader.getNumImages(true) != 1) throw new Failure(Reason.MALFORMED_CONTENT);
                decoded.flush();
                return new Inspection(expected, width, height);
            } finally { reader.dispose(); }
        } catch (Failure known) {
            throw known;
        } catch (IOException | RuntimeException invalid) {
            throw new Failure(Reason.MALFORMED_CONTENT);
        }
    }

    private static Format signature(byte[] bytes) {
        byte[] png = {(byte) 0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a};
        if (startsWith(bytes, png)) return Format.PNG;
        if (bytes.length >= 3 && bytes[0] == (byte) 0xff && bytes[1] == (byte) 0xd8 && bytes[2] == (byte) 0xff) {
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
        if (bytes.length < prefix.length) return false;
        for (int i = 0; i < prefix.length; i++) if (bytes[i] != prefix[i]) return false;
        return true;
    }

    private static boolean matches(Format expected, String actual) {
        return expected == Format.PNG ? "png".equalsIgnoreCase(actual)
                : "jpeg".equalsIgnoreCase(actual) || "jpg".equalsIgnoreCase(actual);
    }

    public record Inspection(Format format, int width, int height) { }
    public record Limits(int maximumBytes, int maximumWidth, int maximumHeight, long maximumPixels) {
        public Limits {
            if (maximumBytes < 1 || maximumWidth < 1 || maximumHeight < 1 || maximumPixels < 1
                    || maximumPixels > (long) maximumWidth * maximumHeight) throw new IllegalArgumentException("Invalid limits");
        }
        public static Limits conservativeDefaults() { return new Limits(8 * 1024 * 1024, 4096, 4096, 16_000_000L); }
    }
    public enum Reason { MALFORMED_CONTENT, UNSUPPORTED_FORMAT, RESOURCE_LIMIT_EXCEEDED }
    public static final class Failure extends RuntimeException {
        private final Reason reason;
        public Failure(Reason reason) { super("Product image technical inspection rejected"); this.reason = reason; }
        public Reason reason() { return reason; }
    }
}
