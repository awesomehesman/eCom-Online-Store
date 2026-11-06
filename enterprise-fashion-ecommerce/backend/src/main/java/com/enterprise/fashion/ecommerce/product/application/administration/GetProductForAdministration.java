package com.enterprise.fashion.ecommerce.product.application.administration;

import java.util.Objects;

import com.enterprise.fashion.ecommerce.product.application.persistence.ProductPersistenceService;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;

/** Internal query; a future Staff adapter must enforce governed authorization before invoking it. */
public final class GetProductForAdministration {
    private final ProductPersistenceService products;

    public GetProductForAdministration(ProductPersistenceService products) {
        this.products = Objects.requireNonNull(products);
    }

    public ProductAdministrationView get(ProductId id) {
        return products.load(Objects.requireNonNull(id)).map(ProductAdministrationView::from).orElseThrow(NotFound::new);
    }

    /** Confirmed absence only. Database, corruption and unexpected failures are not translated. */
    public static final class NotFound extends RuntimeException {
        public NotFound() { super("Product not found for internal administration"); }
    }
}
