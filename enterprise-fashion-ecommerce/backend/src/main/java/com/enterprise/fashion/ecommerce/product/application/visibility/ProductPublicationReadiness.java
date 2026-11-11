package com.enterprise.fashion.ecommerce.product.application.visibility;

import java.util.Set;

/** Immutable, non-authorizing assessment. A READY result does not publish or grant visibility. */
public record ProductPublicationReadiness(Outcome outcome, Set<Reason> reasons) {
    public ProductPublicationReadiness {
        if (outcome == null || reasons == null || (outcome == Outcome.READY) != reasons.isEmpty()) {
            throw new IllegalArgumentException("Publication readiness outcome and reasons are inconsistent");
        }
        reasons = Set.copyOf(reasons);
    }

    public enum Outcome { READY, DENIED, EVIDENCE_UNAVAILABLE, CONFLICTING_EVIDENCE }

    public enum Reason {
        PRODUCT_REPRESENTATION_INCOMPLETE,
        PRODUCT_LIFECYCLE_NOT_ELIGIBLE,
        PRODUCT_LEVEL_IMAGE_REQUIRED,
        CONTENT_ACCEPTANCE_MISSING_OR_STALE,
        MEDIA_ASSOCIATION_EVIDENCE_MISSING_OR_STALE,
        MEDIA_USAGE_RIGHTS_MISSING_OR_STALE,
        MEDIA_SECURITY_EVIDENCE_MISSING_OR_STALE,
        EVIDENCE_SOURCE_UNSUPPORTED,
        EVIDENCE_PROVIDER_UNAVAILABLE,
        EVIDENCE_INVALIDATED_OR_WITHDRAWN,
        EVIDENCE_CONFLICT
    }

    public static ProductPublicationReadiness ready() {
        return new ProductPublicationReadiness(Outcome.READY, Set.of());
    }

    public static ProductPublicationReadiness denied(Set<Reason> reasons) {
        return new ProductPublicationReadiness(Outcome.DENIED, reasons);
    }
}
