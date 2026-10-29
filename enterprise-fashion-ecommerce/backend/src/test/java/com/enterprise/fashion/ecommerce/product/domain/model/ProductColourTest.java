package com.enterprise.fashion.ecommerce.product.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.Locale;

import org.junit.jupiter.api.parallel.ResourceLock;

class ProductColourTest {
    @ParameterizedTest
    @ValueSource(strings = {"Black", "black", "BLACK", "bLaCk"})
    void canonicalizesApprovedCaseEquivalents(String value) {
        ProductColour colour = new ProductColour(value);
        assertThat(colour.code()).isEqualTo("BLACK");
        assertThat(colour.label()).isEqualTo("Black");
        assertThat(colour).isEqualTo(new ProductColour("BLACK"));
        assertThat(colour.hashCode()).isEqualTo(new ProductColour("black").hashCode());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" Black", "BLACK ", "NOIR", "#000000", "Blue", "ＢＬＡＣＫ"})
    void rejectsUnknownValuesAndUnapprovedAliases(String value) {
        assertThatIllegalArgumentException().isThrownBy(() -> new ProductColour(value));
    }

    @Test
    @ResourceLock("default-locale")
    void normalizationIsIndependentOfDefaultLocale() {
        Locale before = Locale.getDefault();
        try {
            Locale.setDefault(Locale.forLanguageTag("tr-TR"));
            assertThat(new ProductColour("black").code()).isEqualTo("BLACK");
            assertThat(ProductSize.apparel("xxxl").code()).isEqualTo("XXXL");
        } finally {
            Locale.setDefault(before);
        }
    }
}
