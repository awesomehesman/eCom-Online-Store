package com.enterprise.fashion.ecommerce.product.application.port.out;

import java.util.List;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductEvidenceEvent;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;

/** Read-only internal history port. Requires an application-owned consistent snapshot transaction. */
public interface ProductEvidenceObservationPort {
    List<ProductEvidenceEvent> history(ProductId product, ProductEvidenceEvent.Purpose purpose);
}
