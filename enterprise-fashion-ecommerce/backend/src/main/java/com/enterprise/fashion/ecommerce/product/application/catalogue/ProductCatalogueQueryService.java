package com.enterprise.fashion.ecommerce.product.application.catalogue;

import java.util.Objects;

import com.enterprise.fashion.ecommerce.product.application.persistence.ProductPersistenceService;
import com.enterprise.fashion.ecommerce.product.application.usecase.AssessProductVisibilityReadiness;
import com.enterprise.fashion.ecommerce.product.application.visibility.ProductVisibilityReadiness;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.transaction.CannotCreateTransactionException;

/**
 * Customer-query orchestration with no positive visibility producer. Never returns aggregates.
 * Existing application persistence boundaries own each database transaction. Internal candidate
 * traversal is not eligible pagination or a snapshot spanning HTTP requests.
 */
public final class ProductCatalogueQueryService {
    public enum ListingOutcome { CONFIRMED_EMPTY, AUTHORITY_UNAVAILABLE, ERROR }

    private final ProductPersistenceService products;
    private final AssessProductVisibilityReadiness readiness;

    public ProductCatalogueQueryService(ProductPersistenceService products, AssessProductVisibilityReadiness readiness) {
        this.products = Objects.requireNonNull(products);
        this.readiness = Objects.requireNonNull(readiness);
    }

    public ProductVisibilityReadiness detail(ProductId id) {
        return readiness.assess(Objects.requireNonNull(id));
    }

    public ListingOutcome list(int size) {
        return list(size, null);
    }

    public ListingOutcome list(int size, ProductIdPage.Cursor cursor) {
        ProductIdPage.validateSize(size);
        if (cursor != null && cursor.pageSize() != size) {
            throw new IllegalArgumentException("Incompatible continuation");
        }
        try {
            ProductIdPage candidates = products.pageCatalogueCandidateIds(size, cursor);
            for (ProductId id : candidates.ids()) {
                switch (readiness.assess(id)) {
                    case NOT_FOUND, NOT_VISIBLE -> { }
                    case EVIDENCE_UNCONFIRMED, AUTHORITY_UNAVAILABLE -> { return ListingOutcome.AUTHORITY_UNAVAILABLE; }
                    case ERROR -> { return ListingOutcome.ERROR; }
                }
            }
            // One bounded candidate page per request. Unexamined candidates cannot be omitted
            // to fabricate empty success, and their IDs must never become public cursors.
            return candidates.nextCursor() == null ? ListingOutcome.CONFIRMED_EMPTY : ListingOutcome.AUTHORITY_UNAVAILABLE;
        } catch (DataAccessResourceFailureException | TransientDataAccessResourceException
                | QueryTimeoutException | CannotCreateTransactionException unavailable) {
            return ListingOutcome.AUTHORITY_UNAVAILABLE;
        } catch (RuntimeException unexpected) {
            return ListingOutcome.ERROR;
        }
    }
}
