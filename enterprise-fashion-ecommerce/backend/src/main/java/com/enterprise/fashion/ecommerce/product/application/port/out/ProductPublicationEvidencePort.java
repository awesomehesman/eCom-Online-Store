package com.enterprise.fashion.ecommerce.product.application.port.out;

import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.Snapshot;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductRevision;

/**
 * Trust boundary for current publication evidence. Implementations must authenticate evidence provenance;
 * retained Product evidence-history records are not a valid implementation of this contract by themselves.
 */
@FunctionalInterface
public interface ProductPublicationEvidencePort {
    Snapshot currentEvidence(ProductId productId, ProductRevision productRevision);
}
