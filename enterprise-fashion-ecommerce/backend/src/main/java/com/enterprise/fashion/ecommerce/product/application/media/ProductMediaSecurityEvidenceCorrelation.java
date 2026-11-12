package com.enterprise.fashion.ecommerce.product.application.media;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductRevision;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariantId;

/** Read-only correlation of technical evidence. It is never authoritative acceptance or authorization. */
public record ProductMediaSecurityEvidenceCorrelation(Subject subject, Outcome outcome,
        TechnicalStatus technicalStatus, MalwareStatus malwareStatus,
        List<ProductMediaSecurityCheckResult> technicalObservations,
        List<ProductImageMalwareObservation> malwareObservations,
        boolean authoritativeAcceptanceEligible) {
    public ProductMediaSecurityEvidenceCorrelation {
        Objects.requireNonNull(subject, "subject");
        Objects.requireNonNull(outcome, "outcome");
        Objects.requireNonNull(technicalStatus, "technicalStatus");
        Objects.requireNonNull(malwareStatus, "malwareStatus");
        technicalObservations = List.copyOf(technicalObservations);
        malwareObservations = List.copyOf(malwareObservations);
        if (authoritativeAcceptanceEligible) {
            throw new IllegalArgumentException("Technical evidence correlation cannot establish acceptance");
        }
    }

    public record Subject(ProductId productId, ProductRevision productRevision,
            String mediaAssociationId, ProductRevision associationRevision, UUID assetId,
            String originalSha256, String assetRevision, ProductVariantId variantId) {
        public Subject {
            Objects.requireNonNull(productId, "productId");
            Objects.requireNonNull(productRevision, "productRevision");
            Objects.requireNonNull(associationRevision, "associationRevision");
            Objects.requireNonNull(assetId, "assetId");
            if (mediaAssociationId == null || !mediaAssociationId.matches("[A-Za-z0-9_-]{1,128}")) {
                throw new IllegalArgumentException("Invalid media association identity");
            }
            if (originalSha256 == null || !originalSha256.matches("[0-9a-f]{64}")) {
                throw new IllegalArgumentException("Invalid original digest");
            }
            if (assetRevision == null || !assetRevision.matches("sha256_[0-9a-f]{64}")) {
                throw new IllegalArgumentException("Invalid asset revision");
            }
        }
    }

    public enum Outcome {
        MATCHING_NON_AUTHORITATIVE,
        MISSING,
        CONFLICTING,
        STALE_OR_INVALIDATED,
        INFECTED,
        INDETERMINATE,
        UNAVAILABLE
    }

    public enum TechnicalStatus { MATCHING, MISSING, CONFLICTING, UNUSABLE, UNAVAILABLE }

    public enum MalwareStatus {
        CLEAN,
        INFECTED,
        INDETERMINATE,
        MISSING,
        CONFLICTING,
        STALE_OR_UNUSABLE,
        UNAVAILABLE
    }
}
