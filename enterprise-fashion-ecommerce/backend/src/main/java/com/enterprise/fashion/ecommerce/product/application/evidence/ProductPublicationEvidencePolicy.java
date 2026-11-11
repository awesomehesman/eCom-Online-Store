package com.enterprise.fashion.ecommerce.product.application.evidence;

import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.Source;

/** Policy supplied by an approved composition boundary; this interface approves no producer or check itself. */
@FunctionalInterface
public interface ProductPublicationEvidencePolicy {
    enum Kind { CONTENT_ACCEPTANCE, MEDIA_USAGE_RIGHTS, MEDIA_SECURITY, MEDIA_ASSOCIATION, INVALIDATION }

    boolean supports(Kind kind, Source source);
}
