package com.enterprise.fashion.ecommerce.product.application.administration;

import java.util.List;
import java.util.Optional;

import com.enterprise.fashion.ecommerce.product.application.catalogue.ProductIdPage;
import com.enterprise.fashion.ecommerce.product.application.persistence.ProductPersistenceService;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductPersistencePort;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductAttributeStandard;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductFamily;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.transaction.support.TransactionOperations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class ProductAdministrationQueriesTest {
    private final ProductPersistencePort port = mock(ProductPersistencePort.class);
    private final ProductPersistenceService persistence = new ProductPersistenceService(port,
            TransactionOperations.withoutTransaction(), TransactionOperations.withoutTransaction());
    private final GetProductForAdministration get = new GetProductForAdministration(persistence);
    private final ListProductsForAdministration list = new ListProductsForAdministration(persistence);
    private final Product product = new Product(new ProductId("A"), ProductFamily.ACCESSORIES,
            ProductAttributeStandard.INITIAL, false, false);

    @Test
    void missingProductIsDistinctFromDatabaseOrUnexpectedFailure() {
        when(port.load(product.id())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> get.get(product.id())).isInstanceOf(GetProductForAdministration.NotFound.class);
        var unavailable = new DataAccessResourceFailureException("private SQL");
        when(port.load(product.id())).thenThrow(unavailable);
        assertThatThrownBy(() -> get.get(product.id())).isSameAs(unavailable);
        // Reset the throwing stub without invoking it again.
        org.mockito.Mockito.doThrow(new IllegalStateException("unexpected")).when(port).load(product.id());
        assertThatThrownBy(() -> get.get(product.id())).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void pageUsesOneBulkLoadAndInternalExclusiveCursor() {
        var later = new ProductId("B");
        when(port.findProductIdsAfter(null, 2)).thenReturn(List.of(product.id(), later));
        when(port.loadAll(List.of(product.id()))).thenReturn(List.of(product));
        var page = list.list(new ListProductsForAdministration.PageRequest(1, null));
        assertThat(page.items()).extracting(ProductAdministrationView::id).containsExactly(product.id());
        assertThat(page.nextCursor()).isEqualTo(new ProductIdPage.Cursor(product.id(), 1));
        assertThatThrownBy(() -> page.items().clear()).isInstanceOf(UnsupportedOperationException.class);
        verify(port).findProductIdsAfter(null, 2);
        verify(port).loadAll(List.of(product.id()));
        verifyNoMoreInteractions(port);
    }

    @Test
    void emptyDefaultPageHasNoCursorAndNeverUsesVisibilityFiltering() {
        when(port.findProductIdsAfter(null, 21)).thenReturn(List.of());
        when(port.loadAll(List.of())).thenReturn(List.of());
        var page = list.list(new ListProductsForAdministration.PageRequest(null, null));
        assertThat(page.items()).isEmpty();
        assertThat(page.pageSize()).isEqualTo(20);
        assertThat(page.nextCursor()).isNull();
        verify(port).findProductIdsAfter(null, 21);
        verify(port).loadAll(List.of());
        verifyNoMoreInteractions(port);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 101, Integer.MAX_VALUE})
    void rejectsInvalidPageSize(int size) {
        assertThatIllegalArgumentException().isThrownBy(() -> new ListProductsForAdministration.PageRequest(size, null));
    }

    @Test
    void rejectsContinuationSizeMismatchRatherThanRestarting() {
        var cursor = new ProductIdPage.Cursor(product.id(), 1);
        assertThatIllegalArgumentException().isThrownBy(() -> new ListProductsForAdministration.PageRequest(null, cursor));
        assertThatIllegalArgumentException().isThrownBy(() -> persistence.pageForAdministration(20, cursor));
    }

    @Test
    void incompleteBatchIsAnErrorNotPartialSuccess() {
        when(port.findProductIdsAfter(null, 21)).thenReturn(List.of(product.id()));
        when(port.loadAll(List.of(product.id()))).thenReturn(List.of());
        assertThatThrownBy(() -> list.list(new ListProductsForAdministration.PageRequest(null, null)))
                .isInstanceOf(IllegalStateException.class).hasMessage("Incomplete Product administration observation");
    }
}
