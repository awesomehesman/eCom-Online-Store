package com.enterprise.fashion.ecommerce.product.application.port.out;

import java.util.Optional;

import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;

/** Product-owned insert/load boundary; no replacement, update or publication authority. */
public interface ProductPersistencePort {
    /** Atomically inserts the aggregate; existing identities are conflicts, never overwritten. */
    void create(Product product);

    /** Returns absence only when the Product does not exist; invalid stored state fails. */
    Optional<Product> load(ProductId id);
}
