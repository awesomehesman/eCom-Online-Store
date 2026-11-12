package com.enterprise.fashion.ecommerce.product.application.usecase;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import com.enterprise.fashion.ecommerce.product.application.media.ProductImageAssociationBinding;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductImageAssociationBindingPort;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductPersistencePort;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductMedia;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductRevision;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariantId;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Internal structural binding use case. It validates current Product Media and quarantine identity,
 * but performs no acceptance, authorization, lifecycle mutation or publication-readiness promotion.
 */
public final class BindProductImageAssociation {
    private final ProductPersistencePort products;
    private final ProductImageAssociationBindingPort bindings;
    private final TransactionOperations reads;
    private final TransactionOperations writes;
    private final Clock clock;
    private final Supplier<UUID> identifiers;

    public BindProductImageAssociation(ProductPersistencePort products,
            ProductImageAssociationBindingPort bindings, TransactionOperations reads,
            TransactionOperations writes, Clock clock, Supplier<UUID> identifiers) {
        this.products = Objects.requireNonNull(products, "products");
        this.bindings = Objects.requireNonNull(bindings, "bindings");
        this.reads = Objects.requireNonNull(reads, "reads");
        this.writes = Objects.requireNonNull(writes, "writes");
        this.clock = Objects.requireNonNull(clock, "clock");
        this.identifiers = Objects.requireNonNull(identifiers, "identifiers");
    }

    public ProductImageAssociationBinding bind(Command command) {
        Objects.requireNonNull(command, "command");
        requireNoAmbientTransaction();
        try {
            return Objects.requireNonNull(writes.execute(status -> bindInTransaction(command)));
        } catch (PessimisticLockingFailureException concurrent) {
            throw new Rejected(Reason.PRODUCT_REVISION_CONFLICT);
        }
    }

    /** Returns a binding only while the same association remains current in the requested revision. */
    public Optional<ProductImageAssociationBinding> current(Query query) {
        Objects.requireNonNull(query, "query");
        requireNoAmbientTransaction();
        return Objects.requireNonNull(reads.execute(status -> {
            Product product = products.load(query.productId()).orElse(null);
            if (product == null || !product.revision().equals(query.productRevision())) {
                return Optional.empty();
            }
            ProductMedia media = media(product, query.mediaAssociationId()).orElse(null);
            if (media == null || !media.assetRevision().equals(query.assetRevision())
                    || !Objects.equals(media.variantId(), query.variantId())) {
                return Optional.empty();
            }
            return bindings.exact(query.productId(), query.productRevision(),
                    query.mediaAssociationId(), query.assetId());
        }));
    }

    private ProductImageAssociationBinding bindInTransaction(Command command) {
        Product product = products.load(command.productId())
                .orElseThrow(() -> new Rejected(Reason.PRODUCT_NOT_FOUND));
        if (!product.revision().equals(command.productRevision())) {
            throw new Rejected(Reason.PRODUCT_REVISION_CONFLICT);
        }
        if (!command.associationRevision().equals(product.revision())) {
            throw new Rejected(Reason.ASSOCIATION_REVISION_CONFLICT);
        }
        ProductMedia media = media(product, command.mediaAssociationId())
                .orElseThrow(() -> new Rejected(Reason.MEDIA_ASSOCIATION_NOT_FOUND));
        if (!media.assetRevision().equals(command.assetRevision())) {
            throw new Rejected(Reason.ASSET_REVISION_CONFLICT);
        }
        if (!Objects.equals(media.variantId(), command.variantId())) {
            throw new Rejected(Reason.VARIANT_ASSOCIATION_CONFLICT);
        }
        var asset = bindings.quarantinedAsset(command.assetId())
                .orElseThrow(() -> new Rejected(Reason.ASSET_NOT_FOUND));
        if (!asset.productId().equals(command.productId())) {
            throw new Rejected(Reason.WRONG_PRODUCT_OWNER);
        }
        if (!asset.sha256().equals(command.originalSha256())) {
            throw new Rejected(Reason.ASSET_DIGEST_CONFLICT);
        }
        if (!asset.assetRevision().equals(command.assetRevision())) {
            throw new Rejected(Reason.ASSET_REVISION_CONFLICT);
        }
        var binding = new ProductImageAssociationBinding(
                Objects.requireNonNull(identifiers.get(), "binding identity"), command.productId(),
                command.productRevision(), command.mediaAssociationId(), command.associationRevision(),
                command.assetId(), command.assetRevision(), command.originalSha256(), command.variantId(),
                Instant.now(clock), false);
        bindings.append(binding);
        return binding;
    }

    private static Optional<ProductMedia> media(Product product, String mediaAssociationId) {
        return product.media().stream().filter(item -> item.id().equals(mediaAssociationId)).findFirst();
    }

    private static void requireNoAmbientTransaction() {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException("Product image association binding must own its transaction");
        }
    }

    public record Command(ProductId productId, ProductRevision productRevision,
            String mediaAssociationId, ProductRevision associationRevision, UUID assetId,
            String assetRevision, String originalSha256, ProductVariantId variantId) {
        public Command {
            require(productId, productRevision, mediaAssociationId, associationRevision,
                    assetId, assetRevision, originalSha256);
        }
    }

    public record Query(ProductId productId, ProductRevision productRevision,
            String mediaAssociationId, UUID assetId, String assetRevision, ProductVariantId variantId) {
        public Query {
            require(productId, productRevision, mediaAssociationId, assetId, assetRevision);
        }
    }

    private static void require(Object... values) {
        for (Object value : values) {
            if (value == null || value instanceof String text && text.isBlank()) {
                throw new IllegalArgumentException("Complete Product image association identity is required");
            }
        }
    }

    public enum Reason {
        PRODUCT_NOT_FOUND,
        PRODUCT_REVISION_CONFLICT,
        ASSOCIATION_REVISION_CONFLICT,
        MEDIA_ASSOCIATION_NOT_FOUND,
        ASSET_NOT_FOUND,
        WRONG_PRODUCT_OWNER,
        ASSET_DIGEST_CONFLICT,
        ASSET_REVISION_CONFLICT,
        VARIANT_ASSOCIATION_CONFLICT
    }

    public static final class Rejected extends RuntimeException {
        private final Reason reason;

        public Rejected(Reason reason) {
            super("Product image association binding rejected: " + reason);
            this.reason = Objects.requireNonNull(reason, "reason");
        }

        public Reason reason() {
            return reason;
        }
    }
}
