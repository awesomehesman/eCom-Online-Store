package com.enterprise.fashion.ecommerce.product.domain.model;

import java.net.URI;

/**
 * Immutable Product-owned association metadata, never media acceptance or public eligibility.
 * assetRevision is an opaque immutable asset-version reference, not a mutable storage location.
 * No asset is fetched: content identity, rights and security still require authoritative evidence.
 */
public record ProductMedia(String id, ProductId productId, ProductVariantId variantId,
        String assetRevision, String url, String alternativeText) {
    public ProductMedia {
        if (id == null || !id.matches("[A-Za-z0-9_-]{1,128}") || productId == null
                || assetRevision == null || !assetRevision.matches("[A-Za-z0-9_-]{1,128}")
                || alternativeText == null || alternativeText.isBlank() || url == null
                || alternativeText.indexOf('\0') >= 0 || url.indexOf('\0') >= 0
                || !java.nio.charset.StandardCharsets.UTF_8.newEncoder().canEncode(alternativeText)
                || !java.nio.charset.StandardCharsets.UTF_8.newEncoder().canEncode(url)) {
            throw new IllegalArgumentException("Invalid Product media metadata");
        }
        URI reference;
        try {
            reference = URI.create(url);
        } catch (IllegalArgumentException invalid) {
            throw new IllegalArgumentException("Invalid Product media reference");
        }
        if (!"https".equals(reference.getScheme()) || reference.getHost() == null
                || reference.getRawUserInfo() != null) {
            throw new IllegalArgumentException("Invalid Product media reference");
        }
        // Syntax alone cannot prove a URL is public, credential-free or otherwise accepted.
    }

    @Override
    public String toString() { return "ProductMedia[internal]"; }
}
