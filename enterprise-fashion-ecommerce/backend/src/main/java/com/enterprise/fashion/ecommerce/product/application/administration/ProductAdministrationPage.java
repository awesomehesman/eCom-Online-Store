package com.enterprise.fashion.ecommerce.product.application.administration;

import java.util.List;

import com.enterprise.fashion.ecommerce.product.application.catalogue.ProductIdPage;

/** Internal live keyset page, never a public catalogue page or cross-request snapshot. */
public record ProductAdministrationPage(List<ProductAdministrationView> items, int pageSize, ProductIdPage.Cursor nextCursor) {
    public ProductAdministrationPage {
        items = List.copyOf(items);
        // Reuse the existing bounded page and continuation invariant rather than redefine it.
        new ProductIdPage(items.stream().map(ProductAdministrationView::id).toList(), pageSize, nextCursor);
    }

    @Override
    public String toString() { return "ProductAdministrationPage[internal]"; }
}
