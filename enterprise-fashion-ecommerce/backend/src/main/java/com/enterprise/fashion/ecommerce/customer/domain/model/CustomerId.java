package com.enterprise.fashion.ecommerce.customer.domain.model;

import java.util.Objects;
import java.util.UUID;

/** Stable opaque Customer reference within the Customer domain. */
public record CustomerId(UUID value) {
    public CustomerId {
        Objects.requireNonNull(value, "Customer ID is required");
    }

    @Override
    public String toString() {
        return "CustomerId[redacted]";
    }
}
