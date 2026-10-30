package com.enterprise.fashion.ecommerce.product.domain.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Immutable Product aggregate with controlled Variant membership. Updates return a new
 * aggregate snapshot; callers must retain that result. Concurrent persistence/version
 * control is outside this domain-only slice. No publication, removal, transfer or
 * reconfiguration workflow is authorized by construction or membership acceptance.
 */
public final class Product {
    private final ProductId id;
    private final ProductContent content;
    private final List<ProductMedia> media;
    private final ProductLifecycle lifecycle;
    private final ProductFamily family;
    private final ProductAttributeStandard standard;
    private final boolean sizeApplicable;
    private final boolean colourApplicable;
    private final List<ProductVariant> variants;

    public Product(ProductId id, ProductFamily family, ProductAttributeStandard standard,
            boolean sizeApplicable, boolean colourApplicable) {
        this(id, family, standard, sizeApplicable, colourApplicable, ProductLifecycle.DRAFT, List.of());
    }

    private Product(ProductId id, ProductFamily family, ProductAttributeStandard standard,
            boolean sizeApplicable, boolean colourApplicable, ProductLifecycle lifecycle, List<ProductVariant> variants) {
        this(id, family, standard, sizeApplicable, colourApplicable, lifecycle, variants, null, List.of());
    }

    private Product(ProductId id, ProductFamily family, ProductAttributeStandard standard,
            boolean sizeApplicable, boolean colourApplicable, ProductLifecycle lifecycle,
            List<ProductVariant> variants, ProductContent content, List<ProductMedia> media) {
        if (id == null || family == null || standard == null || lifecycle == null) {
            throw new IllegalArgumentException("Product identity, family, Attribute standard and lifecycle are required");
        }
        this.id = id;
        this.lifecycle = lifecycle;
        this.family = family;
        this.standard = standard;
        this.sizeApplicable = sizeApplicable;
        this.colourApplicable = colourApplicable;
        this.variants = List.copyOf(variants);
        this.content = content;
        this.media = List.copyOf(media);
        var mediaIds = new java.util.HashSet<String>();
        for (ProductMedia item : this.media) {
            if (!id.equals(item.productId()) || !mediaIds.add(item.id())
                    || (item.variantId() != null && this.variants.stream().noneMatch(v -> v.id().equals(item.variantId())))) {
                throw new IllegalArgumentException("Invalid Product media association");
            }
        }
    }

    /**
     * Restores previously persisted state, checking the same membership invariants as creation.
     * This is not a lifecycle transition or publication authorization and grants no visibility.
     * Persistence callers must validate raw stored values before constructing canonical value objects;
     * this factory accepts typed state only and does not translate unsupported persisted values.
     */
    public static Product reconstruct(ProductId id, ProductFamily family, ProductAttributeStandard standard,
            boolean sizeApplicable, boolean colourApplicable, ProductLifecycle lifecycle,
            List<ProductVariant> variants) {
        if (variants == null) {
            throw new IllegalArgumentException("Product Variant membership is required");
        }
        Product restored = new Product(id, family, standard, sizeApplicable, colourApplicable, lifecycle, List.of());
        for (ProductVariant variant : variants) {
            restored = restored.withVariant(variant);
        }
        return restored;
    }

    /** Restores structural metadata; a matching revision proves no acceptance or publication. */
    public static Product reconstruct(ProductId id, ProductFamily family, ProductAttributeStandard standard,
            boolean sizeApplicable, boolean colourApplicable, ProductLifecycle lifecycle,
            List<ProductVariant> variants, ProductContent content, List<ProductMedia> media, ProductRevision revision) {
        if (revision == null) {
            throw new IllegalArgumentException("Product revision is required");
        }
        Product restored = reconstruct(id, family, standard, sizeApplicable, colourApplicable, lifecycle, variants)
                .withRepresentation(content, media);
        if (!revision.equals(restored.revision())) {
            throw new IllegalArgumentException("Product revision does not match stored representation");
        }
        return restored;
    }

    /**
     * Builds an immutable representation snapshot, not an authorized update/publication command.
     * Persistence remains insert-only; no acceptance or Draft-editing workflow is implemented.
     */
    public Product withRepresentation(ProductContent content, List<ProductMedia> media) {
        if (media == null) {
            throw new IllegalArgumentException("Product media collection is required");
        }
        return new Product(id, family, standard, sizeApplicable, colourApplicable, lifecycle, variants, content, media);
    }

    /** Accepts one Variant only after checking all membership invariants; never replaces prior state. */
    public Product withVariant(ProductVariant variant) {
        if (variant == null || !id.equals(variant.productId())) {
            throw new IllegalArgumentException("Variant must belong to this Product");
        }
        VariantConfiguration configuration = variant.configuration();
        if (!family.equals(configuration.family()) || !standard.equals(configuration.standard())
                || sizeApplicable != configuration.sizeApplicable()
                || colourApplicable != configuration.colourApplicable()) {
            throw new IllegalArgumentException("Variant configuration is incompatible with this Product");
        }
        for (ProductVariant existing : variants) {
            if (existing.id().equals(variant.id())) {
                throw new IllegalArgumentException("Duplicate Product Variant identifier");
            }
            if (existing.configuration().equals(configuration)) {
                throw new IllegalArgumentException("Duplicate Product Variant configuration");
            }
        }
        List<ProductVariant> accepted = new ArrayList<>(variants);
        accepted.add(variant);
        return new Product(id, family, standard, sizeApplicable, colourApplicable, lifecycle, accepted, content, media);
    }

    public ProductContent content() { return content; }
    public List<ProductMedia> media() { return media; }
    public ProductRevision revision() { return ProductRevision.of(this); }
    public ProductId id() { return id; }
    public ProductFamily family() { return family; }
    public ProductAttributeStandard standard() { return standard; }
    public boolean sizeApplicable() { return sizeApplicable; }
    public boolean colourApplicable() { return colourApplicable; }
    public List<ProductVariant> variants() { return variants; }
    public ProductLifecycle lifecycle() { return lifecycle; }

    /** This foundation grants no customer visibility or structural sellability. */
    public boolean isCustomerVisible() { return lifecycle().grantsCustomerVisibility(); }

    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof Product product && id.equals(product.id);
    }

    @Override
    public int hashCode() { return id.hashCode(); }
}
