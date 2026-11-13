package com.enterprise.fashion.ecommerce.product.adapter.out.media;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.application.media.ProductImageAssociationBinding;
import com.enterprise.fashion.ecommerce.product.application.media.ProductImageMalwareObservation;
import com.enterprise.fashion.ecommerce.product.application.media.ProductImageMalwareScanResult;
import com.enterprise.fashion.ecommerce.product.application.media.ProductMediaSecurityCheckResult;
import com.enterprise.fashion.ecommerce.product.application.media.ProductMediaSecurityEvidenceCorrelation.Subject;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductMediaSecurityEvidenceCorrelationPort;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductRevision;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariantId;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/** PostgreSQL read adapter over immutable binding and technical-observation history. */
@Repository
public class JdbcProductMediaSecurityEvidenceCorrelationAdapter
        implements ProductMediaSecurityEvidenceCorrelationPort {
    private final JdbcClient jdbc;

    public JdbcProductMediaSecurityEvidenceCorrelationAdapter(JdbcClient jdbc) {
        this.jdbc = Objects.requireNonNull(jdbc, "jdbc");
    }

    @Override
    public Optional<ProductImageAssociationBinding> exactBinding(Subject subject) {
        requireReadTransaction();
        return jdbc.sql("""
                SELECT binding_id, product_id, product_revision, media_association_id,
                       association_revision, asset_id, asset_revision, original_sha256,
                       variant_id, bound_at, authoritative_acceptance_eligible
                  FROM product.product_image_association_binding
                 WHERE product_id = :product
                   AND product_revision = :productRevision
                   AND media_association_id = :media
                   AND association_revision = :associationRevision
                   AND asset_id = :asset
                   AND original_sha256 = :digest
                   AND asset_revision = :assetRevision
                   AND variant_id IS NOT DISTINCT FROM :variant
                """).param("product", subject.productId().value())
                .param("productRevision", subject.productRevision().value())
                .param("media", subject.mediaAssociationId())
                .param("associationRevision", subject.associationRevision().value())
                .param("asset", subject.assetId()).param("digest", subject.originalSha256())
                .param("assetRevision", subject.assetRevision())
                .param("variant", subject.variantId() == null ? null : subject.variantId().value())
                .query((rs, ignored) -> binding(rs)).optional();
    }

    @Override
    public List<ProductMediaSecurityCheckResult> technicalObservations(Subject subject) {
        requireReadTransaction();
        return jdbc.sql("""
                SELECT check_id, asset_id, product_id, original_sha256, check_definition,
                       implementation_version, executed_at, outcome, failure_reason,
                       malware_scan_status, authoritative_acceptance_eligible
                  FROM product.product_image_security_check
                 WHERE asset_id = :asset AND product_id = :product AND original_sha256 = :digest
                 ORDER BY executed_at, check_id
                """).param("asset", subject.assetId()).param("product", subject.productId().value())
                .param("digest", subject.originalSha256())
                .query((rs, ignored) -> technical(rs)).list();
    }

    @Override
    public List<ProductImageMalwareObservation> malwareObservations(Subject subject) {
        requireReadTransaction();
        return jdbc.sql("""
                SELECT observation_id, asset_id, product_id, original_sha256, scanner_identity,
                       check_definition, implementation_version, engine_version,
                       signature_database_version, signature_database_timestamp, executed_at,
                       outcome, detection_signature, failure_category,
                       authoritative_acceptance_eligible
                  FROM product.product_image_malware_observation
                 WHERE asset_id = :asset AND product_id = :product AND original_sha256 = :digest
                 ORDER BY executed_at, observation_id
                """).param("asset", subject.assetId()).param("product", subject.productId().value())
                .param("digest", subject.originalSha256())
                .query((rs, ignored) -> malware(rs)).list();
    }

    @Override
    public boolean isBindingWithdrawn(UUID bindingId) {
        requireReadTransaction();
        return jdbc.sql("""
                SELECT 1 FROM product.product_image_evidence_withdrawal
                 WHERE binding_id = :bindingId
                """).param("bindingId", bindingId)
                .query((rs, ignored) -> true).optional().orElse(false);
    }

    @Override
    public Set<UUID> withdrawnTechnicalCheckIds(Subject subject) {
        requireReadTransaction();
        return new HashSet<>(jdbc.sql("""
                SELECT w.technical_check_id
                  FROM product.product_image_evidence_withdrawal w
                  JOIN product.product_image_security_check c
                    ON c.check_id = w.technical_check_id
                 WHERE c.asset_id = :asset
                   AND c.product_id = :product
                   AND c.original_sha256 = :digest
                   AND w.technical_check_id IS NOT NULL
                """).param("asset", subject.assetId())
                .param("product", subject.productId().value())
                .param("digest", subject.originalSha256())
                .query((rs, ignored) -> rs.getObject("technical_check_id", UUID.class))
                .list());
    }

    @Override
    public Set<UUID> withdrawnMalwareObservationIds(Subject subject) {
        requireReadTransaction();
        return new HashSet<>(jdbc.sql("""
                SELECT w.malware_observation_id
                  FROM product.product_image_evidence_withdrawal w
                  JOIN product.product_image_malware_observation o
                    ON o.observation_id = w.malware_observation_id
                 WHERE o.asset_id = :asset
                   AND o.product_id = :product
                   AND o.original_sha256 = :digest
                   AND w.malware_observation_id IS NOT NULL
                """).param("asset", subject.assetId())
                .param("product", subject.productId().value())
                .param("digest", subject.originalSha256())
                .query((rs, ignored) -> rs.getObject("malware_observation_id", UUID.class))
                .list());
    }

    private static ProductImageAssociationBinding binding(ResultSet rs) throws SQLException {
        return new ProductImageAssociationBinding(rs.getObject("binding_id", UUID.class),
                new ProductId(rs.getString("product_id")),
                new ProductRevision(rs.getString("product_revision")), rs.getString("media_association_id"),
                new ProductRevision(rs.getString("association_revision")),
                rs.getObject("asset_id", UUID.class), rs.getString("asset_revision"),
                rs.getString("original_sha256"), variant(rs.getString("variant_id")),
                rs.getTimestamp("bound_at").toInstant(),
                rs.getBoolean("authoritative_acceptance_eligible"));
    }

    private static ProductMediaSecurityCheckResult technical(ResultSet rs) throws SQLException {
        String failure = rs.getString("failure_reason");
        return new ProductMediaSecurityCheckResult(rs.getObject("check_id", UUID.class),
                rs.getObject("asset_id", UUID.class), new ProductId(rs.getString("product_id")),
                rs.getString("original_sha256"), rs.getString("check_definition"),
                rs.getString("implementation_version"), rs.getTimestamp("executed_at").toInstant(),
                ProductMediaSecurityCheckResult.Outcome.valueOf(rs.getString("outcome")),
                failure == null ? null : ProductMediaSecurityCheckResult.FailureReason.valueOf(failure),
                ProductMediaSecurityCheckResult.MalwareScan.valueOf(rs.getString("malware_scan_status")),
                rs.getBoolean("authoritative_acceptance_eligible"));
    }

    private static ProductImageMalwareObservation malware(ResultSet rs) throws SQLException {
        String failure = rs.getString("failure_category");
        var signatureTimestamp = rs.getTimestamp("signature_database_timestamp");
        var scan = new ProductImageMalwareScanResult(
                ProductImageMalwareScanResult.Outcome.valueOf(rs.getString("outcome")),
                rs.getString("scanner_identity"), rs.getString("check_definition"),
                rs.getString("implementation_version"), rs.getString("engine_version"),
                rs.getString("signature_database_version"),
                signatureTimestamp == null ? null : signatureTimestamp.toInstant(),
                rs.getString("detection_signature"), failure == null ? null
                        : ProductImageMalwareScanResult.FailureCategory.valueOf(failure));
        return new ProductImageMalwareObservation(rs.getObject("observation_id", UUID.class),
                rs.getObject("asset_id", UUID.class), new ProductId(rs.getString("product_id")),
                rs.getString("original_sha256"), rs.getTimestamp("executed_at").toInstant(), scan,
                rs.getBoolean("authoritative_acceptance_eligible"));
    }

    private static ProductVariantId variant(String value) {
        return value == null ? null : new ProductVariantId(value);
    }

    private static void requireReadTransaction() {
        if (!TransactionSynchronizationManager.isActualTransactionActive()
                || !TransactionSynchronizationManager.isCurrentTransactionReadOnly()) {
            throw new IllegalStateException("Media evidence correlation requires a read-only transaction");
        }
    }
}
