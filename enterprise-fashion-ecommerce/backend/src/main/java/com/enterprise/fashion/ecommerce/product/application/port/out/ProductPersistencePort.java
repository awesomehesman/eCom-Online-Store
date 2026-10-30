package com.enterprise.fashion.ecommerce.product.application.port.out;

import java.util.Optional;
import java.util.List;

import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;

/**
 * Product-owned insert/load boundary; no replacement, update or publication authority.
 * Callers must provide an application-owned transaction: writable for create, and a
 * PostgreSQL repeatable-read or serializable snapshot for load. Failures propagate;
 * the owning application service must roll back failed creation rather than commit it.
 */
public interface ProductPersistencePort {
    /** Atomically inserts the aggregate; existing identities are conflicts, never overwritten. */
    void create(Product product);

    /** Returns absence only when the Product does not exist; invalid stored state fails. */
    Optional<Product> load(ProductId id);

    /**
     * Internal IDs in ascending unsigned UTF-8 byte order, strictly after the optional boundary.
     * Requires a consistent snapshot transaction. Limit is 1–101 (one lookahead beyond a
     * maximum-size page). Includes non-visible Products; failures must propagate.
     */
    List<ProductId> findProductIdsAfter(ProductId afterExclusive, int limit);
    /**
     * Same ordered keyset traversal, excluding only DRAFT/ARCHIVED parents. Candidates are
     * NOT eligible Products: publication evidence and current readiness remain unconfirmed.
     * Protected non-visible rows must not influence public page positions or scan budgets.
     */
    List<ProductId> findCatalogueCandidateIdsAfter(ProductId afterExclusive, int limit);
}
