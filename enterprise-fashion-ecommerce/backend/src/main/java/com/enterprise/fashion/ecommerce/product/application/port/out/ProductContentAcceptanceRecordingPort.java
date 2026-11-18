package com.enterprise.fashion.ecommerce.product.application.port.out;

import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductRevision;

/**
 * Narrowly scoped recording of content acceptance observations. This port records non-authoritative
 * claims about content acceptance decisions; it does NOT establish trusted acceptance evidence.
 * <p>
 * The actor claim is a source-asserted value for audit purposes only — it is NOT authenticated
 * Staff identity and MUST NOT be represented as verified. Database recording attribution
 * (recordedBy/recordedAt) describes storage mechanics, not identity authority.
 * <p>
 * Implementations must require an application-owned writable repeatable-read transaction,
 * validate Product existence and exact revision binding, and preserve the immutable
 * append-only history semantics of the underlying evidence_event table.
 */
public interface ProductContentAcceptanceRecordingPort {

    /**
     * Records a content acceptance observation bound to an exact Product revision.
     *
     * @param observation the content acceptance observation to record
     * @return the outcome of the recording attempt
     * @throws IllegalStateException if called outside an appropriate transaction
     */
    RecordingOutcome record(ContentAcceptanceObservation observation);

    /**
     * Immutable observation shape for content acceptance recording.
     * All fields are source assertions — none is authenticated or trusted.
     *
     * @param evidenceId unique identity for this observation (caller-supplied UUID)
     * @param productId Product identity scope
     * @param productRevision exact Product revision binding — recording fails if stale
     * @param decision the content acceptance decision being recorded
     * @param sourceReference identifier for the source/authority making this claim
     * @param actorClaim source-asserted actor identity (NOT authenticated Staff)
     * @param supportingReferences optional references to supporting evidence/documentation
     * @param auditCorrelation correlation identifier for audit trail linkage
     */
    record ContentAcceptanceObservation(
            UUID evidenceId,
            ProductId productId,
            ProductRevision productRevision,
            Decision decision,
            String sourceReference,
            String actorClaim,
            String[] supportingReferences,
            String auditCorrelation
    ) {
        public ContentAcceptanceObservation {
            if (evidenceId == null) {
                throw new IllegalArgumentException("Evidence identity required");
            }
            if (productId == null) {
                throw new IllegalArgumentException("Product identity required");
            }
            if (productRevision == null) {
                throw new IllegalArgumentException("Product revision required");
            }
            if (decision == null) {
                throw new IllegalArgumentException("Decision required");
            }
            if (sourceReference == null || sourceReference.isBlank()) {
                throw new IllegalArgumentException("Source reference required");
            }
            if (actorClaim == null || actorClaim.isBlank()) {
                throw new IllegalArgumentException("Actor claim required");
            }
            if (supportingReferences == null) {
                supportingReferences = new String[0];
            }
            for (String ref : supportingReferences) {
                if (ref == null || ref.isBlank()) {
                    throw new IllegalArgumentException("Supporting references must not contain blank values");
                }
            }
            supportingReferences = supportingReferences.clone();
            if (auditCorrelation == null || !auditCorrelation.matches("[A-Za-z0-9._-]{1,128}")) {
                throw new IllegalArgumentException("Valid audit correlation required");
            }
        }

        @Override
        public String[] supportingReferences() {
            return supportingReferences.clone();
        }

        @Override
        public String toString() {
            return "ContentAcceptanceObservation[internal]";
        }
    }

    /**
     * Content acceptance decision. This represents the decision being recorded,
     * NOT an authoritative acceptance outcome.
     */
    enum Decision {
        /** Content meets P1 checklist criteria per source assessment. */
        ACCEPTED,
        /** Content does not meet P1 checklist criteria per source assessment. */
        REJECTED
    }

    /** Outcome of a content acceptance recording attempt. */
    sealed interface RecordingOutcome permits RecordingOutcome.Recorded, RecordingOutcome.ProductNotFound,
            RecordingOutcome.RevisionMismatch, RecordingOutcome.DuplicateEvidence, RecordingOutcome.InvalidMetadata {

        /** Observation successfully recorded. This is NOT acceptance — it is storage confirmation only. */
        record Recorded(UUID evidenceId, ProductId productId, ProductRevision productRevision) implements RecordingOutcome {
            public Recorded {
                if (evidenceId == null || productId == null || productRevision == null) {
                    throw new IllegalArgumentException("Recording confirmation requires complete binding");
                }
            }

            @Override
            public String toString() {
                return "RecordingOutcome.Recorded[internal]";
            }
        }

        /** Product with the specified identity does not exist. */
        record ProductNotFound(ProductId productId) implements RecordingOutcome {
            public ProductNotFound {
                if (productId == null) {
                    throw new IllegalArgumentException("Product identity required");
                }
            }
        }

        /** Product exists but revision does not match current revision. */
        record RevisionMismatch(ProductId productId, ProductRevision expected, ProductRevision actual) implements RecordingOutcome {
            public RevisionMismatch {
                if (productId == null || expected == null || actual == null) {
                    throw new IllegalArgumentException("Revision mismatch requires complete binding");
                }
            }
        }

        /** Evidence with the specified identity already exists. */
        record DuplicateEvidence(UUID evidenceId) implements RecordingOutcome {
            public DuplicateEvidence {
                if (evidenceId == null) {
                    throw new IllegalArgumentException("Evidence identity required");
                }
            }
        }

        /** Observation metadata failed validation. */
        record InvalidMetadata(String reason) implements RecordingOutcome {
            public InvalidMetadata {
                if (reason == null || reason.isBlank()) {
                    throw new IllegalArgumentException("Validation failure reason required");
                }
            }
        }
    }
}
