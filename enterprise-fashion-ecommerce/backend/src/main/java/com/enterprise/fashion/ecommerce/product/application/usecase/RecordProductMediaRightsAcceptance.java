package com.enterprise.fashion.ecommerce.product.application.usecase;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.application.port.out.ProductMediaRightsAcceptanceRecordingPort;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductMediaRightsAcceptanceRecordingPort.Decision;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductMediaRightsAcceptanceRecordingPort.MediaRightsAcceptanceObservation;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductMediaRightsAcceptanceRecordingPort.MediaSubjectBinding;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductMediaRightsAcceptanceRecordingPort.RecordingOutcome;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductRevision;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariantId;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Records a non-authoritative media usage-rights acceptance observation for a Product.
 * <p>
 * This use case records claims about media rights acceptance decisions. It does NOT:
 * <ul>
 *   <li>Authenticate the caller or validate Staff identity</li>
 *   <li>Establish trusted acceptance evidence</li>
 *   <li>Enable publication or change Product lifecycle</li>
 *   <li>Grant customer visibility</li>
 *   <li>Infer acceptance from media upload or association</li>
 * </ul>
 * <p>
 * The actor claim is a source-asserted value for audit purposes only. Database recording
 * attribution (recordedBy/recordedAt) describes storage mechanics, not identity authority.
 * Publication remains blocked until an authoritative evidence source is approved and configured.
 * <p>
 * P2 Media Acceptance (DEC-0042) defines WHAT to verify for media rights; this use case records
 * observations about that verification — it does not perform the verification itself.
 * <p>
 * BPRD-REQ-020, 021: evidence binding without operational authorization.
 */
public final class RecordProductMediaRightsAcceptance {
    private final ProductMediaRightsAcceptanceRecordingPort recordingPort;
    private final TransactionOperations writes;

    public RecordProductMediaRightsAcceptance(ProductMediaRightsAcceptanceRecordingPort recordingPort,
            TransactionOperations writes) {
        this.recordingPort = Objects.requireNonNull(recordingPort, "recordingPort");
        this.writes = Objects.requireNonNull(writes, "writes");
    }

    /**
     * Records a media usage-rights acceptance observation.
     *
     * @param command the recording command with all required metadata
     * @return typed result indicating success or specific failure reason
     */
    public Result record(Command command) {
        Objects.requireNonNull(command, "command");
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException("RecordProductMediaRightsAcceptance must own its transaction boundary");
        }

        return Objects.requireNonNull(writes.execute(status -> {
            // Build the observation from command
            MediaRightsAcceptanceObservation observation;
            try {
                MediaSubjectBinding subject = new MediaSubjectBinding(
                        command.mediaAssociationId(),
                        command.assetId(),
                        command.assetRevision(),
                        command.associationRevision(),
                        command.variantId()
                );
                observation = new MediaRightsAcceptanceObservation(
                        command.evidenceId(),
                        command.productId(),
                        command.expectedRevision(),
                        subject,
                        command.decision(),
                        command.sourceReference(),
                        command.actorClaim(),
                        command.supportingReferences(),
                        command.auditCorrelation()
                );
            } catch (IllegalArgumentException invalid) {
                return new ValidationFailed(Set.of(invalid.getMessage()));
            }

            // Delegate to recording port
            RecordingOutcome outcome = recordingPort.record(observation);

            // Map port outcome to use case result
            return switch (outcome) {
                case RecordingOutcome.Recorded r -> new Recorded(r.evidenceId(), r.productId(),
                        r.productRevision(), r.mediaAssociationId());
                case RecordingOutcome.ProductNotFound pnf -> new ProductNotFound(pnf.productId());
                case RecordingOutcome.RevisionMismatch rm -> new RevisionMismatch(rm.productId(),
                        rm.expected(), rm.actual());
                case RecordingOutcome.MediaAssociationNotFound manf -> new MediaAssociationNotFound(
                        manf.productId(), manf.mediaAssociationId());
                case RecordingOutcome.MediaSubjectMismatch msm -> new MediaSubjectMismatch(
                        msm.productId(), msm.mediaAssociationId(), msm.mismatchReason());
                case RecordingOutcome.DuplicateEvidence de -> new DuplicateEvidence(de.evidenceId());
                case RecordingOutcome.InvalidMetadata im -> new ValidationFailed(Set.of(im.reason()));
            };
        }), "RecordProductMediaRightsAcceptance transaction returned null");
    }

    /**
     * Command input for recording a media usage-rights acceptance observation.
     * <p>
     * The actorClaim is a source assertion for audit purposes only — it is NOT authenticated
     * Staff identity and MUST NOT be represented as verified. Do not confuse this with
     * authorization or trusted provenance.
     *
     * @param evidenceId unique identity for this observation (caller-supplied UUID)
     * @param productId Product identity scope
     * @param expectedRevision exact Product revision — recording fails if current revision differs
     * @param mediaAssociationId ProductMedia.id — must exist on Product
     * @param assetId quarantine asset identity
     * @param assetRevision ProductMedia.assetRevision — must match exactly
     * @param associationRevision Product revision at association time — must match current
     * @param variantId ProductMedia.variantId — must match exactly, including null
     * @param decision the media rights acceptance decision being recorded
     * @param sourceReference identifier for the source/authority making this claim
     * @param actorClaim source-asserted actor identity (NOT authenticated Staff)
     * @param supportingReferences optional references to supporting evidence/documentation
     * @param auditCorrelation correlation identifier for audit trail linkage
     */
    public record Command(
            UUID evidenceId,
            ProductId productId,
            ProductRevision expectedRevision,
            String mediaAssociationId,
            String assetId,
            String assetRevision,
            ProductRevision associationRevision,
            ProductVariantId variantId,
            Decision decision,
            String sourceReference,
            String actorClaim,
            String[] supportingReferences,
            String auditCorrelation
    ) {
        public Command {
            Objects.requireNonNull(evidenceId, "Evidence identity required");
            Objects.requireNonNull(productId, "Product identity required");
            Objects.requireNonNull(expectedRevision, "Expected Product revision required");
            Objects.requireNonNull(mediaAssociationId, "Media association identity required");
            Objects.requireNonNull(assetId, "Asset identity required");
            Objects.requireNonNull(assetRevision, "Asset revision required");
            Objects.requireNonNull(associationRevision, "Association revision required");
            // variantId may be null for Product-level media
            Objects.requireNonNull(decision, "Decision required");
            Objects.requireNonNull(sourceReference, "Source reference required");
            Objects.requireNonNull(actorClaim, "Actor claim required");
            if (supportingReferences == null) {
                supportingReferences = new String[0];
            }
            supportingReferences = supportingReferences.clone();
            Objects.requireNonNull(auditCorrelation, "Audit correlation required");
        }

        @Override
        public String[] supportingReferences() {
            return supportingReferences.clone();
        }

        @Override
        public String toString() {
            return "RecordProductMediaRightsAcceptance.Command[internal]";
        }
    }

    /** Sealed result hierarchy for typed outcome handling. */
    public sealed interface Result permits Recorded, ProductNotFound, RevisionMismatch,
            MediaAssociationNotFound, MediaSubjectMismatch, DuplicateEvidence, ValidationFailed {}

    /**
     * Observation successfully recorded. This is NOT acceptance evidence — it is storage
     * confirmation only. Publication and visibility remain blocked.
     */
    public record Recorded(UUID evidenceId, ProductId productId, ProductRevision productRevision,
            String mediaAssociationId) implements Result {
        public Recorded {
            Objects.requireNonNull(evidenceId, "Evidence identity required");
            Objects.requireNonNull(productId, "Product identity required");
            Objects.requireNonNull(productRevision, "Product revision required");
            Objects.requireNonNull(mediaAssociationId, "Media association identity required");
        }

        @Override
        public String toString() {
            return "RecordProductMediaRightsAcceptance.Recorded[internal]";
        }
    }

    /** Product with the specified identity does not exist. */
    public record ProductNotFound(ProductId productId) implements Result {
        public ProductNotFound {
            Objects.requireNonNull(productId, "Product identity required");
        }
    }

    /** Product exists but expected revision does not match current revision. */
    public record RevisionMismatch(ProductId productId, ProductRevision expected,
            ProductRevision actual) implements Result {
        public RevisionMismatch {
            Objects.requireNonNull(productId, "Product identity required");
            Objects.requireNonNull(expected, "Expected revision required");
            Objects.requireNonNull(actual, "Actual revision required");
        }
    }

    /** ProductMedia with the specified association identity does not exist on this Product. */
    public record MediaAssociationNotFound(ProductId productId, String mediaAssociationId) implements Result {
        public MediaAssociationNotFound {
            Objects.requireNonNull(productId, "Product identity required");
            Objects.requireNonNull(mediaAssociationId, "Media association identity required");
        }
    }

    /** Media subject binding does not match the existing ProductMedia association. */
    public record MediaSubjectMismatch(ProductId productId, String mediaAssociationId,
            String mismatchReason) implements Result {
        public MediaSubjectMismatch {
            Objects.requireNonNull(productId, "Product identity required");
            Objects.requireNonNull(mediaAssociationId, "Media association identity required");
            Objects.requireNonNull(mismatchReason, "Mismatch reason required");
        }
    }

    /** Evidence with the specified identity already exists. */
    public record DuplicateEvidence(UUID evidenceId) implements Result {
        public DuplicateEvidence {
            Objects.requireNonNull(evidenceId, "Evidence identity required");
        }
    }

    /** Command validation failed. */
    public record ValidationFailed(Set<String> reasons) implements Result {
        public ValidationFailed {
            Objects.requireNonNull(reasons, "Validation reasons required");
            if (reasons.isEmpty()) {
                throw new IllegalArgumentException("At least one validation reason required");
            }
            reasons = Set.copyOf(reasons);
        }
    }
}
