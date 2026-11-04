package com.enterprise.fashion.ecommerce.product.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductVariantTest {
    private VariantConfiguration configuration(String size) {
        return new VariantConfiguration(ProductFamily.APPAREL, ProductAttributeStandard.INITIAL,
                true, false, ProductSize.apparel(size), null);
    }

    @Test
    void stableEntityEqualityDoesNotDependOnMutableDescriptionsOrConfigurationValues() {
        var id = new ProductVariantId("v1");
        var owner = new ProductId("p1");
        var first = new ProductVariant(id, owner, configuration("M"), "Regular", "Cotton");
        var otherSnapshot = new ProductVariant(id, owner, configuration("L"), "Relaxed", "Linen");
        assertThat(first).isEqualTo(otherSnapshot);
        assertThat(first.hashCode()).isEqualTo(otherSnapshot.hashCode());
        assertThat(first.id()).isEqualTo(id);
        assertThat(first.productId()).isEqualTo(owner);
        assertThat(first.configuration().standard()).isEqualTo(ProductAttributeStandard.INITIAL);
        assertThat(first.fit()).isEqualTo("Regular");
        assertThat(first.material()).isEqualTo("Cotton");
        assertThat(first).isNotEqualTo(new ProductVariant(new ProductVariantId("v2"), owner, configuration("M"), null, null));
        assertThat(first).isNotEqualTo(new ProductVariant(id, new ProductId("p2"), configuration("M"), null, null));
    }

    @Test
    void requiresIdentityExactlyOneOwnerAndCanonicalConfiguration() {
        assertThatIllegalArgumentException().isThrownBy(() -> new ProductVariant(null, new ProductId("p"), configuration("M"), null, null));
        assertThatIllegalArgumentException().isThrownBy(() -> new ProductVariant(new ProductVariantId("v"), null, configuration("M"), null, null));
        assertThatIllegalArgumentException().isThrownBy(() -> new ProductVariant(new ProductVariantId("v"), new ProductId("p"), null, null, null));
    }
}
