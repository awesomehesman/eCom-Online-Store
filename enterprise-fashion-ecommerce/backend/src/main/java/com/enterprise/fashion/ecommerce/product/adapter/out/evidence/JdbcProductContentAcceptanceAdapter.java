package com.enterprise.fashion.ecommerce.product.adapter.out.evidence;

import java.util.Objects;

import com.enterprise.fashion.ecommerce.product.application.port.out.ProductContentAcceptanceRecordingPort;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductPersistencePort;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

/**
 * JDBC adapter for recording content acceptance observations using the existing V6 evidence_event schema.
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
 */
@Component
class JdbcProductContentAcceptanceAdapter implements ProductContentAcceptanceRecordingPort {
    private final JdbcProductEvidenceStore store;
    private final ProductPersistencePort products;

    JdbcProductContentAcceptanceAdapter(JdbcProductEvidenceStore store, ProductPersistencePort products) {
        this.store = Objects.requireNonNull(store, "store");
        this.products = Objects.requireNonNull(products, "products");
    }

    @Override
    public RecordingOutcome record(ContentAcceptanceObservation observation) {
        Objects.requireNonNull(observation, "observation");

        // Load current Product to verify existence and revision
        Product product = products.load(observation.productId()).orElse(null);
        if (product == null) {
            return new RecordingOutcome.ProductNotFound(observation.productId());
        }

        // Verify revision matches
        if (!observation.productRevision().equals(product.revision())) {
            return new RecordingOutcome.RevisionMismatch(
                    observation.productId(),
                    observation.productRevision(),
                    product.revision()
            );
        }

        // Build the evidence event row
        // Note: The decision (ACCEPTED/REJECTED) is encoded in sourceReference as part of the
        // source claim structure. The evidence_event schema stores observations, not acceptance outcomes.
        // The sourceReference format includes the decision for audit traceability.
        String sourceReferenceWithDecision = observation.sourceReference() + "/decision:" + observation.decision().name();

        EvidenceEventRow row = new EvidenceEventRow(
                observation.evidenceId(),
                observation.productId().value(),
                observation.productRevision().value(),
                "CONTENT",  // purpose = CONTENT for content acceptance
                "RECORD",   // event_type = RECORD (not INVALIDATE or SUPERSEDE)
                sourceReferenceWithDecision,
                observation.actorClaim(),
                observation.supportingReferences(),
                observation.auditCorrelation(),
                null,  // recordedAt set by database trigger
                null,  // recordedBy set by database trigger
                null,  // targetEventId (null for RECORD type)
                null   // replacementEventId (null for RECORD type)
        );

        try {
            store.append(row);
            return new RecordingOutcome.Recorded(
                    observation.evidenceId(),
                    observation.productId(),
                    observation.productRevision()
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
