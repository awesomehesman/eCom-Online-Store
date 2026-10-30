package com.enterprise.fashion.ecommerce.product.application.catalogue;

import java.util.List;

import com.enterprise.fashion.ecommerce.product.application.persistence.ProductPersistenceService;
import com.enterprise.fashion.ecommerce.product.application.usecase.AssessProductVisibilityReadiness;
import com.enterprise.fashion.ecommerce.product.application.visibility.ProductVisibilityReadiness;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ProductCatalogueQueryServiceTest {
    private final ProductPersistenceService products = mock(ProductPersistenceService.class);
    private final AssessProductVisibilityReadiness readiness = mock(AssessProductVisibilityReadiness.class);
    private final ProductCatalogueQueryService queries = new ProductCatalogueQueryService(products, readiness);
    private final ProductId id = new ProductId("product-1");

    @Test
    void confirmedEmptyCandidateSetReturnsEmptyWithoutInventingVisibility() {
        when(products.pageCatalogueCandidateIds(20, null)).thenReturn(new ProductIdPage(List.of(), 20, null));
        assertThat(queries.list(20)).isEqualTo(ProductCatalogueQueryService.ListingOutcome.CONFIRMED_EMPTY);
        verifyNoInteractions(readiness);
    }

    @ParameterizedTest
    @CsvSource({"NOT_FOUND,CONFIRMED_EMPTY", "NOT_VISIBLE,CONFIRMED_EMPTY",
            "EVIDENCE_UNCONFIRMED,AUTHORITY_UNAVAILABLE", "AUTHORITY_UNAVAILABLE,AUTHORITY_UNAVAILABLE", "ERROR,ERROR"})
    void eachCandidateMustHaveConfirmingNegativeReadinessBeforeEmptySuccess(ProductVisibilityReadiness state,
            ProductCatalogueQueryService.ListingOutcome expected) {
        when(products.pageCatalogueCandidateIds(1, null)).thenReturn(new ProductIdPage(List.of(id), 1, null));
        when(readiness.assess(id)).thenReturn(state);
        assertThat(queries.list(1)).isEqualTo(expected);
        verify(readiness).assess(id);
    }

    @Test
    void cannotSkipUnexaminedCandidatesOrExposeTheirContinuation() {
        when(products.pageCatalogueCandidateIds(1, null)).thenReturn(
                new ProductIdPage(List.of(id), 1, new ProductIdPage.Cursor(id, 1)));
        when(readiness.assess(id)).thenReturn(ProductVisibilityReadiness.NOT_VISIBLE);
        assertThat(queries.list(1)).isEqualTo(ProductCatalogueQueryService.ListingOutcome.AUTHORITY_UNAVAILABLE);
    }

    @Test
    void differentiatesDatabaseUnavailabilityFromUnexpectedQueryFailure() {
        when(products.pageCatalogueCandidateIds(20, null)).thenThrow(new DataAccessResourceFailureException("private failure"));
        assertThat(queries.list(20)).isEqualTo(ProductCatalogueQueryService.ListingOutcome.AUTHORITY_UNAVAILABLE);
        doThrow(new InvalidDataAccessResourceUsageException("private SQL")).when(products).pageCatalogueCandidateIds(20, null);
        assertThat(queries.list(20)).isEqualTo(ProductCatalogueQueryService.ListingOutcome.ERROR);
    }

    @Test
    void continuationUsesValidatedExclusiveBoundaryAndStillRevalidatesVisibility() {
        var cursor = new ProductIdPage.Cursor(new ProductId("before"), 20);
        when(products.pageCatalogueCandidateIds(20, cursor)).thenReturn(new ProductIdPage(List.of(id), 20, null));
        when(readiness.assess(id)).thenReturn(ProductVisibilityReadiness.EVIDENCE_UNCONFIRMED);
        assertThat(queries.list(20, cursor)).isEqualTo(ProductCatalogueQueryService.ListingOutcome.AUTHORITY_UNAVAILABLE);
        verify(products).pageCatalogueCandidateIds(20, cursor);
        verify(readiness).assess(id);
    }

    @Test
    void incompatibleContinuationIsRejectedBeforePersistence() {
        org.assertj.core.api.Assertions.assertThatIllegalArgumentException().isThrownBy(
                () -> queries.list(20, new ProductIdPage.Cursor(id, 1)));
        verifyNoInteractions(products, readiness);
    }

    @Test
    void detailDelegatesToExistingReadinessWithoutSecondProductLoad() {
        when(readiness.assess(id)).thenReturn(ProductVisibilityReadiness.EVIDENCE_UNCONFIRMED);
        assertThat(queries.detail(id)).isEqualTo(ProductVisibilityReadiness.EVIDENCE_UNCONFIRMED);
        verifyNoInteractions(products);
    }
}
