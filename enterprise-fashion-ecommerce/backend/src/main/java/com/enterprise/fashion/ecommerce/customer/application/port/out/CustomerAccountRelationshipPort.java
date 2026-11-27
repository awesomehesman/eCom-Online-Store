package com.enterprise.fashion.ecommerce.customer.application.port.out;

import com.enterprise.fashion.ecommerce.customer.domain.model.CustomerAccountRelationship;

/** Owner-local storage for an exact relationship pair. Supplies no lifecycle or access authority. */
public interface CustomerAccountRelationshipPort {
    /**
     * Reports the statement outcome. With an enclosing transaction, the receipt
     * does not establish committed durability. On UNAVAILABLE, the transaction
     * owner must roll back rather than assume the transaction can continue or commit.
     */
    WriteOutcome record(CustomerAccountRelationship relationship);

    /**
     * Present inside an enclosing transaction may reflect uncommitted state.
     * Independent durability requires a successful commit and separate observation.
     * UNAVAILABLE inside an enclosing transaction requires the owner to roll back.
     */
    Observation observe(CustomerAccountRelationship relationship);

    enum WriteOutcome { INSERTED, ALREADY_RECORDED, UNAVAILABLE }

    sealed interface Observation {
        record Present(CustomerAccountRelationship relationship) implements Observation {}
        enum Status implements Observation { ABSENT, UNAVAILABLE }
    }
}
