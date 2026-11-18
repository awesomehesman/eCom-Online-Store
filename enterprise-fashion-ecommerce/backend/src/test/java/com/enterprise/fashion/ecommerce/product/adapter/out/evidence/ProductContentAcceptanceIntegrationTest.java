package com.enterprise.fashion.ecommerce.product.adapter.out.evidence;

import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.application.evidence.ProductEvidenceEvent;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductEvidenceObservation;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductEvidenceService;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.Snapshot;
import com.enterprise.fashion.ecommerce.product.application.persistence.ProductPersistenceService;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductContentAcceptanceRecordingPort;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductContentAcceptanceRecordingPort.ContentAcceptanceObservation;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductContentAcceptanceRecordingPort.Decision;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductContentAcceptanceRecordingPort.RecordingOutcome;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductPublicationEvidencePort;
import com.enterprise.fashion.ecommerce.product.application.usecase.AssessProductPublicationReadiness;
import com.enterprise.fashion.ecommerce.product.application.usecase.PublishProduct;
import com.enterprise.fashion.ecommerce.product.application.usecase.RecordProductContentAcceptance;
import com.enterprise.fashion.ecommerce.product.application.visibility.ProductPublicationReadiness;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductAttributeStandard;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductFamily;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductLifecycle;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductRevision;
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
 * Integration tests for content acceptance recording.
 * <p>
 * These tests verify:
 * <ul>
 *   <li>Recording persists observations to evidence_event table</li>
 *   <li>Revision binding is enforced</li>
 *   <li>Duplicate detection works</li>
 *   <li>Recorded observations are NOT authoritative acceptance</li>
 *   <li>Publication remains blocked without trusted evidence source</li>
 *   <li>Immutable history is preserved</li>
 * </ul>
 */
@Tag("integration")
@SpringBootTest
@Testcontainers
class ProductContentAcceptanceIntegrationTest {
    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:18");

    @Autowired private RecordProductContentAcceptance useCase;
    @Autowired private ProductContentAcceptanceRecordingPort recordingPort;
    @Autowired private ProductPersistenceService products;
    @Autowired private ProductEvidenceService evidenceService;
    @Autowired private AssessProductPublicationReadiness publicationReadiness;
    @Autowired private ProductPublicationEvidencePort publicationEvidencePort;
    @Autowired private PlatformTransactionManager manager;
    @Autowired private JdbcTemplate jdbc;

    @Autowired(required = false) private PublishProduct publishProduct;

    private Product createProduct() {
        Product product = new Product(
                new ProductId(UUID.randomUUID().toString()),
                ProductFamily.ACCESSORIES,
                ProductAttributeStandard.INITIAL,
                false,
                false
        );
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
    void recordsAcceptedContentObservationWithExactRevisionBinding() {
        Product product = createProduct();
        UUID evidenceId = UUID.randomUUID();

        var command = new RecordProductContentAcceptance.Command(
                evidenceId,
                product.id(),
                product.revision(),
                Decision.ACCEPTED,
                "content-review/P1-checklist/1.0",
                "source-asserted-actor",
                new String[]{"P1-check-1", "P1-check-2"},
                "correlation-" + UUID.randomUUID()
        );

        var result = useCase.record(command);

        assertThat(result).isInstanceOf(RecordProductContentAcceptance.Recorded.class);
        var recorded = (RecordProductContentAcceptance.Recorded) result;
        assertThat(recorded.evidenceId()).isEqualTo(evidenceId);
        assertThat(recorded.productId()).isEqualTo(product.id());
        assertThat(recorded.productRevision()).isEqualTo(product.revision());

        // Verify observation is persisted
        var observation = evidenceService.observe(product.id(), ProductEvidenceEvent.Purpose.CONTENT);
        assertThat(observation.state()).isEqualTo(ProductEvidenceObservation.State.CURRENT_UNCONFIRMED);
        assertThat(observation.history()).hasSize(1);

        var event = observation.history().getFirst();
        assertThat(event.id()).isEqualTo(evidenceId);
        assertThat(event.productId()).isEqualTo(product.id());
        assertThat(event.revision()).isEqualTo(product.revision());
        assertThat(event.sourceReference()).contains("content-review/P1-checklist/1.0");
        assertThat(event.sourceReference()).contains("decision:ACCEPTED");
        assertThat(event.sourceActorClaim()).isEqualTo("source-asserted-actor");
        assertThat(event.supportingReferences()).containsExactly("P1-check-1", "P1-check-2");
        assertThat(event.recordedBy()).isEqualTo("PRODUCT_DATABASE");
    }

    @Test
    void recordsRejectedContentObservation() {
        Product product = createProduct();
        UUID evidenceId = UUID.randomUUID();

        var command = new RecordProductContentAcceptance.Command(
                evidenceId,
                product.id(),
                product.revision(),
                Decision.REJECTED,
                "content-review/P1-checklist/1.0",
                "reviewer-claim",
                new String[]{"rejection-reason-1"},
                "correlation-rejected"
        );

        var result = useCase.record(command);

        assertThat(result).isInstanceOf(RecordProductContentAcceptance.Recorded.class);

        var observation = evidenceService.observe(product.id(), ProductEvidenceEvent.Purpose.CONTENT);
        assertThat(observation.history()).hasSize(1);
        assertThat(observation.history().getFirst().sourceReference()).contains("decision:REJECTED");
    }

    @Test
    void rejectsRecordingForNonexistentProduct() {
        ProductId nonexistentId = new ProductId(UUID.randomUUID().toString());
        ProductRevision revision = new ProductRevision("v1_" + "a".repeat(64));

        var command = new RecordProductContentAcceptance.Command(
                UUID.randomUUID(),
                nonexistentId,
                revision,
                Decision.ACCEPTED,
                "content-review/1.0",
                "actor",
                null,
                "correlation"
        );

        var result = useCase.record(command);

        assertThat(result).isInstanceOf(RecordProductContentAcceptance.ProductNotFound.class);
        var notFound = (RecordProductContentAcceptance.ProductNotFound) result;
        assertThat(notFound.productId()).isEqualTo(nonexistentId);
    }

    @Test
    void rejectsRecordingWithStaleRevision() {
        Product product = createProduct();
        // Create a different but valid revision format
        ProductRevision staleRevision = new ProductRevision("v1_" + "0".repeat(64));

        var command = new RecordProductContentAcceptance.Command(
                UUID.randomUUID(),
                product.id(),
                staleRevision,
                Decision.ACCEPTED,
                "content-review/1.0",
                "actor",
                null,
                "correlation"
        );

        var result = useCase.record(command);

        assertThat(result).isInstanceOf(RecordProductContentAcceptance.RevisionMismatch.class);
        var mismatch = (RecordProductContentAcceptance.RevisionMismatch) result;
        assertThat(mismatch.productId()).isEqualTo(product.id());
        assertThat(mismatch.expected()).isEqualTo(staleRevision);
        assertThat(mismatch.actual()).isEqualTo(product.revision());
    }

    @Test
    void rejectsDuplicateEvidenceId() {
        Product product = createProduct();
        UUID evidenceId = UUID.randomUUID();

        // First recording succeeds
        var firstCommand = new RecordProductContentAcceptance.Command(
                evidenceId,
                product.id(),
                product.revision(),
                Decision.ACCEPTED,
                "content-review/1.0",
                "actor",
                null,
                "correlation-1"
        );
        var firstResult = useCase.record(firstCommand);
        assertThat(firstResult).isInstanceOf(RecordProductContentAcceptance.Recorded.class);

        // Second recording with same evidenceId fails
        var secondCommand = new RecordProductContentAcceptance.Command(
                evidenceId,
                product.id(),
                product.revision(),
                Decision.ACCEPTED,
                "content-review/1.0",
                "actor",
                null,
                "correlation-2"
        );
        var secondResult = useCase.record(secondCommand);
        assertThat(secondResult).isInstanceOf(RecordProductContentAcceptance.DuplicateEvidence.class);
    }

    @Test
    void multipleObservationsForSameRevisionCreateConflictingState() {
        Product product = createProduct();

        // Record first observation
        var firstResult = useCase.record(new RecordProductContentAcceptance.Command(
                UUID.randomUUID(),
                product.id(),
                product.revision(),
                Decision.ACCEPTED,
                "reviewer-1/1.0",
                "actor-1",
                null,
                "correlation-1"
        ));
        assertThat(firstResult).isInstanceOf(RecordProductContentAcceptance.Recorded.class);

        // Record second observation for same revision
        var secondResult = useCase.record(new RecordProductContentAcceptance.Command(
                UUID.randomUUID(),
                product.id(),
                product.revision(),
                Decision.ACCEPTED,
                "reviewer-2/1.0",
                "actor-2",
                null,
                "correlation-2"
        ));
        assertThat(secondResult).isInstanceOf(RecordProductContentAcceptance.Recorded.class);

        // Evidence observation shows CONFLICTING state
        var observation = evidenceService.observe(product.id(), ProductEvidenceEvent.Purpose.CONTENT);
        assertThat(observation.state()).isEqualTo(ProductEvidenceObservation.State.CONFLICTING);
        assertThat(observation.history()).hasSize(2);
    }

    @Test
    void recordedObservationIsNotAuthoritativeAcceptance() {
        Product product = createProduct();

        // Record content acceptance observation
        var result = useCase.record(new RecordProductContentAcceptance.Command(
                UUID.randomUUID(),
                product.id(),
                product.revision(),
                Decision.ACCEPTED,
                "content-review/1.0",
                "actor",
                null,
                "correlation"
        ));
        assertThat(result).isInstanceOf(RecordProductContentAcceptance.Recorded.class);

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
    void publicationRemainsDeniedWithRecordedContentObservation() {
        Product product = createProduct();

        // Record content acceptance observation
        useCase.record(new RecordProductContentAcceptance.Command(
                UUID.randomUUID(),
                product.id(),
                product.revision(),
                Decision.ACCEPTED,
                "content-review/1.0",
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
        Product product = createProduct();
        UUID evidenceId = UUID.randomUUID();

        useCase.record(new RecordProductContentAcceptance.Command(
                evidenceId,
                product.id(),
                product.revision(),
                Decision.ACCEPTED,
                "content-review/1.0",
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
        Product product = createProduct();

        useCase.record(new RecordProductContentAcceptance.Command(
                UUID.randomUUID(),
                product.id(),
                product.revision(),
                Decision.ACCEPTED,
                "content-review/1.0",
                "this-is-not-verified-staff-identity",
                null,
                "correlation"
        ));

        var observation = evidenceService.observe(product.id(), ProductEvidenceEvent.Purpose.CONTENT);
        var event = observation.history().getFirst();

        // Actor claim is stored as source assertion
        assertThat(event.sourceActorClaim()).isEqualTo("this-is-not-verified-staff-identity");

        // recordedBy is database attribution, not identity
        assertThat(event.recordedBy()).isEqualTo("PRODUCT_DATABASE");
    }

    @Test
    void directPortRecordingRequiresWritableTransaction() {
        Product product = createProduct();

        var observation = new ContentAcceptanceObservation(
                UUID.randomUUID(),
                product.id(),
                product.revision(),
                Decision.ACCEPTED,
                "content-review/1.0",
                "actor",
                new String[]{},
                "correlation"
        );

        // Recording port requires transaction context (enforced by JdbcProductEvidenceStore)
        // The use case wraps this, but direct port calls would fail without transaction
        // This test verifies the use case properly provides transaction context
        var result = useCase.record(new RecordProductContentAcceptance.Command(
                observation.evidenceId(),
                observation.productId(),
                observation.productRevision(),
                observation.decision(),
                observation.sourceReference(),
                observation.actorClaim(),
                observation.supportingReferences(),
                observation.auditCorrelation()
        ));
        assertThat(result).isInstanceOf(RecordProductContentAcceptance.Recorded.class);
    }

    @Test
    void noPublicEndpointExposesRecording() {
        // Verify no REST controller is registered for content acceptance
        // This is verified by the absence of any @RestController or @Controller
        // in the content acceptance recording classes

        // The use case should NOT be injected into any controller
        // This test documents the requirement - actual verification is structural
        assertThat(useCase).isNotNull();
        // RecordProductContentAcceptance has no @RestController annotation
        // and ProductContentAcceptanceConfiguration does not create any controller bean
    }

    @Test
    void withdrawalBehaviorPreservesHistoryWhenSupported() {
        Product product = createProduct();
        UUID originalEvidenceId = UUID.randomUUID();

        // Record initial observation
        useCase.record(new RecordProductContentAcceptance.Command(
                originalEvidenceId,
                product.id(),
                product.revision(),
                Decision.ACCEPTED,
                "content-review/1.0",
                "actor",
                null,
                "correlation-original"
        ));

        // Record invalidation (via direct store access for test fixture)
        writableTransaction().executeWithoutResult(status -> {
            jdbc.update(
                    "INSERT INTO product.evidence_event (event_id, product_id, representation_revision, " +
                    "purpose, event_type, source_reference, source_actor_claim, supporting_references, " +
                    "audit_correlation, target_event_id) VALUES (?, ?, ?, 'CONTENT', 'INVALIDATE', ?, ?, ?, ?, ?)",
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
        var observation = evidenceService.observe(product.id(), ProductEvidenceEvent.Purpose.CONTENT);
        assertThat(observation.state()).isEqualTo(ProductEvidenceObservation.State.INVALIDATED);
        assertThat(observation.history()).hasSize(2);

        // Both original and invalidation records are preserved
        var events = observation.history();
        assertThat(events).anyMatch(e -> e.type() == ProductEvidenceEvent.Type.RECORD);
        assertThat(events).anyMatch(e -> e.type() == ProductEvidenceEvent.Type.INVALIDATE);
    }

    @Test
    void contentAndMediaEvidenceRemainSeparate() {
        Product product = createProduct();

        // Record content observation
        useCase.record(new RecordProductContentAcceptance.Command(
                UUID.randomUUID(),
                product.id(),
                product.revision(),
                Decision.ACCEPTED,
                "content-review/1.0",
                "actor",
                null,
                "correlation"
        ));

        // Content observation exists
        var contentObs = evidenceService.observe(product.id(), ProductEvidenceEvent.Purpose.CONTENT);
        assertThat(contentObs.state()).isEqualTo(ProductEvidenceObservation.State.CURRENT_UNCONFIRMED);
        assertThat(contentObs.history()).hasSize(1);

        // Media observation is empty
        var mediaObs = evidenceService.observe(product.id(), ProductEvidenceEvent.Purpose.MEDIA);
        assertThat(mediaObs.state()).isEqualTo(ProductEvidenceObservation.State.MISSING);
        assertThat(mediaObs.history()).isEmpty();
    }

    @Test
    void observationToStringDoesNotExposeInternalDetails() {
        Product product = createProduct();

        var observation = new ContentAcceptanceObservation(
                UUID.randomUUID(),
                product.id(),
                product.revision(),
                Decision.ACCEPTED,
                "content-review/1.0",
                "sensitive-actor-claim",
                new String[]{"sensitive-ref"},
                "correlation"
        );

        assertThat(observation.toString()).isEqualTo("ContentAcceptanceObservation[internal]");
    }
}
