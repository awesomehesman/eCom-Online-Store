package com.enterprise.fashion.ecommerce.product.application.catalogue;

import java.util.List;
import java.util.Optional;

import com.enterprise.fashion.ecommerce.product.application.persistence.ProductPersistenceService;
import com.enterprise.fashion.ecommerce.product.application.usecase.AssessProductVisibilityReadiness;
import com.enterprise.fashion.ecommerce.product.application.visibility.ProductVisibilityReadiness;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductAttributeStandard;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductContent;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductFamily;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ProductCatalogueQueryServiceTest {
    private final ProductPersistenceService products = mock(ProductPersistenceService.class);
    private final AssessProductVisibilityReadiness readiness = mock(AssessProductVisibilityReadiness.class);
    private final ProductCatalogueQueryService queries = new ProductCatalogueQueryService(
            products, readiness, new ProjectProductCatalogueRepresentation());
    private final Product product = new Product(new ProductId("product-1"), ProductFamily.ACCESSORIES,
            ProductAttributeStandard.INITIAL, false, false)
            .withRepresentation(new ProductContent("Product", "Catalogue description"), List.of());

    @Test
    void confirmedEmptyCandidateSetReturnsAContractPageWithoutInventingVisibility() {
        when(products.pageCatalogueCandidates(20, null))
                .thenReturn(new ProductCatalogueCandidatePage(List.of(), 20, null));

        assertThat(queries.list(20)).isEqualTo(new ProductCatalogueQueryService.Page(List.of(), 20, null));
        verifyNoInteractions(readiness);
    }

    @ParameterizedTest
    @EnumSource(value = ProductVisibilityReadiness.class, names = {"NOT_FOUND", "NOT_VISIBLE",
            "EVIDENCE_UNCONFIRMED", "AUTHORITY_UNAVAILABLE"})
    void nonvisibleCandidateCannotManufactureEmptySuccess(ProductVisibilityReadiness state) {
        when(products.pageCatalogueCandidates(1, null))
                .thenReturn(new ProductCatalogueCandidatePage(List.of(product), 1, null));
        when(readiness.assess(product)).thenReturn(state);

        assertThat(queries.list(1)).isInstanceOf(ProductCatalogueQueryService.AuthorityUnavailable.class);
    }

    @Test
    void visibleExactCandidateIsProjectedAndKeepsItsExclusiveContinuation() {
        var cursor = new ProductIdPage.Cursor(product.id(), 1);
        when(products.pageCatalogueCandidates(1, null))
                .thenReturn(new ProductCatalogueCandidatePage(List.of(product), 1, cursor));
        when(readiness.assess(product)).thenReturn(ProductVisibilityReadiness.VISIBLE);

        var result = queries.list(1);

        assertThat(result).isInstanceOfSatisfying(ProductCatalogueQueryService.Page.class, page -> {
            assertThat(page.items()).singleElement().satisfies(item -> {
                assertThat(item.productId()).isEqualTo(product.id().value());
                assertThat(item.name()).isEqualTo("Product");
            });
            assertThat(page.nextCursor()).isEqualTo(cursor);
        });
    }

    @Test
    void differentiatesPersistenceUnavailabilityFromUnexpectedFailure() {
        when(products.pageCatalogueCandidates(20, null))
                .thenThrow(new DataAccessResourceFailureException("private failure"));
        assertThat(queries.list(20)).isInstanceOf(ProductCatalogueQueryService.AuthorityUnavailable.class);

        doThrow(new InvalidDataAccessResourceUsageException("private SQL"))
                .when(products).pageCatalogueCandidates(20, null);
        assertThat(queries.list(20)).isInstanceOf(ProductCatalogueQueryService.QueryError.class);
    }

    @Test
    void continuationUsesValidatedExclusiveBoundaryAndExactLoadedRepresentation() {
        var cursor = new ProductIdPage.Cursor(new ProductId("before"), 20);
        when(products.pageCatalogueCandidates(20, cursor))
                .thenReturn(new ProductCatalogueCandidatePage(List.of(product), 20, null));
        when(readiness.assess(product)).thenReturn(ProductVisibilityReadiness.EVIDENCE_UNCONFIRMED);

        assertThat(queries.list(20, cursor))
                .isInstanceOf(ProductCatalogueQueryService.AuthorityUnavailable.class);
        verify(products).pageCatalogueCandidates(20, cursor);
        verify(readiness).assess(product);
    }

    @Test
    void incompatibleContinuationIsRejectedBeforePersistence() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> queries.list(20, new ProductIdPage.Cursor(product.id(), 1)));
        verifyNoInteractions(products, readiness);
    }

    @Test
    void detailLoadsOnceAndProjectsOnlyAnExplicitVisibleResult() {
        when(products.load(product.id())).thenReturn(Optional.of(product));
        when(readiness.assess(product)).thenReturn(ProductVisibilityReadiness.VISIBLE);

        assertThat(queries.detail(product.id()))
                .isInstanceOfSatisfying(ProductCatalogueQueryService.Found.class,
                        found -> assertThat(found.product().productId()).isEqualTo(product.id().value()));
        verify(products).load(product.id());
        verify(readiness).assess(product);
    }

    @Test
    void detailConcealsAbsenceAndIneligibilityButSeparatesUnavailableEvidence() {
        when(products.load(product.id())).thenReturn(Optional.empty());
        assertThat(queries.detail(product.id())).isInstanceOf(ProductCatalogueQueryService.NotFound.class);

        when(products.load(product.id())).thenReturn(Optional.of(product));
        when(readiness.assess(product)).thenReturn(ProductVisibilityReadiness.NOT_VISIBLE);
        assertThat(queries.detail(product.id())).isInstanceOf(ProductCatalogueQueryService.NotVisible.class);

        when(readiness.assess(product)).thenReturn(ProductVisibilityReadiness.EVIDENCE_UNCONFIRMED);
        assertThat(queries.detail(product.id()))
                .isInstanceOf(ProductCatalogueQueryService.DetailAuthorityUnavailable.class);
    }
}
