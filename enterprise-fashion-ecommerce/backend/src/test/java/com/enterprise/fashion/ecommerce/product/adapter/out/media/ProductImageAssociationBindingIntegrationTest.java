package com.enterprise.fashion.ecommerce.product.adapter.out.media;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.enterprise.fashion.ecommerce.product.application.persistence.ProductPersistenceService;
import com.enterprise.fashion.ecommerce.product.application.usecase.AssessProductPublicationReadiness;
import com.enterprise.fashion.ecommerce.product.application.usecase.BindProductImageAssociation;
import com.enterprise.fashion.ecommerce.product.application.usecase.IngestProductImage;
import com.enterprise.fashion.ecommerce.product.application.usecase.UpdateDraftProduct;
import com.enterprise.fashion.ecommerce.product.application.visibility.ProductPublicationReadiness;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductAttributeStandard;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductContent;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductFamily;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductMedia;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductRevision;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariantId;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("integration")
@SpringBootTest
@Testcontainers
class ProductImageAssociationBindingIntegrationTest {
    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:18");

    @Autowired ProductPersistenceService products;
    @Autowired IngestProductImage ingestion;
    @Autowired UpdateDraftProduct updates;
    @Autowired BindProductImageAssociation bindings;
    @Autowired AssessProductPublicationReadiness readiness;
    @Autowired JdbcTemplate jdbc;

    @Test
    void exactBindingPersistsWithoutAcceptanceOrSecurityCheckSideEffects() throws Exception {
        Fixture fixture = fixture();

        var binding = bindings.bind(command(fixture));

        assertThat(bindings.current(query(fixture))).contains(binding);
        var row = jdbc.queryForMap("""
                SELECT product_revision, association_revision, asset_revision, original_sha256,
                       variant_id, authoritative_acceptance_eligible
                  FROM product.product_image_association_binding WHERE binding_id = ?
                """, binding.bindingId());
        assertThat(row.get("product_revision")).isEqualTo(fixture.product().revision().value());
        assertThat(row.get("association_revision")).isEqualTo(fixture.product().revision().value());
        assertThat(row.get("asset_revision")).isEqualTo(fixture.assetRevision());
        assertThat(row.get("original_sha256")).isEqualTo(fixture.sha256());
        assertThat(row.get("variant_id")).isNull();
        assertThat(row.get("authoritative_acceptance_eligible")).isEqualTo(false);
        assertThat(jdbc.queryForObject("SELECT count(*) FROM product.product_image_security_check", Integer.class)).isZero();
        assertThat(jdbc.queryForObject("SELECT count(*) FROM product.product_image_malware_observation", Integer.class)).isZero();
        assertThat(jdbc.queryForObject("SELECT count(*) FROM product.evidence_event", Integer.class)).isZero();
        assertThat(readiness.assess(fixture.product()).outcome())
                .isEqualTo(ProductPublicationReadiness.Outcome.EVIDENCE_UNAVAILABLE);
    }

    @Test
    void missingWronglyOwnedAndMismatchedAssetsFailWithoutRows() throws Exception {
        Fixture owner = fixture();
        Fixture other = fixture();

        assertReason(new BindProductImageAssociation.Command(owner.product().id(), owner.product().revision(),
                owner.mediaId(), owner.product().revision(), UUID.randomUUID(), owner.assetRevision(),
                owner.sha256(), null), BindProductImageAssociation.Reason.ASSET_NOT_FOUND);
        assertReason(new BindProductImageAssociation.Command(owner.product().id(), owner.product().revision(),
                owner.mediaId(), owner.product().revision(), other.assetId(), other.assetRevision(),
                other.sha256(), null), BindProductImageAssociation.Reason.WRONG_PRODUCT_OWNER);
        assertReason(new BindProductImageAssociation.Command(owner.product().id(), owner.product().revision(),
                owner.mediaId(), owner.product().revision(), owner.assetId(), owner.assetRevision(),
                "0".repeat(64), null), BindProductImageAssociation.Reason.ASSET_DIGEST_CONFLICT);

        assertThat(bindingCount(owner.product().id())).isZero();
    }

    @Test
    void invalidAssociationInputsAndDuplicateBindingRollBack() throws Exception {
        Fixture fixture = fixture();
        ProductRevision stale = new ProductRevision("v1_" + "f".repeat(64));

        assertReason(new BindProductImageAssociation.Command(fixture.product().id(), fixture.product().revision(),
                "unknown", fixture.product().revision(), fixture.assetId(), fixture.assetRevision(),
                fixture.sha256(), null), BindProductImageAssociation.Reason.MEDIA_ASSOCIATION_NOT_FOUND);
        assertReason(new BindProductImageAssociation.Command(fixture.product().id(), stale,
                fixture.mediaId(), stale, fixture.assetId(), fixture.assetRevision(), fixture.sha256(), null),
                BindProductImageAssociation.Reason.PRODUCT_REVISION_CONFLICT);
        assertReason(new BindProductImageAssociation.Command(fixture.product().id(), fixture.product().revision(),
                fixture.mediaId(), stale, fixture.assetId(), fixture.assetRevision(), fixture.sha256(), null),
                BindProductImageAssociation.Reason.ASSOCIATION_REVISION_CONFLICT);
        assertReason(new BindProductImageAssociation.Command(fixture.product().id(), fixture.product().revision(),
                fixture.mediaId(), fixture.product().revision(), fixture.assetId(), "sha256_" + "0".repeat(64),
                fixture.sha256(), null), BindProductImageAssociation.Reason.ASSET_REVISION_CONFLICT);
        assertReason(new BindProductImageAssociation.Command(fixture.product().id(), fixture.product().revision(),
                fixture.mediaId(), fixture.product().revision(), fixture.assetId(), fixture.assetRevision(),
                fixture.sha256(), new ProductVariantId("unknown-variant")),
                BindProductImageAssociation.Reason.VARIANT_ASSOCIATION_CONFLICT);

        bindings.bind(command(fixture));
        assertThatThrownBy(() -> bindings.bind(command(fixture)))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThat(bindingCount(fixture.product().id())).isOne();
    }

    @Test
    void historicalBindingIsImmutableAndBecomesObsoleteAfterRepresentationChange() throws Exception {
        Fixture fixture = fixture();
        var original = bindings.bind(command(fixture));

        var updated = updates.update(new UpdateDraftProduct.Command(fixture.product().id(),
                fixture.product().revision(), new ProductContent("Coat", "Updated description"),
                fixture.product().variants(), fixture.product().media()));

        assertThat(bindings.current(query(fixture))).isEmpty();
        assertThat(bindingCount(fixture.product().id())).isOne();
        assertThatThrownBy(() -> jdbc.update("""
                UPDATE product.product_image_association_binding SET bound_at = now()
                 WHERE binding_id = ?
                """, original.bindingId())).isInstanceOf(DataIntegrityViolationException.class);
        assertThatThrownBy(() -> jdbc.update("""
                DELETE FROM product.product_image_association_binding WHERE binding_id = ?
                """, original.bindingId())).isInstanceOf(DataIntegrityViolationException.class);
        assertThatThrownBy(() -> jdbc.execute("TRUNCATE product.product_image_association_binding"))
                .isInstanceOf(DataIntegrityViolationException.class);

        Product current = products.load(fixture.product().id()).orElseThrow();
        assertThat(current.revision()).isEqualTo(updated.revision());
        Fixture revised = new Fixture(current, fixture.mediaId(), fixture.assetId(), fixture.sha256(),
                fixture.assetRevision());
        assertThat(bindings.bind(command(revised)).productRevision()).isEqualTo(updated.revision());
        assertThat(bindingCount(fixture.product().id())).isEqualTo(2);
    }

    private Fixture fixture() throws Exception {
        ProductId id = new ProductId("product-" + UUID.randomUUID());
        Product draft = new Product(id, ProductFamily.APPAREL, ProductAttributeStandard.INITIAL, true, false);
        products.create(draft);
        var asset = ingestion.ingest(id, image());
        String mediaId = "hero-" + UUID.randomUUID();
        ProductMedia media = new ProductMedia(mediaId, id, null, asset.assetRevision(),
                "https://media.example/hero.png", "Front view of coat");
        updates.update(new UpdateDraftProduct.Command(id, draft.revision(),
                new ProductContent("Coat", "A winter coat"), List.of(), List.of(media)));
        return new Fixture(products.load(id).orElseThrow(), mediaId, asset.assetId(), asset.sha256(),
                asset.assetRevision());
    }

    private static BindProductImageAssociation.Command command(Fixture fixture) {
        return new BindProductImageAssociation.Command(fixture.product().id(), fixture.product().revision(),
                fixture.mediaId(), fixture.product().revision(), fixture.assetId(), fixture.assetRevision(),
                fixture.sha256(), null);
    }

    private static BindProductImageAssociation.Query query(Fixture fixture) {
        return new BindProductImageAssociation.Query(fixture.product().id(), fixture.product().revision(),
                fixture.mediaId(), fixture.assetId(), fixture.assetRevision(), null);
    }

    private void assertReason(BindProductImageAssociation.Command command,
            BindProductImageAssociation.Reason expected) {
        assertThatThrownBy(() -> bindings.bind(command))
                .isInstanceOfSatisfying(BindProductImageAssociation.Rejected.class,
                        rejected -> assertThat(rejected.reason()).isEqualTo(expected));
    }

    private int bindingCount(ProductId productId) {
        return jdbc.queryForObject("""
                SELECT count(*) FROM product.product_image_association_binding WHERE product_id = ?
                """, Integer.class, productId.value());
    }

    private static byte[] image() throws Exception {
        var output = new ByteArrayOutputStream();
        ImageIO.write(new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB), "png", output);
        return output.toByteArray();
    }

    private record Fixture(Product product, String mediaId, UUID assetId, String sha256, String assetRevision) { }
}
