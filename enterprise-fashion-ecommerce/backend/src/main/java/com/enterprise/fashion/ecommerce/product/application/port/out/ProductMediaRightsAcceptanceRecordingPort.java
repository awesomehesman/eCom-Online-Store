package com.enterprise.fashion.ecommerce.product.application.port.out;

import java.util.Objects;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductRevision;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariantId;

/**
 * Narrowly scoped recording of media usage-rights acceptance observations. This port records
 * non-authoritative claims about media rights decisions; it does NOT establish trusted acceptance evidence.
 * <p>
 * The actor claim is a source-asserted value for audit purposes only — it is NOT authenticated
 * Staff identity and MUST NOT be represented as verified. Database recording attribution
 * (recordedBy/recordedAt) describes storage mechanics, not identity authority.
 * <p>
 * Implementations must require an application-owned writable repeatable-read transaction,
 * validate Product existence, exact revision binding, and ProductMedia association existence
 * with exact subject correlation, and preserve the immutable append-only history semantics
 * of the underlying evidence_event table.
 */
public interface ProductMediaRightsAcceptanceRecordingPort {

    /**
     * Records a media usage-rights acceptance observation bound to an exact Product revision
     * and exact media subject.
     *
     * @param observation the media rights acceptance observation to record
     * @return the outcome of the recording attempt
     * @throws IllegalStateException if called outside an appropriate transaction
     */
    RecordingOutcome record(MediaRightsAcceptanceObservation observation);

    /**
     * Immutable observation shape for media usage-rights acceptance recording.
     * All fields are source assertions — none is authenticated or trusted.
     *
     * @param evidenceId unique identity for this observation (caller-supplied UUID)
     * @param productId Product identity scope
     * @param productRevision exact Product revision binding — recording fails if stale
     * @param subject exact media subject binding (association, asset, revisions, variant)
     * @param decision the media rights acceptance decision being recorded
     * @param sourceReference identifier for the source/authority making this claim
     * @param actorClaim source-asserted actor identity (NOT authenticated Staff)
     * @param supportingReferences optional references to supporting evidence/documentation
     * @param auditCorrelation correlation identifier for audit trail linkage
     */
    record MediaRightsAcceptanceObservation(
            UUID evidenceId,
            ProductId productId,
            ProductRevision productRevision,
            MediaSubjectBinding subject,
            Decision decision,
            String sourceReference,
            String actorClaim,
            String[] supportingReferences,
            String auditCorrelation
    ) {
        public MediaRightsAcceptanceObservation {
            if (evidenceId == null) {
                throw new IllegalArgumentException("Evidence identity required");
            }
            if (productId == null) {
                throw new IllegalArgumentException("Product identity required");
            }
            if (productRevision == null) {
                throw new IllegalArgumentException("Product revision required");
            }
            if (subject == null) {
                throw new IllegalArgumentException("Media subject binding required");
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
            return "MediaRightsAcceptanceObservation[internal]";
        }
    }

    /**
     * Exact media subject binding for rights acceptance observations.
     * All fields must match an existing ProductMedia association on the Product.
     *
     * @param mediaAssociationId ProductMedia.id — must exist on the Product
     * @param assetId quarantine asset identity — must match ProductMedia context
     * @param assetRevision ProductMedia.assetRevision — must match exactly
     * @param associationRevision Product revision at association time — must match current
     * @param variantId ProductMedia.variantId — must match exactly, including null
     */
    record MediaSubjectBinding(
            String mediaAssociationId,
            String assetId,
            String assetRevision,
            ProductRevision associationRevision,
            ProductVariantId variantId
    ) {
        public MediaSubjectBinding {
            if (mediaAssociationId == null || mediaAssociationId.isBlank()) {
                throw new IllegalArgumentException("Media association identity required");
            }
            if (assetId == null || assetId.isBlank()) {
                throw new IllegalArgumentException("Asset identity required");
            }
            if (assetRevision == null || assetRevision.isBlank()) {
                throw new IllegalArgumentException("Asset revision required");
            }
            if (associationRevision == null) {
                throw new IllegalArgumentException("Association revision required");
            }
            // variantId may be null for Product-level media
        }

        @Override
        public String toString() {
            return "MediaSubjectBinding[internal]";
        }
    }

    /**
     * Media rights acceptance decision. This represents the decision being recorded,
     * NOT an authoritative acceptance outcome.
     */
    enum Decision {
        /** Media usage rights are confirmed per source assessment. */
        ACCEPTED,
        /** Media usage rights are not confirmed per source assessment. */
        REJECTED
    }

    /** Outcome of a media rights acceptance recording attempt. */
    sealed interface RecordingOutcome permits RecordingOutcome.Recorded, RecordingOutcome.ProductNotFound,
            RecordingOutcome.RevisionMismatch, RecordingOutcome.MediaAssociationNotFound,
            RecordingOutcome.MediaSubjectMismatch, RecordingOutcome.DuplicateEvidence,
            RecordingOutcome.InvalidMetadata {

        /** Observation successfully recorded. This is NOT acceptance — it is storage confirmation only. */
        record Recorded(UUID evidenceId, ProductId productId, ProductRevision productRevision,
                String mediaAssociationId) implements RecordingOutcome {
            public Recorded {
                Objects.requireNonNull(evidenceId, "Evidence identity required");
                Objects.requireNonNull(productId, "Product identity required");
                Objects.requireNonNull(productRevision, "Product revision required");
                Objects.requireNonNull(mediaAssociationId, "Media association identity required");
            }

            @Override
            public String toString() {
                return "RecordingOutcome.Recorded[internal]";
            }
        }

        /** Product with the specified identity does not exist. */
        record ProductNotFound(ProductId productId) implements RecordingOutcome {
            public ProductNotFound {
                Objects.requireNonNull(productId, "Product identity required");
            }
        }

        /** Product exists but revision does not match current revision. */
        record RevisionMismatch(ProductId productId, ProductRevision expected,
                ProductRevision actual) implements RecordingOutcome {
            public RevisionMismatch {
                Objects.requireNonNull(productId, "Product identity required");
                Objects.requireNonNull(expected, "Expected revision required");
                Objects.requireNonNull(actual, "Actual revision required");
            }
        }

        /** ProductMedia with the specified association identity does not exist on this Product. */
        record MediaAssociationNotFound(ProductId productId, String mediaAssociationId) implements RecordingOutcome {
            public MediaAssociationNotFound {
                Objects.requireNonNull(productId, "Product identity required");
                Objects.requireNonNull(mediaAssociationId, "Media association identity required");
            }
        }

        /** Media subject binding does not match the existing ProductMedia association. */
        record MediaSubjectMismatch(ProductId productId, String mediaAssociationId,
                String mismatchReason) implements RecordingOutcome {
            public MediaSubjectMismatch {
                Objects.requireNonNull(productId, "Product identity required");
                Objects.requireNonNull(mediaAssociationId, "Media association identity required");
                Objects.requireNonNull(mismatchReason, "Mismatch reason required");
            }
        }

        /** Evidence with the specified identity already exists. */
        record DuplicateEvidence(UUID evidenceId) implements RecordingOutcome {
            public DuplicateEvidence {
                Objects.requireNonNull(evidenceId, "Evidence identity required");
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
