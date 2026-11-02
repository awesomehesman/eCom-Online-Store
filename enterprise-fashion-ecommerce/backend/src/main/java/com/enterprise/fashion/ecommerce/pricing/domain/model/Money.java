package com.enterprise.fashion.ecommerce.pricing.domain.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

/**
 * Exact monetary value under REQ-PRC-006 / BPRC-REQ-018, not a Price or commercial decision.
 * Retains supplied decimal precision/scale. No implicit rounding, conversion or sign policy.
 * Equality uses numeric amount and currency: 1.0 and 1.00 represent equal monetary values.
 */
public record Money(BigDecimal amount, Currency currency) {
    public Money {
        if (amount == null || currency == null) {
            throw new IllegalArgumentException("Money requires an amount and explicit currency");
        }
    }

    public Money add(Money other) {
        requireSameCurrency(other);
        return new Money(amount.add(other.amount), currency);
    }

    public Money subtract(Money other) {
        requireSameCurrency(other);
        return new Money(amount.subtract(other.amount), currency);
    }

    private void requireSameCurrency(Money other) {
        if (other == null || !currency.equals(other.currency)) {
            throw new IllegalArgumentException("Money arithmetic requires the same currency");
        }
    }

    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof Money money
                && currency.equals(money.currency) && amount.compareTo(money.amount) == 0;
    }

    @Override
    public int hashCode() {
        // Numeric hashing without changing the stored representation. A long scale also
        // avoids stripTrailingZeros() overflowing at BigDecimal's minimum integer scale.
        BigInteger coefficient = amount.unscaledValue();
        long scale = amount.scale();
        if (coefficient.signum() == 0) { scale = 0; }
        else {
            while (coefficient.remainder(BigInteger.TEN).signum() == 0) {
                coefficient = coefficient.divide(BigInteger.TEN);
                scale--;
            }
        }
        return Objects.hash(coefficient, scale, currency);
    }
}
