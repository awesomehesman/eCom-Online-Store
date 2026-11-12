package com.enterprise.fashion.ecommerce.product.application.usecase;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.application.media.ProductImageAssociationBinding;
import com.enterprise.fashion.ecommerce.product.application.media.QuarantinedProductImageAsset;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductImageAssociationBindingPort;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductPersistencePort;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductAttributeStandard;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductContent;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductFamily;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductMedia;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductRevision;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariantId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionOperations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BindProductImageAssociationTest {
    private static final Instant NOW = Instant.parse("2026-11-12T12:00:00Z");
    private static final UUID ASSET_ID = UUID.fromString("10000000-0000-0000-0000-000000000001");
    private static final UUID BINDING_ID = UUID.fromString("20000000-0000-0000-0000-000000000001");
    private static final String SHA256 = "a".repeat(64);
    private static final String ASSET_REVISION = "sha256_" + SHA256;
    private Product product;
    private QuarantinedProductImageAsset asset;
    private FakeBindings bindings;
    private BindProductImageAssociation useCase;

    @BeforeEach
    void setUp() {
        ProductId id = new ProductId("product-1");
        product = new Product(id, ProductFamily.APPAREL, ProductAttributeStandard.INITIAL, true, false)
                .withRepresentation(new ProductContent("Coat", "A winter coat"), List.of(
                        new ProductMedia("hero", id, null, ASSET_REVISION,
                                "https://media.example/hero.png", "Front view of coat")));
        asset = new QuarantinedProductImageAsset(ASSET_ID, id, SHA256,
                QuarantinedProductImageAsset.Format.PNG, 100, 10, 10,
                QuarantinedProductImageAsset.Status.QUARANTINED, NOW.minusSeconds(60));
        bindings = new FakeBindings(asset);
        useCase = new BindProductImageAssociation(new ProductSource(product), bindings,
                direct(), direct(), Clock.fixed(NOW, ZoneOffset.UTC), () -> BINDING_ID);
    }

    @Test
    void bindsExactCurrentProductAssetAndAssociationWithoutAcceptance() {
        ProductImageAssociationBinding binding = useCase.bind(command());

        assertThat(binding.bindingId()).isEqualTo(BINDING_ID);
        assertThat(binding.productRevision()).isEqualTo(product.revision());
        assertThat(binding.associationRevision()).isEqualTo(product.revision());
        assertThat(binding.assetId()).isEqualTo(ASSET_ID);
        assertThat(binding.originalSha256()).isEqualTo(SHA256);
        assertThat(binding.authoritativeAcceptanceEligible()).isFalse();
        assertThat(useCase.current(query())).contains(binding);
    }

    @Test
    void rejectsUnknownAssociationAndStaleProductOrAssociationRevision() {
        assertReason(command("missing", product.revision(), product.revision(), ASSET_ID,
                ASSET_REVISION, SHA256, null), BindProductImageAssociation.Reason.MEDIA_ASSOCIATION_NOT_FOUND);
        ProductRevision stale = revision('b');
        assertReason(command("hero", stale, stale, ASSET_ID, ASSET_REVISION, SHA256, null),
                BindProductImageAssociation.Reason.PRODUCT_REVISION_CONFLICT);
        assertReason(command("hero", product.revision(), stale, ASSET_ID, ASSET_REVISION, SHA256, null),
                BindProductImageAssociation.Reason.ASSOCIATION_REVISION_CONFLICT);
    }

    @Test
    void rejectsMissingWronglyOwnedOrMismatchedAsset() {
        bindings.asset = null;
        assertReason(command(), BindProductImageAssociation.Reason.ASSET_NOT_FOUND);

        bindings.asset = new QuarantinedProductImageAsset(ASSET_ID, new ProductId("other"), SHA256,
                QuarantinedProductImageAsset.Format.PNG, 100, 10, 10,
                QuarantinedProductImageAsset.Status.QUARANTINED, NOW);
        assertReason(command(), BindProductImageAssociation.Reason.WRONG_PRODUCT_OWNER);

        bindings.asset = asset;
        assertReason(command("hero", product.revision(), product.revision(), ASSET_ID,
                ASSET_REVISION, "b".repeat(64), null), BindProductImageAssociation.Reason.ASSET_DIGEST_CONFLICT);
        assertReason(command("hero", product.revision(), product.revision(), ASSET_ID,
                "sha256_" + "b".repeat(64), SHA256, null), BindProductImageAssociation.Reason.ASSET_REVISION_CONFLICT);
    }

    @Test
    void rejectsIncorrectVariantAssociation() {
        assertReason(command("hero", product.revision(), product.revision(), ASSET_ID,
                ASSET_REVISION, SHA256, new ProductVariantId("variant-1")),
                BindProductImageAssociation.Reason.VARIANT_ASSOCIATION_CONFLICT);
    }

    @Test
    void obsoleteBindingCannotResolveAgainstChangedRepresentation() {
        ProductImageAssociationBinding binding = useCase.bind(command());
        Product changed = product.withRepresentation(new ProductContent("Coat", "Updated description"), product.media());
        useCase = new BindProductImageAssociation(new ProductSource(changed), bindings,
                direct(), direct(), Clock.fixed(NOW, ZoneOffset.UTC), UUID::randomUUID);

        assertThat(useCase.current(query())).isEmpty();
        assertThat(bindings.history).containsExactly(binding);
    }

    @Test
    void concurrentRevisionChangeFailsClosedWithoutRecordingAStaleBinding() {
        bindings.appendFailure = new PessimisticLockingFailureException("concurrent Product revision change");

        assertReason(command(), BindProductImageAssociation.Reason.PRODUCT_REVISION_CONFLICT);
        assertThat(bindings.history).isEmpty();
    }

    private BindProductImageAssociation.Command command() {
        return command("hero", product.revision(), product.revision(), ASSET_ID,
                ASSET_REVISION, SHA256, null);
    }

    private BindProductImageAssociation.Command command(String mediaId, ProductRevision productRevision,
            ProductRevision associationRevision, UUID assetId, String assetRevision, String digest,
            ProductVariantId variantId) {
        return new BindProductImageAssociation.Command(product.id(), productRevision, mediaId,
                associationRevision, assetId, assetRevision, digest, variantId);
    }

    private BindProductImageAssociation.Query query() {
        return new BindProductImageAssociation.Query(product.id(), product.revision(), "hero",
                ASSET_ID, ASSET_REVISION, null);
    }

    private void assertReason(BindProductImageAssociation.Command command,
            BindProductImageAssociation.Reason reason) {
        assertThatThrownBy(() -> useCase.bind(command))
                .isInstanceOfSatisfying(BindProductImageAssociation.Rejected.class,
                        rejected -> assertThat(rejected.reason()).isEqualTo(reason));
    }

    private static ProductRevision revision(char value) {
        return new ProductRevision("v1_" + String.valueOf(value).repeat(64));
    }

    private static TransactionOperations direct() {
        return new TransactionOperations() {
            @Override
            public <T> T execute(TransactionCallback<T> action) {
                return action.doInTransaction(null);
            }
        };
    }

    private static final class ProductSource implements ProductPersistencePort {
        private final Product product;
        private ProductSource(Product product) { this.product = product; }
        @Override public void create(Product ignored) { throw new UnsupportedOperationException(); }
        @Override public boolean replaceDraft(Product replacement, ProductRevision expected) { return false; }
        @Override public Optional<Product> load(ProductId id) {
            return product.id().equals(id) ? Optional.of(product) : Optional.empty();
        }
        @Override public List<Product> loadAll(List<ProductId> ids) { throw new UnsupportedOperationException(); }
        @Override public List<ProductId> findProductIdsAfter(ProductId after, int limit) { throw new UnsupportedOperationException(); }
        @Override public List<ProductId> findCatalogueCandidateIdsAfter(ProductId after, int limit) { throw new UnsupportedOperationException(); }
    }

    private static final class FakeBindings implements ProductImageAssociationBindingPort {
        private QuarantinedProductImageAsset asset;
        private final List<ProductImageAssociationBinding> history = new ArrayList<>();
        private RuntimeException appendFailure;
        private FakeBindings(QuarantinedProductImageAsset asset) { this.asset = asset; }
        @Override public Optional<QuarantinedProductImageAsset> quarantinedAsset(UUID assetId) {
            return asset != null && asset.assetId().equals(assetId) ? Optional.of(asset) : Optional.empty();
        }
        @Override public ProductImageAssociationBinding append(ProductImageAssociationBinding binding) {
            if (appendFailure != null) {
                throw appendFailure;
            }
            history.add(binding);
            return binding;
        }
        @Override public Optional<ProductImageAssociationBinding> exact(ProductId productId,
                ProductRevision revision, String mediaId, UUID assetId) {
            return history.stream().filter(item -> item.productId().equals(productId)
                    && item.productRevision().equals(revision)
                    && item.mediaAssociationId().equals(mediaId)
                    && item.assetId().equals(assetId)).findFirst();
        }
    }
}
