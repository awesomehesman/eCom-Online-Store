package com.enterprise.fashion.ecommerce.product.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class ProductAttributeStandardTest {
    @Test
    void preservesTheAcceptedVersion() {
        assertThat(new ProductAttributeStandard("1.0.0")).isEqualTo(ProductAttributeStandard.INITIAL);
        assertThat(ProductAttributeStandard.INITIAL.version()).isEqualTo("1.0.0");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"1", "1.0", "1.0.1", "2.0.0", " 1.0.0"})
    void rejectsUnsupportedVersionsWithoutFallback(String input) {
        assertThatIllegalArgumentException().isThrownBy(() -> new ProductAttributeStandard(input));
    }
}
