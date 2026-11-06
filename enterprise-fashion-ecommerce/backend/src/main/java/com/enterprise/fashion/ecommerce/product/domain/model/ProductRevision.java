package com.enterprise.fashion.ecommerce.product.domain.model;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.HexFormat;

/**
 * Versioned deterministic representation identity, not an acceptance record or change counter.
 * Covers identity/context, Variant configurations/descriptions and ordered content/media metadata.
 * Lifecycle is separate: restoring a lifecycle does not change the represented content.
 * Equal snapshots may share a revision; future acceptance must still check explicit invalidation.
 */
public record ProductRevision(String value) {
    public ProductRevision {
        if (value == null || !value.matches("v1_[0-9a-f]{64}")) {
            throw new IllegalArgumentException("Invalid Product representation revision");
        }
    }

    static ProductRevision of(Product product) {
        try {
            var bytes = new ByteArrayOutputStream();
            var out = new DataOutputStream(bytes);
            text(out, "product-representation-v1");
            text(out, product.id().value());
            text(out, product.family().value());
            text(out, product.standard().version());
            out.writeBoolean(product.sizeApplicable());
            out.writeBoolean(product.colourApplicable());
            out.writeInt(product.variants().size());
            // Variant membership is unordered; media presentation order is meaningful.
            for (ProductVariant variant : product.variants().stream()
                    .sorted(Comparator.comparing(v -> v.id().value())).toList()) {
                text(out, variant.id().value());
                text(out, variant.configuration().size() == null ? null : variant.configuration().size().code());
                text(out, variant.configuration().colour() == null ? null : variant.configuration().colour().code());
                text(out, variant.fit());
                text(out, variant.material());
            }
            out.writeBoolean(product.content() != null);
            if (product.content() != null) {
                text(out, product.content().name());
                text(out, product.content().description());
            }
            out.writeInt(product.media().size());
            for (ProductMedia media : product.media()) {
                text(out, media.id());
                text(out, media.variantId() == null ? null : media.variantId().value());
                text(out, media.assetRevision());
                text(out, media.url());
                text(out, media.alternativeText());
            }
            out.flush();
            return new ProductRevision("v1_" + HexFormat.of().formatHex(
                    MessageDigest.getInstance("SHA-256").digest(bytes.toByteArray())));
        } catch (IOException | NoSuchAlgorithmException impossible) {
            throw new IllegalStateException("Product revision computation failed", impossible);
        }
    }

    private static void text(DataOutputStream out, String value) throws IOException {
        if (value == null) {
            out.writeInt(-1);
        } else {
            out.writeInt(value.length());
            for (int index = 0; index < value.length(); index++) {
                out.writeChar(value.charAt(index));
            }
        }
    }
}
