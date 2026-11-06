package com.enterprise.fashion.ecommerce.product.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class ProductFamilyTest {
    @Test
    void supportsOnlyInitialProductContexts() {
        assertThat(new ProductFamily("Apparel")).isEqualTo(ProductFamily.APPAREL);
        assertThat(new ProductFamily("Footwear")).isEqualTo(ProductFamily.FOOTWEAR);
        assertThat(new ProductFamily("Accessories")).isEqualTo(ProductFamily.ACCESSORIES);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"apparel", "Category", "Children", " Apparel"})
    void rejectsUnknownContextsRatherThanInferringTaxonomy(String input) {
        assertThatIllegalArgumentException().isThrownBy(() -> new ProductFamily(input));
    }
}
