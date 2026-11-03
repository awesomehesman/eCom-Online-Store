package com.enterprise.fashion.ecommerce.pricing.application.query;

import java.util.Objects;
import com.enterprise.fashion.ecommerce.pricing.application.persistence.BasePricePersistenceService;
import com.enterprise.fashion.ecommerce.pricing.domain.model.SelectVariantBasePrice;
import com.enterprise.fashion.ecommerce.pricing.domain.model.VariantBasePriceAssignment.Subject;

/**
 * Internal conditional Pricing observation under DEC-0043 / BPRC-REQ-019.
 * Selected means consistency of stored Pricing data only, not a commercially
 * authoritative Price. Product membership, publication, freshness, authorized commitment,
 * Inventory availability and sellability are not established by this query.
 */
public final class ObserveVariantBasePrice {
    private final BasePricePersistenceService persistence;
    private final SelectVariantBasePrice selector = new SelectVariantBasePrice();

    public ObserveVariantBasePrice(BasePricePersistenceService persistence) {
        this.persistence = Objects.requireNonNull(persistence);
    }

    /**
     * Loads one immutable read-only REPEATABLE READ snapshot via the existing service,
     * then selects entirely in memory. Missing means a confirmed empty observation;
     * corruption, database and transaction failures propagate rather than become Missing.
     */
    public SelectVariantBasePrice.Result observe(Subject subject) {
        if (subject == null) { throw new IllegalArgumentException("Explicit Product/Variant subject required"); }
        return selector.select(subject, persistence.load(subject));
    }
}
