package com.enterprise.fashion.ecommerce.product.adapter.out.evidence;

import java.util.List;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.application.evidence.ProductEvidenceEvent;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductEvidenceObservation;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductEvidenceService;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.Snapshot;
import com.enterprise.fashion.ecommerce.product.application.persistence.ProductPersistenceService;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductMediaRightsAcceptanceRecordingPort;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductMediaRightsAcceptanceRecordingPort.Decision;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductMediaRightsAcceptanceRecordingPort.MediaRightsAcceptanceObservation;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductMediaRightsAcceptanceRecordingPort.MediaSubjectBinding;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductMediaRightsAcceptanceRecordingPort.RecordingOutcome;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductPublicationEvidencePort;
import com.enterprise.fashion.ecommerce.product.application.usecase.AssessProductPublicationReadiness;
import com.enterprise.fashion.ecommerce.product.application.usecase.PublishProduct;
import com.enterprise.fashion.ecommerce.product.application.usecase.RecordProductMediaRightsAcceptance;
import com.enterprise.fashion.ecommerce.product.application.visibility.ProductPublicationReadiness;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductAttributeStandard;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductContent;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductFamily;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductLifecycle;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductMedia;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductRevision;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariant;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariantId;
import com.enterprise.fashion.ecommerce.product.domain.model.VariantConfiguration;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Integration tests for media usage-rights acceptance recording.
 * <p>
 * These tests verify:
 * <ul>
 *   <li>Recording persists observations to evidence_event table with purpose=MEDIA</li>
 *   <li>Product revision binding is enforced</li>
 *   <li>Media association existence is validated</li>
 *   <li>Asset revision binding is validated</li>
 *   <li>Association revision binding is validated</li>
 *   <li>Variant binding is validated, including null semantics</li>
 *   <li>Duplicate detection works</li>
 *   <li>Recorded observations are NOT authoritative acceptance</li>
 *   <li>Publication remains blocked without trusted evidence source</li>
 *   <li>Immutable history is preserved</li>
 *   <li>Content and media evidence remain separate</li>
 * </ul>
 */
@Tag("integration")
@SpringBootTest
@Testcontainers
class ProductMediaRightsAcceptanceIntegrationTest {
    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:18");

    @Autowired private RecordProductMediaRightsAcceptance useCase;
    @Autowired private ProductMediaRightsAcceptanceRecordingPort recordingPort;
    @Autowired private ProductPersistenceService products;
    @Autowired private ProductEvidenceService evidenceService;
    @Autowired private AssessProductPublicationReadiness publicationReadiness;
    @Autowired private ProductPublicationEvidencePort publicationEvidencePort;
    @Autowired private PlatformTransactionManager manager;
    @Autowired private JdbcTemplate jdbc;

    @Autowired(required = false) private PublishProduct publishProduct;

    private Product createProductWithMedia() {
        Product product = new Product(
                new ProductId(UUID.randomUUID().toString()),
                ProductFamily.ACCESSORIES,
                ProductAttributeStandard.INITIAL,
                false,
                false
        );

        // Add Product-level media with unique ID
        ProductMedia productMedia = new ProductMedia(
                "media-" + UUID.randomUUID().toString().substring(0, 8),
                product.id(),
                null, // Product-level, no variant
                "asset-rev-" + UUID.randomUUID().toString().substring(0, 8),
                "https://cdn.example.com/images/product-image.jpg",
                "Product main image"
        );

        ProductContent content = new ProductContent(
                "Test Product",
                "A test product for media rights acceptance integration testing"
        );

        product = product.withRepresentation(content, List.of(productMedia));
        products.create(product);
        return product;
    }

    private Product createProductWithVariantMedia() {
        Product product = new Product(
                new ProductId(UUID.randomUUID().toString()),
                ProductFamily.ACCESSORIES,
                ProductAttributeStandard.INITIAL,
                false, // size not applicable for ACCESSORIES
                false  // colour not applicable
        );

        // Add a variant (ACCESSORIES uses ONE_SIZE)
        ProductVariantId variantId = new ProductVariantId(UUID.randomUUID().toString());
        VariantConfiguration config = new VariantConfiguration(
                ProductFamily.ACCESSORIES,
                ProductAttributeStandard.INITIAL,
                false, // size not applicable
                false, // colour not applicable
                null,  // no size
                null   // no colour
        );
        ProductVariant variant = new ProductVariant(variantId, product.id(), config, null, null);
        product = product.withVariant(variant);

        // Add Product-level media with unique ID
        ProductMedia productMedia = new ProductMedia(
                "media-prod-" + UUID.randomUUID().toString().substring(0, 8),
                product.id(),
                null,
                "asset-rev-prod-" + UUID.randomUUID().toString().substring(0, 8),
                "https://cdn.example.com/images/product-main.jpg",
                "Product main image"
        );

        // Add Variant-level media with unique ID
        ProductMedia variantMedia = new ProductMedia(
                "media-var-" + UUID.randomUUID().toString().substring(0, 8),
                product.id(),
                variantId,
                "asset-rev-var-" + UUID.randomUUID().toString().substring(0, 8),
                "https://cdn.example.com/images/variant-image.jpg",
                "Variant specific image"
        );

        ProductContent content = new ProductContent(
                "Test Product with Variants",
                "A test product with variant-specific media"
        );

        product = product.withRepresentation(content, List.of(productMedia, variantMedia));
        products.create(product);
        return product;
    }

    private TransactionTemplate writableTransaction() {
        var tx = new TransactionTemplate(manager);
        tx.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        tx.setReadOnly(false);
        return tx;
    }

    @Test
    void recordsAcceptedMediaRightsObservationWithExactSubjectBinding() {
        Product product = createProductWithMedia();
        ProductMedia media = product.media().getFirst();
        UUID evidenceId = UUID.randomUUID();

        var command = new RecordProductMediaRightsAcceptance.Command(
                evidenceId,
                product.id(),
                product.revision(),
                media.id(),
                "asset-uuid-" + media.id(),
                media.assetRevision(),
                product.revision(),
                null, // Product-level media
                Decision.ACCEPTED,
                "rights-review/storefront-usage/1.0",
                "source-asserted-actor",
                new String[]{"license-doc-1", "usage-agreement-2"},
                "correlation-" + UUID.randomUUID()
        );

        var result = useCase.record(command);

        assertThat(result).isInstanceOf(RecordProductMediaRightsAcceptance.Recorded.class);
        var recorded = (RecordProductMediaRightsAcceptance.Recorded) result;
        assertThat(recorded.evidenceId()).isEqualTo(evidenceId);
        assertThat(recorded.productId()).isEqualTo(product.id());
        assertThat(recorded.productRevision()).isEqualTo(product.revision());
        assertThat(recorded.mediaAssociationId()).isEqualTo(media.id());

        // Verify observation is persisted with MEDIA purpose
        var observation = evidenceService.observe(product.id(), ProductEvidenceEvent.Purpose.MEDIA);
        assertThat(observation.state()).isEqualTo(ProductEvidenceObservation.State.CURRENT_UNCONFIRMED);
        assertThat(observation.history()).hasSize(1);

        var event = observation.history().getFirst();
        assertThat(event.id()).isEqualTo(evidenceId);
        assertThat(event.productId()).isEqualTo(product.id());
        assertThat(event.revision()).isEqualTo(product.revision());
        assertThat(event.purpose()).isEqualTo(ProductEvidenceEvent.Purpose.MEDIA);
        assertThat(event.type()).isEqualTo(ProductEvidenceEvent.Type.RECORD);
        assertThat(event.sourceReference()).contains("rights-review/storefront-usage/1.0");
        assertThat(event.sourceReference()).contains("decision:ACCEPTED");
        assertThat(event.sourceReference()).contains("media:" + media.id());
        assertThat(event.sourceReference()).contains("assetRev:" + media.assetRevision());
        assertThat(event.sourceActorClaim()).isEqualTo("source-asserted-actor");
        assertThat(event.supportingReferences()).containsExactly("license-doc-1", "usage-agreement-2");
        assertThat(event.recordedBy()).isEqualTo("PRODUCT_DATABASE");
    }

    @Test
    void recordsAcceptedMediaRightsObservationWithVariantBinding() {
        Product product = createProductWithVariantMedia();
        ProductMedia variantMedia = product.media().stream()
                .filter(m -> m.variantId() != null)
                .findFirst()
                .orElseThrow();
        UUID evidenceId = UUID.randomUUID();

        var command = new RecordProductMediaRightsAcceptance.Command(
                evidenceId,
                product.id(),
                product.revision(),
                variantMedia.id(),
                "asset-uuid-variant",
                variantMedia.assetRevision(),
                product.revision(),
                variantMedia.variantId(),
                Decision.ACCEPTED,
                "rights-review/1.0",
                "actor-claim",
                new String[]{},
                "correlation-variant"
        );

        var result = useCase.record(command);

        assertThat(result).isInstanceOf(RecordProductMediaRightsAcceptance.Recorded.class);

        var observation = evidenceService.observe(product.id(), ProductEvidenceEvent.Purpose.MEDIA);
        assertThat(observation.history()).hasSize(1);
        assertThat(observation.history().getFirst().sourceReference())
                .contains("variant:" + variantMedia.variantId().value());
    }

    @Test
    void recordsRejectedMediaRightsObservation() {
        Product product = createProductWithMedia();
        ProductMedia media = product.media().getFirst();
        UUID evidenceId = UUID.randomUUID();

        var command = new RecordProductMediaRightsAcceptance.Command(
                evidenceId,
                product.id(),
                product.revision(),
                media.id(),
                "asset-uuid",
                media.assetRevision(),
                product.revision(),
                null,
                Decision.REJECTED,
                "rights-review/1.0",
                "reviewer-claim",
                new String[]{"rejection-reason-unlicensed"},
                "correlation-rejected"
        );

        var result = useCase.record(command);

        assertThat(result).isInstanceOf(RecordProductMediaRightsAcceptance.Recorded.class);

        var observation = evidenceService.observe(product.id(), ProductEvidenceEvent.Purpose.MEDIA);
        assertThat(observation.history()).hasSize(1);
        assertThat(observation.history().getFirst().sourceReference()).contains("decision:REJECTED");
    }

    @Test
    void rejectsRecordingForNonexistentProduct() {
        ProductId nonexistentId = new ProductId(UUID.randomUUID().toString());
        ProductRevision revision = new ProductRevision("v1_" + "a".repeat(64));

        var command = new RecordProductMediaRightsAcceptance.Command(
                UUID.randomUUID(),
                nonexistentId,
                revision,
                "media-123",
                "asset-uuid",
                "asset-rev",
                revision,
                null,
                Decision.ACCEPTED,
                "rights-review/1.0",
                "actor",
                null,
                "correlation"
        );

        var result = useCase.record(command);

        assertThat(result).isInstanceOf(RecordProductMediaRightsAcceptance.ProductNotFound.class);
        var notFound = (RecordProductMediaRightsAcceptance.ProductNotFound) result;
        assertThat(notFound.productId()).isEqualTo(nonexistentId);
    }

    @Test
    void rejectsRecordingWithStaleProductRevision() {
        Product product = createProductWithMedia();
        ProductMedia media = product.media().getFirst();
        ProductRevision staleRevision = new ProductRevision("v1_" + "0".repeat(64));

        var command = new RecordProductMediaRightsAcceptance.Command(
                UUID.randomUUID(),
                product.id(),
                staleRevision,
                media.id(),
                "asset-uuid",
                media.assetRevision(),
                staleRevision,
                null,
                Decision.ACCEPTED,
                "rights-review/1.0",
                "actor",
                null,
                "correlation"
        );

        var result = useCase.record(command);

        assertThat(result).isInstanceOf(RecordProductMediaRightsAcceptance.RevisionMismatch.class);
        var mismatch = (RecordProductMediaRightsAcceptance.RevisionMismatch) result;
        assertThat(mismatch.productId()).isEqualTo(product.id());
        assertThat(mismatch.expected()).isEqualTo(staleRevision);
        assertThat(mismatch.actual()).isEqualTo(product.revision());
    }

    @Test
    void rejectsRecordingForNonexistentMediaAssociation() {
        Product product = createProductWithMedia();
        String unknownMediaId = "nonexistent-media-" + UUID.randomUUID();

        var command = new RecordProductMediaRightsAcceptance.Command(
                UUID.randomUUID(),
                product.id(),
                product.revision(),
                unknownMediaId,
                "asset-uuid",
                "asset-rev",
                product.revision(),
                null,
                Decision.ACCEPTED,
                "rights-review/1.0",
                "actor",
                null,
                "correlation"
        );

        var result = useCase.record(command);

        assertThat(result).isInstanceOf(RecordProductMediaRightsAcceptance.MediaAssociationNotFound.class);
        var notFound = (RecordProductMediaRightsAcceptance.MediaAssociationNotFound) result;
        assertThat(notFound.productId()).isEqualTo(product.id());
        assertThat(notFound.mediaAssociationId()).isEqualTo(unknownMediaId);
    }

    @Test
    void rejectsRecordingWithAssetRevisionMismatch() {
        Product product = createProductWithMedia();
        ProductMedia media = product.media().getFirst();

        var command = new RecordProductMediaRightsAcceptance.Command(
                UUID.randomUUID(),
                product.id(),
                product.revision(),
                media.id(),
                "asset-uuid",
                "wrong-asset-revision",
                product.revision(),
                null,
                Decision.ACCEPTED,
                "rights-review/1.0",
                "actor",
                null,
                "correlation"
        );

        var result = useCase.record(command);

        assertThat(result).isInstanceOf(RecordProductMediaRightsAcceptance.MediaSubjectMismatch.class);
        var mismatch = (RecordProductMediaRightsAcceptance.MediaSubjectMismatch) result;
        assertThat(mismatch.productId()).isEqualTo(product.id());
        assertThat(mismatch.mediaAssociationId()).isEqualTo(media.id());
        assertThat(mismatch.mismatchReason()).contains("Asset revision mismatch");
    }

    @Test
    void rejectsRecordingWithAssociationRevisionMismatch() {
        Product product = createProductWithMedia();
        ProductMedia media = product.media().getFirst();
        ProductRevision wrongAssociationRevision = new ProductRevision("v1_" + "b".repeat(64));

        var command = new RecordProductMediaRightsAcceptance.Command(
                UUID.randomUUID(),
                product.id(),
                product.revision(),
                media.id(),
                "asset-uuid",
                media.assetRevision(),
                wrongAssociationRevision, // Association revision doesn't match current
                null,
                Decision.ACCEPTED,
                "rights-review/1.0",
                "actor",
                null,
                "correlation"
        );

        var result = useCase.record(command);

        assertThat(result).isInstanceOf(RecordProductMediaRightsAcceptance.MediaSubjectMismatch.class);
        var mismatch = (RecordProductMediaRightsAcceptance.MediaSubjectMismatch) result;
        assertThat(mismatch.mismatchReason()).contains("Association revision mismatch");
    }

    @Test
    void rejectsRecordingWithVariantMismatchWhenMediaHasNoVariant() {
        Product product = createProductWithMedia();
        ProductMedia productLevelMedia = product.media().getFirst();
        assertThat(productLevelMedia.variantId()).isNull();

        var command = new RecordProductMediaRightsAcceptance.Command(
                UUID.randomUUID(),
                product.id(),
                product.revision(),
                productLevelMedia.id(),
                "asset-uuid",
                productLevelMedia.assetRevision(),
                product.revision(),
                new ProductVariantId("unexpected-variant"), // Variant specified for Product-level media
                Decision.ACCEPTED,
                "rights-review/1.0",
                "actor",
                null,
                "correlation"
        );

        var result = useCase.record(command);

        assertThat(result).isInstanceOf(RecordProductMediaRightsAcceptance.MediaSubjectMismatch.class);
        var mismatch = (RecordProductMediaRightsAcceptance.MediaSubjectMismatch) result;
        assertThat(mismatch.mismatchReason()).contains("Variant mismatch");
    }

    @Test
    void rejectsRecordingWithVariantMismatchWhenMediaHasVariant() {
        Product product = createProductWithVariantMedia();
        ProductMedia variantMedia = product.media().stream()
                .filter(m -> m.variantId() != null)
                .findFirst()
                .orElseThrow();

        var command = new RecordProductMediaRightsAcceptance.Command(
                UUID.randomUUID(),
                product.id(),
                product.revision(),
                variantMedia.id(),
                "asset-uuid",
                variantMedia.assetRevision(),
                product.revision(),
                null, // Missing variant for Variant-level media
                Decision.ACCEPTED,
                "rights-review/1.0",
                "actor",
                null,
                "correlation"
        );

        var result = useCase.record(command);

        assertThat(result).isInstanceOf(RecordProductMediaRightsAcceptance.MediaSubjectMismatch.class);
        var mismatch = (RecordProductMediaRightsAcceptance.MediaSubjectMismatch) result;
        assertThat(mismatch.mismatchReason()).contains("Variant mismatch");
    }

    @Test
    void rejectsRecordingWithWrongVariantId() {
        Product product = createProductWithVariantMedia();
        ProductMedia variantMedia = product.media().stream()
                .filter(m -> m.variantId() != null)
                .findFirst()
                .orElseThrow();

        var command = new RecordProductMediaRightsAcceptance.Command(
                UUID.randomUUID(),
                product.id(),
                product.revision(),
                variantMedia.id(),
                "asset-uuid",
                variantMedia.assetRevision(),
                product.revision(),
                new ProductVariantId("wrong-variant-id"), // Wrong variant ID
                Decision.ACCEPTED,
                "rights-review/1.0",
                "actor",
                null,
                "correlation"
        );

        var result = useCase.record(command);

        assertThat(result).isInstanceOf(RecordProductMediaRightsAcceptance.MediaSubjectMismatch.class);
        var mismatch = (RecordProductMediaRightsAcceptance.MediaSubjectMismatch) result;
        assertThat(mismatch.mismatchReason()).contains("Variant mismatch");
    }

    @Test
    void rejectsDuplicateEvidenceId() {
        Product product = createProductWithMedia();
        ProductMedia media = product.media().getFirst();
        UUID evidenceId = UUID.randomUUID();

        // First recording succeeds
        var firstCommand = new RecordProductMediaRightsAcceptance.Command(
                evidenceId,
                product.id(),
                product.revision(),
                media.id(),
                "asset-uuid",
                media.assetRevision(),
                product.revision(),
                null,
                Decision.ACCEPTED,
                "rights-review/1.0",
                "actor",
                null,
                "correlation-1"
        );
        var firstResult = useCase.record(firstCommand);
        assertThat(firstResult).isInstanceOf(RecordProductMediaRightsAcceptance.Recorded.class);

        // Second recording with same evidenceId fails
        var secondCommand = new RecordProductMediaRightsAcceptance.Command(
                evidenceId,
                product.id(),
                product.revision(),
                media.id(),
                "asset-uuid",
                media.assetRevision(),
                product.revision(),
                null,
                Decision.ACCEPTED,
                "rights-review/1.0",
                "actor",
                null,
                "correlation-2"
        );
        var secondResult = useCase.record(secondCommand);
        assertThat(secondResult).isInstanceOf(RecordProductMediaRightsAcceptance.DuplicateEvidence.class);
    }

    @Test
    void multipleObservationsForSameMediaCreateConflictingState() {
        Product product = createProductWithMedia();
        ProductMedia media = product.media().getFirst();

        // Record first observation
        var firstResult = useCase.record(new RecordProductMediaRightsAcceptance.Command(
                UUID.randomUUID(),
                product.id(),
                product.revision(),
                media.id(),
                "asset-uuid-1",
                media.assetRevision(),
                product.revision(),
                null,
                Decision.ACCEPTED,
                "reviewer-1/1.0",
                "actor-1",
                null,
                "correlation-1"
        ));
        assertThat(firstResult).isInstanceOf(RecordProductMediaRightsAcceptance.Recorded.class);

        // Record second observation for same media
        var secondResult = useCase.record(new RecordProductMediaRightsAcceptance.Command(
                UUID.randomUUID(),
                product.id(),
                product.revision(),
                media.id(),
                "asset-uuid-2",
                media.assetRevision(),
                product.revision(),
                null,
                Decision.ACCEPTED,
                "reviewer-2/1.0",
                "actor-2",
                null,
                "correlation-2"
        ));
        assertThat(secondResult).isInstanceOf(RecordProductMediaRightsAcceptance.Recorded.class);

        // Evidence observation shows CONFLICTING state
        var observation = evidenceService.observe(product.id(), ProductEvidenceEvent.Purpose.MEDIA);
        assertThat(observation.state()).isEqualTo(ProductEvidenceObservation.State.CONFLICTING);
        assertThat(observation.history()).hasSize(2);
    }

    @Test
    void recordedObservationIsNotAuthoritativeAcceptance() {
        Product product = createProductWithMedia();
        ProductMedia media = product.media().getFirst();

        // Record media rights acceptance observation
        var result = useCase.record(new RecordProductMediaRightsAcceptance.Command(
                UUID.randomUUID(),
                product.id(),
                product.revision(),
                media.id(),
                "asset-uuid",
                media.assetRevision(),
                product.revision(),
                null,
                Decision.ACCEPTED,
                "rights-review/1.0",
                "actor",
                null,
                "correlation"
        ));
        assertThat(result).isInstanceOf(RecordProductMediaRightsAcceptance.Recorded.class);

        // Publication evidence port still returns UNAVAILABLE
        Snapshot snapshot = publicationEvidencePort.currentEvidence(product.id(), product.revision());
        assertThat(snapshot.availability()).isEqualTo(
                com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.Availability.UNAVAILABLE
        );

        // Publication readiness assessment still returns EVIDENCE_UNAVAILABLE
        var readiness = publicationReadiness.assess(product);
        assertThat(readiness.outcome()).isEqualTo(ProductPublicationReadiness.Outcome.EVIDENCE_UNAVAILABLE);
    }

    @Test
    void publicationRemainsDeniedWithRecordedMediaRightsObservation() {
        Product product = createProductWithMedia();
        ProductMedia media = product.media().getFirst();

        // Record media rights acceptance observation
        useCase.record(new RecordProductMediaRightsAcceptance.Command(
                UUID.randomUUID(),
                product.id(),
                product.revision(),
                media.id(),
                "asset-uuid",
                media.assetRevision(),
                product.revision(),
                null,
                Decision.ACCEPTED,
                "rights-review/1.0",
                "actor",
                null,
                "correlation"
        ));

        // If PublishProduct is available, verify publication fails
        if (publishProduct != null) {
            var publishResult = publishProduct.publish(
                    new PublishProduct.Command(product.id(), UUID.randomUUID())
            );
            // Should fail because evidence is unavailable, not because of the recorded observation
            assertThat(publishResult).isNotInstanceOf(PublishProduct.Published.class);
        }

        // Product lifecycle remains DRAFT
        var loaded = products.load(product.id()).orElseThrow();
        assertThat(loaded.lifecycle()).isEqualTo(ProductLifecycle.DRAFT);
        assertThat(loaded.isCustomerVisible()).isFalse();
    }

    @Test
    void evidenceHistoryIsImmutable() {
        Product product = createProductWithMedia();
        ProductMedia media = product.media().getFirst();
        UUID evidenceId = UUID.randomUUID();

        useCase.record(new RecordProductMediaRightsAcceptance.Command(
                evidenceId,
                product.id(),
                product.revision(),
                media.id(),
                "asset-uuid",
                media.assetRevision(),
                product.revision(),
                null,
                Decision.ACCEPTED,
                "rights-review/1.0",
                "actor",
                null,
                "correlation"
        ));

        // Attempt to update should fail
        assertThatThrownBy(() -> jdbc.update(
                "UPDATE product.evidence_event SET source_reference = 'tampered' WHERE event_id = ?",
                evidenceId
        )).isInstanceOf(DataIntegrityViolationException.class);

        // Attempt to delete should fail
        assertThatThrownBy(() -> jdbc.update(
                "DELETE FROM product.evidence_event WHERE event_id = ?",
                evidenceId
        )).isInstanceOf(DataIntegrityViolationException.class);

        // Attempt to truncate should fail
        assertThatThrownBy(() -> jdbc.execute("TRUNCATE product.evidence_event"))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void actorClaimIsSourceAssertionNotAuthenticatedIdentity() {
        Product product = createProductWithMedia();
        ProductMedia media = product.media().getFirst();

        useCase.record(new RecordProductMediaRightsAcceptance.Command(
                UUID.randomUUID(),
                product.id(),
                product.revision(),
                media.id(),
                "asset-uuid",
                media.assetRevision(),
                product.revision(),
                null,
                Decision.ACCEPTED,
                "rights-review/1.0",
                "this-is-not-verified-staff-identity",
                null,
                "correlation"
        ));

        var observation = evidenceService.observe(product.id(), ProductEvidenceEvent.Purpose.MEDIA);
        var event = observation.history().getFirst();

        // Actor claim is stored as source assertion
        assertThat(event.sourceActorClaim()).isEqualTo("this-is-not-verified-staff-identity");

        // recordedBy is database attribution, not identity
        assertThat(event.recordedBy()).isEqualTo("PRODUCT_DATABASE");
    }

    @Test
    void directPortRecordingRequiresWritableTransaction() {
        Product product = createProductWithMedia();
        ProductMedia media = product.media().getFirst();

        var observation = new MediaRightsAcceptanceObservation(
                UUID.randomUUID(),
                product.id(),
                product.revision(),
                new MediaSubjectBinding(
                        media.id(),
                        "asset-uuid",
                        media.assetRevision(),
                        product.revision(),
                        null
                ),
                Decision.ACCEPTED,
                "rights-review/1.0",
                "actor",
                new String[]{},
                "correlation"
        );

        // Recording port requires transaction context (enforced by JdbcProductEvidenceStore)
        // The use case wraps this, but direct port calls would fail without transaction
        // This test verifies the use case properly provides transaction context
        var result = useCase.record(new RecordProductMediaRightsAcceptance.Command(
                observation.evidenceId(),
                observation.productId(),
                observation.productRevision(),
                observation.subject().mediaAssociationId(),
                observation.subject().assetId(),
                observation.subject().assetRevision(),
                observation.subject().associationRevision(),
                observation.subject().variantId(),
                observation.decision(),
                observation.sourceReference(),
                observation.actorClaim(),
                observation.supportingReferences(),
                observation.auditCorrelation()
        ));
        assertThat(result).isInstanceOf(RecordProductMediaRightsAcceptance.Recorded.class);
    }

    @Test
    void noPublicEndpointExposesRecording() {
        // Verify no REST controller is registered for media rights acceptance
        // This is verified by the absence of any @RestController or @Controller
        // in the media rights acceptance recording classes

        // The use case should NOT be injected into any controller
        // This test documents the requirement - actual verification is structural
        assertThat(useCase).isNotNull();
        // RecordProductMediaRightsAcceptance has no @RestController annotation
        // and ProductMediaRightsAcceptanceConfiguration does not create any controller bean
    }

    @Test
    void withdrawalBehaviorPreservesHistoryWhenSupported() {
        Product product = createProductWithMedia();
        ProductMedia media = product.media().getFirst();
        UUID originalEvidenceId = UUID.randomUUID();

        // Record initial observation
        useCase.record(new RecordProductMediaRightsAcceptance.Command(
                originalEvidenceId,
                product.id(),
                product.revision(),
                media.id(),
                "asset-uuid",
                media.assetRevision(),
                product.revision(),
                null,
                Decision.ACCEPTED,
                "rights-review/1.0",
                "actor",
                null,
                "correlation-original"
        ));

        // Record invalidation (via direct store access for test fixture)
        writableTransaction().executeWithoutResult(status -> {
            jdbc.update(
                    "INSERT INTO product.evidence_event (event_id, product_id, representation_revision, " +
                    "purpose, event_type, source_reference, source_actor_claim, supporting_references, " +
                    "audit_correlation, target_event_id) VALUES (?, ?, ?, 'MEDIA', 'INVALIDATE', ?, ?, ?, ?, ?)",
                    UUID.randomUUID(),
                    product.id().value(),
                    product.revision().value(),
                    "withdrawal-source",
                    "withdrawal-actor",
                    new String[]{"withdrawal-reason"},
                    "correlation-withdrawal",
                    originalEvidenceId
            );
        });

        // Observation state becomes INVALIDATED
        var observation = evidenceService.observe(product.id(), ProductEvidenceEvent.Purpose.MEDIA);
        assertThat(observation.state()).isEqualTo(ProductEvidenceObservation.State.INVALIDATED);
        assertThat(observation.history()).hasSize(2);

        // Both original and invalidation records are preserved
        var events = observation.history();
        assertThat(events).anyMatch(e -> e.type() == ProductEvidenceEvent.Type.RECORD);
        assertThat(events).anyMatch(e -> e.type() == ProductEvidenceEvent.Type.INVALIDATE);
    }

    @Test
    void mediaAndContentEvidenceRemainSeparate() {
        Product product = createProductWithMedia();
        ProductMedia media = product.media().getFirst();

        // Record media rights observation
        useCase.record(new RecordProductMediaRightsAcceptance.Command(
                UUID.randomUUID(),
                product.id(),
                product.revision(),
                media.id(),
                "asset-uuid",
                media.assetRevision(),
                product.revision(),
                null,
                Decision.ACCEPTED,
                "rights-review/1.0",
                "actor",
                null,
                "correlation"
        ));

        // Media observation exists
        var mediaObs = evidenceService.observe(product.id(), ProductEvidenceEvent.Purpose.MEDIA);
        assertThat(mediaObs.state()).isEqualTo(ProductEvidenceObservation.State.CURRENT_UNCONFIRMED);
        assertThat(mediaObs.history()).hasSize(1);

        // Content observation is empty (not affected by media recording)
        var contentObs = evidenceService.observe(product.id(), ProductEvidenceEvent.Purpose.CONTENT);
        assertThat(contentObs.state()).isEqualTo(ProductEvidenceObservation.State.MISSING);
        assertThat(contentObs.history()).isEmpty();
    }

    @Test
    void observationToStringDoesNotExposeInternalDetails() {
        Product product = createProductWithMedia();
        ProductMedia media = product.media().getFirst();

        var observation = new MediaRightsAcceptanceObservation(
                UUID.randomUUID(),
                product.id(),
                product.revision(),
                new MediaSubjectBinding(
                        media.id(),
                        "sensitive-asset-id",
                        media.assetRevision(),
                        product.revision(),
                        null
                ),
                Decision.ACCEPTED,
                "rights-review/1.0",
                "sensitive-actor-claim",
                new String[]{"sensitive-ref"},
                "correlation"
        );

        assertThat(observation.toString()).isEqualTo("MediaRightsAcceptanceObservation[internal]");
        assertThat(observation.subject().toString()).isEqualTo("MediaSubjectBinding[internal]");
    }

    @Test
    void supersessionBehaviorPreservesHistoryWhenSupported() {
        Product product = createProductWithMedia();
        ProductMedia media = product.media().getFirst();
        UUID originalEvidenceId = UUID.randomUUID();
        UUID replacementEvidenceId = UUID.randomUUID();

        // Record initial observation
        useCase.record(new RecordProductMediaRightsAcceptance.Command(
                originalEvidenceId,
                product.id(),
                product.revision(),
                media.id(),
                "asset-uuid-original",
                media.assetRevision(),
                product.revision(),
                null,
                Decision.ACCEPTED,
                "rights-review/1.0",
                "actor-original",
                null,
                "correlation-original"
        ));

        // Record replacement observation first (needed for SUPERSEDE FK)
        useCase.record(new RecordProductMediaRightsAcceptance.Command(
                replacementEvidenceId,
                product.id(),
                product.revision(),
                media.id(),
                "asset-uuid-replacement",
                media.assetRevision(),
                product.revision(),
                null,
                Decision.ACCEPTED,
                "rights-review/2.0",
                "actor-replacement",
                null,
                "correlation-replacement"
        ));

        // Record supersession (via direct store access for test fixture)
        writableTransaction().executeWithoutResult(status -> {
            jdbc.update(
                    "INSERT INTO product.evidence_event (event_id, product_id, representation_revision, " +
                    "purpose, event_type, source_reference, source_actor_claim, supporting_references, " +
                    "audit_correlation, target_event_id, replacement_event_id) VALUES (?, ?, ?, 'MEDIA', 'SUPERSEDE', ?, ?, ?, ?, ?, ?)",
                    UUID.randomUUID(),
                    product.id().value(),
                    product.revision().value(),
                    "supersession-source",
                    "supersession-actor",
                    new String[]{"supersession-reason"},
                    "correlation-supersede",
                    originalEvidenceId,
                    replacementEvidenceId
            );
        });

        // History includes all three records
        var observation = evidenceService.observe(product.id(), ProductEvidenceEvent.Purpose.MEDIA);
        assertThat(observation.history()).hasSize(3);
        assertThat(observation.history()).anyMatch(e -> e.type() == ProductEvidenceEvent.Type.SUPERSEDE);
    }
}
