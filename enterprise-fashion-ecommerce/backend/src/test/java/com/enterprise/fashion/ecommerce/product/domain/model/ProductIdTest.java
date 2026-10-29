package com.enterprise.fashion.ecommerce.product.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class ProductIdTest {
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", " abc", "abc ", "a/b", "a.b", "é", "a\n", "a@b"})
    void rejectsInvalidOpaqueReferences(String input) {
        assertThatIllegalArgumentException().isThrownBy(() -> new ProductId(input));
    }

    @Test
    void retainsExactIdentityAndInclusiveWireBounds() {
        assertThat(new ProductId("A_a-1")).isEqualTo(new ProductId("A_a-1"));
        assertThat(new ProductId("A")).isNotEqualTo(new ProductId("a"));
        assertThat(new ProductId("a".repeat(128)).value()).hasSize(128);
        assertThatIllegalArgumentException().isThrownBy(() -> new ProductId("a".repeat(129)));
        assertThat(new ProductId("a").hashCode()).isEqualTo(new ProductId("a").hashCode());
        assertThat(new ProductId("a")).isNotEqualTo(new ProductVariantId("a"));
    }
}
