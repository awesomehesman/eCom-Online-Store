package com.enterprise.fashion.ecommerce.product.application.usecase;

import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.application.port.out.ProductMediaRightsAcceptanceRecordingPort;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductMediaRightsAcceptanceRecordingPort.Decision;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductMediaRightsAcceptanceRecordingPort.MediaRightsAcceptanceObservation;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductMediaRightsAcceptanceRecordingPort.RecordingOutcome;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductRevision;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariantId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for RecordProductMediaRightsAcceptance use case.
 * <p>
 * These tests verify the use case behavior with a stubbed recording port.
 * They do NOT test database persistence or transaction management.
 */
class RecordProductMediaRightsAcceptanceTest {
    private StubRecordingPort recordingPort;
    private RecordProductMediaRightsAcceptance useCase;

    // Valid revision format: v1_ followed by 64 hex characters
    private static ProductRevision revision(String hexSuffix) {
        String hex = (hexSuffix + "0".repeat(64)).substring(0, 64);
        return new ProductRevision("v1_" + hex);
    }

    @BeforeEach
    void setUp() {
        recordingPort = new StubRecordingPort();
        useCase = new RecordProductMediaRightsAcceptance(recordingPort, new DirectTransactionOperations());
    }

    /** Test-only transaction wrapper that executes callbacks directly without actual transaction. */
    private static class DirectTransactionOperations implements org.springframework.transaction.support.TransactionOperations {
        @Override
        public <T> T execute(org.springframework.transaction.support.TransactionCallback<T> action)
                throws org.springframework.transaction.TransactionException {
            return action.doInTransaction(null);
        }
    }

    @Test
    void recordsAcceptedObservationSuccessfully() {
        ProductId productId = new ProductId(UUID.randomUUID().toString());
        ProductRevision revision = revision("a");
        UUID evidenceId = UUID.randomUUID();
        String mediaAssociationId = "media-123";

        recordingPort.nextOutcome = new RecordingOutcome.Recorded(evidenceId, productId, revision, mediaAssociationId);

        var command = new RecordProductMediaRightsAcceptance.Command(
                evidenceId,
                productId,
                revision,
                mediaAssociationId,
                "asset-uuid-456",
                "asset-rev-789",
                revision,
                null, // Product-level media
                Decision.ACCEPTED,
                "rights-review/1.0",
                "test-actor-claim",
                new String[]{"license-ref-1"},
                "correlation-123"
        );

        var result = useCase.record(command);

        assertThat(result).isInstanceOf(RecordProductMediaRightsAcceptance.Recorded.class);
        var recorded = (RecordProductMediaRightsAcceptance.Recorded) result;
        assertThat(recorded.evidenceId()).isEqualTo(evidenceId);
        assertThat(recorded.productId()).isEqualTo(productId);
        assertThat(recorded.productRevision()).isEqualTo(revision);
        assertThat(recorded.mediaAssociationId()).isEqualTo(mediaAssociationId);

        // Verify observation was passed correctly to port
        assertThat(recordingPort.lastObservation).isNotNull();
        assertThat(recordingPort.lastObservation.evidenceId()).isEqualTo(evidenceId);
        assertThat(recordingPort.lastObservation.productId()).isEqualTo(productId);
        assertThat(recordingPort.lastObservation.productRevision()).isEqualTo(revision);
        assertThat(recordingPort.lastObservation.subject().mediaAssociationId()).isEqualTo(mediaAssociationId);
        assertThat(recordingPort.lastObservation.subject().assetId()).isEqualTo("asset-uuid-456");
        assertThat(recordingPort.lastObservation.subject().assetRevision()).isEqualTo("asset-rev-789");
        assertThat(recordingPort.lastObservation.subject().variantId()).isNull();
        assertThat(recordingPort.lastObservation.decision()).isEqualTo(Decision.ACCEPTED);
    }

    @Test
    void recordsAcceptedObservationWithVariantBinding() {
        ProductId productId = new ProductId(UUID.randomUUID().toString());
        ProductRevision revision = revision("b");
        UUID evidenceId = UUID.randomUUID();
        String mediaAssociationId = "variant-media-456";
        ProductVariantId variantId = new ProductVariantId(UUID.randomUUID().toString());

        recordingPort.nextOutcome = new RecordingOutcome.Recorded(evidenceId, productId, revision, mediaAssociationId);

        var command = new RecordProductMediaRightsAcceptance.Command(
                evidenceId,
                productId,
                revision,
                mediaAssociationId,
                "variant-asset-uuid",
                "variant-asset-rev",
                revision,
                variantId,
                Decision.ACCEPTED,
                "rights-review/1.0",
                "test-actor-claim",
                new String[]{},
                "correlation-456"
        );

        var result = useCase.record(command);

        assertThat(result).isInstanceOf(RecordProductMediaRightsAcceptance.Recorded.class);
        assertThat(recordingPort.lastObservation.subject().variantId()).isEqualTo(variantId);
    }

    @Test
    void recordsRejectedObservationSuccessfully() {
        ProductId productId = new ProductId(UUID.randomUUID().toString());
        ProductRevision revision = revision("c");
        UUID evidenceId = UUID.randomUUID();
        String mediaAssociationId = "media-rejected";

        recordingPort.nextOutcome = new RecordingOutcome.Recorded(evidenceId, productId, revision, mediaAssociationId);

        var command = new RecordProductMediaRightsAcceptance.Command(
                evidenceId,
                productId,
                revision,
                mediaAssociationId,
                "asset-uuid",
                "asset-rev",
                revision,
                null,
                Decision.REJECTED,
                "rights-review/1.0",
                "test-actor-claim",
                new String[]{"rejection-reason"},
                "correlation-rejected"
        );

        var result = useCase.record(command);

        assertThat(result).isInstanceOf(RecordProductMediaRightsAcceptance.Recorded.class);
        assertThat(recordingPort.lastObservation.decision()).isEqualTo(Decision.REJECTED);
    }

    @Test
    void returnsProductNotFoundWhenProductDoesNotExist() {
        ProductId productId = new ProductId(UUID.randomUUID().toString());
        ProductRevision revision = revision("d");
        UUID evidenceId = UUID.randomUUID();

        recordingPort.nextOutcome = new RecordingOutcome.ProductNotFound(productId);

        var command = new RecordProductMediaRightsAcceptance.Command(
                evidenceId,
                productId,
                revision,
                "media-123",
                "asset-uuid",
                "asset-rev",
                revision,
                null,
                Decision.ACCEPTED,
                "rights-review/1.0",
                "test-actor-claim",
                null,
                "correlation-789"
        );

        var result = useCase.record(command);

        assertThat(result).isInstanceOf(RecordProductMediaRightsAcceptance.ProductNotFound.class);
        var notFound = (RecordProductMediaRightsAcceptance.ProductNotFound) result;
        assertThat(notFound.productId()).isEqualTo(productId);
    }

    @Test
    void returnsRevisionMismatchWhenRevisionIsStale() {
        ProductId productId = new ProductId(UUID.randomUUID().toString());
        ProductRevision expectedRevision = revision("e");
        ProductRevision actualRevision = revision("f");
        UUID evidenceId = UUID.randomUUID();

        recordingPort.nextOutcome = new RecordingOutcome.RevisionMismatch(productId, expectedRevision, actualRevision);

        var command = new RecordProductMediaRightsAcceptance.Command(
                evidenceId,
                productId,
                expectedRevision,
                "media-123",
                "asset-uuid",
                "asset-rev",
                expectedRevision,
                null,
                Decision.ACCEPTED,
                "rights-review/1.0",
                "test-actor-claim",
                new String[]{"ref"},
                "correlation-abc"
        );

        var result = useCase.record(command);

        assertThat(result).isInstanceOf(RecordProductMediaRightsAcceptance.RevisionMismatch.class);
        var mismatch = (RecordProductMediaRightsAcceptance.RevisionMismatch) result;
        assertThat(mismatch.productId()).isEqualTo(productId);
        assertThat(mismatch.expected()).isEqualTo(expectedRevision);
        assertThat(mismatch.actual()).isEqualTo(actualRevision);
    }

    @Test
    void returnsMediaAssociationNotFoundWhenMediaDoesNotExist() {
        ProductId productId = new ProductId(UUID.randomUUID().toString());
        ProductRevision revision = revision("0");
        UUID evidenceId = UUID.randomUUID();
        String unknownMediaId = "nonexistent-media";

        recordingPort.nextOutcome = new RecordingOutcome.MediaAssociationNotFound(productId, unknownMediaId);

        var command = new RecordProductMediaRightsAcceptance.Command(
                evidenceId,
                productId,
                revision,
                unknownMediaId,
                "asset-uuid",
                "asset-rev",
                revision,
                null,
                Decision.ACCEPTED,
                "rights-review/1.0",
                "test-actor-claim",
                null,
                "correlation-notfound"
        );

        var result = useCase.record(command);

        assertThat(result).isInstanceOf(RecordProductMediaRightsAcceptance.MediaAssociationNotFound.class);
        var notFound = (RecordProductMediaRightsAcceptance.MediaAssociationNotFound) result;
        assertThat(notFound.productId()).isEqualTo(productId);
        assertThat(notFound.mediaAssociationId()).isEqualTo(unknownMediaId);
    }

    @Test
    void returnsMediaSubjectMismatchWhenAssetRevisionDiffers() {
        ProductId productId = new ProductId(UUID.randomUUID().toString());
        ProductRevision revision = revision("1");
        UUID evidenceId = UUID.randomUUID();
        String mediaAssociationId = "media-123";

        recordingPort.nextOutcome = new RecordingOutcome.MediaSubjectMismatch(
                productId, mediaAssociationId, "Asset revision mismatch");

        var command = new RecordProductMediaRightsAcceptance.Command(
                evidenceId,
                productId,
                revision,
                mediaAssociationId,
                "asset-uuid",
                "wrong-asset-rev",
                revision,
                null,
                Decision.ACCEPTED,
                "rights-review/1.0",
                "test-actor-claim",
                null,
                "correlation-mismatch"
        );

        var result = useCase.record(command);

        assertThat(result).isInstanceOf(RecordProductMediaRightsAcceptance.MediaSubjectMismatch.class);
        var mismatch = (RecordProductMediaRightsAcceptance.MediaSubjectMismatch) result;
        assertThat(mismatch.productId()).isEqualTo(productId);
        assertThat(mismatch.mediaAssociationId()).isEqualTo(mediaAssociationId);
        assertThat(mismatch.mismatchReason()).contains("Asset revision mismatch");
    }

    @Test
    void returnsMediaSubjectMismatchWhenVariantDiffers() {
        ProductId productId = new ProductId(UUID.randomUUID().toString());
        ProductRevision revision = revision("2");
        UUID evidenceId = UUID.randomUUID();
        String mediaAssociationId = "media-123";

        recordingPort.nextOutcome = new RecordingOutcome.MediaSubjectMismatch(
                productId, mediaAssociationId, "Variant mismatch");

        var command = new RecordProductMediaRightsAcceptance.Command(
                evidenceId,
                productId,
                revision,
                mediaAssociationId,
                "asset-uuid",
                "asset-rev",
                revision,
                new ProductVariantId("wrong-variant"),
                Decision.ACCEPTED,
                "rights-review/1.0",
                "test-actor-claim",
                null,
                "correlation-variant"
        );

        var result = useCase.record(command);

        assertThat(result).isInstanceOf(RecordProductMediaRightsAcceptance.MediaSubjectMismatch.class);
        var mismatch = (RecordProductMediaRightsAcceptance.MediaSubjectMismatch) result;
        assertThat(mismatch.mismatchReason()).contains("Variant mismatch");
    }

    @Test
    void returnsDuplicateEvidenceWhenEvidenceAlreadyExists() {
        ProductId productId = new ProductId(UUID.randomUUID().toString());
        ProductRevision revision = revision("3");
        UUID evidenceId = UUID.randomUUID();

        recordingPort.nextOutcome = new RecordingOutcome.DuplicateEvidence(evidenceId);

        var command = new RecordProductMediaRightsAcceptance.Command(
                evidenceId,
                productId,
                revision,
                "media-123",
                "asset-uuid",
                "asset-rev",
                revision,
                null,
                Decision.ACCEPTED,
                "rights-review/1.0",
                "test-actor-claim",
                new String[]{},
                "correlation-def"
        );

        var result = useCase.record(command);

        assertThat(result).isInstanceOf(RecordProductMediaRightsAcceptance.DuplicateEvidence.class);
        var duplicate = (RecordProductMediaRightsAcceptance.DuplicateEvidence) result;
        assertThat(duplicate.evidenceId()).isEqualTo(evidenceId);
    }

    @Test
    void returnsValidationFailedWhenMetadataIsInvalid() {
        ProductId productId = new ProductId(UUID.randomUUID().toString());
        ProductRevision revision = revision("4");
        UUID evidenceId = UUID.randomUUID();

        recordingPort.nextOutcome = new RecordingOutcome.InvalidMetadata("Invalid source reference format");

        var command = new RecordProductMediaRightsAcceptance.Command(
                evidenceId,
                productId,
                revision,
                "media-123",
                "asset-uuid",
                "asset-rev",
                revision,
                null,
                Decision.ACCEPTED,
                "rights-review/1.0",
                "test-actor-claim",
                new String[]{},
                "correlation-012"
        );

        var result = useCase.record(command);

        assertThat(result).isInstanceOf(RecordProductMediaRightsAcceptance.ValidationFailed.class);
        var failed = (RecordProductMediaRightsAcceptance.ValidationFailed) result;
        assertThat(failed.reasons()).contains("Invalid source reference format");
    }

    @Test
    void commandRejectsNullEvidenceId() {
        ProductId productId = new ProductId(UUID.randomUUID().toString());
        ProductRevision revision = revision("5");

        assertThatThrownBy(() -> new RecordProductMediaRightsAcceptance.Command(
                null, productId, revision, "media-123", "asset", "rev", revision, null,
                Decision.ACCEPTED, "source", "actor", null, "correlation"
        )).isInstanceOf(NullPointerException.class).hasMessageContaining("Evidence identity");
    }

    @Test
    void commandRejectsNullProductId() {
        ProductRevision revision = revision("6");

        assertThatThrownBy(() -> new RecordProductMediaRightsAcceptance.Command(
                UUID.randomUUID(), null, revision, "media-123", "asset", "rev", revision, null,
                Decision.ACCEPTED, "source", "actor", null, "correlation"
        )).isInstanceOf(NullPointerException.class).hasMessageContaining("Product identity");
    }

    @Test
    void commandRejectsNullRevision() {
        ProductId productId = new ProductId(UUID.randomUUID().toString());
        ProductRevision revision = revision("7");

        assertThatThrownBy(() -> new RecordProductMediaRightsAcceptance.Command(
                UUID.randomUUID(), productId, null, "media-123", "asset", "rev", revision, null,
                Decision.ACCEPTED, "source", "actor", null, "correlation"
        )).isInstanceOf(NullPointerException.class).hasMessageContaining("Expected Product revision");
    }

    @Test
    void commandRejectsNullMediaAssociationId() {
        ProductId productId = new ProductId(UUID.randomUUID().toString());
        ProductRevision revision = revision("8");

        assertThatThrownBy(() -> new RecordProductMediaRightsAcceptance.Command(
                UUID.randomUUID(), productId, revision, null, "asset", "rev", revision, null,
                Decision.ACCEPTED, "source", "actor", null, "correlation"
        )).isInstanceOf(NullPointerException.class).hasMessageContaining("Media association identity");
    }

    @Test
    void commandRejectsNullAssetId() {
        ProductId productId = new ProductId(UUID.randomUUID().toString());
        ProductRevision revision = revision("9");

        assertThatThrownBy(() -> new RecordProductMediaRightsAcceptance.Command(
                UUID.randomUUID(), productId, revision, "media-123", null, "rev", revision, null,
                Decision.ACCEPTED, "source", "actor", null, "correlation"
        )).isInstanceOf(NullPointerException.class).hasMessageContaining("Asset identity");
    }

    @Test
    void commandRejectsNullAssetRevision() {
        ProductId productId = new ProductId(UUID.randomUUID().toString());
        ProductRevision revision = revision("a1");

        assertThatThrownBy(() -> new RecordProductMediaRightsAcceptance.Command(
                UUID.randomUUID(), productId, revision, "media-123", "asset", null, revision, null,
                Decision.ACCEPTED, "source", "actor", null, "correlation"
        )).isInstanceOf(NullPointerException.class).hasMessageContaining("Asset revision");
    }

    @Test
    void commandRejectsNullAssociationRevision() {
        ProductId productId = new ProductId(UUID.randomUUID().toString());
        ProductRevision revision = revision("a2");

        assertThatThrownBy(() -> new RecordProductMediaRightsAcceptance.Command(
                UUID.randomUUID(), productId, revision, "media-123", "asset", "rev", null, null,
                Decision.ACCEPTED, "source", "actor", null, "correlation"
        )).isInstanceOf(NullPointerException.class).hasMessageContaining("Association revision");
    }

    @Test
    void commandRejectsNullDecision() {
        ProductId productId = new ProductId(UUID.randomUUID().toString());
        ProductRevision revision = revision("a3");

        assertThatThrownBy(() -> new RecordProductMediaRightsAcceptance.Command(
                UUID.randomUUID(), productId, revision, "media-123", "asset", "rev", revision, null,
                null, "source", "actor", null, "correlation"
        )).isInstanceOf(NullPointerException.class).hasMessageContaining("Decision");
    }

    @Test
    void commandToStringDoesNotExposeInternalDetails() {
        ProductId productId = new ProductId(UUID.randomUUID().toString());
        ProductRevision revision = revision("a4");

        var command = new RecordProductMediaRightsAcceptance.Command(
                UUID.randomUUID(), productId, revision, "media-123", "asset", "rev", revision, null,
                Decision.ACCEPTED, "source", "actor", null, "correlation"
        );

        assertThat(command.toString()).isEqualTo("RecordProductMediaRightsAcceptance.Command[internal]");
    }

    @Test
    void recordedResultToStringDoesNotExposeInternalDetails() {
        ProductId productId = new ProductId(UUID.randomUUID().toString());
        ProductRevision revision = revision("a5");

        var recorded = new RecordProductMediaRightsAcceptance.Recorded(
                UUID.randomUUID(), productId, revision, "media-123");

        assertThat(recorded.toString()).isEqualTo("RecordProductMediaRightsAcceptance.Recorded[internal]");
    }

    @Test
    void supportingReferencesAreDefensivelyCloned() {
        ProductId productId = new ProductId(UUID.randomUUID().toString());
        ProductRevision revision = revision("a6");
        String[] refs = new String[]{"ref1", "ref2"};

        var command = new RecordProductMediaRightsAcceptance.Command(
                UUID.randomUUID(), productId, revision, "media-123", "asset", "rev", revision, null,
                Decision.ACCEPTED, "source", "actor", refs, "correlation"
        );

        // Modify original array
        refs[0] = "modified";

        // Command should have a defensive copy
        assertThat(command.supportingReferences()[0]).isEqualTo("ref1");
    }

    @Test
    void rejectsNullCommand() {
        assertThatThrownBy(() -> useCase.record(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("command");
    }

    @Test
    void variantIdCanBeNullForProductLevelMedia() {
        ProductId productId = new ProductId(UUID.randomUUID().toString());
        ProductRevision revision = revision("a7");
        UUID evidenceId = UUID.randomUUID();
        String mediaAssociationId = "product-level-media";

        recordingPort.nextOutcome = new RecordingOutcome.Recorded(evidenceId, productId, revision, mediaAssociationId);

        var command = new RecordProductMediaRightsAcceptance.Command(
                evidenceId,
                productId,
                revision,
                mediaAssociationId,
                "asset-uuid",
                "asset-rev",
                revision,
                null, // Explicitly null for Product-level media
                Decision.ACCEPTED,
                "rights-review/1.0",
                "test-actor-claim",
                null,
                "correlation-null-variant"
        );

        var result = useCase.record(command);

        assertThat(result).isInstanceOf(RecordProductMediaRightsAcceptance.Recorded.class);
        assertThat(recordingPort.lastObservation.subject().variantId()).isNull();
    }

    /** Stub implementation of the recording port for unit testing. */
    private static class StubRecordingPort implements ProductMediaRightsAcceptanceRecordingPort {
        MediaRightsAcceptanceObservation lastObservation;
        RecordingOutcome nextOutcome;

        @Override
        public RecordingOutcome record(MediaRightsAcceptanceObservation observation) {
            this.lastObservation = observation;
            return nextOutcome;
        }
    }
}
