package com.enterprise.fashion.ecommerce.product.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;

class ProductSizeTest {
    @ParameterizedTest
    @ValueSource(strings = {"xs", "s", "m", "l", "xl", "xxl", "xxxl"})
    void canonicalizesEveryApprovedApparelSize(String token) {
        ProductSize size = ProductSize.apparel(token);
        assertThat(size).isEqualTo(ProductSize.apparel(token.toUpperCase(java.util.Locale.ROOT)));
        assertThat(size.family()).isEqualTo(ProductFamily.APPAREL);
    }

    @Test
    void supportsEveryWholeAndHalfUkSizeWithoutRounding() {
        for (int halves = 6; halves <= 26; halves++) {
            BigDecimal value = BigDecimal.valueOf(halves).divide(BigDecimal.valueOf(2));
            ProductSize size = ProductSize.footwear("UK", value);
            assertThat(new BigDecimal(size.code())).isEqualByComparingTo(value);
            assertThat(size).isEqualTo(ProductSize.footwear("UK", value.setScale(3)));
            assertThat(size.hashCode()).isEqualTo(ProductSize.footwear("UK", value.setScale(3)).hashCode());
            assertThat(size.label()).isEqualTo("UK " + size.code());
        }
        assertThat(ProductSize.oneSize().code()).isEqualTo("ONE_SIZE");
        assertThat(ProductSize.oneSize().label()).isEqualTo("One Size");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"XXXXL", "Small", " XS", "M ", "Ｍ", "3"})
    void rejectsUnsupportedApparelValues(String input) {
        assertThatIllegalArgumentException().isThrownBy(() -> ProductSize.apparel(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"2.5", "13.5", "3.25", "3.499999", "0", "-3", "3e0", "+3", "03", "3 "})
    void rejectsUnsupportedUkValuesAndRepresentations(String input) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new ProductSize(ProductFamily.FOOTWEAR, input));
    }

    @Test
    void rejectsUnsupportedSystemsFamiliesAccessoryExtensionsAndMissingValues() {
        assertThatIllegalArgumentException().isThrownBy(() -> ProductSize.footwear("EU", new BigDecimal("3")));
        assertThatIllegalArgumentException().isThrownBy(() -> ProductSize.footwear("uk", new BigDecimal("3")));
        assertThatIllegalArgumentException().isThrownBy(() -> ProductSize.footwear(null, BigDecimal.TEN));
        assertThatIllegalArgumentException().isThrownBy(() -> ProductSize.footwear("UK", null));
        assertThatIllegalArgumentException().isThrownBy(() -> new ProductSize(null, "M"));
        assertThatIllegalArgumentException().isThrownBy(() -> new ProductSize(ProductFamily.ACCESSORIES, null));
        assertThatIllegalArgumentException().isThrownBy(() -> new ProductSize(ProductFamily.ACCESSORIES, "Medium"));
        assertThatIllegalArgumentException().isThrownBy(() -> new ProductSize(ProductFamily.ACCESSORIES, "One Size"));
    }
}
