package com.enterprise.fashion.ecommerce.inventory.application.query;

import java.util.stream.Stream;

import com.enterprise.fashion.ecommerce.inventory.domain.model.InventoryResourceReference;
import com.enterprise.fashion.ecommerce.inventory.config.InventoryEvidenceConfiguration;
import com.enterprise.fashion.ecommerce.product.application.api.ProductVariantEvidence;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ObserveInventoryProductAssociationTest {
    private final ProductVariantEvidence products = mock(ProductVariantEvidence.class);
    private final ObserveInventoryProductAssociation query = new ObserveInventoryProductAssociation(products);
    private final InventoryResourceReference reference = new InventoryResourceReference("Product_A-1", "Variant_b-2");

    private ProductVariantEvidence.ConfirmedAssociation association(ProductVariantEvidence.Lifecycle lifecycle, String revision) {
        return new ProductVariantEvidence.ConfirmedAssociation(
                reference.productReference(), reference.variantReference(), lifecycle, revision);
    }

    @ParameterizedTest
    @EnumSource(ProductVariantEvidence.Lifecycle.class)
    void matchingEvidencePreservesExactReferencesLifecycleAndRevision(ProductVariantEvidence.Lifecycle lifecycle) {
        var evidence = association(lifecycle, "representation-r1");
        when(products.observe(reference.productReference(), reference.variantReference())).thenReturn(evidence);

        var result = (ObserveInventoryProductAssociation.Confirmed) query.observe(reference);

        assertThat(result.product()).isSameAs(evidence);
        assertThat(result.product().lifecycle()).isEqualTo(lifecycle);
        assertThat(result.product().representationRevision()).isEqualTo("representation-r1");
        verify(products).observe("Product_A-1", "Variant_b-2");
        verifyNoMoreInteractions(products);
    }

    static Stream<ProductVariantEvidence.Result> nonConfirmed() {
        return Stream.of(new ProductVariantEvidence.ProductAbsent(), new ProductVariantEvidence.VariantNotAssociated(),
                new ProductVariantEvidence.Unavailable(),
                new ProductVariantEvidence.Invalid(ProductVariantEvidence.InvalidReason.INVALID_INPUT),
                new ProductVariantEvidence.Invalid(ProductVariantEvidence.InvalidReason.INCONSISTENT_EVIDENCE),
                new ProductVariantEvidence.Invalid(ProductVariantEvidence.InvalidReason.INTERNAL_FAILURE));
    }

    @ParameterizedTest
    @MethodSource("nonConfirmed")
    void preservesEachNonConfirmedOutcomeAndReason(ProductVariantEvidence.Result evidence) {
        when(products.observe(reference.productReference(), reference.variantReference())).thenReturn(evidence);

        var result = (ObserveInventoryProductAssociation.NotConfirmed) query.observe(reference);

        assertThat(result.product()).isSameAs(evidence);
        verify(products).observe(reference.productReference(), reference.variantReference());
        verifyNoMoreInteractions(products);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void rejectsEitherReferenceSubstitution(boolean productMismatch) {
        var evidence = new ProductVariantEvidence.ConfirmedAssociation(
                productMismatch ? "other-product" : reference.productReference(),
                productMismatch ? reference.variantReference() : "other-variant",
                ProductVariantEvidence.Lifecycle.PUBLISHED, "revision");
        when(products.observe(reference.productReference(), reference.variantReference())).thenReturn(evidence);

        assertThat(query.observe(reference)).isEqualTo(new ObserveInventoryProductAssociation.ReferenceMismatch());
        verify(products).observe(reference.productReference(), reference.variantReference());
        verifyNoMoreInteractions(products);
    }

    @Test
    void rejectsNullDependencyAndInputWithoutCallingProvider() {
        assertThatThrownBy(() -> new ObserveInventoryProductAssociation(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> query.observe(null)).isInstanceOf(NullPointerException.class);
        verifyNoInteractions(products);
    }

    @Test
    void nullProviderResultFailsExplicitlyWithoutFallback() {
        assertThatThrownBy(() -> query.observe(reference)).isInstanceOf(NullPointerException.class);
        verify(products).observe(reference.productReference(), reference.variantReference());
        verifyNoMoreInteractions(products);
    }

    @Test
    void repeatedCallsObserveIndependentlyWithoutCaching() {
        var first = association(ProductVariantEvidence.Lifecycle.DRAFT, "r1");
        var second = association(ProductVariantEvidence.Lifecycle.ARCHIVED, "r2");
        when(products.observe(reference.productReference(), reference.variantReference())).thenReturn(first, second);

        assertThat(((ObserveInventoryProductAssociation.Confirmed) query.observe(reference)).product()).isSameAs(first);
        assertThat(((ObserveInventoryProductAssociation.Confirmed) query.observe(reference)).product()).isSameAs(second);
        verify(products, times(2)).observe(reference.productReference(), reference.variantReference());
        verifyNoMoreInteractions(products);
    }

    @Test
    void unexpectedProviderFailurePropagatesWithoutFabricatedObservationOrRetry() {
        var failure = new IllegalStateException("test failure");
        when(products.observe(reference.productReference(), reference.variantReference())).thenThrow(failure);

        assertThatThrownBy(() -> query.observe(reference)).isSameAs(failure);
        verify(products).observe(reference.productReference(), reference.variantReference());
        verifyNoMoreInteractions(products);
    }

    @Test
    void resultsCannotMislabelEvidenceOrAddStockAndAuthorizationClaims() {
        assertThatThrownBy(() -> new ObserveInventoryProductAssociation.Confirmed(null))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new ObserveInventoryProductAssociation.NotConfirmed(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new ObserveInventoryProductAssociation.NotConfirmed(
                association(ProductVariantEvidence.Lifecycle.PUBLISHED, "r1")))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(ObserveInventoryProductAssociation.Confirmed.class.getRecordComponents())
                .extracting("name").containsExactly("product");
        assertThat(ObserveInventoryProductAssociation.NotConfirmed.class.getRecordComponents())
                .extracting("name").containsExactly("product");
        assertThat(ObserveInventoryProductAssociation.ReferenceMismatch.class.getRecordComponents()).isEmpty();
    }

    @Test
    void integrationUsesOnlyExportedProductApiAndHasNoPersistenceOrPricingDependencies() {
        noClasses().should().dependOnClassesThat().resideOutsideOfPackages(
                "java..", "com.enterprise.fashion.ecommerce.inventory..",
                "com.enterprise.fashion.ecommerce.product.application.api..", "org.springframework.context.annotation..")
                .check(new ClassFileImporter().importClasses(ObserveInventoryProductAssociation.class,
                        InventoryEvidenceConfiguration.class, ObserveInventoryProductAssociation.Result.class,
                        ObserveInventoryProductAssociation.Confirmed.class,
                        ObserveInventoryProductAssociation.NotConfirmed.class,
                        ObserveInventoryProductAssociation.ReferenceMismatch.class));
    }

    @Test
    void configurationWiresExistingExportedProviderWithoutObservingIt() {
        try (var context = new AnnotationConfigApplicationContext()) {
            context.registerBean(ProductVariantEvidence.class, () -> products);
            context.register(InventoryEvidenceConfiguration.class);
            context.refresh();
            assertThat(context.getBeansOfType(ObserveInventoryProductAssociation.class)).hasSize(1);
            verifyNoInteractions(products);
        }
    }
}
