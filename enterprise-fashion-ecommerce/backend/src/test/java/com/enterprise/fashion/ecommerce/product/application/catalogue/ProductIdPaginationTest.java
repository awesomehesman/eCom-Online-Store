package com.enterprise.fashion.ecommerce.product.application.catalogue;

import java.util.ArrayList;
import java.util.List;

import com.enterprise.fashion.ecommerce.product.application.persistence.ProductPersistenceService;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductPersistencePort;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.transaction.support.SimpleTransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionOperations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class ProductIdPaginationTest {
    private final ProductPersistencePort port = mock(ProductPersistencePort.class);
    private final TransactionOperations writes = mock(TransactionOperations.class);
    private final TransactionOperations reads = mock(TransactionOperations.class);
    private final ProductPersistenceService service = new ProductPersistenceService(port, writes, reads);

    @BeforeEach
    void executeReadCallback() {
        when(reads.execute(any())).thenAnswer(invocation ->
                ((TransactionCallback<?>) invocation.getArgument(0)).doInTransaction(new SimpleTransactionStatus()));
    }

    @Test
    void defaultsToTwentyAndUsesBoundedLookaheadWithoutWrites() {
        when(port.findProductIdsAfter(null, 21)).thenReturn(List.of());
        var page = service.pageProductIds(null, null);
        assertThat(page.pageSize()).isEqualTo(20);
        assertThat(page.ids()).isEmpty();
        assertThat(page.nextCursor()).isNull();
        verify(port).findProductIdsAfter(null, 21);
        verifyNoMoreInteractions(port);
        verifyNoInteractions(writes);
    }

    @Test
    void continuationUsesLastReturnedIdAndBindsSize() {
        var a = new ProductId("A");
        var b = new ProductId("B");
        when(port.findProductIdsAfter(null, 2)).thenReturn(List.of(a, b));
        when(port.findProductIdsAfter(a, 2)).thenReturn(List.of(b));
        var first = service.pageProductIds(1, null);
        assertThat(first.ids()).containsExactly(a);
        assertThat(first.nextCursor()).isEqualTo(new ProductIdPage.Cursor(a, 1));
        var second = service.pageProductIds(1, first.nextCursor());
        assertThat(second.ids()).containsExactly(b);
        assertThat(second.nextCursor()).isNull();
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 101, Integer.MAX_VALUE})
    void invalidPageSizeFailsBeforePersistence(int size) {
        assertThatThrownBy(() -> service.pageProductIds(size, null)).isInstanceOf(IllegalArgumentException.class);
        verifyNoInteractions(port);
    }

    @Test
    void rejectsMalformedPositionsAndIncompatibleContinuationWithoutRestarting() {
        assertThatThrownBy(() -> new ProductIdPage.Cursor(null, 20)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new ProductIdPage.Cursor(new ProductId("A"), 0)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new ProductIdPage.Cursor(new ProductId("bad/id"), 20)).isInstanceOf(IllegalArgumentException.class);
        var cursor = new ProductIdPage.Cursor(new ProductId("A"), 10);
        assertThatThrownBy(() -> service.pageProductIds(11, cursor)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> service.pageProductIds(null, cursor)).isInstanceOf(IllegalArgumentException.class);
        verifyNoInteractions(port);
    }

    @Test
    void failureNeverBecomesEmptySuccessOrFallback() {
        when(port.findProductIdsAfter(null, 21)).thenThrow(new DataAccessResourceFailureException("unavailable"));
        assertThatThrownBy(() -> service.pageProductIds(null, null)).isInstanceOf(DataAccessResourceFailureException.class);
    }

    @Test
    void resultMembershipIsImmutableAndContainsOnlyInternalIds() {
        var a = new ProductId("A");
        var input = new ArrayList<>(List.of(a));
        var page = new ProductIdPage(input, 20, null);
        input.clear();
        assertThat(page.ids()).containsExactly(a);
        assertThatThrownBy(() -> page.ids().clear()).isInstanceOf(UnsupportedOperationException.class);
        assertThat(page.toString()).doesNotContain("ids=");
    }
}
