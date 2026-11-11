package com.enterprise.fashion.ecommerce.product.adapter.out.media;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Objects;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.application.media.ProductMediaSecurityCheckResult;
import com.enterprise.fashion.ecommerce.product.application.media.QuarantinedProductImageAsset;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductImageSecurityCheckPort;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Repository
public class JdbcProductImageSecurityCheckAdapter implements ProductImageSecurityCheckPort {
    private final JdbcClient jdbc;
    public JdbcProductImageSecurityCheckAdapter(JdbcClient jdbc) { this.jdbc = Objects.requireNonNull(jdbc); }

    @Override
    public RetrievedAsset retrieve(UUID assetId, ProductId expectedOwner) {
        requireTransaction();
        var row = jdbc.sql("""
                SELECT product_id, sha256, detected_format, byte_length, pixel_width, pixel_height,
                       quarantine_status, created_at, original_bytes
                  FROM product.product_image_quarantine WHERE asset_id = :assetId
                """).param("assetId", assetId).query((rs, ignored) -> new Raw(
                        rs.getString("product_id"), rs.getString("sha256"), rs.getString("detected_format"),
                        rs.getLong("byte_length"), rs.getInt("pixel_width"), rs.getInt("pixel_height"),
                        rs.getString("quarantine_status"), rs.getTimestamp("created_at").toInstant(),
                        rs.getBytes("original_bytes"))).optional()
                .orElseThrow(() -> new Failure(FailureReason.MISSING_ASSET));
        if (!row.productId.equals(expectedOwner.value())) throw new Failure(FailureReason.WRONG_PRODUCT_OWNER);
        if (row.bytes == null || !digest(row.bytes).equals(row.sha256)) throw new Failure(FailureReason.DIGEST_MISMATCH);
        try {
            var asset = new QuarantinedProductImageAsset(assetId, expectedOwner, row.sha256,
                    QuarantinedProductImageAsset.Format.valueOf(row.format), row.length, row.width, row.height,
                    QuarantinedProductImageAsset.Status.valueOf(row.status), row.createdAt);
            return new RetrievedAsset(asset, row.bytes);
        } catch (RuntimeException invalid) { throw new Failure(FailureReason.CORRUPT_ASSET); }
    }

    @Override
    public void append(ProductMediaSecurityCheckResult result) {
        requireTransaction();
        int inserted = jdbc.sql("""
                INSERT INTO product.product_image_security_check
                    (check_id, asset_id, product_id, original_sha256, check_definition,
                     implementation_version, executed_at, outcome, failure_reason,
                     malware_scan_status, authoritative_acceptance_eligible)
                VALUES (:checkId, :assetId, :productId, :digest, :definition,
                        :version, :executedAt, :outcome, :failure,
                        :malware, :eligible)
                """).param("checkId", result.checkId()).param("assetId", result.assetId())
                .param("productId", result.productId().value()).param("digest", result.originalSha256())
                .param("definition", result.checkDefinition()).param("version", result.implementationVersion())
                .param("executedAt", java.sql.Timestamp.from(result.executedAt())).param("outcome", result.outcome().name())
                .param("failure", result.failureReason() == null ? null : result.failureReason().name())
                .param("malware", result.malwareScan().name()).param("eligible", result.authoritativeAcceptanceEligible()).update();
        if (inserted != 1) throw new IllegalStateException("Technical media-security result was not persisted");
    }

    private void requireTransaction() {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) throw new IllegalStateException("Checking requires a transaction");
    }
    private static String digest(byte[] bytes) {
        try { return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes)); }
        catch (NoSuchAlgorithmException impossible) { throw new IllegalStateException(impossible); }
    }
    private record Raw(String productId, String sha256, String format, long length, int width, int height,
            String status, java.time.Instant createdAt, byte[] bytes) { }
}
