package com.enterprise.fashion.ecommerce.product.application.evidence;

import java.util.List;
import java.util.Objects;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductEvidenceObservationPort;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductPersistencePort;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/** Application-owned observation transaction only. There is deliberately no acceptance or writing API. */
public final class ProductEvidenceService {
    private final ProductPersistencePort products;
    private final ProductEvidenceObservationPort evidence;
    private final TransactionOperations reads;

    public ProductEvidenceService(ProductPersistencePort products, ProductEvidenceObservationPort evidence,
            TransactionOperations reads) {
        this.products = Objects.requireNonNull(products);
        this.evidence = Objects.requireNonNull(evidence);
        this.reads = Objects.requireNonNull(reads);
    }

    public ProductEvidenceObservation observe(ProductId id, ProductEvidenceEvent.Purpose purpose) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(purpose);
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException("Product evidence service must own its transaction");
        }
        return Objects.requireNonNull(reads.execute(status -> {
            var product = products.load(id);
            if (product.isEmpty()) {
                return new ProductEvidenceObservation(ProductEvidenceObservation.State.PRODUCT_NOT_FOUND, null, List.of());
            }
            return ProductEvidenceObservation.inspect(id, product.get().revision(), purpose, evidence.history(id, purpose));
        }));
    }
}
