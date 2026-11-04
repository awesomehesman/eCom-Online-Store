package com.enterprise.fashion.ecommerce.product.application.evidence;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductRevision;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductEvidenceObservationTest {
    private final ProductId id = new ProductId("p1");
    private final ProductRevision revision = new ProductRevision("v1_" + "a".repeat(64));
    private final ProductRevision other = new ProductRevision("v1_" + "b".repeat(64));

    private ProductEvidenceEvent event(ProductId owner, ProductRevision rev, ProductEvidenceEvent.Type type, UUID target, UUID replacement) {
        return new ProductEvidenceEvent(UUID.randomUUID(), owner, rev, ProductEvidenceEvent.Purpose.CONTENT,
                type, "source-reference", "unverified-source-actor", List.of("support-reference"), "correlation-1",
                Instant.parse("2026-10-30T00:00:00Z"), "PRODUCT_DATABASE", target, replacement);
    }

    private ProductEvidenceObservation observe(List<ProductEvidenceEvent> history) {
        return ProductEvidenceObservation.inspect(id, revision, ProductEvidenceEvent.Purpose.CONTENT, history);
    }

    @Test
    void missingAndCurrentClaimsNeverBecomeAcceptance() {
        assertThat(observe(List.of()).state()).isEqualTo(ProductEvidenceObservation.State.MISSING);
        var record = event(id, revision, ProductEvidenceEvent.Type.RECORD, null, null);
        assertThat(observe(List.of(record)).state()).isEqualTo(ProductEvidenceObservation.State.CURRENT_UNCONFIRMED);
        assertThat(record.toString()).doesNotContain("source-reference", "source-actor");
    }

    @Test
    void oldRevisionIsStaleAndMultipleCurrentRecordsConflict() {
        var old = event(id, other, ProductEvidenceEvent.Type.RECORD, null, null);
        assertThat(observe(List.of(old)).state()).isEqualTo(ProductEvidenceObservation.State.STALE);
        var a = event(id, revision, ProductEvidenceEvent.Type.RECORD, null, null);
        var b = event(id, revision, ProductEvidenceEvent.Type.RECORD, null, null);
        assertThat(observe(List.of(a, b)).state()).isEqualTo(ProductEvidenceObservation.State.CONFLICTING);
    }

    @Test
    void invalidationHistorySurvivesReturningToSameRepresentation() {
        var record = event(id, revision, ProductEvidenceEvent.Type.RECORD, null, null);
        var invalidation = event(id, revision, ProductEvidenceEvent.Type.INVALIDATE, record.id(), null);
        var observation = observe(List.of(record, invalidation));
        assertThat(observation.state()).isEqualTo(ProductEvidenceObservation.State.INVALIDATED);
        assertThat(observation.history()).containsExactly(record, invalidation);
        assertThatThrownBy(() -> observation.history().clear()).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void supersessionPreservesOldRecordWithoutGrantingNewAuthority() {
        var old = event(id, other, ProductEvidenceEvent.Type.RECORD, null, null);
        var replacement = event(id, revision, ProductEvidenceEvent.Type.RECORD, null, null);
        var change = event(id, other, ProductEvidenceEvent.Type.SUPERSEDE, old.id(), replacement.id());
        assertThat(observe(List.of(old, replacement, change)).state()).isEqualTo(ProductEvidenceObservation.State.CURRENT_UNCONFIRMED);
        var contradictory = event(id, other, ProductEvidenceEvent.Type.INVALIDATE, old.id(), null);
        assertThat(observe(List.of(old, replacement, change, contradictory)).state()).isEqualTo(ProductEvidenceObservation.State.CONFLICTING);
    }

    @Test
    void rejectsCrossProductSubstitutionAndIncompleteHistory() {
        var foreign = event(new ProductId("other"), revision, ProductEvidenceEvent.Type.RECORD, null, null);
        assertThatThrownBy(() -> observe(List.of(foreign))).isInstanceOf(IllegalStateException.class);
        var missing = event(id, revision, ProductEvidenceEvent.Type.INVALIDATE, UUID.randomUUID(), null);
        assertThatThrownBy(() -> observe(List.of(missing))).isInstanceOf(IllegalStateException.class);
    }
}
