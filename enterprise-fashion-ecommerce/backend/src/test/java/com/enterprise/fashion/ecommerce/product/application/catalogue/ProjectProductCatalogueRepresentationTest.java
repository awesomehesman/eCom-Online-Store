package com.enterprise.fashion.ecommerce.product.application.catalogue;

import java.math.BigDecimal;
import java.util.List;

import com.enterprise.fashion.ecommerce.product.application.catalogue.ProjectProductCatalogueRepresentation.EligibleProductRepresentation;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductAttributeStandard;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductColour;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductContent;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductFamily;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductMedia;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductSize;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariant;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariantId;
import com.enterprise.fashion.ecommerce.product.domain.model.VariantConfiguration;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class ProjectProductCatalogueRepresentationTest {
    private static final ProductId PRODUCT_ID = new ProductId("public-product-1");
    private static final ProductContent CONTENT = new ProductContent("Black shirt", "A plain black shirt.");
    private final ProjectProductCatalogueRepresentation projector = new ProjectProductCatalogueRepresentation();

    @Test
    void mapsEveryRequiredProductFieldAndCanonicalApparelAttributes() {
        var variant = variant("variant-m", ProductFamily.APPAREL, true, true,
                ProductSize.apparel("m"), new ProductColour("black"));

        var result = projector.project(input(ProductFamily.APPAREL, true, true, List.of(variant), List.of()));

        assertThat(result.productId()).isEqualTo("public-product-1");
        assertThat(result.name()).isEqualTo("Black shirt");
        assertThat(result.description()).isEqualTo("A plain black shirt.");
        assertThat(result.family()).isEqualTo("Apparel");
        assertThat(result.attributeStandardVersion()).isEqualTo("1.0.0");
        assertThat(result.variantDefiningAttributes()).containsExactly("Size", "Colour");
        assertThat(result.variants()).singleElement().satisfies(projected -> {
            assertThat(projected.variantId()).isEqualTo("variant-m");
            assertThat(projected.attributes()).containsOnlyKeys("size", "colour");
            assertThat(projected.attributes().get("size"))
                    .isEqualTo(new ProductCatalogueRepresentation.ApparelSize("APPAREL", "M", "M"));
            assertThat(projected.attributes().get("colour"))
                    .isEqualTo(new ProductCatalogueRepresentation.Colour("BLACK", "Black"));
        });
    }

    @Test
    void mapsFootwearAndAccessorySizeShapesWithoutInventingAttributes() {
        var footwear = projector.project(input(ProductFamily.FOOTWEAR, true, false,
                List.of(variant("shoe-7-5", ProductFamily.FOOTWEAR, true, false,
                        ProductSize.footwear("UK", new BigDecimal("7.5")), null)), List.of()));
        var accessory = projector.project(input(ProductFamily.ACCESSORIES, true, false,
                List.of(variant("one-size", ProductFamily.ACCESSORIES, true, false,
                        ProductSize.oneSize(), null)), List.of()));

        assertThat(footwear.variants().getFirst().attributes().get("size")).isEqualTo(
                new ProductCatalogueRepresentation.FootwearSize("FOOTWEAR", "UK", new BigDecimal("7.5"), "UK 7.5"));
        assertThat(accessory.variants().getFirst().attributes().get("size")).isEqualTo(
                new ProductCatalogueRepresentation.AccessorySize("ACCESSORY", "ONE_SIZE", "One Size"));
        assertThat(footwear.variantDefiningAttributes()).containsExactly("Size");
        assertThat(accessory.variantDefiningAttributes()).containsExactly("Size");
    }

    @Test
    void supportsConfirmedNonapplicabilityWithExactlyEmptyVariantAttributes() {
        var variant = variant("plain-accessory", ProductFamily.ACCESSORIES, false, false, null, null);

        var result = projector.project(input(ProductFamily.ACCESSORIES, false, false, List.of(variant), List.of()));

        assertThat(result.variantDefiningAttributes()).isEmpty();
        assertThat(result.variants().getFirst().attributes()).isEmpty();
    }

    @Test
    void preservesVariantOrderAndProducesImmutableCollections() {
        var first = variant("variant-s", ProductFamily.APPAREL, true, false, ProductSize.apparel("S"), null);
        var second = variant("variant-l", ProductFamily.APPAREL, true, false, ProductSize.apparel("L"), null);
        var source = new java.util.ArrayList<>(List.of(first, second));
        var eligible = input(ProductFamily.APPAREL, true, false, source, List.of());
        source.clear();

        var result = projector.project(eligible);

        assertThat(result.variants()).extracting(ProductCatalogueRepresentation.Variant::variantId)
                .containsExactly("variant-s", "variant-l");
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> result.variants().add(result.variants().getFirst()));
    }

    @Test
    void preservesProductAndVariantMediaAssociationAndOrder() {
        var variant = variant("variant-m", ProductFamily.APPAREL, true, false, ProductSize.apparel("M"), null);
        var productMedia = media("media-product", null, "https://media.example.invalid/front", "Front view");
        var variantMedia = media("media-variant", variant.id(), "https://media.example.invalid/m", "Size M view");

        var result = projector.project(input(ProductFamily.APPAREL, true, false, List.of(variant),
                List.of(productMedia, variantMedia)));

        assertThat(result.media()).containsExactly(
                new ProductCatalogueRepresentation.Media("media-product", productMedia.url(), "Front view", null),
                new ProductCatalogueRepresentation.Media("media-variant", variantMedia.url(), "Size M view", "variant-m"));
    }

    @Test
    void rejectsDuplicateVariantAndMediaIdentifiers() {
        var first = variant("duplicate", ProductFamily.APPAREL, true, false, ProductSize.apparel("S"), null);
        var duplicate = variant("duplicate", ProductFamily.APPAREL, true, false, ProductSize.apparel("M"), null);
        assertThatIllegalArgumentException().isThrownBy(() ->
                input(ProductFamily.APPAREL, true, false, List.of(first, duplicate), List.of()));

        var media = media("duplicate-media", null, "https://media.example.invalid/one", "One");
        var duplicateMedia = media("duplicate-media", null, "https://media.example.invalid/two", "Two");
        assertThatIllegalArgumentException().isThrownBy(() ->
                input(ProductFamily.ACCESSORIES, false, false, List.of(), List.of(media, duplicateMedia)));
    }

    @Test
    void rejectsDuplicateCanonicalVariantConfigurations() {
        var first = variant("first", ProductFamily.APPAREL, true, false, ProductSize.apparel("M"), null);
        var second = variant("second", ProductFamily.APPAREL, true, false, ProductSize.apparel("m"), null);

        assertThatIllegalArgumentException().isThrownBy(() ->
                input(ProductFamily.APPAREL, true, false, List.of(first, second), List.of()));
    }

    @Test
    void rejectsCrossProductVariantAndMediaAssociations() {
        var otherId = new ProductId("other-product");
        var foreignVariant = new ProductVariant(new ProductVariantId("foreign"), otherId,
                configuration(ProductFamily.APPAREL, true, false, ProductSize.apparel("M"), null), null, null);
        assertThatIllegalArgumentException().isThrownBy(() ->
                input(ProductFamily.APPAREL, true, false, List.of(foreignVariant), List.of()));

        var foreignMedia = new ProductMedia("foreign-media", otherId, null, "revision-1",
                "https://media.example.invalid/foreign", "Foreign");
        assertThatIllegalArgumentException().isThrownBy(() ->
                input(ProductFamily.ACCESSORIES, false, false, List.of(), List.of(foreignMedia)));
    }

    @Test
    void rejectsMediaForVariantOutsideTheEligibleRepresentation() {
        var missing = new ProductVariantId("missing-variant");
        var media = media("orphan", missing, "https://media.example.invalid/orphan", "Orphan");

        assertThatIllegalArgumentException().isThrownBy(() ->
                input(ProductFamily.ACCESSORIES, false, false, List.of(), List.of(media)));
    }

    @Test
    void rejectsMissingRequiredRepresentationFieldsAndProjectorInput() {
        assertThatIllegalArgumentException().isThrownBy(() -> new EligibleProductRepresentation(
                null, CONTENT, ProductFamily.APPAREL, ProductAttributeStandard.INITIAL,
                false, false, List.of(), List.of()));
        assertThatIllegalArgumentException().isThrownBy(() -> new EligibleProductRepresentation(
                PRODUCT_ID, null, ProductFamily.APPAREL, ProductAttributeStandard.INITIAL,
                false, false, List.of(), List.of()));
        assertThatIllegalArgumentException().isThrownBy(() -> new EligibleProductRepresentation(
                PRODUCT_ID, CONTENT, null, ProductAttributeStandard.INITIAL,
                false, false, List.of(), List.of()));
        assertThatIllegalArgumentException().isThrownBy(() -> new EligibleProductRepresentation(
                PRODUCT_ID, CONTENT, ProductFamily.APPAREL, null,
                false, false, List.of(), List.of()));
        assertThatIllegalArgumentException().isThrownBy(() -> new EligibleProductRepresentation(
                PRODUCT_ID, CONTENT, ProductFamily.APPAREL, ProductAttributeStandard.INITIAL,
                false, false, null, List.of()));
        assertThatIllegalArgumentException().isThrownBy(() -> new EligibleProductRepresentation(
                PRODUCT_ID, CONTENT, ProductFamily.APPAREL, ProductAttributeStandard.INITIAL,
                false, false, List.of(), null));
        assertThatNullPointerException().isThrownBy(() -> projector.project(null));
    }

    @Test
    void canonicalTypesRejectInvalidAttributesAndUnsafeMediaBeforeProjection() {
        assertThatIllegalArgumentException().isThrownBy(() -> ProductSize.apparel("INVALID"));
        assertThatIllegalArgumentException().isThrownBy(() -> new ProductColour("RED"));
        assertThatIllegalArgumentException().isThrownBy(() ->
                media("http", null, "http://media.example.invalid/image", "Image"));
        assertThatIllegalArgumentException().isThrownBy(() ->
                media("credentials", null, "https://user:secret@media.example.invalid/image", "Image"));
        assertThatIllegalArgumentException().isThrownBy(() ->
                media("malformed", null, "not a URI", "Image"));
    }

    @Test
    void lifecycleCannotBeSuppliedOrDerivedByTheRepresentationProjector() {
        assertThat(EligibleProductRepresentation.class.getRecordComponents())
                .extracting(java.lang.reflect.RecordComponent::getName)
                .doesNotContain("lifecycle", "publication", "visible", "eligible");
        assertThat(ProductCatalogueRepresentation.class.getRecordComponents())
                .extracting(java.lang.reflect.RecordComponent::getName)
                .doesNotContain("lifecycle", "publication", "visible", "eligible");
    }

    private static EligibleProductRepresentation input(ProductFamily family, boolean sizeApplicable,
            boolean colourApplicable, List<ProductVariant> variants, List<ProductMedia> media) {
        return new EligibleProductRepresentation(PRODUCT_ID, CONTENT, family, ProductAttributeStandard.INITIAL,
                sizeApplicable, colourApplicable, variants, media);
    }

    private static ProductVariant variant(String id, ProductFamily family, boolean sizeApplicable,
            boolean colourApplicable, ProductSize size, ProductColour colour) {
        return new ProductVariant(new ProductVariantId(id), PRODUCT_ID,
                configuration(family, sizeApplicable, colourApplicable, size, colour), null, null);
    }

    private static VariantConfiguration configuration(ProductFamily family, boolean sizeApplicable,
            boolean colourApplicable, ProductSize size, ProductColour colour) {
        return new VariantConfiguration(family, ProductAttributeStandard.INITIAL,
                sizeApplicable, colourApplicable, size, colour);
    }

    private static ProductMedia media(String id, ProductVariantId variantId, String url, String alternativeText) {
        return new ProductMedia(id, PRODUCT_ID, variantId, "revision-1", url, alternativeText);
    }
}
