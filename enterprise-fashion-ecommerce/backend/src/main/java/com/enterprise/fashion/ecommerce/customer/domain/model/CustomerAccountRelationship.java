package com.enterprise.fashion.ecommerce.customer.domain.model;

import java.util.Objects;

/** Identifies a Customer-owned relationship. Construction supplies no access or lifecycle evidence. */
public record CustomerAccountRelationship(CustomerId customerId, AccountId accountId) {
    public CustomerAccountRelationship {
        Objects.requireNonNull(customerId, "Customer ID is required");
        Objects.requireNonNull(accountId, "Account ID is required");
    }
}
