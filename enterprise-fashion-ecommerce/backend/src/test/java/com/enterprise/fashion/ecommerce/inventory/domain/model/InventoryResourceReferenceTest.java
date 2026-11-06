package com.enterprise.fashion.ecommerce.inventory.domain.model;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class InventoryResourceReferenceTest {
    @Test
    void referenceHasNoProductPricingOrFrameworkDependencies() {
        noClasses().should().dependOnClassesThat().resideOutsideOfPackages(
                "java..", "com.enterprise.fashion.ecommerce.inventory.domain.model..")
                .check(new ClassFileImporter().importClasses(InventoryResourceReference.class));
    }

    @Test
    void preservesValidReferencesExactlyWithoutExternalIdentityResolution() {
        var reference = new InventoryResourceReference("Product_A-09", "Variant_b-01");

        assertThat(reference.productReference()).isEqualTo("Product_A-09");
        assertThat(reference.variantReference()).isEqualTo("Variant_b-01");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n", " product", "product ", "a b", "a/b",
            "a.b", "a:b", "a@b", "é", "Ａ", "a\n", "a\u0000"})
    void rejectsInvalidProductReferences(String value) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new InventoryResourceReference(value, "variant"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n", " variant", "variant ", "a b", "a/b",
            "a.b", "a:b", "a@b", "é", "Ａ", "a\n", "a\u0000"})
    void rejectsInvalidVariantReferences(String value) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new InventoryResourceReference("product", value));
    }

    @Test
    void rejectsOversizedReferencesInEitherPosition() {
        String oversized = "a".repeat(129);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> new InventoryResourceReference(oversized, "variant"));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new InventoryResourceReference("product", oversized));
    }

    @Test
    void acceptsBothBoundaryLengthsInEitherPosition() {
        String longest = "Az09_-".repeat(21) + "aZ";

        assertThat(new InventoryResourceReference("_", longest).variantReference()).isEqualTo(longest);
        assertThat(new InventoryResourceReference(longest, "-").productReference()).isEqualTo(longest);
    }

    @Test
    void recordEqualityAndHashCodeUseBothExactReferences() {
        var reference = new InventoryResourceReference("Product", "Variant");
        var equal = new InventoryResourceReference("Product", "Variant");

        assertThat(reference).isEqualTo(equal);
        assertThat(reference.hashCode()).isEqualTo(equal.hashCode());
        assertThat(reference).isNotEqualTo(new InventoryResourceReference("product", "Variant"));
        assertThat(reference).isNotEqualTo(new InventoryResourceReference("Product", "variant"));
    }
}
