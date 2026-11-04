package com.enterprise.fashion.ecommerce.product.domain.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {
    private static final ProductId ID = new ProductId("product-1");
    private static final ProductAttributeStandard STANDARD = ProductAttributeStandard.INITIAL;

    private Product product() {
        return new Product(ID, ProductFamily.APPAREL, STANDARD, true, true);
    }

    private VariantConfiguration configuration(String size, String colour) {
        return new VariantConfiguration(ProductFamily.APPAREL, STANDARD, true, true,
                ProductSize.apparel(size), new ProductColour(colour));
    }

    private ProductVariant variant(String id, String size, String colour) {
        return new ProductVariant(new ProductVariantId(id), ID, configuration(size, colour), null, null);
    }

    private Product reconstruct(ProductLifecycle lifecycle, List<ProductVariant> variants) {
        return Product.reconstruct(ID, ProductFamily.APPAREL, STANDARD, true, true, lifecycle, variants);
    }

    @Test
    void newProductsStartDraft() {
        assertThat(product().lifecycle()).isEqualTo(ProductLifecycle.DRAFT);
        assertThat(product().isCustomerVisible()).isFalse();
    }

    @ParameterizedTest
    @EnumSource(ProductLifecycle.class)
    void reconstructsPersistedLifecycleIdentityContextAndMembershipWithoutVisibility(ProductLifecycle lifecycle) {
        ProductVariant first = variant("v1", "M", "BLACK");
        Product restored = reconstruct(lifecycle, List.of(first));
        assertThat(restored.id()).isEqualTo(ID);
        assertThat(restored.family()).isEqualTo(ProductFamily.APPAREL);
        assertThat(restored.standard()).isEqualTo(STANDARD);
        assertThat(restored.sizeApplicable()).isTrue();
        assertThat(restored.colourApplicable()).isTrue();
        assertThat(restored.lifecycle()).isEqualTo(lifecycle);
        assertThat(restored.variants()).containsExactly(first);
        assertThat(restored.variants().getFirst().id()).isEqualTo(new ProductVariantId("v1"));
        assertThat(restored).isEqualTo(product());
        assertThat(restored.isCustomerVisible()).isFalse();
    }

    @ParameterizedTest
    @EnumSource(ProductLifecycle.class)
    void membershipUpdatesPreserveRestoredLifecycleAndPriorSnapshot(ProductLifecycle lifecycle) {
        Product restored = reconstruct(lifecycle, List.of(variant("v1", "M", "BLACK")));
        Product updated = restored.withVariant(variant("v2", "L", "BLACK"));
        assertThat(updated.lifecycle()).isEqualTo(lifecycle);
        assertThat(restored.lifecycle()).isEqualTo(lifecycle);
        assertThat(restored.variants()).hasSize(1);
        assertThat(updated.variants()).hasSize(2);
        assertThat(updated.isCustomerVisible()).isFalse();
    }

    @Test
    void restoredMembershipIsDefensivelyCopiedAndUnmodifiable() {
        ProductVariant first = variant("v1", "M", "BLACK");
        List<ProductVariant> input = new ArrayList<>(List.of(first));
        Product restored = reconstruct(ProductLifecycle.PUBLISHED, input);
        input.clear();
        assertThat(restored.variants()).containsExactly(first);
        assertThatThrownBy(() -> restored.variants().clear()).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void reconstructionRejectsDuplicateVariantIds() {
        assertThatIllegalArgumentException().isThrownBy(() -> reconstruct(ProductLifecycle.PUBLISHED,
                List.of(variant("v1", "M", "BLACK"), variant("v1", "L", "BLACK"))))
                .withMessage("Duplicate Product Variant identifier");
    }

    @Test
    void reconstructionRejectsCanonicalDuplicatesDespiteDescriptionsAndColourCase() {
        ProductVariant first = variant("v1", "m", "black");
        ProductVariant duplicate = new ProductVariant(new ProductVariantId("v2"), ID,
                configuration("M", "BLACK"), "Different fit", "Different material");
        assertThatIllegalArgumentException().isThrownBy(() -> reconstruct(ProductLifecycle.ARCHIVED,
                List.of(first, duplicate))).withMessage("Duplicate Product Variant configuration");
    }

    @Test
    void reconstructionRejectsForeignOwnershipFamilyAndApplicability() {
        ProductVariant foreign = new ProductVariant(new ProductVariantId("v1"), new ProductId("other"),
                configuration("M", "BLACK"), null, null);
        assertThatIllegalArgumentException().isThrownBy(() -> reconstruct(ProductLifecycle.PUBLISHED, List.of(foreign)));
        VariantConfiguration[] incompatible = {
                new VariantConfiguration(ProductFamily.ACCESSORIES, STANDARD, true, true,
                        ProductSize.oneSize(), new ProductColour("BLACK")),
                new VariantConfiguration(ProductFamily.APPAREL, STANDARD, false, true, null, new ProductColour("BLACK")),
                new VariantConfiguration(ProductFamily.APPAREL, STANDARD, true, false, ProductSize.apparel("M"), null)
        };
        for (VariantConfiguration config : incompatible) {
            ProductVariant variant = new ProductVariant(new ProductVariantId("v1"), ID, config, null, null);
            assertThatIllegalArgumentException().isThrownBy(() -> reconstruct(ProductLifecycle.PUBLISHED, List.of(variant)));
        }
    }

    @Test
    void reconstructionRejectsMissingLifecycleMembershipAndUnsupportedTypedState() {
        assertThatIllegalArgumentException().isThrownBy(() -> reconstruct(null, List.of()));
        assertThatIllegalArgumentException().isThrownBy(() -> reconstruct(ProductLifecycle.DRAFT, null));
        assertThatIllegalArgumentException().isThrownBy(() -> reconstruct(ProductLifecycle.DRAFT,
                java.util.Arrays.asList((ProductVariant) null)));
        // Unsupported stored representations must fail at the typed value-object boundary.
        assertThatIllegalArgumentException().isThrownBy(() -> new ProductAttributeStandard("2.0.0"));
        assertThatIllegalArgumentException().isThrownBy(() -> new ProductFamily("Unknown"));
        assertThatIllegalArgumentException().isThrownBy(() -> new ProductColour("BLUE"));
        assertThatIllegalArgumentException().isThrownBy(() -> ProductLifecycle.valueOf("UNKNOWN"));
    }

    @Test
    void membershipUpdatesPreserveIdentityContextAndPriorSnapshots() {
        Product initial = product();
        ProductVariant first = variant("v1", "M", "Black");
        Product one = initial.withVariant(first);
        Product two = one.withVariant(variant("v2", "L", "BLACK"));
        assertThat(initial.variants()).isEmpty();
        assertThat(one.variants()).containsExactly(first);
        assertThat(two.variants()).hasSize(2);
        assertThat(two.id()).isEqualTo(ID);
        assertThat(two.family()).isEqualTo(ProductFamily.APPAREL);
        assertThat(two.standard()).isEqualTo(STANDARD);
        assertThat(two).isEqualTo(initial);
        assertThat(two.hashCode()).isEqualTo(initial.hashCode());
        assertThat(two.lifecycle()).isEqualTo(ProductLifecycle.DRAFT);
        assertThat(two.isCustomerVisible()).isFalse();
    }

    @Test
    void duplicateIdCannotReplaceExistingVariantEvenWithDifferentConfiguration() {
        Product initial = product().withVariant(variant("v1", "M", "Black"));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> initial.withVariant(variant("v1", "L", "Black")))
                .withMessage("Duplicate Product Variant identifier");
        assertThat(initial.variants()).hasSize(1);
        assertThat(initial.variants().getFirst().configuration().size()).isEqualTo(ProductSize.apparel("M"));
    }

    @Test
    void colourCasingApparelNormalizationAndDescriptiveDifferencesCannotBypassUniqueness() {
        ProductVariant first = new ProductVariant(new ProductVariantId("v1"), ID,
                configuration("m", "black"), "Regular", "Cotton");
        ProductVariant duplicate = new ProductVariant(new ProductVariantId("v2"), ID,
                configuration("M", "BLACK"), "Relaxed", "Linen");
        Product accepted = product().withVariant(first);
        assertThatIllegalArgumentException().isThrownBy(() -> accepted.withVariant(duplicate))
                .withMessage("Duplicate Product Variant configuration");
        assertThat(accepted.variants()).containsExactly(first);
    }

    @Test
    void rejectsForeignMembershipWithoutTransferringOwner() {
        ProductVariant foreign = new ProductVariant(new ProductVariantId("v1"), new ProductId("other"),
                configuration("M", "BLACK"), null, null);
        Product initial = product();
        assertThatIllegalArgumentException().isThrownBy(() -> initial.withVariant(foreign));
        assertThat(foreign.productId()).isEqualTo(new ProductId("other"));
        assertThat(initial.variants()).isEmpty();
    }

    @Test
    void differentProductsCanIndependentlyAcceptEquivalentConfigurations() {
        ProductVariant first = variant("v1", "M", "BLACK");
        ProductId otherId = new ProductId("product-2");
        ProductVariant second = new ProductVariant(new ProductVariantId("v2"), otherId,
                configuration("M", "black"), null, null);
        assertThat(product().withVariant(first).variants()).containsExactly(first);
        Product other = new Product(otherId, ProductFamily.APPAREL, STANDARD, true, true).withVariant(second);
        assertThat(other.variants()).containsExactly(second);
        assertThat(other).isNotEqualTo(product());
    }

    @Test
    void returnedMembershipCannotBeMutated() {
        Product accepted = product().withVariant(variant("v1", "M", "BLACK"));
        assertThatThrownBy(() -> accepted.variants().clear()).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> accepted.variants().add(variant("v2", "L", "BLACK")))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThat(accepted.variants()).hasSize(1);
    }

    @Test
    void rejectsWrongFamilyAndMismatchedApplicability() {
        VariantConfiguration[] incompatible = {
                new VariantConfiguration(ProductFamily.ACCESSORIES, STANDARD, true, true,
                        ProductSize.oneSize(), new ProductColour("BLACK")),
                new VariantConfiguration(ProductFamily.APPAREL, STANDARD, false, true,
                        null, new ProductColour("BLACK")),
                new VariantConfiguration(ProductFamily.APPAREL, STANDARD, true, false,
                        ProductSize.apparel("M"), null)
        };
        for (VariantConfiguration config : incompatible) {
            assertThatIllegalArgumentException().isThrownBy(() -> product().withVariant(
                    new ProductVariant(new ProductVariantId("v1"), ID, config, null, null)));
        }
    }

    @Test
    void accessoriesWithNeitherDefiningAttributeCannotMultiplyUsingDescriptions() {
        Product initial = new Product(ID, ProductFamily.ACCESSORIES, STANDARD, false, false);
        var config = new VariantConfiguration(ProductFamily.ACCESSORIES, STANDARD, false, false, null, null);
        Product accepted = initial.withVariant(new ProductVariant(new ProductVariantId("v1"), ID, config, "A", "B"));
        assertThatIllegalArgumentException().isThrownBy(() -> accepted.withVariant(
                new ProductVariant(new ProductVariantId("v2"), ID, config, "C", "D")));
    }

    @Test
    void equivalentUkNumericRepresentationsAreDuplicateConfigurations() {
        Product footwear = new Product(ID, ProductFamily.FOOTWEAR, STANDARD, true, false);
        var first = new VariantConfiguration(ProductFamily.FOOTWEAR, STANDARD, true, false,
                ProductSize.footwear("UK", new java.math.BigDecimal("3.00")), null);
        var second = new VariantConfiguration(ProductFamily.FOOTWEAR, STANDARD, true, false,
                ProductSize.footwear("UK", new java.math.BigDecimal("3")), null);
        Product accepted = footwear.withVariant(new ProductVariant(new ProductVariantId("v1"), ID, first, null, null));
        assertThatIllegalArgumentException().isThrownBy(() -> accepted.withVariant(
                new ProductVariant(new ProductVariantId("v2"), ID, second, null, null)));
    }

    @Test
    void rejectsMissingContextAndNullMembershipWithoutChangingState() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Product(null, ProductFamily.APPAREL, STANDARD, true, true));
        assertThatIllegalArgumentException().isThrownBy(() -> new Product(ID, null, STANDARD, true, true));
        assertThatIllegalArgumentException().isThrownBy(() -> new Product(ID, ProductFamily.APPAREL, null, true, true));
        assertThatIllegalArgumentException().isThrownBy(() -> product().withVariant(null));
        // Version mismatch cannot be constructed: M1.1A rejects unsupported versions at its boundary.
        assertThatIllegalArgumentException().isThrownBy(() -> new ProductAttributeStandard("2.0.0"));
    }
}
