package com.enterprise.fashion.ecommerce.product.adapter.out.evidence;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.application.media.ProductImageAssociationBinding;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductImageAssociationBindingPort;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductMediaRightsAcceptanceRecordingPort;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductPersistencePort;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductMedia;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.simple.JdbcClient;
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
    private final ProductImageAssociationBindingPort bindings;
    private final JdbcClient jdbc;

    JdbcProductMediaRightsAcceptanceAdapter(JdbcProductEvidenceStore store, ProductPersistencePort products,
            ProductImageAssociationBindingPort bindings, JdbcClient jdbc) {
        this.store = Objects.requireNonNull(store, "store");
        this.products = Objects.requireNonNull(products, "products");
        this.bindings = Objects.requireNonNull(bindings, "bindings");
        this.jdbc = Objects.requireNonNull(jdbc, "jdbc");
    }

    @Override
    public RecordingOutcome record(MediaRightsAcceptanceObservation observation) {
        Objects.requireNonNull(observation, "observation");

        // The Draft replacement CAS updates this same row. Holding its lock through append
        // serializes the final revision/association validation with representation replacement.
        boolean exists = jdbc.sql("""
                SELECT product_id FROM product.product
                 WHERE product_id = :productId
                 FOR UPDATE
                """).param("productId", observation.productId().value()).query(String.class).optional().isPresent();
        if (!exists) {
            return new RecordingOutcome.ProductNotFound(observation.productId());
        }

        // Load current Product only after acquiring the serialization lock.
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

        UUID assetId;
        try {
            assetId = UUID.fromString(subject.assetId());
            if (!assetId.toString().equals(subject.assetId())) {
                return mismatch(observation, "Asset identity is not canonical");
            }
        } catch (IllegalArgumentException invalid) {
            return mismatch(observation, "Asset identity is invalid");
        }

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

        ProductImageAssociationBinding binding = bindings.exact(observation.productId(),
                observation.productRevision(), subject.mediaAssociationId(), assetId).orElse(null);
        if (binding == null) {
            return mismatch(observation, "Exact V14 media asset binding is absent");
        }
        if (!binding.productId().equals(observation.productId())
                || !binding.productRevision().equals(observation.productRevision())
                || !binding.mediaAssociationId().equals(subject.mediaAssociationId())
                || !binding.associationRevision().equals(subject.associationRevision())
                || !binding.assetId().equals(assetId)
                || !binding.assetRevision().equals(subject.assetRevision())
                || !Objects.equals(binding.variantId(), subject.variantId())
                || binding.authoritativeAcceptanceEligible()) {
            return mismatch(observation, "V14 media asset binding does not match the exact subject");
        }

        String sourceReferenceWithSubject = encodeSourceReference(observation);

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

    private static RecordingOutcome.MediaSubjectMismatch mismatch(
            MediaRightsAcceptanceObservation observation, String reason) {
        return new RecordingOutcome.MediaSubjectMismatch(observation.productId(),
                observation.subject().mediaAssociationId(), reason);
    }

    static String encodeSourceReference(MediaRightsAcceptanceObservation observation) {
        MediaSubjectBinding subject = observation.subject();
        return String.join("|", "bprd-media-rights:v1",
                "source=" + token(observation.sourceReference()),
                "decision=" + observation.decision().name(),
                "media=" + token(subject.mediaAssociationId()),
                "asset=" + token(subject.assetId()),
                "assetRevision=" + token(subject.assetRevision()),
                "associationRevision=" + token(subject.associationRevision().value()),
                "variant=" + (subject.variantId() == null ? "null" : token(subject.variantId().value())));
    }

    static PersistedSubject decodeSourceReference(String encoded) {
        if (encoded == null) {
            throw new IllegalArgumentException("Encoded media subject required");
        }
        String[] fields = encoded.split("\\|", -1);
        if (fields.length != 8 || !"bprd-media-rights:v1".equals(fields[0])) {
            throw new IllegalArgumentException("Malformed media subject encoding");
        }
        try {
            String source = value(fields[1], "source=", false);
            Decision decision = Decision.valueOf(value(fields[2], "decision=", true));
            String media = value(fields[3], "media=", false);
            String asset = value(fields[4], "asset=", false);
            String assetRevision = value(fields[5], "assetRevision=", false);
            String associationRevision = value(fields[6], "associationRevision=", false);
            String variantValue = raw(fields[7], "variant=");
            String variant = "null".equals(variantValue) ? null : decodeToken(variantValue);
            PersistedSubject result = new PersistedSubject(source, decision, media, asset,
                    assetRevision, associationRevision, variant);
            if (!encoded.equals(result.encode())) {
                throw new IllegalArgumentException("Non-canonical media subject encoding");
            }
            return result;
        } catch (IllegalArgumentException invalid) {
            throw new IllegalArgumentException("Malformed media subject encoding", invalid);
        }
    }

    private static String value(String field, String prefix, boolean plain) {
        String value = raw(field, prefix);
        return plain ? value : decodeToken(value);
    }

    private static String raw(String field, String prefix) {
        if (!field.startsWith(prefix) || field.length() == prefix.length()) {
            throw new IllegalArgumentException("Missing media subject field");
        }
        return field.substring(prefix.length());
    }

    private static String token(String value) {
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private static String decodeToken(String token) {
        String decoded = new String(Base64.getUrlDecoder().decode(token), StandardCharsets.UTF_8);
        if (decoded.isBlank() || !token(decoded).equals(token)) {
            throw new IllegalArgumentException("Invalid media subject token");
        }
        return decoded;
    }

    record PersistedSubject(String sourceReference, Decision decision, String mediaAssociationId,
            String assetId, String assetRevision, String associationRevision, String variantId) {
        PersistedSubject {
            Objects.requireNonNull(sourceReference);
            Objects.requireNonNull(decision);
            Objects.requireNonNull(mediaAssociationId);
            Objects.requireNonNull(assetId);
            Objects.requireNonNull(assetRevision);
            Objects.requireNonNull(associationRevision);
        }

        String encode() {
            return String.join("|", "bprd-media-rights:v1", "source=" + token(sourceReference),
                    "decision=" + decision.name(), "media=" + token(mediaAssociationId),
                    "asset=" + token(assetId), "assetRevision=" + token(assetRevision),
                    "associationRevision=" + token(associationRevision),
                    "variant=" + (variantId == null ? "null" : token(variantId)));
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
