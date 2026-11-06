package com.enterprise.fashion.ecommerce.pricing.application.persistence;

import java.util.List;
import java.util.Objects;
import com.enterprise.fashion.ecommerce.pricing.application.port.out.BasePricePersistencePort;
import com.enterprise.fashion.ecommerce.pricing.domain.model.Money;
import com.enterprise.fashion.ecommerce.pricing.domain.model.VariantBasePriceAssignment;
import com.enterprise.fashion.ecommerce.pricing.domain.model.VariantBasePriceAssignment.Subject;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Application-owned internal persistence boundary, not an operational Pricing command API.
 * No trusted Product/Staff evidence is available here. Stored observations remain conditional
 * and must not be presented as authorized, current commercial Pricing truth.
 */
public final class BasePricePersistenceService {
    private final BasePricePersistencePort persistence;
    private final TransactionOperations writes;
    private final TransactionOperations reads;

    public BasePricePersistenceService(BasePricePersistencePort persistence, TransactionOperations writes, TransactionOperations reads) {
        this.persistence = Objects.requireNonNull(persistence);
        this.writes = Objects.requireNonNull(writes);
        this.reads = Objects.requireNonNull(reads);
    }
    public void initialize(Subject subject, String revision, Money money) {
        new VariantBasePriceAssignment(subject, revision, money, VariantBasePriceAssignment.State.CURRENT);
        boundary();
        writes.executeWithoutResult(status -> persistence.initialize(subject, revision, money));
    }
    public void replace(Subject subject, String expectedRevision, String revision, Money money) {
        new VariantBasePriceAssignment(subject, revision, money, VariantBasePriceAssignment.State.CURRENT);
        expected(expectedRevision, revision);
        boundary();
        writes.executeWithoutResult(status -> persistence.replace(subject, expectedRevision, revision, money));
    }
    public void withdraw(Subject subject, String expectedRevision, String revision) {
        Objects.requireNonNull(subject);
        expected(expectedRevision, revision);
        boundary();
        writes.executeWithoutResult(status -> persistence.withdraw(subject, expectedRevision, revision));
    }
    public List<VariantBasePriceAssignment> load(Subject subject) {
        Objects.requireNonNull(subject);
        boundary();
        return List.copyOf(Objects.requireNonNull(reads.execute(status -> persistence.load(subject))));
    }
    private static void expected(String expected, String revision) {
        if (expected == null || expected.isBlank() || revision == null || revision.isBlank() || expected.equals(revision)) {
            throw new IllegalArgumentException("Distinct explicit revision references required");
        }
    }
    private static void boundary() {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException("Pricing persistence service must own its transaction boundary");
        }
    }
}
