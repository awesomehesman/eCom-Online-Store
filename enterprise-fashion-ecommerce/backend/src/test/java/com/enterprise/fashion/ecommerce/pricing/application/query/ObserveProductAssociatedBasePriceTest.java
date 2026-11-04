package com.enterprise.fashion.ecommerce.pricing.application.query;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Stream;
import com.enterprise.fashion.ecommerce.product.application.api.ProductVariantEvidence;
import com.enterprise.fashion.ecommerce.pricing.domain.model.Currency;
import com.enterprise.fashion.ecommerce.pricing.domain.model.Money;
import com.enterprise.fashion.ecommerce.pricing.domain.model.SelectVariantBasePrice;
import com.enterprise.fashion.ecommerce.pricing.domain.model.VariantBasePriceAssignment;
import com.enterprise.fashion.ecommerce.pricing.domain.model.VariantBasePriceAssignment.State;
import com.enterprise.fashion.ecommerce.pricing.domain.model.VariantBasePriceAssignment.Subject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.inOrder;

class ObserveProductAssociatedBasePriceTest {
    private final ProductVariantEvidence products = mock(ProductVariantEvidence.class);
    private final ObserveVariantBasePrice pricing = mock(ObserveVariantBasePrice.class);
    private final ObserveProductAssociatedBasePrice query = new ObserveProductAssociatedBasePrice(products, pricing);
    private final Subject subject = new Subject("product-1", "variant-1");
    private ProductVariantEvidence.ConfirmedAssociation association() {
        return new ProductVariantEvidence.ConfirmedAssociation(subject.productReference(), subject.variantReference(),
                ProductVariantEvidence.Lifecycle.DRAFT, "observed-revision");
    }
    private SelectVariantBasePrice.Selected selected(Subject target) {
        return new SelectVariantBasePrice.Selected(new VariantBasePriceAssignment(target, "price-r1",
                new Money(new BigDecimal("1.12345678901234567890"), Currency.ZAR), State.CURRENT));
    }
    private ObserveProductAssociatedBasePrice.Result observe() { return query.observe(subject.productReference(), subject.variantReference()); }

    @Test
    void confirmedAssociationAndSelectedProduceOnlyAConditionalStoredObservation() {
        var product = association(); var price = selected(subject);
        when(products.observe(subject.productReference(), subject.variantReference())).thenReturn(product);
        when(pricing.observe(subject)).thenReturn(price);
        var result = (ObserveProductAssociatedBasePrice.ConditionallyValidatedStoredObservation) observe();
        assertThat(result.product()).isSameAs(product);
        assertThat(result.pricing()).isSameAs(price);
        assertThat(result.pricing().assignment().money().amount()).isEqualTo(new BigDecimal("1.12345678901234567890"));
        assertThat(result.getClass().getRecordComponents()).extracting("name").containsExactly("product", "pricing");
        var order = inOrder(products, pricing);
        order.verify(products).observe(subject.productReference(), subject.variantReference());
        order.verify(pricing).observe(subject);
        order.verifyNoMoreInteractions();
    }

    static Stream<ProductVariantEvidence.Result> negativeProductObservations() {
        return Stream.of(new ProductVariantEvidence.ProductAbsent(), new ProductVariantEvidence.VariantNotAssociated(),
                new ProductVariantEvidence.Unavailable(), new ProductVariantEvidence.Invalid(ProductVariantEvidence.InvalidReason.INVALID_INPUT),
                new ProductVariantEvidence.Invalid(ProductVariantEvidence.InvalidReason.INCONSISTENT_EVIDENCE),
                new ProductVariantEvidence.Invalid(ProductVariantEvidence.InvalidReason.INTERNAL_FAILURE));
    }
    @ParameterizedTest @MethodSource("negativeProductObservations")
    void negativeProductEvidenceIsPreservedAndPricingIsNotRead(ProductVariantEvidence.Result product) {
        when(products.observe(subject.productReference(), subject.variantReference())).thenReturn(product);
        assertThat(observe()).isEqualTo(new ObserveProductAssociatedBasePrice.ProductNotConfirmed(product));
        verifyNoInteractions(pricing);
    }

    static Stream<SelectVariantBasePrice.Result> nonSelectedPricing() {
        return Stream.of(new SelectVariantBasePrice.Missing(), new SelectVariantBasePrice.Conflict(), new SelectVariantBasePrice.Invalid(),
                new SelectVariantBasePrice.Historical(Set.of(State.OBSOLETE)),
                new SelectVariantBasePrice.Historical(Set.of(State.WITHDRAWN)),
                new SelectVariantBasePrice.Historical(Set.of(State.OBSOLETE, State.WITHDRAWN)));
    }
    @ParameterizedTest @MethodSource("nonSelectedPricing")
    void nonSelectedPricingRetainsAllExistingOutcomeDistinctions(SelectVariantBasePrice.Result price) {
        var product = association();
        when(products.observe(subject.productReference(), subject.variantReference())).thenReturn(product);
        when(pricing.observe(subject)).thenReturn(price);
        assertThat(observe()).isEqualTo(new ObserveProductAssociatedBasePrice.PricingNotSelected(product, price));
        verify(pricing).observe(subject); verifyNoMoreInteractions(pricing);
    }

    @Test
    void productReferenceSubstitutionFailsBeforePricingRead() {
        for (var product : java.util.List.of(
                new ProductVariantEvidence.ConfirmedAssociation("other", subject.variantReference(), ProductVariantEvidence.Lifecycle.DRAFT, "r"),
                new ProductVariantEvidence.ConfirmedAssociation(subject.productReference(), "other", ProductVariantEvidence.Lifecycle.DRAFT, "r"))) {
            when(products.observe(subject.productReference(), subject.variantReference())).thenReturn(product);
            assertThat(observe()).isEqualTo(new ObserveProductAssociatedBasePrice.ReferenceMismatch());
        }
        verifyNoInteractions(pricing);
    }

    @Test
    void selectedPricingReferenceSubstitutionFailsClosed() {
        when(products.observe(subject.productReference(), subject.variantReference())).thenReturn(association());
        for (var wrong : java.util.List.of(new Subject("other", subject.variantReference()), new Subject(subject.productReference(), "other"))) {
            when(pricing.observe(subject)).thenReturn(selected(wrong));
            assertThat(observe()).isEqualTo(new ObserveProductAssociatedBasePrice.ReferenceMismatch());
        }
    }

    @Test
    void productMayChangeBetweenObservationsWithoutImplyingAtomicityOrCaching() {
        var before = association();
        var after = new ProductVariantEvidence.ConfirmedAssociation(subject.productReference(), subject.variantReference(),
                ProductVariantEvidence.Lifecycle.ARCHIVED, before.representationRevision());
        when(products.observe(subject.productReference(), subject.variantReference())).thenReturn(before);
        when(pricing.observe(subject)).thenAnswer(call -> {
            // Product changes after its read; this composition has no shared snapshot.
            when(products.observe(subject.productReference(), subject.variantReference())).thenReturn(after);
            return selected(subject);
        });
        var first = (ObserveProductAssociatedBasePrice.ConditionallyValidatedStoredObservation) observe();
        var second = (ObserveProductAssociatedBasePrice.ConditionallyValidatedStoredObservation) observe();
        assertThat(first.product()).isSameAs(before);
        assertThat(second.product()).isSameAs(after);
        assertThat(second.product().lifecycle()).isEqualTo(ProductVariantEvidence.Lifecycle.ARCHIVED);
        // Even Archived can confirm membership; neither result claims commercial eligibility.
        var order = inOrder(products, pricing);
        order.verify(products).observe(subject.productReference(), subject.variantReference()); order.verify(pricing).observe(subject);
        order.verify(products).observe(subject.productReference(), subject.variantReference()); order.verify(pricing).observe(subject);
        order.verifyNoMoreInteractions();
    }

    @Test
    void unexpectedProductFailurePropagatesAndPricingIsNotRead() {
        var failure = new IllegalStateException("synthetic producer failure");
        doThrow(failure).when(products).observe(subject.productReference(), subject.variantReference());
        assertThatThrownBy(this::observe).isSameAs(failure);
        verifyNoInteractions(pricing);
    }

    @Test
    void pricingFailurePropagatesWithoutMissingFallbackOrValidatedObservation() {
        when(products.observe(subject.productReference(), subject.variantReference())).thenReturn(association());
        for (RuntimeException failure : java.util.List.of(
                new org.springframework.dao.DataAccessResourceFailureException("synthetic database failure"),
                new org.springframework.transaction.CannotCreateTransactionException("synthetic transaction failure"),
                new IllegalStateException("synthetic corruption"))) {
            doThrow(failure).when(pricing).observe(subject);
            assertThatThrownBy(this::observe).isSameAs(failure);
        }
    }

    @Test
    void invalidInputPreservesProductContractOutcome() {
        var invalid = new ProductVariantEvidence.Invalid(ProductVariantEvidence.InvalidReason.INVALID_INPUT);
        when(products.observe(null, "v")).thenReturn(invalid);
        assertThat(query.observe(null, "v")).isEqualTo(new ObserveProductAssociatedBasePrice.ProductNotConfirmed(invalid));
        verifyNoInteractions(pricing);
    }

    @Test
    void nullProducerObservationsCannotBecomeSuccessfulOrMissingResults() {
        when(products.observe(subject.productReference(), subject.variantReference())).thenReturn(null);
        assertThatThrownBy(this::observe).isInstanceOf(NullPointerException.class);
        verifyNoInteractions(pricing);
        when(products.observe(subject.productReference(), subject.variantReference())).thenReturn(association());
        when(pricing.observe(subject)).thenReturn(null);
        assertThatThrownBy(this::observe).isInstanceOf(NullPointerException.class);
    }

    @Test
    void resultConstructorsCannotMislabelNegativeOrMismatchedEvidence() {
        assertThatThrownBy(() -> new ObserveProductAssociatedBasePrice.ProductNotConfirmed(association()))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new ObserveProductAssociatedBasePrice.PricingNotSelected(association(), selected(subject)))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new ObserveProductAssociatedBasePrice.ConditionallyValidatedStoredObservation(
                association(), selected(new Subject("other", subject.variantReference())))).isInstanceOf(IllegalArgumentException.class);
    }
}
