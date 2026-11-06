package com.enterprise.fashion.ecommerce.product.application.usecase;

import java.util.List;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.application.persistence.ProductPersistenceService;
import com.enterprise.fashion.ecommerce.product.application.visibility.ProductVisibilityReadiness;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductAttributeStandard;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductColour;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductContent;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductFamily;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductLifecycle;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductMedia;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariant;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariantId;
import com.enterprise.fashion.ecommerce.product.domain.model.VariantConfiguration;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("integration")
@SpringBootTest
@Testcontainers
class CreateDraftProductIntegrationTest {
    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:18");

    @Autowired private CreateDraftProduct commands;
    @Autowired private ProductPersistenceService persistence;
    @Autowired private JdbcTemplate jdbc;

    private ProductId id() { return new ProductId(UUID.randomUUID().toString()); }

    private ProductVariant variant(ProductId owner, String variantId) {
        return new ProductVariant(new ProductVariantId(variantId), owner,
                new VariantConfiguration(ProductFamily.ACCESSORIES, ProductAttributeStandard.INITIAL,
                        false, true, null, new ProductColour("black")), "Regular", "Cotton");
    }

    private ProductMedia media(ProductId owner, String mediaId, ProductVariantId variantId) {
        return new ProductMedia(mediaId, owner, variantId, "asset-v1", "https://example.test/image", "Bag front");
    }

    private CreateDraftProduct.Command command(ProductId id, List<ProductVariant> variants, List<ProductMedia> media) {
        return new CreateDraftProduct.Command(id, ProductFamily.ACCESSORIES, ProductAttributeStandard.INITIAL,
                false, true, variants, new ProductContent("Bag", "Cotton bag"), media);
    }

    @Test
    void createsWholeDraftAndReconstructsExactRevisionWithoutVisibilityOrAcceptance() {
        ProductId id = id();
        var variant = variant(id, UUID.randomUUID().toString());
        var media = media(id, UUID.randomUUID().toString(), variant.id());
        var command = command(id, List.of(variant), List.of(media));
        var created = commands.create(command);
        Product loaded = persistence.load(id).orElseThrow();
        assertThat(created.id()).isEqualTo(id);
        assertThat(loaded.lifecycle()).isEqualTo(ProductLifecycle.DRAFT);
        assertThat(loaded.family()).isEqualTo(command.family());
        assertThat(loaded.standard()).isEqualTo(command.standard());
        assertThat(loaded.sizeApplicable()).isFalse();
        assertThat(loaded.colourApplicable()).isTrue();
        assertThat(loaded.variants()).containsExactly(variant);
        assertThat(loaded.variants().getFirst().configuration().colour().code()).isEqualTo("BLACK");
        assertThat(loaded.content()).isEqualTo(command.content());
        assertThat(loaded.media()).containsExactly(media);
        assertThat(loaded.revision()).isEqualTo(created.revision());
        assertThat(persistence.load(id).orElseThrow().revision()).isEqualTo(created.revision());
        assertThat(loaded.isCustomerVisible()).isFalse();
        assertThat(new AssessProductVisibilityReadiness(persistence).assess(id)).isEqualTo(ProductVisibilityReadiness.NOT_VISIBLE);
        assertThatThrownBy(() -> loaded.variants().clear()).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> loaded.media().clear()).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void supportsIncompleteDraftWithoutContentVariantsOrMedia() {
        ProductId id = id();
        var result = commands.create(new CreateDraftProduct.Command(id, ProductFamily.ACCESSORIES,
                ProductAttributeStandard.INITIAL, false, false, List.of(), null, List.of()));
        Product loaded = persistence.load(id).orElseThrow();
        assertThat(loaded.content()).isNull();
        assertThat(loaded.variants()).isEmpty();
        assertThat(loaded.media()).isEmpty();
        assertThat(loaded.revision()).isEqualTo(result.revision());
        assertThat(loaded.lifecycle()).isEqualTo(ProductLifecycle.DRAFT);
    }

    @Test
    void duplicateIdentityFailsWithoutOverwritingOriginalRepresentation() {
        ProductId id = id();
        var original = commands.create(command(id, List.of(), List.of()));
        var changed = new CreateDraftProduct.Command(id, ProductFamily.ACCESSORIES, ProductAttributeStandard.INITIAL,
                false, true, List.of(), new ProductContent("Other bag", "Other content"), List.of());
        assertThatThrownBy(() -> commands.create(changed)).isInstanceOf(DbActionExecutionException.class).hasCauseInstanceOf(DuplicateKeyException.class);
        assertThat(persistence.load(id).orElseThrow().revision()).isEqualTo(original.revision());
    }

    @Test
    void duplicateVariantIdentityRollsBackProductAndContent() {
        ProductId first = id();
        String variantId = UUID.randomUUID().toString();
        commands.create(command(first, List.of(variant(first, variantId)), List.of()));
        ProductId failed = id();
        assertThatThrownBy(() -> commands.create(command(failed, List.of(variant(failed, variantId)), List.of())))
                .isInstanceOf(DbActionExecutionException.class).hasCauseInstanceOf(DuplicateKeyException.class);
        assertThat(persistence.load(failed)).isEmpty();
        assertThat(persistence.load(first).orElseThrow().variants().getFirst().productId()).isEqualTo(first);
    }

    @Test
    void lateMediaFailureRollsBackProductVariantsContentAndEarlierMedia() {
        ProductId first = id();
        String collision = UUID.randomUUID().toString();
        commands.create(command(first, List.of(), List.of(media(first, collision, null))));
        ProductId failed = id();
        var variant = variant(failed, UUID.randomUUID().toString());
        String earlierMedia = UUID.randomUUID().toString();
        assertThatThrownBy(() -> commands.create(command(failed, List.of(variant),
                List.of(media(failed, earlierMedia, variant.id()), media(failed, collision, null)))))
                .isInstanceOf(DbActionExecutionException.class).hasCauseInstanceOf(DuplicateKeyException.class);
        assertThat(persistence.load(failed)).isEmpty();
        assertThat(jdbc.queryForObject("SELECT count(*) FROM product.product_variant WHERE product_id = ?", Long.class, failed.value())).isZero();
        assertThat(jdbc.queryForObject("SELECT count(*) FROM product.product_media WHERE media_id = ?", Long.class, earlierMedia)).isZero();
        assertThat(persistence.load(first).orElseThrow().media()).hasSize(1);
    }
}
