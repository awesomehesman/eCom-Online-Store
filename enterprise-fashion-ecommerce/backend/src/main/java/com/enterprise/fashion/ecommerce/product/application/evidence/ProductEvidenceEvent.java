package com.enterprise.fashion.ecommerce.product.application.evidence;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductRevision;

/**
 * Immutable retained claim, NOT an acceptance decision. Source/actor fields are source assertions.
 * recordedBy/recordedAt describe database recording only, never trusted Staff identity or authority.
 */
public record ProductEvidenceEvent(UUID id, ProductId productId, ProductRevision revision,
        Purpose purpose, Type type, String sourceReference, String sourceActorClaim,
        List<String> supportingReferences, String auditCorrelation, Instant recordedAt,
        String recordedBy, UUID target, UUID replacement) {
    public enum Purpose { CONTENT, MEDIA }
    public enum Type { RECORD, INVALIDATE, SUPERSEDE }

    public ProductEvidenceEvent {
        if (id == null || productId == null || revision == null || purpose == null || type == null
                || blank(sourceReference) || blank(sourceActorClaim) || auditCorrelation == null
                || !auditCorrelation.matches("[A-Za-z0-9._-]{1,128}") || recordedAt == null
                || !"PRODUCT_DATABASE".equals(recordedBy) || supportingReferences == null
                || supportingReferences.stream().anyMatch(ProductEvidenceEvent::blank)) {
            throw new IllegalArgumentException("Invalid Product evidence metadata");
        }
        supportingReferences = List.copyOf(supportingReferences);
        if ((type == Type.RECORD && (target != null || replacement != null))
                || (type == Type.INVALIDATE && (target == null || replacement != null))
                || (type == Type.SUPERSEDE && (target == null || replacement == null || target.equals(replacement)))
                || id.equals(target) || id.equals(replacement)) {
            throw new IllegalArgumentException("Invalid Product evidence history shape");
        }
    }

    private static boolean blank(String value) { return value == null || value.isBlank(); }

    @Override
    public String toString() { return "ProductEvidenceEvent[unconfirmed]"; }
}
