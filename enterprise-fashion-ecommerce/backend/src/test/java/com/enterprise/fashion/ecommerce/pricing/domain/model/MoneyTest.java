package com.enterprise.fashion.ecommerce.pricing.domain.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class MoneyTest {
    private Money zar(String value) { return new Money(new BigDecimal(value), Currency.ZAR); }

    @Test
    void additionAndSubtractionAreExactAndDoNotMutateOperands() {
        Money first = zar("0.10");
        Money second = zar("0.20");
        assertThat(first.add(second).amount()).isEqualTo(new BigDecimal("0.30"));
        assertThat(zar("0.30").subtract(first)).isEqualTo(second);
        assertThat(first.amount()).isEqualTo(new BigDecimal("0.10"));
        assertThat(second.amount()).isEqualTo(new BigDecimal("0.20"));
        assertThat(first.add(second).currency()).isEqualTo(Currency.ZAR);
    }

    @Test
    void retainsPrecisionWithoutImposingScaleOrRounding() {
        String value = "123456789012345678901234567890.123456789012345678901234567890";
        Money money = zar(value);
        assertThat(money.amount()).isEqualTo(new BigDecimal(value));
        assertThat(money.amount().scale()).isEqualTo(30);
        assertThat(money.add(zar("0.000000000000000000000000000001")).amount())
                .isEqualTo(new BigDecimal("123456789012345678901234567890.123456789012345678901234567891"));
        assertThat(zar("1E+10").amount().scale()).isEqualTo(-10);
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-0.000", "-10.125", "10.1234567"})
    void exactValuesDoNotSelectPriceSignOrMinorUnitPolicy(String value) {
        assertThat(zar(value).amount()).isEqualTo(new BigDecimal(value));
    }

    @Test
    void equalityAndHashingUseNumericValueAndExplicitCurrency() {
        var values = new HashSet<>(List.of(zar("1"), zar("1.0"), zar("1.00")));
        assertThat(values).hasSize(1);
        assertThat(zar("1")).isNotEqualTo(zar("1.01"));
        assertThat(zar("-0.00")).isEqualTo(zar("0"));
        assertThat(zar("-10.0").hashCode()).isEqualTo(zar("-10.00").hashCode());
        assertThat(zar("1")).isNotEqualTo(null).isNotEqualTo("1");
        assertThat(zar("1.00").amount().scale()).isEqualTo(2);
    }

    @Test
    void numericHashingPreservesValidExtremeScaleRepresentations() {
        Money first = new Money(new BigDecimal(BigInteger.TEN, Integer.MIN_VALUE), Currency.ZAR);
        Money second = new Money(new BigDecimal(BigInteger.valueOf(100), Integer.MIN_VALUE + 1), Currency.ZAR);
        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
        assertThat(first.amount().scale()).isEqualTo(Integer.MIN_VALUE);
    }

    @Test
    void rejectsNullAmountCurrencyAndOperandsWithoutDefaults() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Money(null, Currency.ZAR));
        assertThatIllegalArgumentException().isThrownBy(() -> new Money(BigDecimal.ONE, null));
        assertThatIllegalArgumentException().isThrownBy(() -> zar("1").add(null));
        assertThatIllegalArgumentException().isThrownBy(() -> zar("1").subtract(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"USD", "EUR"})
    void unsupportedCurrencyCannotEnterArithmetic(String code) {
        // There is no second supported currency: reject mismatch at construction, rather
        // than inventing an additional currency or bypassing Currency invariants for a test.
        assertThatIllegalArgumentException().isThrownBy(() -> zar("1").add(new Money(BigDecimal.ONE, new Currency(code))));
        assertThatIllegalArgumentException().isThrownBy(() -> zar("1").subtract(new Money(BigDecimal.ONE, new Currency(code))));
    }

    @Test
    void equivalentCurrencyInstancesAreCompatible() {
        assertThat(zar("1").add(new Money(BigDecimal.ONE, new Currency("ZAR")))).isEqualTo(zar("2"));
    }

    @Test
    void comparisonOrdersExactValuesWithoutRoundingOrMutatingOperands() {
        Money lower = zar("12345678901234567890.12345678901234567890");
        Money higher = zar("12345678901234567890.12345678901234567891");
        assertThat(lower.compareTo(higher)).isNegative();
        assertThat(higher.compareTo(lower)).isPositive();
        assertThat(zar("-1").compareTo(zar("0"))).isNegative();
        assertThat(lower.amount()).isEqualTo(new BigDecimal("12345678901234567890.12345678901234567890"));
        assertThat(higher.amount()).isEqualTo(new BigDecimal("12345678901234567890.12345678901234567891"));
    }

    @Test
    void comparisonIsConsistentWithNumericEqualityAndEquivalentCurrencies() {
        Money first = zar("1.0");
        Money second = new Money(new BigDecimal("1.00"), new Currency("ZAR"));
        assertThat(first.compareTo(second)).isZero();
        assertThat(first).isEqualTo(second);
        assertThat(zar("-0.00").compareTo(zar("0"))).isZero();
        assertThat(zar("1E+10").compareTo(zar("10000000000.00"))).isZero();
    }

    @Test
    void comparisonRejectsMissingOperandAndUnsupportedCurrency() {
        assertThatIllegalArgumentException().isThrownBy(() -> zar("1").compareTo(null));
        // Unsupported currencies fail at construction; no invalid Currency is fabricated.
        assertThatIllegalArgumentException().isThrownBy(
                () -> zar("1").compareTo(new Money(BigDecimal.ONE, new Currency("USD"))));
    }
}
