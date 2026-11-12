package com.enterprise.fashion.ecommerce.product.adapter.out.media;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.application.media.ProductImageAssociationBinding;
import com.enterprise.fashion.ecommerce.product.application.media.QuarantinedProductImageAsset;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductImageAssociationBindingPort;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductRevision;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariantId;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/** PostgreSQL adapter for append-only, non-authoritative Product image association bindings. */
@Repository
public class JdbcProductImageAssociationBindingAdapter implements ProductImageAssociationBindingPort {
    private final JdbcClient jdbc;

    public JdbcProductImageAssociationBindingAdapter(JdbcClient jdbc) {
        this.jdbc = Objects.requireNonNull(jdbc, "jdbc");
    }

    @Override
    public Optional<QuarantinedProductImageAsset> quarantinedAsset(UUID assetId) {
        requireTransaction();
        Objects.requireNonNull(assetId, "assetId");
        return jdbc.sql("""
                SELECT product_id, sha256, detected_format, byte_length, pixel_width,
                       pixel_height, quarantine_status, created_at
                  FROM product.product_image_quarantine
                 WHERE asset_id = :assetId
                """).param("assetId", assetId).query((rs, ignored) ->
                        new QuarantinedProductImageAsset(assetId,
                                new ProductId(rs.getString("product_id")), rs.getString("sha256"),
                                QuarantinedProductImageAsset.Format.valueOf(rs.getString("detected_format")),
                                rs.getLong("byte_length"), rs.getInt("pixel_width"),
                                rs.getInt("pixel_height"),
                                QuarantinedProductImageAsset.Status.valueOf(rs.getString("quarantine_status")),
                                rs.getTimestamp("created_at").toInstant())).optional();
    }

    @Override
    public void append(ProductImageAssociationBinding binding) {
        requireTransaction();
        Objects.requireNonNull(binding, "binding");
        int inserted = jdbc.sql("""
                INSERT INTO product.product_image_association_binding
                    (binding_id, product_id, product_revision, media_association_id,
                     association_revision, asset_id, asset_revision, original_sha256,
                     variant_id, bound_at, authoritative_acceptance_eligible)
                VALUES (:bindingId, :productId, :productRevision, :mediaAssociationId,
                        :associationRevision, :assetId, :assetRevision, :originalSha256,
                        :variantId, :boundAt, false)
                """)
                .param("bindingId", binding.bindingId())
                .param("productId", binding.productId().value())
                .param("productRevision", binding.productRevision().value())
                .param("mediaAssociationId", binding.mediaAssociationId())
                .param("associationRevision", binding.associationRevision().value())
                .param("assetId", binding.assetId())
                .param("assetRevision", binding.assetRevision())
                .param("originalSha256", binding.originalSha256())
                .param("variantId", binding.variantId() == null ? null : binding.variantId().value())
                .param("boundAt", java.sql.Timestamp.from(binding.boundAt()))
                .update();
        if (inserted != 1) {
            throw new IllegalStateException("Product image association binding was not persisted");
        }
    }

    @Override
    public Optional<ProductImageAssociationBinding> exact(ProductId productId,
            ProductRevision productRevision, String mediaAssociationId, UUID assetId) {
        requireTransaction();
        Objects.requireNonNull(productId, "productId");
        Objects.requireNonNull(productRevision, "productRevision");
        Objects.requireNonNull(mediaAssociationId, "mediaAssociationId");
        Objects.requireNonNull(assetId, "assetId");
        return jdbc.sql("""
                SELECT binding_id, association_revision, asset_revision, original_sha256,
                       variant_id, bound_at, authoritative_acceptance_eligible
                  FROM product.product_image_association_binding
                 WHERE product_id = :productId
                   AND product_revision = :productRevision
                   AND media_association_id = :mediaAssociationId
                   AND asset_id = :assetId
                """)
                .param("productId", productId.value())
                .param("productRevision", productRevision.value())
                .param("mediaAssociationId", mediaAssociationId)
                .param("assetId", assetId)
                .query((rs, ignored) -> new ProductImageAssociationBinding(
                        rs.getObject("binding_id", UUID.class), productId, productRevision,
                        mediaAssociationId, new ProductRevision(rs.getString("association_revision")),
                        assetId, rs.getString("asset_revision"), rs.getString("original_sha256"),
                        rs.getString("variant_id") == null ? null
                                : new ProductVariantId(rs.getString("variant_id")),
                        rs.getTimestamp("bound_at").toInstant(),
                        rs.getBoolean("authoritative_acceptance_eligible")))
                .optional();
    }

    private static void requireTransaction() {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException("Product image association binding requires a transaction");
        }
    }
}
