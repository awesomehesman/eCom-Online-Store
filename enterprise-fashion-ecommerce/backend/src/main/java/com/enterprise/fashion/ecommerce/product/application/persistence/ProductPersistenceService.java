package com.enterprise.fashion.ecommerce.product.application.persistence;

import java.util.Objects;
import java.util.Optional;

import com.enterprise.fashion.ecommerce.product.application.port.out.ProductPersistencePort;
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

    private void requireOwnBoundary() {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException("Product persistence service must own its transaction boundary");
        }
    }
}
