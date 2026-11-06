package com.enterprise.fashion.ecommerce.product.application.administration;

import java.util.Objects;

import com.enterprise.fashion.ecommerce.product.application.catalogue.ProductIdPage;
import com.enterprise.fashion.ecommerce.product.application.persistence.ProductPersistenceService;

/** All lifecycle states; no customer eligibility assessment or public cursor protocol. */
public final class ListProductsForAdministration {
    private final ProductPersistenceService products;

    public ListProductsForAdministration(ProductPersistenceService products) {
        this.products = Objects.requireNonNull(products);
    }

    public ProductAdministrationPage list(PageRequest request) {
        Objects.requireNonNull(request);
        return products.pageForAdministration(request.pageSize(), request.cursor());
    }

    public record PageRequest(Integer pageSize, ProductIdPage.Cursor cursor) {
        public PageRequest {
            pageSize = pageSize == null ? ProductIdPage.DEFAULT_SIZE : pageSize;
            ProductIdPage.validateSize(pageSize);
            if (cursor != null && cursor.pageSize() != pageSize) {
                throw new IllegalArgumentException("Product administration continuation size mismatch");
            }
        }
    }
}
