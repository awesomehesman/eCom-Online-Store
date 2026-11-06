package com.enterprise.fashion.ecommerce.product.application.catalogue;

import java.util.List;

import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;

/**
 * Internal identity traversal only, including non-visible Products. Never a public catalogue
 * page or publication evidence. Public traversal must independently evaluate current visibility
 * and supply its own opaque cursor without protected identity data.
 */
public record ProductIdPage(List<ProductId> ids, int pageSize, Cursor nextCursor) {
    public static final int DEFAULT_SIZE = 20;
    public static final int MAX_SIZE = 100;

    public ProductIdPage {
        validateSize(pageSize);
        ids = List.copyOf(ids);
        if (ids.size() > pageSize || (nextCursor != null && (ids.isEmpty()
                || nextCursor.pageSize() != pageSize || !nextCursor.afterExclusive().equals(ids.getLast())))) {
            throw new IllegalArgumentException("Invalid internal Product ID page");
        }
    }

    public static void validateSize(int size) {
        if (size < 1 || size > MAX_SIZE) {
            throw new IllegalArgumentException("Product ID page size must be between 1 and 100");
        }
    }

    /**
     * Typed internal position, not the HTTP Contract's opaque wire cursor. It binds the size
     * and exact exclusive ID boundary; it grants no existence, publication or visibility authority.
     * No string cursor decoding, wire format, expiry or cryptographic policy is introduced.
     */
    public record Cursor(ProductId afterExclusive, int pageSize) {
        public Cursor {
            if (afterExclusive == null) {
                throw new IllegalArgumentException("Product continuation position is required");
            }
            validateSize(pageSize);
        }

        @Override
        public String toString() { return "ProductIdPage.Cursor[internal]"; }
    }

    @Override
    public String toString() { return "ProductIdPage[internal]"; }
}
