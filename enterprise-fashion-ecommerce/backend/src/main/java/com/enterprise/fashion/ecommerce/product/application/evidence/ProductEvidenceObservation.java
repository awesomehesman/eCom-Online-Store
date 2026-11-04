package com.enterprise.fashion.ecommerce.product.application.evidence;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductRevision;

/** Internal structural diagnostics only. Even CURRENT_UNCONFIRMED grants no acceptance or visibility. */
public record ProductEvidenceObservation(State state, ProductRevision currentRevision, List<ProductEvidenceEvent> history) {
    public enum State { PRODUCT_NOT_FOUND, MISSING, STALE, CONFLICTING, INVALIDATED, CURRENT_UNCONFIRMED }

    public ProductEvidenceObservation {
        if (state == null || history == null || (state != State.PRODUCT_NOT_FOUND && currentRevision == null)) {
            throw new IllegalArgumentException("Invalid Product evidence observation");
        }
        history = List.copyOf(history);
    }

    public static ProductEvidenceObservation inspect(ProductId product, ProductRevision revision,
            ProductEvidenceEvent.Purpose purpose, List<ProductEvidenceEvent> history) {
        var records = new HashMap<UUID, ProductEvidenceEvent>();
        for (ProductEvidenceEvent event : history) {
            if (!product.equals(event.productId()) || purpose != event.purpose()) {
                throw new IllegalStateException("Evidence Product/purpose binding mismatch");
            }
            if (event.type() == ProductEvidenceEvent.Type.RECORD && records.put(event.id(), event) != null) {
                throw new IllegalStateException("Duplicate evidence identity");
            }
        }
        var removed = new HashSet<UUID>();
        var targets = new HashSet<UUID>();
        boolean conflicting = false;
        for (ProductEvidenceEvent event : history) {
            if (event.target() != null) {
                ProductEvidenceEvent target = records.get(event.target());
                if (target == null || !target.revision().equals(event.revision())
                        || (event.replacement() != null && !records.containsKey(event.replacement()))) {
                    throw new IllegalStateException("Incomplete evidence history");
                }
                if (!targets.add(event.target())) {
                    conflicting = true;
                }
                removed.add(event.target());
            }
        }
        var live = records.values().stream().filter(e -> !removed.contains(e.id())).toList();
        long current = live.stream().filter(e -> revision.equals(e.revision())).count();
        State state = conflicting || current > 1 ? State.CONFLICTING
                : current == 1 ? State.CURRENT_UNCONFIRMED
                : !live.isEmpty() ? State.STALE : !records.isEmpty() ? State.INVALIDATED : State.MISSING;
        return new ProductEvidenceObservation(state, revision, history);
    }

    @Override
    public String toString() { return "ProductEvidenceObservation[internal]"; }
}
