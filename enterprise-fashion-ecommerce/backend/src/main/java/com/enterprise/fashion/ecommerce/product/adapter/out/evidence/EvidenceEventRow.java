package com.enterprise.fashion.ecommerce.product.adapter.out.evidence;

import java.time.Instant;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "evidence_event", schema = "product")
record EvidenceEventRow(@Id UUID eventId, String productId, String representationRevision,
        String purpose, String eventType, String sourceReference, String sourceActorClaim,
        String[] supportingReferences, String auditCorrelation,
        @ReadOnlyProperty Instant recordedAt, @ReadOnlyProperty String recordedBy,
        UUID targetEventId, UUID replacementEventId) {
    @Override
    public String toString() { return "EvidenceEventRow[internal]"; }
}
