package com.enterprise.fashion.ecommerce.customer.domain.model;

import java.util.Objects;
import java.util.UUID;

/** Stable opaque Account reference within the Customer domain. */
public record AccountId(UUID value) {
    public AccountId {
        Objects.requireNonNull(value, "Account ID is required");
    }

    @Override
    public String toString() {
        return "AccountId[redacted]";
    }
}
