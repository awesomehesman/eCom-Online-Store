package com.enterprise.fashion.ecommerce.product.application.catalogue;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Framework-neutral public Product wire values from the initial catalogue Contract.
 * Construction carries no publication, eligibility or authorization evidence.
 */
public record ProductCatalogueRepresentation(
        String productId,
        String name,
        String description,
        String family,
        String attributeStandardVersion,
        List<String> variantDefiningAttributes,
        List<Variant> variants,
        List<Media> media) {

    public ProductCatalogueRepresentation {
        variantDefiningAttributes = List.copyOf(variantDefiningAttributes);
        variants = List.copyOf(variants);
        media = List.copyOf(media);
    }

    public record Variant(String variantId, Map<String, Object> attributes) {
        public Variant {
            attributes = java.util.Collections.unmodifiableMap(new LinkedHashMap<>(attributes));
        }
    }

    public sealed interface Size permits ApparelSize, FootwearSize, AccessorySize { }

    public record ApparelSize(String kind, String code, String label) implements Size { }

    public record FootwearSize(String kind, String system, BigDecimal value, String label) implements Size { }

    public record AccessorySize(String kind, String code, String label) implements Size { }

    public record Colour(String code, String label) { }

    /** A null variantId is the Contract's Product-level media association. */
    public record Media(String mediaId, String url, String alternativeText, String variantId) { }
}
