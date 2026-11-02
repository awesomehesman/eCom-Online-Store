package com.enterprise.fashion.ecommerce.pricing.domain.model;

/** Explicit governed currency context; no aliases, locale interpretation or conversion. */
public record Currency(String code) {
    public static final Currency ZAR = new Currency("ZAR");

    public Currency {
        if (!"ZAR".equals(code)) {
            throw new IllegalArgumentException("Unsupported Pricing currency");
        }
    }
}
