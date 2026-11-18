package com.enterprise.fashion.ecommerce.product.application.catalogue;

import java.util.List;

import com.enterprise.fashion.ecommerce.product.domain.model.Product;

/**
 * One internally consistent Product-backed catalogue candidate page. Candidates are not visible
 * until the caller independently establishes current visibility for the exact loaded aggregate.
 */
public record ProductCatalogueCandidatePage(List<Product> products, int pageSize, ProductIdPage.Cursor nextCursor) {
    public ProductCatalogueCandidatePage {
        ProductIdPage.validateSize(pageSize);
        products = List.copyOf(products);
        if (products.size() > pageSize || (nextCursor != null && (products.isEmpty()
                || nextCursor.pageSize() != pageSize
                || !nextCursor.afterExclusive().equals(products.getLast().id())))) {
            throw new IllegalArgumentException("Invalid Product catalogue candidate page");
        }
    }

    @Override
    public String toString() { return "ProductCatalogueCandidatePage[internal]"; }
}
