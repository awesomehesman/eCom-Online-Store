package com.enterprise.fashion.ecommerce.product.application.usecase;

import java.util.List;
import java.util.Optional;
import com.enterprise.fashion.ecommerce.product.application.api.ProductVariantEvidence;
import com.enterprise.fashion.ecommerce.product.application.api.ProductVariantEvidence.ConfirmedAssociation;
import com.enterprise.fashion.ecommerce.product.application.api.ProductVariantEvidence.ProductAbsent;
import com.enterprise.fashion.ecommerce.product.application.api.ProductVariantEvidence.VariantNotAssociated;
import com.enterprise.fashion.ecommerce.product.application.api.ProductVariantEvidence.Unavailable;
import com.enterprise.fashion.ecommerce.product.application.api.ProductVariantEvidence.Invalid;
import com.enterprise.fashion.ecommerce.product.application.api.ProductVariantEvidence.InvalidReason;
import com.enterprise.fashion.ecommerce.product.application.api.ProductVariantEvidence.Lifecycle;
import com.enterprise.fashion.ecommerce.product.application.persistence.ProductPersistenceService;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductAttributeStandard;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductFamily;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductLifecycle;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariant;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariantId;
import com.enterprise.fashion.ecommerce.product.domain.model.VariantConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;

class ObserveProductVariantEvidenceTest {
    private final ProductPersistenceService persistence = mock(ProductPersistenceService.class);
    private final ProductVariantEvidence provider = new ObserveProductVariantEvidence(persistence);
    private final ProductId id = new ProductId("product-1");
    private Product product(ProductId owner, String variant, ProductLifecycle lifecycle) {
        var configuration = new VariantConfiguration(ProductFamily.ACCESSORIES, ProductAttributeStandard.INITIAL,
                false, false, null, null);
        var member = new ProductVariant(new ProductVariantId(variant), owner, configuration, null, null);
        return Product.reconstruct(owner, ProductFamily.ACCESSORIES, ProductAttributeStandard.INITIAL, false, false, lifecycle, List.of(member));
    }

    @ParameterizedTest @EnumSource(ProductLifecycle.class)
    void confirmsMatchingIdentityMembershipLifecycleAndRevisionWithoutCommercialGrant(ProductLifecycle lifecycle) {
        var product = product(id, "variant-1", lifecycle);
        when(persistence.load(id)).thenReturn(Optional.of(product));
        assertThat(provider.observe(id.value(), "variant-1")).isEqualTo(new ConfirmedAssociation(
                id.value(), "variant-1", Lifecycle.valueOf(lifecycle.name()), product.revision().value()));
        verify(persistence).load(id);
        verifyNoMoreInteractions(persistence);
    }

    @Test
    void confirmsProductAbsenceOnlyOnSuccessfulNegativeRead() {
        when(persistence.load(id)).thenReturn(Optional.empty());
        assertThat(provider.observe(id.value(), "variant-1")).isEqualTo(new ProductAbsent());
    }

    @Test
    void missingMemberDoesNotSearchOtherProductsOrDiscloseTheirOwnership() {
        when(persistence.load(id)).thenReturn(Optional.of(product(id, "own-variant", ProductLifecycle.DRAFT)));
        assertThat(provider.observe(id.value(), "other-products-variant")).isEqualTo(new VariantNotAssociated());
        verify(persistence).load(id);
        verifyNoMoreInteractions(persistence);
    }

    @Test
    void rejectsMissingMalformedAndOverlongReferencesWithoutReading() {
        for (String invalid : new String[]{null, "", " ", "p/1", "x".repeat(129)}) {
            assertThat(provider.observe(invalid, "v")).isEqualTo(new Invalid(InvalidReason.INVALID_INPUT));
            assertThat(provider.observe("p", invalid)).isEqualTo(new Invalid(InvalidReason.INVALID_INPUT));
        }
        verifyNoInteractions(persistence);
    }

    @Test
    void mismatchedProductObservationIsNotConfirmed() {
        when(persistence.load(id)).thenReturn(Optional.of(product(new ProductId("other"), "variant-1", ProductLifecycle.DRAFT)));
        assertThat(provider.observe(id.value(), "variant-1")).isEqualTo(new Invalid(InvalidReason.INCONSISTENT_EVIDENCE));
    }

    @Test
    void corruptReconstructionIsDistinctFromInputFailureAndAbsence() {
        doThrow(new IllegalArgumentException("private stored revision mismatch")).when(persistence).load(id);
        assertThat(provider.observe(id.value(), "variant-1")).isEqualTo(new Invalid(InvalidReason.INCONSISTENT_EVIDENCE));
        doThrow(new org.springframework.dao.DataIntegrityViolationException("private corruption")).when(persistence).load(id);
        assertThat(provider.observe(id.value(), "variant-1")).isEqualTo(new Invalid(InvalidReason.INCONSISTENT_EVIDENCE));
    }

    @Test
    void resourceTimeoutTransactionAndConcurrencyFailuresAreUnavailable() {
        for (RuntimeException failure : List.of(
                new org.springframework.dao.DataAccessResourceFailureException("private database"),
                new org.springframework.dao.TransientDataAccessResourceException("private resource"),
                new org.springframework.dao.QueryTimeoutException("private query"),
                new org.springframework.dao.CannotAcquireLockException("private lock"),
                new org.springframework.transaction.CannotCreateTransactionException("private transaction"),
                new org.springframework.transaction.TransactionTimedOutException("private timeout"))) {
            doThrow(failure).when(persistence).load(id);
            assertThat(provider.observe(id.value(), "variant-1")).isEqualTo(new Unavailable());
        }
    }

    @Test
    void recoverableConnectionFailureIsUnavailableWithoutLeakingDetails() {
        doThrow(new org.springframework.dao.RecoverableDataAccessException("private connection details"))
                .when(persistence).load(id);
        assertThat(provider.observe(id.value(), "variant-1")).isEqualTo(new Unavailable());
    }

    @Test
    void transactionWrapperWithRecognizedResourceFailureIsUnavailable() {
        var failure = new org.springframework.transaction.TransactionSystemException("private transaction",
                new org.springframework.dao.DataAccessResourceFailureException("private connection"));
        doThrow(failure).when(persistence).load(id);
        assertThat(provider.observe(id.value(), "variant-1")).isEqualTo(new Unavailable());
    }

    @Test
    void nestedAvailabilityFailureInsideTransactionWrapperIsUnavailable() {
        var failure = new org.springframework.transaction.TransactionSystemException("private transaction",
                new IllegalStateException("internal wrapper", new org.springframework.transaction.TransactionSystemException(
                        "nested transaction", new org.springframework.dao.RecoverableDataAccessException("private connection"))));
        doThrow(failure).when(persistence).load(id);
        assertThat(provider.observe(id.value(), "variant-1")).isEqualTo(new Unavailable());
    }

    @Test
    void unrelatedWrappedFailureRemainsInvalid() {
        doThrow(new org.springframework.transaction.TransactionSystemException("private transaction",
                new IllegalStateException("private internal failure"))).when(persistence).load(id);
        assertThat(provider.observe(id.value(), "variant-1")).isEqualTo(new Invalid(InvalidReason.INTERNAL_FAILURE));
    }

    @Test
    void nonAvailabilityDataAccessFailureDoesNotBecomeUnavailable() {
        doThrow(new org.springframework.dao.InvalidDataAccessResourceUsageException("private invalid SQL"))
                .when(persistence).load(id);
        assertThat(provider.observe(id.value(), "variant-1")).isEqualTo(new Invalid(InvalidReason.INTERNAL_FAILURE));
        doThrow(new org.springframework.transaction.TransactionSystemException("private transaction",
                new org.springframework.dao.InvalidDataAccessResourceUsageException("private invalid SQL")))
                .when(persistence).load(id);
        assertThat(provider.observe(id.value(), "variant-1")).isEqualTo(new Invalid(InvalidReason.INTERNAL_FAILURE));
    }

    @Test
    void arbitraryInternalWrapperIsNotAssumedToBeATransactionAvailabilityFailure() {
        doThrow(new IllegalStateException("internal failure",
                new org.springframework.dao.DataAccessResourceFailureException("private resource")))
                .when(persistence).load(id);
        assertThat(provider.observe(id.value(), "variant-1")).isEqualTo(new Invalid(InvalidReason.INTERNAL_FAILURE));
    }

    @Test
    void cyclicTransactionCauseChainTerminatesWithoutGrantingAvailability() {
        var first = new IllegalStateException("first");
        var second = new IllegalStateException("second", first);
        first.initCause(second);
        doThrow(new org.springframework.transaction.TransactionSystemException("cycle", first)).when(persistence).load(id);
        org.junit.jupiter.api.Assertions.assertTimeoutPreemptively(java.time.Duration.ofSeconds(2), () ->
                assertThat(provider.observe(id.value(), "variant-1")).isEqualTo(new Invalid(InvalidReason.INTERNAL_FAILURE)));
    }

    @Test
    void causeInspectionIsBoundedEvenForVeryDeepChains() {
        RuntimeException chain = new org.springframework.dao.DataAccessResourceFailureException("deep resource");
        for (int index = 0; index < 40; index++) { chain = new IllegalStateException("wrapper", chain); }
        doThrow(new org.springframework.transaction.TransactionSystemException("deep transaction", chain)).when(persistence).load(id);
        assertThat(provider.observe(id.value(), "variant-1")).isEqualTo(new Invalid(InvalidReason.INTERNAL_FAILURE));
    }

    @Test
    void unexpectedFailureAndMissingProducerObservationAreSafeInternalFailures() {
        doThrow(new IllegalStateException("sensitive implementation details")).when(persistence).load(id);
        var result = provider.observe(id.value(), "variant-1");
        assertThat(result).isEqualTo(new Invalid(InvalidReason.INTERNAL_FAILURE));
        assertThat(result.toString()).doesNotContain("sensitive");
        doReturn(null).when(persistence).load(id);
        assertThat(provider.observe(id.value(), "variant-1")).isEqualTo(new Invalid(InvalidReason.INTERNAL_FAILURE));
    }

    @Test
    void lifecycleMayChangeWithoutChangingRepresentationRevision() {
        var draft = product(id, "variant-1", ProductLifecycle.DRAFT);
        var archived = product(id, "variant-1", ProductLifecycle.ARCHIVED);
        assertThat(draft.revision()).isEqualTo(archived.revision());
        when(persistence.load(id)).thenReturn(Optional.of(draft)).thenReturn(Optional.of(archived));
        var first = (ConfirmedAssociation) provider.observe(id.value(), "variant-1");
        var second = (ConfirmedAssociation) provider.observe(id.value(), "variant-1");
        assertThat(first.representationRevision()).isEqualTo(second.representationRevision());
        assertThat(first.lifecycle()).isEqualTo(Lifecycle.DRAFT);
        assertThat(second.lifecycle()).isEqualTo(Lifecycle.ARCHIVED);
        verify(persistence, times(2)).load(id);
    }

    @Test
    void exportedConfirmationContainsOnlyContractValuesAndNoEligibilityOrActorClaims() {
        assertThat(ConfirmedAssociation.class.getRecordComponents()).extracting("name")
                .containsExactly("productReference", "variantReference", "lifecycle", "representationRevision");
        assertThat(ConfirmedAssociation.class.getRecordComponents()).extracting("type")
                .containsExactly(String.class, String.class, Lifecycle.class, String.class);
    }
}
