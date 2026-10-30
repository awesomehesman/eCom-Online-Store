package com.enterprise.fashion.ecommerce.product.application.administration;

import java.util.List;
import java.util.Objects;

import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductAttributeStandard;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductContent;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductFamily;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductLifecycle;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductMedia;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductRevision;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariant;

/** Internal editing snapshot only; no evidence history, Staff claims or visibility authority. */
public record ProductAdministrationView(ProductId id, ProductLifecycle lifecycle, ProductRevision revision,
        ProductFamily family, ProductAttributeStandard standard, boolean sizeApplicable, boolean colourApplicable,
        ProductContent content, List<ProductVariant> variants, List<ProductMedia> media) {
    public ProductAdministrationView {
        Objects.requireNonNull(id);
        Objects.requireNonNull(lifecycle);
        Objects.requireNonNull(revision);
        Objects.requireNonNull(family);
        Objects.requireNonNull(standard);
        variants = List.copyOf(variants);
        media = List.copyOf(media);
    }

    public static ProductAdministrationView from(Product product) {
        return new ProductAdministrationView(product.id(), product.lifecycle(), product.revision(), product.family(),
                product.standard(), product.sizeApplicable(), product.colourApplicable(), product.content(), product.variants(), product.media());
    }

    @Override
    public String toString() { return "ProductAdministrationView[internal]"; }
}
