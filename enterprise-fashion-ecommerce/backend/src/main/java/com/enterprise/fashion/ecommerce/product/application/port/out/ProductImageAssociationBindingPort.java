package com.enterprise.fashion.ecommerce.product.application.port.out;

import java.util.Optional;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.application.media.ProductImageAssociationBinding;
import com.enterprise.fashion.ecommerce.product.application.media.QuarantinedProductImageAsset;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductRevision;

/** Internal persistence boundary for non-authoritative quarantine-to-media bindings. */
public interface ProductImageAssociationBindingPort {
    Optional<QuarantinedProductImageAsset> quarantinedAsset(UUID assetId);

    void append(ProductImageAssociationBinding binding);

    Optional<ProductImageAssociationBinding> exact(ProductId productId, ProductRevision productRevision,
            String mediaAssociationId, UUID assetId);
}
