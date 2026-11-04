package com.enterprise.fashion.ecommerce.product.application.usecase;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.enterprise.fashion.ecommerce.product.application.persistence.ProductPersistenceService;
import com.enterprise.fashion.ecommerce.product.application.visibility.ProductVisibilityReadiness;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductAttributeStandard;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductColour;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductFamily;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductLifecycle;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductSize;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariant;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariantId;
import com.enterprise.fashion.ecommerce.product.domain.model.VariantConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionSystemException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class AssessProductVisibilityReadinessTest {
    private static final ProductId ID = new ProductId("product-1");
    private final ProductPersistenceService persistence = mock(ProductPersistenceService.class);
    private final AssessProductVisibilityReadiness evaluator = new AssessProductVisibilityReadiness(persistence);

    @Test
    void confirmedAbsenceIsNotFoundAndOnlyLoads() {
        when(persistence.load(ID)).thenReturn(Optional.empty());
        assertThat(evaluator.assess(ID)).isEqualTo(ProductVisibilityReadiness.NOT_FOUND);
        verify(persistence).load(ID);
        verifyNoMoreInteractions(persistence);
    }

    @ParameterizedTest
    @CsvSource({"DRAFT,false,NOT_VISIBLE", "DRAFT,true,NOT_VISIBLE",
            "ARCHIVED,false,NOT_VISIBLE", "ARCHIVED,true,NOT_VISIBLE",
            "PUBLISHED,false,EVIDENCE_UNCONFIRMED", "PUBLISHED,true,EVIDENCE_UNCONFIRMED"})
    void lifecycleAndValidMembershipNeverGrantVisibilityOrMutate(ProductLifecycle lifecycle,
            boolean withVariant, ProductVisibilityReadiness expected) {
        var configuration = new VariantConfiguration(ProductFamily.APPAREL, ProductAttributeStandard.INITIAL,
                true, true, ProductSize.apparel("M"), new ProductColour("black"));
        var variant = new ProductVariant(new ProductVariantId("variant-1"), ID, configuration, "Fit", "Material");
        List<ProductVariant> membership = withVariant ? List.of(variant) : List.of();
        Product original = Product.reconstruct(ID, ProductFamily.APPAREL, ProductAttributeStandard.INITIAL,
                true, true, lifecycle, membership);
        when(persistence.load(ID)).thenReturn(Optional.of(original));
        assertThat(evaluator.assess(ID)).isEqualTo(expected);
        assertThat(original.lifecycle()).isEqualTo(lifecycle);
        assertThat(original.variants()).containsExactlyElementsOf(membership);
        assertThat(original.isCustomerVisible()).isFalse();
        verify(persistence).load(ID);
        verifyNoMoreInteractions(persistence);
    }

    static Stream<RuntimeException> unavailableFailures() {
        return Stream.of(new DataAccessResourceFailureException("private connection detail"),
                new TransientDataAccessResourceException("private resource detail"),
                new QueryTimeoutException("private query detail"),
                new CannotCreateTransactionException("private transaction detail"));
    }

    @ParameterizedTest
    @MethodSource("unavailableFailures")
    void narrowlyClassifiesUnavailableAuthorityWithoutReturningFailureDetails(RuntimeException failure) {
        when(persistence.load(ID)).thenThrow(failure);
        assertThat(evaluator.assess(ID)).isEqualTo(ProductVisibilityReadiness.AUTHORITY_UNAVAILABLE);
        verify(persistence).load(ID);
        verifyNoMoreInteractions(persistence);
    }

    static Stream<RuntimeException> unexpectedFailures() {
        return Stream.of(new IllegalStateException("invalid transaction context"),
                new DataIntegrityViolationException("invalid persisted representation"),
                new InvalidDataAccessResourceUsageException("SQL defect"),
                new TransactionSystemException("unexpected completion failure"),
                new NullPointerException("internal defect"));
    }

    @ParameterizedTest
    @MethodSource("unexpectedFailures")
    void unexpectedFailuresRemainErrorsRatherThanAbsenceOrUnavailability(RuntimeException failure) {
        when(persistence.load(ID)).thenThrow(failure);
        assertThat(evaluator.assess(ID)).isEqualTo(ProductVisibilityReadiness.ERROR);
    }

    @Test
    void rejectsMissingDependenciesAndIdentifier() {
        assertThatNullPointerException().isThrownBy(() -> new AssessProductVisibilityReadiness(null));
        assertThatNullPointerException().isThrownBy(() -> evaluator.assess(null));
        verifyNoMoreInteractions(persistence);
    }
}
