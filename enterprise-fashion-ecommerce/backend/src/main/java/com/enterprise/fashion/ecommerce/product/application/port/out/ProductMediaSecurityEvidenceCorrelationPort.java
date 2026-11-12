package com.enterprise.fashion.ecommerce.product.application.port.out;

import java.util.List;
import java.util.Optional;

import com.enterprise.fashion.ecommerce.product.application.media.ProductImageAssociationBinding;
import com.enterprise.fashion.ecommerce.product.application.media.ProductImageMalwareObservation;
import com.enterprise.fashion.ecommerce.product.application.media.ProductMediaSecurityCheckResult;
import com.enterprise.fashion.ecommerce.product.application.media.ProductMediaSecurityEvidenceCorrelation.Subject;

/** Read-only persistence boundary for exact, non-authoritative media evidence correlation. */
public interface ProductMediaSecurityEvidenceCorrelationPort {
    Optional<ProductImageAssociationBinding> exactBinding(Subject subject);

    List<ProductMediaSecurityCheckResult> technicalObservations(Subject subject);

    List<ProductImageMalwareObservation> malwareObservations(Subject subject);
}
