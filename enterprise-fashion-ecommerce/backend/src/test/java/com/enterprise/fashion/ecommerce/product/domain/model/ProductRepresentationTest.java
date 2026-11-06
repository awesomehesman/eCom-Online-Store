package com.enterprise.fashion.ecommerce.product.domain.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductRepresentationTest {
    private Product product() {
        return new Product(new ProductId("p1"), ProductFamily.APPAREL, ProductAttributeStandard.INITIAL, true, true);
    }

    private ProductVariant variant(String id, ProductId owner, String size) {
        return new ProductVariant(new ProductVariantId(id), owner,
                new VariantConfiguration(ProductFamily.APPAREL, ProductAttributeStandard.INITIAL,
                        true, true, ProductSize.apparel(size), new ProductColour("BLACK")), "Regular", "Cotton");
    }

    private ProductMedia media(String id, ProductId owner, ProductVariantId variant, String asset) {
        return new ProductMedia(id, owner, variant, asset, "https://media.example.invalid/front", "Front view");
    }

    @Test
    void representationEncodingVersionHasStableCompatibilityVector() {
        assertThat(product().revision().value()).isEqualTo("v1_92e5605881fbcf9b41fad90a128d8ae4a34a1d3003d0c251949cf44d50e6d7f8");
    }

    @Test
    void contentStructurePreservesExactTextWithoutClaimingAcceptance() {
        var content = new ProductContent(" Example shirt ", " Black shirt description ");
        assertThat(content.name()).isEqualTo(" Example shirt ");
        assertThat(content.description()).isEqualTo(" Black shirt description ");
        for (String invalid : new String[]{null, "", "  ", "\n\t", "\uD800", "\0"}) {
            assertThatThrownBy(() -> new ProductContent(invalid, "Description")).isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> new ProductContent("Name", invalid)).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void mediaRequiresStructuralMetadataAndSafeReferenceSyntaxOnly() {
        Product owner = product();
        assertThat(media("m1", owner.id(), null, "asset-1").assetRevision()).isEqualTo("asset-1");
        for (String url : new String[]{null, "http://media.example.invalid/a", "https://user:secret@example.invalid/a", "bad url", "https:/a"}) {
            assertThatThrownBy(() -> new ProductMedia("m1", owner.id(), null, "asset-1", url, "Front view"))
                    .isInstanceOf(IllegalArgumentException.class);
        }
        assertThatThrownBy(() -> new ProductMedia("m1", owner.id(), null, "asset-1", "https://example.invalid/a", " "))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> media("bad/id", owner.id(), null, "asset-1")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> media("m1", owner.id(), null, "")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsForeignProductMissingVariantAndDuplicateMediaIdentity() {
        Product owner = product().withVariant(variant("v1", product().id(), "M"));
        assertThatThrownBy(() -> owner.withRepresentation(null,
                List.of(media("m1", new ProductId("other"), null, "asset-1"))))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> owner.withRepresentation(null,
                List.of(media("m1", owner.id(), new ProductVariantId("foreign"), "asset-1"))))
                .isInstanceOf(IllegalArgumentException.class);
        var first = media("m1", owner.id(), new ProductVariantId("v1"), "asset-1");
        assertThatThrownBy(() -> owner.withRepresentation(null, List.of(first, first))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void snapshotsPreserveIdentityMembershipAndDefensivelyCopyMedia() {
        Product owner = product().withVariant(variant("v1", product().id(), "M"));
        var content = new ProductContent("Shirt", "Black shirt");
        var input = new ArrayList<>(List.of(media("m1", owner.id(), new ProductVariantId("v1"), "asset-1")));
        Product represented = owner.withRepresentation(content, input);
        input.clear();
        assertThat(owner.content()).isNull();
        assertThat(owner.media()).isEmpty();
        assertThat(represented.id()).isEqualTo(owner.id());
        assertThat(represented.variants()).containsExactlyElementsOf(owner.variants());
        assertThat(represented.media()).hasSize(1);
        assertThatThrownBy(() -> represented.media().clear()).isInstanceOf(UnsupportedOperationException.class);
        Product added = represented.withVariant(variant("v2", owner.id(), "L"));
        assertThat(added.content()).isEqualTo(content);
        assertThat(added.media()).isEqualTo(represented.media());
        assertThat(added.revision()).isNotEqualTo(represented.revision());
    }

    @Test
    void changedContentAssetAssociationTextAndOrderCannotReuseRevision() {
        Product owner = product().withVariant(variant("v1", product().id(), "M"));
        var content = new ProductContent("Shirt", "Black shirt");
        var a = media("m1", owner.id(), null, "asset-1");
        var b = media("m2", owner.id(), null, "asset-2");
        Product original = owner.withRepresentation(content, List.of(a, b));
        assertThat(owner.withRepresentation(new ProductContent("New name", "Black shirt"), List.of(a, b)).revision())
                .isNotEqualTo(original.revision());
        assertThat(owner.withRepresentation(content, List.of(b, a)).revision()).isNotEqualTo(original.revision());
        assertThat(owner.withRepresentation(content, List.of(media("m1", owner.id(), null, "asset-3"), b)).revision())
                .isNotEqualTo(original.revision());
        assertThat(owner.withRepresentation(content, List.of(media("m1", owner.id(), new ProductVariantId("v1"), "asset-1"), b)).revision())
                .isNotEqualTo(original.revision());
        var differentAlt = new ProductMedia("m1", owner.id(), null, "asset-1", a.url(), "Back view");
        assertThat(owner.withRepresentation(content, List.of(differentAlt, b)).revision()).isNotEqualTo(original.revision());
        assertThatThrownBy(() -> Product.reconstruct(owner.id(), owner.family(), owner.standard(), true, true,
                owner.lifecycle(), owner.variants(), content, List.of(b, a), original.revision()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void revisionIgnoresUnorderedVariantEnumerationButNotConfiguration() {
        Product owner = product();
        var a = variant("v1", owner.id(), "M");
        var b = variant("v2", owner.id(), "L");
        Product first = owner.withVariant(a).withVariant(b);
        Product reordered = owner.withVariant(b).withVariant(a);
        assertThat(first.revision()).isEqualTo(reordered.revision());
        assertThat(owner.withVariant(variant("v1", owner.id(), "S")).withVariant(b).revision()).isNotEqualTo(first.revision());
        assertThatThrownBy(() -> first.withVariant(variant("v3", owner.id(), "m"))).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @EnumSource(ProductLifecycle.class)
    void reconstructsAllLifecyclesWithoutAcceptanceOrVisibility(ProductLifecycle lifecycle) {
        Product owner = product().withRepresentation(new ProductContent("Shirt", "Black shirt"),
                List.of(media("m1", product().id(), null, "asset-1")));
        Product restored = Product.reconstruct(owner.id(), owner.family(), owner.standard(), true, true,
                lifecycle, owner.variants(), owner.content(), owner.media(), owner.revision());
        assertThat(restored.lifecycle()).isEqualTo(lifecycle);
        assertThat(restored.revision()).isEqualTo(owner.revision());
        assertThat(restored.content()).isEqualTo(owner.content());
        assertThat(restored.media()).isEqualTo(owner.media());
        assertThat(restored.isCustomerVisible()).isFalse();
    }
}
