package com.enterprise.fashion.ecommerce.product.adapter.out.publication;

import java.util.Objects;

import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.Snapshot;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductPublicationEvidencePort;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductRevision;

/**
 * Safe production fallback used until an authoritative publication-evidence source is approved and configured.
 * It deliberately does not translate retained unconfirmed Product evidence history into trusted acceptance.
 */
public final class UnavailableProductPublicationEvidenceAdapter implements ProductPublicationEvidencePort {
    @Override
    public Snapshot currentEvidence(ProductId productId, ProductRevision productRevision) {
        Objects.requireNonNull(productId, "productId");
        Objects.requireNonNull(productRevision, "productRevision");
        return Snapshot.unavailable();
    }
}
