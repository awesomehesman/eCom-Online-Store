package com.enterprise.fashion.ecommerce.product.application.evidence;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductRevision;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariantId;

/**
 * Immutable evidence shapes returned by a trusted Product application port.
 * Construction validates shape only: it does not make caller-supplied values trusted.
 */
public final class ProductPublicationEvidence {
    private ProductPublicationEvidence() {}

    public enum Availability { AVAILABLE, UNAVAILABLE, CONFLICTING }

    public enum Result { ACCEPTED, REJECTED }

    public enum Validity { CURRENT, INVALIDATED, REVOKED }

    public enum InvalidationKind { INVALIDATED, WITHDRAWN }

    public record Source(String authority, String definition, String version) {
        public Source {
            authority = token(authority, "evidence authority");
            definition = token(definition, "evidence definition");
            version = token(version, "evidence version");
        }
    }

    public record MediaSubject(String mediaAssociationId, String assetId, String assetRevision,
            ProductRevision associationRevision, ProductVariantId variantId) {
        public MediaSubject {
            mediaAssociationId = token(mediaAssociationId, "media association identity");
            assetId = token(assetId, "media asset identity");
            assetRevision = token(assetRevision, "media asset revision");
            Objects.requireNonNull(associationRevision, "associationRevision");
        }
    }

    public record ContentAcceptance(UUID evidenceId, ProductId productId, ProductRevision productRevision,
            Source source, Result result, Validity validity) {
        public ContentAcceptance {
            requireCommon(evidenceId, productId, productRevision, source, result, validity);
        }
    }

    public record MediaUsageRightsAcceptance(UUID evidenceId, ProductId productId,
            ProductRevision productRevision, MediaSubject subject, Source source, Result result,
            Validity validity) {
        public MediaUsageRightsAcceptance {
            requireCommon(evidenceId, productId, productRevision, source, result, validity);
            Objects.requireNonNull(subject, "subject");
        }
    }

    public record MediaSecurityCheck(UUID evidenceId, ProductId productId, ProductRevision productRevision,
            MediaSubject subject, Source source, Result result, Validity validity) {
        public MediaSecurityCheck {
            requireCommon(evidenceId, productId, productRevision, source, result, validity);
            Objects.requireNonNull(subject, "subject");
        }
    }

    public record MediaAssociationAcceptance(UUID evidenceId, ProductId productId,
            ProductRevision productRevision, MediaSubject subject, Source source, Result result,
            Validity validity) {
        public MediaAssociationAcceptance {
            requireCommon(evidenceId, productId, productRevision, source, result, validity);
            Objects.requireNonNull(subject, "subject");
        }
    }

    public record Invalidation(UUID invalidationId, UUID targetEvidenceId, ProductId productId,
            ProductRevision productRevision, Source source, InvalidationKind kind) {
        public Invalidation {
            Objects.requireNonNull(invalidationId, "invalidationId");
            Objects.requireNonNull(targetEvidenceId, "targetEvidenceId");
            Objects.requireNonNull(productId, "productId");
            Objects.requireNonNull(productRevision, "productRevision");
            Objects.requireNonNull(source, "source");
            Objects.requireNonNull(kind, "kind");
        }
    }

    public record Snapshot(Availability availability, List<ContentAcceptance> contentAcceptances,
            List<MediaUsageRightsAcceptance> usageRightsAcceptances,
            List<MediaSecurityCheck> securityChecks,
            List<MediaAssociationAcceptance> associationAcceptances,
            List<Invalidation> invalidations) {
        public Snapshot {
            Objects.requireNonNull(availability, "availability");
            contentAcceptances = List.copyOf(contentAcceptances);
            usageRightsAcceptances = List.copyOf(usageRightsAcceptances);
            securityChecks = List.copyOf(securityChecks);
            associationAcceptances = List.copyOf(associationAcceptances);
            invalidations = List.copyOf(invalidations);
        }

        public static Snapshot unavailable() {
            return new Snapshot(Availability.UNAVAILABLE, List.of(), List.of(), List.of(), List.of(), List.of());
        }
    }

    private static void requireCommon(UUID evidenceId, ProductId productId, ProductRevision productRevision,
            Source source, Result result, Validity validity) {
        Objects.requireNonNull(evidenceId, "evidenceId");
        Objects.requireNonNull(productId, "productId");
        Objects.requireNonNull(productRevision, "productRevision");
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(result, "result");
        Objects.requireNonNull(validity, "validity");
    }

    private static String token(String value, String label) {
        if (value == null || !value.matches("[A-Za-z0-9][A-Za-z0-9._:/-]{0,255}")) {
            throw new IllegalArgumentException("Invalid " + label);
        }
        return value;
    }
}
