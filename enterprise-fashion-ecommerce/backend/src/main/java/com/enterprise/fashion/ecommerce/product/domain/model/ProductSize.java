package com.enterprise.fashion.ecommerce.product.domain.model;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Set;

/** Canonical Size under standard 1.0.0; Product-level applicability is a separate decision. */
public record ProductSize(ProductFamily family, String code) {
    private static final Set<String> APPAREL_CODES = Set.of("XS", "S", "M", "L", "XL", "XXL", "XXXL");

    public ProductSize {
        if (family == null || code == null) {
            throw invalid();
        }
        if (family.equals(ProductFamily.APPAREL)) {
            if (!code.matches("[A-Za-z]+")) {
                throw invalid();
            }
            code = code.toUpperCase(Locale.ROOT);
            if (!APPAREL_CODES.contains(code)) {
                throw invalid();
            }
        } else if (family.equals(ProductFamily.FOOTWEAR)) {
            // Exact numeric representations only: no exponent, signs, whitespace or system conversion.
            if (!code.matches("(?:[3-9]|1[0-3])(?:\\.[0-9]+)?")) {
                throw invalid();
            }
            BigDecimal value = new BigDecimal(code);
            if (value.compareTo(BigDecimal.valueOf(3)) < 0
                    || value.compareTo(BigDecimal.valueOf(13)) > 0
                    || value.remainder(new BigDecimal("0.5")).signum() != 0) {
                throw invalid();
            }
            code = value.stripTrailingZeros().toPlainString();
        } else if (!"ONE_SIZE".equals(code)) {
            throw invalid();
        }
    }

    public static ProductSize apparel(String token) {
        return new ProductSize(ProductFamily.APPAREL, token);
    }

    /** Half-size support must additionally be confirmed by the owning Product when applicable. */
    public static ProductSize footwear(String system, BigDecimal value) {
        if (!"UK".equals(system) || value == null) {
            throw invalid();
        }
        return new ProductSize(ProductFamily.FOOTWEAR, value.toPlainString());
    }

    public static ProductSize oneSize() {
        return new ProductSize(ProductFamily.ACCESSORIES, "ONE_SIZE");
    }

    public String label() {
        if (family.equals(ProductFamily.FOOTWEAR)) {
            return "UK " + code;
        }
        return family.equals(ProductFamily.ACCESSORIES) ? "One Size" : code;
    }

    private static IllegalArgumentException invalid() {
        return new IllegalArgumentException("Unsupported Product Size");
    }
}
