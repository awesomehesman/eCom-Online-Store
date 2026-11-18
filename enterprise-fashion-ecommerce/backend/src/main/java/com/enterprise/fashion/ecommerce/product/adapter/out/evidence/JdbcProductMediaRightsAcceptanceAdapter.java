package com.enterprise.fashion.ecommerce.product.adapter.out.evidence;

import java.util.Objects;

import com.enterprise.fashion.ecommerce.product.application.port.out.ProductMediaRightsAcceptanceRecordingPort;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductPersistencePort;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductMedia;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

/**
 * JDBC adapter for recording media usage-rights acceptance observations using the existing V6 evidence_event schema.
 * <p>
 * This adapter delegates to {@link JdbcProductEvidenceStore#append(EvidenceEventRow)} for actual storage,
 * preserving all database constraints (immutability, FK integrity, revision binding).
 * <p>
 * Recording an observation does NOT establish authoritative acceptance evidence. The evidence_event
 * table stores non-authoritative claims only; publication requires a separate trusted evidence
 * source that is not yet approved or configured.
 * <p>
 * The decision field (ACCEPTED/REJECTED) represents a source-asserted claim, not a validated outcome.
 * Actor claims are source assertions — they are NOT authenticated Staff identity.
 * <p>
 * Media subject validation ensures the observation is bound to an existing ProductMedia association
 * with exact asset revision, association revision and variant binding.
 */
@Component
class JdbcProductMediaRightsAcceptanceAdapter implements ProductMediaRightsAcceptanceRecordingPort {
    private final JdbcProductEvidenceStore store;
    private final ProductPersistencePort products;

    JdbcProductMediaRightsAcceptanceAdapter(JdbcProductEvidenceStore store, ProductPersistencePort products) {
        this.store = Objects.requireNonNull(store, "store");
        this.products = Objects.requireNonNull(products, "products");
    }

    @Override
    public RecordingOutcome record(MediaRightsAcceptanceObservation observation) {
        Objects.requireNonNull(observation, "observation");

        // Load current Product to verify existence and revision
        Product product = products.load(observation.productId()).orElse(null);
        if (product == null) {
            return new RecordingOutcome.ProductNotFound(observation.productId());
        }

        // Verify Product revision matches
        if (!observation.productRevision().equals(product.revision())) {
            return new RecordingOutcome.RevisionMismatch(
                    observation.productId(),
                    observation.productRevision(),
                    product.revision()
            );
        }

        // Find the ProductMedia association
        ProductMedia media = product.media().stream()
                .filter(m -> m.id().equals(observation.subject().mediaAssociationId()))
                .findFirst()
                .orElse(null);

        if (media == null) {
            return new RecordingOutcome.MediaAssociationNotFound(
                    observation.productId(),
                    observation.subject().mediaAssociationId()
            );
        }

        // Validate exact media subject binding
        MediaSubjectBinding subject = observation.subject();

        // Verify asset revision matches
        if (!media.assetRevision().equals(subject.assetRevision())) {
            return new RecordingOutcome.MediaSubjectMismatch(
                    observation.productId(),
                    subject.mediaAssociationId(),
                    "Asset revision mismatch: expected " + media.assetRevision() + ", got " + subject.assetRevision()
            );
        }

        // Verify association revision matches current Product revision
        if (!product.revision().equals(subject.associationRevision())) {
            return new RecordingOutcome.MediaSubjectMismatch(
                    observation.productId(),
                    subject.mediaAssociationId(),
                    "Association revision mismatch: expected " + product.revision().value() +
                            ", got " + subject.associationRevision().value()
            );
        }

        // Verify variant binding matches exactly, including null semantics
        if (!Objects.equals(media.variantId(), subject.variantId())) {
            String expectedVariant = media.variantId() != null ? media.variantId().value() : "null";
            String actualVariant = subject.variantId() != null ? subject.variantId().value() : "null";
            return new RecordingOutcome.MediaSubjectMismatch(
                    observation.productId(),
                    subject.mediaAssociationId(),
                    "Variant mismatch: expected " + expectedVariant + ", got " + actualVariant
            );
        }

        // Build source reference with full subject binding for audit traceability
        // This encoding preserves all correlation fields in the evidence_event record
        StringBuilder sourceReferenceBuilder = new StringBuilder(observation.sourceReference())
                .append("/decision:").append(observation.decision().name())
                .append("/media:").append(subject.mediaAssociationId())
                .append("/asset:").append(subject.assetId())
                .append("/assetRev:").append(subject.assetRevision())
                .append("/assocRev:").append(subject.associationRevision().value());
        if (subject.variantId() != null) {
            sourceReferenceBuilder.append("/variant:").append(subject.variantId().value());
        }
        String sourceReferenceWithSubject = sourceReferenceBuilder.toString();

        EvidenceEventRow row = new EvidenceEventRow(
                observation.evidenceId(),
                observation.productId().value(),
                observation.productRevision().value(),
                "MEDIA",    // purpose = MEDIA for media rights acceptance
                "RECORD",   // event_type = RECORD (not INVALIDATE or SUPERSEDE)
                sourceReferenceWithSubject,
                observation.actorClaim(),
                observation.supportingReferences(),
                observation.auditCorrelation(),
                null,   // recordedAt set by database trigger
                null,   // recordedBy set by database trigger
                null,   // targetEventId (null for RECORD type)
                null    // replacementEventId (null for RECORD type)
        );

        try {
            store.append(row);
            return new RecordingOutcome.Recorded(
                    observation.evidenceId(),
                    observation.productId(),
                    observation.productRevision(),
                    subject.mediaAssociationId()
            );
        } catch (DuplicateKeyException duplicate) {
            return new RecordingOutcome.DuplicateEvidence(observation.evidenceId());
        } catch (org.springframework.data.relational.core.conversion.DbActionExecutionException wrapped) {
            // Spring Data wraps the underlying exception
            if (wrapped.getCause() instanceof DuplicateKeyException) {
                return new RecordingOutcome.DuplicateEvidence(observation.evidenceId());
            }
            return new RecordingOutcome.InvalidMetadata("Database constraint violation: " + extractMessage(wrapped));
        } catch (IllegalArgumentException invalid) {
            return new RecordingOutcome.InvalidMetadata(invalid.getMessage());
        } catch (DataIntegrityViolationException integrity) {
            // This could be FK violation or other constraint — treat as metadata error
            return new RecordingOutcome.InvalidMetadata("Database constraint violation: " + extractMessage(integrity));
        }
    }

    private static String extractMessage(Exception e) {
        // Extract a safe message without exposing internal details
        String message = e.getMessage();
        if (message == null) {
            return "constraint violation";
        }
        // Truncate to avoid leaking internal details
        return message.length() > 100 ? message.substring(0, 100) + "..." : message;
    }
}
