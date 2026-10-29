package com.enterprise.fashion.ecommerce.product.application.usecase;

import java.util.Objects;

import com.enterprise.fashion.ecommerce.product.application.persistence.ProductPersistenceService;
import com.enterprise.fashion.ecommerce.product.application.visibility.ProductVisibilityReadiness;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.transaction.CannotCreateTransactionException;

/**
 * Read-only readiness assessment under REQ-PRD-018–023 and BPRD-REQ-021–023.
 * Uses the existing application-owned consistent-read transaction, never the raw persistence
 * Port. Lifecycle and structurally valid Variants cannot manufacture publication evidence.
 * No result authorizes customer visibility, publication or another Product mutation.
 */
public final class AssessProductVisibilityReadiness {
    private final ProductPersistenceService persistence;

    public AssessProductVisibilityReadiness(ProductPersistenceService persistence) {
        this.persistence = Objects.requireNonNull(persistence);
    }

    public ProductVisibilityReadiness assess(ProductId id) {
        Objects.requireNonNull(id);
        try {
            var product = persistence.load(id);
            if (product.isEmpty()) {
                return ProductVisibilityReadiness.NOT_FOUND;
            }
            return switch (product.get().lifecycle()) {
                case DRAFT, ARCHIVED -> ProductVisibilityReadiness.NOT_VISIBLE;
                case PUBLISHED -> ProductVisibilityReadiness.EVIDENCE_UNCONFIRMED;
            };
        } catch (DataAccessResourceFailureException | TransientDataAccessResourceException
                | QueryTimeoutException | CannotCreateTransactionException unavailable) {
            // Explicit resource/timeout failures only; absence is never inferred from failure.
            return ProductVisibilityReadiness.AUTHORITY_UNAVAILABLE;
        } catch (RuntimeException unexpected) {
            // Includes malformed stored representations and transaction-context violations.
            // Never return or log raw exception details or protected Product information.
            return ProductVisibilityReadiness.ERROR;
        }
    }
}
