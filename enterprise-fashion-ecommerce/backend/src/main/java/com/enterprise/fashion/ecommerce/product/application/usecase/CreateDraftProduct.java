package com.enterprise.fashion.ecommerce.product.application.usecase;

import java.util.List;
import java.util.Objects;

import com.enterprise.fashion.ecommerce.product.application.persistence.ProductPersistenceService;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductAttributeStandard;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductContent;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductFamily;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductMedia;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductRevision;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariant;

/**
 * Internal construction capability, not a Staff entry point or authorization decision.
 * A future operational adapter must enforce governed authorization and audit requirements.
 * REQ-PRD-003–014, 034, 039; BPRD-REQ-011: validate before insert, never publish.
 */
public final class CreateDraftProduct {
    private final ProductPersistenceService persistence;

    public CreateDraftProduct(ProductPersistenceService persistence) {
        this.persistence = Objects.requireNonNull(persistence);
    }

    public Created create(Command command) {
        Objects.requireNonNull(command);
        Product product = new Product(command.id(), command.family(), command.standard(),
                command.sizeApplicable(), command.colourApplicable());
        for (ProductVariant variant : command.variants()) {
            product = product.withVariant(variant);
        }
        product = product.withRepresentation(command.content(), command.media());
        Created created = new Created(product.id(), product.revision());
        // Existing application service owns the single write transaction and rejects ambient
        // transactions. No success is returned until all Product/Variant/media inserts commit.
        persistence.create(product);
        return created;
    }

    /** Typed metadata only: applicability flags describe Attributes, never acceptance. */
    public record Command(ProductId id, ProductFamily family, ProductAttributeStandard standard,
            boolean sizeApplicable, boolean colourApplicable, List<ProductVariant> variants,
            ProductContent content, List<ProductMedia> media) {
        public Command {
            if (variants == null || media == null) {
                throw new IllegalArgumentException("Variant and media collections are required");
            }
            variants = List.copyOf(variants);
            media = List.copyOf(media);
        }

        @Override
        public String toString() { return "CreateDraftProduct.Command[internal]"; }
    }

    /** Structural revision only; proves neither content/media acceptance nor visibility. */
    public record Created(ProductId id, ProductRevision revision) { }
}
