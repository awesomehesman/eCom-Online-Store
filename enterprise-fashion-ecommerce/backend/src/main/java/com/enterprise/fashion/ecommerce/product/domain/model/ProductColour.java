package com.enterprise.fashion.ecommerce.product.domain.model;

import java.util.Locale;

/** Canonical defining value; display labels do not establish Variant identity. */
public record ProductColour(String code) {
    public ProductColour {
        if (code == null || !code.matches("[Bb][Ll][Aa][Cc][Kk]")) {
            throw new IllegalArgumentException("Unsupported Product Colour");
        }
        code = code.toUpperCase(Locale.ROOT);
    }

    public String label() {
        return "Black";
    }
}
