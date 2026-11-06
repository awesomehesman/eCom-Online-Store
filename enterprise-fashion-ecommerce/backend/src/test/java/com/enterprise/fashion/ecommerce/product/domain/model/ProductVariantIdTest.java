package com.enterprise.fashion.ecommerce.product.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class ProductVariantIdTest {
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", " abc", "abc ", "a/b", "a.b", "é", "a\n", "a@b"})
    void rejectsInvalidOpaqueReferences(String input) {
        assertThatIllegalArgumentException().isThrownBy(() -> new ProductVariantId(input));
    }

    @Test
    void retainsExactIdentityAndInclusiveWireBounds() {
        assertThat(new ProductVariantId("A_a-1")).isEqualTo(new ProductVariantId("A_a-1"));
        assertThat(new ProductVariantId("A")).isNotEqualTo(new ProductVariantId("a"));
        assertThat(new ProductVariantId("a".repeat(128)).value()).hasSize(128);
        assertThatIllegalArgumentException().isThrownBy(() -> new ProductVariantId("a".repeat(129)));
        assertThat(new ProductVariantId("a").hashCode()).isEqualTo(new ProductVariantId("a").hashCode());
        assertThat(new ProductId("a")).isNotEqualTo(new ProductVariantId("a"));
    }
}
