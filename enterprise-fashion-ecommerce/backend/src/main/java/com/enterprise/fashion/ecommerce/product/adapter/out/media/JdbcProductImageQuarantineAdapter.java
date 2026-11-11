package com.enterprise.fashion.ecommerce.product.adapter.out.media;

import java.util.Objects;

import com.enterprise.fashion.ecommerce.product.application.port.out.ProductImageQuarantinePort;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/** PostgreSQL-backed private write-only quarantine. No retrieval or serving capability is exposed. */
@Repository
public class JdbcProductImageQuarantineAdapter implements ProductImageQuarantinePort {
    private final JdbcClient jdbc;

    public JdbcProductImageQuarantineAdapter(JdbcClient jdbc) {
        this.jdbc = Objects.requireNonNull(jdbc, "jdbc");
    }

    @Override
    public void store(Payload payload) {
        Objects.requireNonNull(payload, "payload");
        requireWritableTransaction();
        var asset = payload.asset();
        int inserted = jdbc.sql("""
                INSERT INTO product.product_image_quarantine
                    (asset_id, product_id, sha256, detected_format, byte_length,
                     pixel_width, pixel_height, quarantine_status, created_at, original_bytes)
                VALUES (:assetId, :productId, :sha256, :format, :byteLength,
                        :width, :height, :status, :createdAt, :bytes)
                """)
                .param("assetId", asset.assetId())
                .param("productId", asset.productId().value())
                .param("sha256", asset.sha256())
                .param("format", asset.format().name())
                .param("byteLength", asset.byteLength())
                .param("width", asset.pixelWidth())
                .param("height", asset.pixelHeight())
                .param("status", asset.status().name())
                .param("createdAt", java.sql.Timestamp.from(asset.createdAt()))
                .param("bytes", payload.originalBytes())
                .update();
        if (inserted != 1) {
            throw new IllegalStateException("Product image quarantine insert was not completed");
        }
    }

    private void requireWritableTransaction() {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException("Product image quarantine requires an application-owned transaction");
        }
        String readOnly = jdbc.sql("SELECT current_setting('transaction_read_only')").query(String.class).single();
        if (!"off".equals(readOnly)) {
            throw new IllegalStateException("Product image quarantine requires a writable transaction");
        }
        String isolation = jdbc.sql("SELECT current_setting('transaction_isolation')").query(String.class).single();
        if (!"repeatable read".equals(isolation) && !"serializable".equals(isolation)) {
            throw new IllegalStateException("Product image quarantine requires a consistent Product snapshot");
        }
    }
}
