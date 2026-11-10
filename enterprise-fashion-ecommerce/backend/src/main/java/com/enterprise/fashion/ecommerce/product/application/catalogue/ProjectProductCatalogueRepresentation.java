package com.enterprise.fashion.ecommerce.product.application.catalogue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductAttributeStandard;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductColour;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductContent;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductFamily;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductMedia;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductSize;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariant;

/**
 * Pure representation mapping. The caller must establish current eligibility before invoking it;
 * this class reads no lifecycle/evidence, performs no I/O and grants no visibility or authority.
 */
public final class ProjectProductCatalogueRepresentation {

    public ProductCatalogueRepresentation project(EligibleProductRepresentation eligible) {
        Objects.requireNonNull(eligible, "Eligible Product representation is required");
        List<String> defining = new ArrayList<>(2);
        if (eligible.sizeApplicable()) { defining.add("Size"); }
        if (eligible.colourApplicable()) { defining.add("Colour"); }

        return new ProductCatalogueRepresentation(
                eligible.productId().value(),
                eligible.content().name(),
                eligible.content().description(),
                eligible.family().value(),
                eligible.standard().version(),
                defining,
                eligible.variants().stream().map(this::variant).toList(),
                eligible.media().stream().map(this::media).toList());
    }

    private ProductCatalogueRepresentation.Variant variant(ProductVariant variant) {
        var attributes = new LinkedHashMap<String, Object>(2);
        ProductSize size = variant.configuration().size();
        if (size != null) { attributes.put("size", size(size)); }
        ProductColour colour = variant.configuration().colour();
        if (colour != null) {
            attributes.put("colour", new ProductCatalogueRepresentation.Colour(colour.code(), colour.label()));
        }
        return new ProductCatalogueRepresentation.Variant(variant.id().value(), attributes);
    }

    private ProductCatalogueRepresentation.Size size(ProductSize size) {
        if (size.family().equals(ProductFamily.APPAREL)) {
            return new ProductCatalogueRepresentation.ApparelSize("APPAREL", size.code(), size.label());
        }
        if (size.family().equals(ProductFamily.FOOTWEAR)) {
            return new ProductCatalogueRepresentation.FootwearSize(
                    "FOOTWEAR", "UK", new BigDecimal(size.code()), size.label());
        }
        return new ProductCatalogueRepresentation.AccessorySize("ACCESSORY", size.code(), size.label());
    }

    private ProductCatalogueRepresentation.Media media(ProductMedia media) {
        return new ProductCatalogueRepresentation.Media(media.id(), media.url(), media.alternativeText(),
                media.variantId() == null ? null : media.variantId().value());
    }

    /**
     * Lifecycle-free, immutable input. Its name records a caller assertion, not verified evidence.
     * Rebuilding the aggregate applies the canonical ownership, membership and uniqueness rules.
     */
    public record EligibleProductRepresentation(
            ProductId productId,
            ProductContent content,
            ProductFamily family,
            ProductAttributeStandard standard,
            boolean sizeApplicable,
            boolean colourApplicable,
            List<ProductVariant> variants,
            List<ProductMedia> media) {

        public EligibleProductRepresentation {
            if (productId == null || content == null || family == null || standard == null
                    || variants == null || media == null) {
                throw new IllegalArgumentException("Complete Product representation is required");
            }
            variants = List.copyOf(variants);
            media = List.copyOf(media);
            Product validated = new Product(productId, family, standard, sizeApplicable, colourApplicable);
            for (ProductVariant variant : variants) {
                validated = validated.withVariant(variant);
            }
            validated.withRepresentation(content, media);
        }
    }
}
