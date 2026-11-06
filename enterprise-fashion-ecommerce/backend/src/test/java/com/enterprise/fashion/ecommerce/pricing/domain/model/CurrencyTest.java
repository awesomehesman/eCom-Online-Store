package com.enterprise.fashion.ecommerce.pricing.domain.model;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.parallel.ResourceLock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class CurrencyTest {
    @Test
    void explicitZarHasImmutableValueSemantics() {
        var currency = new Currency("ZAR");
        assertThat(currency.code()).isEqualTo("ZAR");
        assertThat(currency).isEqualTo(Currency.ZAR);
        assertThat(currency.hashCode()).isEqualTo(Currency.ZAR.hashCode());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"USD", "EUR", "zar", "Zar", " ZAR", "ZAR ", "R", "ＺＡＲ"})
    void rejectsUnsupportedMissingAndUnapprovedCurrencyRepresentations(String value) {
        assertThatIllegalArgumentException().isThrownBy(() -> new Currency(value));
    }

    @Test
    @ResourceLock("default-locale")
    void interpretationIsLocaleIndependent() {
        Locale original = Locale.getDefault();
        try {
            Locale.setDefault(Locale.forLanguageTag("tr-TR"));
            assertThat(new Currency("ZAR")).isEqualTo(Currency.ZAR);
            assertThatIllegalArgumentException().isThrownBy(() -> new Currency("zar"));
        } finally {
            Locale.setDefault(original);
        }
    }
}
