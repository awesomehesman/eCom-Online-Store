package com.enterprise.fashion.ecommerce.product.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class VariantConfigurationTest {
    private static final ProductAttributeStandard STANDARD = ProductAttributeStandard.INITIAL;

    @Test
    void accessoriesSupportAllFourExplicitApplicabilityCombinations() {
        for (boolean sized : new boolean[]{false, true}) {
            for (boolean coloured : new boolean[]{false, true}) {
                var value = new VariantConfiguration(ProductFamily.ACCESSORIES, STANDARD, sized, coloured,
                        sized ? ProductSize.oneSize() : null, coloured ? new ProductColour("Black") : null);
                assertThat(value.sizeApplicable()).isEqualTo(sized);
                assertThat(value.colourApplicable()).isEqualTo(coloured);
                assertThat(value.standard().version()).isEqualTo("1.0.0");
            }
        }
    }

    @Test
    void canonicalValuesDefineEqualityWithinFamilyAndStandard() {
        var first = new VariantConfiguration(ProductFamily.APPAREL, STANDARD, true, true,
                ProductSize.apparel("m"), new ProductColour("black"));
        var second = new VariantConfiguration(new ProductFamily("Apparel"),
                new ProductAttributeStandard("1.0.0"), true, true,
                ProductSize.apparel("M"), new ProductColour("BLACK"));
        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
        assertThat(first).isNotEqualTo(new VariantConfiguration(ProductFamily.APPAREL, STANDARD, true, true,
                ProductSize.apparel("L"), new ProductColour("BLACK")));
        assertThat(new VariantConfiguration(ProductFamily.APPAREL, STANDARD, false, false, null, null))
                .isNotEqualTo(new VariantConfiguration(ProductFamily.ACCESSORIES, STANDARD, false, false, null, null));
    }

    @Test
    void doesNotInventUniversalSizeAndColourRequirements() {
        for (ProductFamily family : new ProductFamily[]{ProductFamily.APPAREL, ProductFamily.FOOTWEAR}) {
            var value = new VariantConfiguration(family, STANDARD, false, true, null, new ProductColour("BLACK"));
            assertThat(value.sizeApplicable()).isFalse();
        }
    }

    @Test
    void rejectsMissingApplicableValuesAndValuesDeclaredNonApplicable() {
        for (boolean sized : new boolean[]{false, true}) {
            for (boolean coloured : new boolean[]{false, true}) {
                assertThatIllegalArgumentException().isThrownBy(() -> new VariantConfiguration(
                        ProductFamily.ACCESSORIES, STANDARD, sized, coloured,
                        sized ? null : ProductSize.oneSize(), coloured ? new ProductColour("BLACK") : null));
                assertThatIllegalArgumentException().isThrownBy(() -> new VariantConfiguration(
                        ProductFamily.ACCESSORIES, STANDARD, sized, coloured,
                        sized ? ProductSize.oneSize() : null, coloured ? null : new ProductColour("BLACK")));
            }
        }
    }

    @Test
    void rejectsMissingContextAndWrongFamilySize() {
        assertThatIllegalArgumentException().isThrownBy(() -> new VariantConfiguration(null, STANDARD, false, false, null, null));
        assertThatIllegalArgumentException().isThrownBy(() -> new VariantConfiguration(ProductFamily.APPAREL, null, false, false, null, null));
        assertThatIllegalArgumentException().isThrownBy(() -> new VariantConfiguration(
                ProductFamily.FOOTWEAR, STANDARD, true, false, ProductSize.apparel("M"), null));
        assertThatIllegalArgumentException().isThrownBy(() -> new VariantConfiguration(
                ProductFamily.APPAREL, STANDARD, true, false, ProductSize.oneSize(), null));
    }
}
