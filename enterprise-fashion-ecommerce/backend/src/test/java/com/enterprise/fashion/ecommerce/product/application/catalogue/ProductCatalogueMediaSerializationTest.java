package com.enterprise.fashion.ecommerce.product.application.catalogue;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies the JSON serialization of Media in ProductCatalogueRepresentation aligns
 * with the OpenAPI contract: variantId is omitted for Product-level media and present
 * for Variant-level media.
 */
class ProductCatalogueMediaSerializationTest {

    private final ObjectMapper json = Jackson2ObjectMapperBuilder.json().build();

    @Test
    void productLevelMediaOmitsVariantIdProperty() throws Exception {
        var media = new ProductCatalogueRepresentation.Media(
                "media-product-1",
                "https://media.example.invalid/front",
                "Front view",
                null);

        String serialized = json.writeValueAsString(media);
        JsonNode node = json.readTree(serialized);

        assertThat(node.has("variantId")).as("Product-level media must omit variantId property").isFalse();
        assertThat(node.get("mediaId").asText()).isEqualTo("media-product-1");
        assertThat(node.get("url").asText()).isEqualTo("https://media.example.invalid/front");
        assertThat(node.get("alternativeText").asText()).isEqualTo("Front view");
    }

    @Test
    void variantLevelMediaIncludesVariantIdProperty() throws Exception {
        var media = new ProductCatalogueRepresentation.Media(
                "media-variant-1",
                "https://media.example.invalid/variant",
                "Variant view",
                "variant-abc");

        String serialized = json.writeValueAsString(media);
        JsonNode node = json.readTree(serialized);

        assertThat(node.has("variantId")).as("Variant-level media must include variantId property").isTrue();
        assertThat(node.get("variantId").asText()).isEqualTo("variant-abc");
        assertThat(node.get("mediaId").asText()).isEqualTo("media-variant-1");
        assertThat(node.get("url").asText()).isEqualTo("https://media.example.invalid/variant");
        assertThat(node.get("alternativeText").asText()).isEqualTo("Variant view");
    }

    @Test
    void fullProductRepresentationSerializesMediaCorrectly() throws Exception {
        var productMedia = new ProductCatalogueRepresentation.Media(
                "media-product",
                "https://media.example.invalid/product",
                "Product front view",
                null);
        var variantMedia = new ProductCatalogueRepresentation.Media(
                "media-variant",
                "https://media.example.invalid/variant",
                "Variant detail view",
                "variant-1");

        var product = new ProductCatalogueRepresentation(
                "product-1",
                "Example Product",
                "An example product description.",
                "Apparel",
                "1.0.0",
                List.of("Size", "Colour"),
                List.of(new ProductCatalogueRepresentation.Variant("variant-1", java.util.Map.of())),
                List.of(productMedia, variantMedia));

        String serialized = json.writeValueAsString(product);
        JsonNode node = json.readTree(serialized);
        JsonNode mediaArray = node.get("media");

        assertThat(mediaArray).hasSize(2);

        JsonNode productMediaNode = mediaArray.get(0);
        assertThat(productMediaNode.has("variantId"))
                .as("Product-level media in array must omit variantId").isFalse();
        assertThat(productMediaNode.get("mediaId").asText()).isEqualTo("media-product");

        JsonNode variantMediaNode = mediaArray.get(1);
        assertThat(variantMediaNode.has("variantId"))
                .as("Variant-level media in array must include variantId").isTrue();
        assertThat(variantMediaNode.get("variantId").asText()).isEqualTo("variant-1");
    }

    @Test
    void mediaSerializationMatchesOpenApiContractRequiredFields() throws Exception {
        var productMedia = new ProductCatalogueRepresentation.Media(
                "media-1",
                "https://media.example.invalid/image",
                "Image description",
                null);

        String serialized = json.writeValueAsString(productMedia);
        JsonNode node = json.readTree(serialized);

        // OpenAPI contract required fields: mediaId, url, alternativeText
        assertThat(node.has("mediaId")).as("mediaId is required").isTrue();
        assertThat(node.has("url")).as("url is required").isTrue();
        assertThat(node.has("alternativeText")).as("alternativeText is required").isTrue();

        // variantId is optional per OpenAPI, must be omitted when null
        assertThat(node.has("variantId")).as("variantId must be omitted when null").isFalse();

        // Verify exact field count matches contract (no extra fields)
        var fieldNames = new java.util.ArrayList<String>();
        node.fieldNames().forEachRemaining(fieldNames::add);
        assertThat(fieldNames).containsExactlyInAnyOrder("mediaId", "url", "alternativeText");
    }

    @Test
    void variantMediaSerializationIncludesAllFourFields() throws Exception {
        var variantMedia = new ProductCatalogueRepresentation.Media(
                "media-1",
                "https://media.example.invalid/variant",
                "Variant image",
                "variant-xyz");

        String serialized = json.writeValueAsString(variantMedia);
        JsonNode node = json.readTree(serialized);

        var fieldNames = new java.util.ArrayList<String>();
        node.fieldNames().forEachRemaining(fieldNames::add);
        assertThat(fieldNames).containsExactlyInAnyOrder("mediaId", "url", "alternativeText", "variantId");
    }

    @Test
    void productLevelMediaNeverSerializesNullLiteral() throws Exception {
        var media = new ProductCatalogueRepresentation.Media(
                "media-1",
                "https://media.example.invalid/image",
                "Description",
                null);

        String serialized = json.writeValueAsString(media);

        // The JSON string must not contain "variantId" at all, not even as null
        assertThat(serialized).doesNotContain("variantId");
        assertThat(serialized).doesNotContain("null");
    }
}
