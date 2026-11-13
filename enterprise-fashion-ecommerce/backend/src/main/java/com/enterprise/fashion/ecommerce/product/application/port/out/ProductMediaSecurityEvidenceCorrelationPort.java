package com.enterprise.fashion.ecommerce.product.application.port.out;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.product.application.media.ProductImageAssociationBinding;
import com.enterprise.fashion.ecommerce.product.application.media.ProductImageMalwareObservation;
import com.enterprise.fashion.ecommerce.product.application.media.ProductMediaSecurityCheckResult;
import com.enterprise.fashion.ecommerce.product.application.media.ProductMediaSecurityEvidenceCorrelation.Subject;

/** Read-only persistence boundary for exact, non-authoritative media evidence correlation. */
public interface ProductMediaSecurityEvidenceCorrelationPort {
    Optional<ProductImageAssociationBinding> exactBinding(Subject subject);

    List<ProductMediaSecurityCheckResult> technicalObservations(Subject subject);

    List<ProductImageMalwareObservation> malwareObservations(Subject subject);

    /** Returns true if the exact binding has a withdrawal record. */
    boolean isBindingWithdrawn(UUID bindingId);

    /** Returns the set of withdrawn technical check IDs for the given subject's asset identity. */
    Set<UUID> withdrawnTechnicalCheckIds(Subject subject);

    /** Returns the set of withdrawn malware observation IDs for the given subject's asset identity. */
    Set<UUID> withdrawnMalwareObservationIds(Subject subject);
}
