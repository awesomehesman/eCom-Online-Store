package com.enterprise.fashion.ecommerce.product.application.persistence;

import java.util.Objects;
import java.util.Optional;

import com.enterprise.fashion.ecommerce.product.application.port.out.ProductPersistencePort;
import com.enterprise.fashion.ecommerce.product.application.catalogue.ProductIdPage;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Application-owned create/load transaction boundary. Rejects ambient transactions so
 * completion is explicit: no hidden suspension, partial commit or inherited weaker isolation.
 * This persistence foundation provides no publication or lifecycle-transition authority.
 */
public final class ProductPersistenceService {
    private final ProductPersistencePort persistence;
    private final TransactionOperations writes;
    private final TransactionOperations reads;

    public ProductPersistenceService(ProductPersistencePort persistence,
            TransactionOperations writes, TransactionOperations reads) {
        this.persistence = Objects.requireNonNull(persistence);
        this.writes = Objects.requireNonNull(writes);
        this.reads = Objects.requireNonNull(reads);
    }

    public void create(Product product) {
        Objects.requireNonNull(product);
        requireOwnBoundary();
        writes.executeWithoutResult(status -> persistence.create(product));
    }

    public Optional<Product> load(ProductId id) {
        Objects.requireNonNull(id);
        requireOwnBoundary();
        return Objects.requireNonNull(reads.execute(status -> persistence.load(id)),
                "Product persistence returned no observation");
    }

    /**
     * Live internal keyset traversal, not customer-visible listing. Each call observes its own
     * snapshot; later insertions at/before the boundary may be missed, never replayed.
     * Omitted size means 20 even on continuation and must match the bound size.
     */
    public ProductIdPage pageProductIds(Integer requestedSize, ProductIdPage.Cursor cursor) {
        int size = requestedSize == null ? ProductIdPage.DEFAULT_SIZE : requestedSize;
        ProductIdPage.validateSize(size);
        if (cursor != null && cursor.pageSize() != size) {
            throw new IllegalArgumentException("Product continuation page size mismatch");
        }
        requireOwnBoundary();
        return Objects.requireNonNull(reads.execute(status -> {
            var ids = Objects.requireNonNull(persistence.findProductIdsAfter(
                    cursor == null ? null : cursor.afterExclusive(), size + 1));
            boolean more = ids.size() > size;
            var page = ids.subList(0, Math.min(size, ids.size()));
            return new ProductIdPage(page, size, more ? new ProductIdPage.Cursor(page.getLast(), size) : null);
        }), "Product persistence returned no ID page");
    }

    private void requireOwnBoundary() {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException("Product persistence service must own its transaction boundary");
        }
    }
}
