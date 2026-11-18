package com.enterprise.fashion.ecommerce.product.application.usecase;

import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.application.port.out.ProductContentAcceptanceRecordingPort;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductContentAcceptanceRecordingPort.ContentAcceptanceObservation;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductContentAcceptanceRecordingPort.Decision;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductContentAcceptanceRecordingPort.RecordingOutcome;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductRevision;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for RecordProductContentAcceptance use case.
 * <p>
 * These tests verify the use case behavior with a stubbed recording port.
 * They do NOT test database persistence or transaction management.
 */
class RecordProductContentAcceptanceTest {
    private StubRecordingPort recordingPort;
    private RecordProductContentAcceptance useCase;

    // Valid revision format: v1_ followed by 64 hex characters
    private static ProductRevision revision(String hexSuffix) {
        // Pad or truncate to ensure 64 hex chars
        String hex = (hexSuffix + "0".repeat(64)).substring(0, 64);
        return new ProductRevision("v1_" + hex);
    }

    @BeforeEach
    void setUp() {
        recordingPort = new StubRecordingPort();
        // Use a direct execution wrapper that doesn't require actual transaction infrastructure
        useCase = new RecordProductContentAcceptance(recordingPort, new DirectTransactionOperations());
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

        recordingPort.nextOutcome = new RecordingOutcome.Recorded(evidenceId, productId, revision);

        var command = new RecordProductContentAcceptance.Command(
                evidenceId,
                productId,
                revision,
                Decision.ACCEPTED,
                "content-review/1.0",
                "test-actor-claim",
                new String[]{"doc-ref-1"},
                "correlation-123"
        );

        var result = useCase.record(command);

        assertThat(result).isInstanceOf(RecordProductContentAcceptance.Recorded.class);
        var recorded = (RecordProductContentAcceptance.Recorded) result;
        assertThat(recorded.evidenceId()).isEqualTo(evidenceId);
        assertThat(recorded.productId()).isEqualTo(productId);
        assertThat(recorded.productRevision()).isEqualTo(revision);

        // Verify observation was passed correctly to port
        assertThat(recordingPort.lastObservation).isNotNull();
        assertThat(recordingPort.lastObservation.evidenceId()).isEqualTo(evidenceId);
        assertThat(recordingPort.lastObservation.productId()).isEqualTo(productId);
        assertThat(recordingPort.lastObservation.productRevision()).isEqualTo(revision);
        assertThat(recordingPort.lastObservation.decision()).isEqualTo(Decision.ACCEPTED);
    }

    @Test
    void recordsRejectedObservationSuccessfully() {
        ProductId productId = new ProductId(UUID.randomUUID().toString());
        ProductRevision revision = revision("b");
        UUID evidenceId = UUID.randomUUID();

        recordingPort.nextOutcome = new RecordingOutcome.Recorded(evidenceId, productId, revision);

        var command = new RecordProductContentAcceptance.Command(
                evidenceId,
                productId,
                revision,
                Decision.REJECTED,
                "content-review/1.0",
                "test-actor-claim",
                new String[]{},
                "correlation-456"
        );

        var result = useCase.record(command);

        assertThat(result).isInstanceOf(RecordProductContentAcceptance.Recorded.class);
        assertThat(recordingPort.lastObservation.decision()).isEqualTo(Decision.REJECTED);
    }

    @Test
    void returnsProductNotFoundWhenProductDoesNotExist() {
        ProductId productId = new ProductId(UUID.randomUUID().toString());
        ProductRevision revision = revision("c");
        UUID evidenceId = UUID.randomUUID();

        recordingPort.nextOutcome = new RecordingOutcome.ProductNotFound(productId);

        var command = new RecordProductContentAcceptance.Command(
                evidenceId,
                productId,
                revision,
                Decision.ACCEPTED,
                "content-review/1.0",
                "test-actor-claim",
                null,
                "correlation-789"
        );

        var result = useCase.record(command);

        assertThat(result).isInstanceOf(RecordProductContentAcceptance.ProductNotFound.class);
        var notFound = (RecordProductContentAcceptance.ProductNotFound) result;
        assertThat(notFound.productId()).isEqualTo(productId);
    }

    @Test
    void returnsRevisionMismatchWhenRevisionIsStale() {
        ProductId productId = new ProductId(UUID.randomUUID().toString());
        ProductRevision expectedRevision = revision("d");
        ProductRevision actualRevision = revision("e");
        UUID evidenceId = UUID.randomUUID();

        recordingPort.nextOutcome = new RecordingOutcome.RevisionMismatch(productId, expectedRevision, actualRevision);

        var command = new RecordProductContentAcceptance.Command(
                evidenceId,
                productId,
                expectedRevision,
                Decision.ACCEPTED,
                "content-review/1.0",
                "test-actor-claim",
                new String[]{"ref"},
                "correlation-abc"
        );

        var result = useCase.record(command);

        assertThat(result).isInstanceOf(RecordProductContentAcceptance.RevisionMismatch.class);
        var mismatch = (RecordProductContentAcceptance.RevisionMismatch) result;
        assertThat(mismatch.productId()).isEqualTo(productId);
        assertThat(mismatch.expected()).isEqualTo(expectedRevision);
        assertThat(mismatch.actual()).isEqualTo(actualRevision);
    }

    @Test
    void returnsDuplicateEvidenceWhenEvidenceAlreadyExists() {
        ProductId productId = new ProductId(UUID.randomUUID().toString());
        ProductRevision revision = revision("f");
        UUID evidenceId = UUID.randomUUID();

        recordingPort.nextOutcome = new RecordingOutcome.DuplicateEvidence(evidenceId);

        var command = new RecordProductContentAcceptance.Command(
                evidenceId,
                productId,
                revision,
                Decision.ACCEPTED,
                "content-review/1.0",
                "test-actor-claim",
                new String[]{},
                "correlation-def"
        );

        var result = useCase.record(command);

        assertThat(result).isInstanceOf(RecordProductContentAcceptance.DuplicateEvidence.class);
        var duplicate = (RecordProductContentAcceptance.DuplicateEvidence) result;
        assertThat(duplicate.evidenceId()).isEqualTo(evidenceId);
    }

    @Test
    void returnsValidationFailedWhenMetadataIsInvalid() {
        ProductId productId = new ProductId(UUID.randomUUID().toString());
        ProductRevision revision = revision("0");
        UUID evidenceId = UUID.randomUUID();

        recordingPort.nextOutcome = new RecordingOutcome.InvalidMetadata("Invalid source reference format");

        var command = new RecordProductContentAcceptance.Command(
                evidenceId,
                productId,
                revision,
                Decision.ACCEPTED,
                "content-review/1.0",
                "test-actor-claim",
                new String[]{},
                "correlation-012"
        );

        var result = useCase.record(command);

        assertThat(result).isInstanceOf(RecordProductContentAcceptance.ValidationFailed.class);
        var failed = (RecordProductContentAcceptance.ValidationFailed) result;
        assertThat(failed.reasons()).contains("Invalid source reference format");
    }

    @Test
    void commandRejectsNullEvidenceId() {
        ProductId productId = new ProductId(UUID.randomUUID().toString());
        ProductRevision revision = revision("1");

        assertThatThrownBy(() -> new RecordProductContentAcceptance.Command(
                null, productId, revision, Decision.ACCEPTED, "source", "actor", null, "correlation"
        )).isInstanceOf(NullPointerException.class).hasMessageContaining("Evidence identity");
    }

    @Test
    void commandRejectsNullProductId() {
        ProductRevision revision = revision("2");

        assertThatThrownBy(() -> new RecordProductContentAcceptance.Command(
                UUID.randomUUID(), null, revision, Decision.ACCEPTED, "source", "actor", null, "correlation"
        )).isInstanceOf(NullPointerException.class).hasMessageContaining("Product identity");
    }

    @Test
    void commandRejectsNullRevision() {
        ProductId productId = new ProductId(UUID.randomUUID().toString());

        assertThatThrownBy(() -> new RecordProductContentAcceptance.Command(
                UUID.randomUUID(), productId, null, Decision.ACCEPTED, "source", "actor", null, "correlation"
        )).isInstanceOf(NullPointerException.class).hasMessageContaining("Expected Product revision");
    }

    @Test
    void commandRejectsNullDecision() {
        ProductId productId = new ProductId(UUID.randomUUID().toString());
        ProductRevision revision = revision("3");

        assertThatThrownBy(() -> new RecordProductContentAcceptance.Command(
                UUID.randomUUID(), productId, revision, null, "source", "actor", null, "correlation"
        )).isInstanceOf(NullPointerException.class).hasMessageContaining("Decision");
    }

    @Test
    void commandToStringDoesNotExposeInternalDetails() {
        ProductId productId = new ProductId(UUID.randomUUID().toString());
        ProductRevision revision = revision("4");

        var command = new RecordProductContentAcceptance.Command(
                UUID.randomUUID(), productId, revision, Decision.ACCEPTED, "source", "actor", null, "correlation"
        );

        assertThat(command.toString()).isEqualTo("RecordProductContentAcceptance.Command[internal]");
    }

    @Test
    void recordedResultToStringDoesNotExposeInternalDetails() {
        ProductId productId = new ProductId(UUID.randomUUID().toString());
        ProductRevision revision = revision("5");

        var recorded = new RecordProductContentAcceptance.Recorded(UUID.randomUUID(), productId, revision);

        assertThat(recorded.toString()).isEqualTo("RecordProductContentAcceptance.Recorded[internal]");
    }

    @Test
    void supportingReferencesAreDefensivelyCloned() {
        ProductId productId = new ProductId(UUID.randomUUID().toString());
        ProductRevision revision = revision("6");
        String[] refs = new String[]{"ref1", "ref2"};

        var command = new RecordProductContentAcceptance.Command(
                UUID.randomUUID(), productId, revision, Decision.ACCEPTED, "source", "actor", refs, "correlation"
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

    /** Stub implementation of the recording port for unit testing. */
    private static class StubRecordingPort implements ProductContentAcceptanceRecordingPort {
        ContentAcceptanceObservation lastObservation;
        RecordingOutcome nextOutcome;

        @Override
        public RecordingOutcome record(ContentAcceptanceObservation observation) {
            this.lastObservation = observation;
            return nextOutcome;
        }
    }
}
