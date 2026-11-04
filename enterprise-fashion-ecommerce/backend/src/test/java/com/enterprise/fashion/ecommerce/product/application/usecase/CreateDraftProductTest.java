package com.enterprise.fashion.ecommerce.product.application.usecase;

import java.util.ArrayList;
import java.util.List;

import com.enterprise.fashion.ecommerce.product.application.persistence.ProductPersistenceService;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductAttributeStandard;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductColour;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductContent;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductFamily;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductLifecycle;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductMedia;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductSize;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariant;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariantId;
import com.enterprise.fashion.ecommerce.product.domain.model.VariantConfiguration;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

class CreateDraftProductTest {
    private final ProductPersistenceService persistence = mock(ProductPersistenceService.class);
    private final CreateDraftProduct useCase = new CreateDraftProduct(persistence);
    private final ProductId id = new ProductId("draft-product");

    private ProductVariant variant(String variantId, ProductId owner, String colour) {
        return new ProductVariant(new ProductVariantId(variantId), owner,
                new VariantConfiguration(ProductFamily.APPAREL, ProductAttributeStandard.INITIAL, true, true,
                        new ProductSize(ProductFamily.APPAREL, "m"), new ProductColour(colour)), "Regular", "Cotton");
    }

    private CreateDraftProduct.Command command(List<ProductVariant> variants, List<ProductMedia> media) {
        return new CreateDraftProduct.Command(id, ProductFamily.APPAREL, ProductAttributeStandard.INITIAL,
                true, true, variants, new ProductContent("Shirt", "Cotton shirt"), media);
    }

    @Test
    void createsCanonicalDraftAndReturnsExactStructuralRevision() {
        var media = new ProductMedia("image", id, new ProductVariantId("variant"), "asset-v1",
                "https://example.test/image", "Shirt front");
        var result = useCase.create(command(List.of(variant("variant", id, "black")), List.of(media)));
        var captured = ArgumentCaptor.forClass(Product.class);
        verify(persistence).create(captured.capture());
        Product product = captured.getValue();
        assertThat(result.id()).isEqualTo(id);
        assertThat(result.revision()).isEqualTo(product.revision());
        assertThat(product.lifecycle()).isEqualTo(ProductLifecycle.DRAFT);
        assertThat(product.isCustomerVisible()).isFalse();
        assertThat(product.variants().getFirst().configuration().size().code()).isEqualTo("M");
        assertThat(product.variants().getFirst().configuration().colour().code()).isEqualTo("BLACK");
        assertThat(product.media()).containsExactly(media);
    }

    @Test
    void rejectsCanonicalDuplicatesBeforeWriting() {
        assertThatIllegalArgumentException().isThrownBy(() -> useCase.create(command(
                List.of(variant("one", id, "black"), variant("two", id, "BLACK")), List.of())));
        verifyNoInteractions(persistence);
    }

    @Test
    void rejectsCrossProductVariantAndMediaMembershipBeforeWriting() {
        assertThatIllegalArgumentException().isThrownBy(() -> useCase.create(command(
                List.of(variant("one", new ProductId("other"), "black")), List.of())));
        var missingVariantMedia = new ProductMedia("image", id, new ProductVariantId("missing"), "asset",
                "https://example.test/image", "Shirt");
        assertThatIllegalArgumentException().isThrownBy(() -> useCase.create(command(List.of(), List.of(missingVariantMedia))));
        var foreignMedia = new ProductMedia("image", new ProductId("other"), null, "asset",
                "https://example.test/image", "Shirt");
        assertThatIllegalArgumentException().isThrownBy(() -> useCase.create(command(List.of(), List.of(foreignMedia))));
        verifyNoInteractions(persistence);
    }

    @Test
    void duplicateVariantIdentityAndIncompatibleFamilyFailBeforeWriting() {
        var one = variant("same", id, "black");
        var differentConfiguration = new VariantConfiguration(ProductFamily.APPAREL, ProductAttributeStandard.INITIAL,
                true, true, new ProductSize(ProductFamily.APPAREL, "L"), new ProductColour("black"));
        var duplicate = new ProductVariant(one.id(), id, differentConfiguration, null, null);
        assertThatIllegalArgumentException().isThrownBy(() -> useCase.create(command(List.of(one, duplicate), List.of())));
        var wrongFamily = new VariantConfiguration(ProductFamily.ACCESSORIES, ProductAttributeStandard.INITIAL,
                false, false, null, null);
        assertThatIllegalArgumentException().isThrownBy(() -> useCase.create(command(
                List.of(new ProductVariant(one.id(), id, wrongFamily, null, null)), List.of())));
        verifyNoInteractions(persistence);
    }

    @Test
    void commandDefensivelyCopiesMembershipAndDoesNotPrintPrivateContent() {
        var variants = new ArrayList<>(List.of(variant("one", id, "black")));
        var command = command(variants, List.of());
        variants.clear();
        assertThat(command.variants()).hasSize(1);
        assertThatThrownBy(() -> command.variants().clear()).isInstanceOf(UnsupportedOperationException.class);
        assertThat(command.toString()).doesNotContain("Shirt", "Cotton", id.value());
    }

    @Test
    void failureDoesNotReturnSuccessOrRetryCreation() {
        var failure = new IllegalStateException("transaction failed");
        doThrow(failure).when(persistence).create(any());
        assertThatThrownBy(() -> useCase.create(command(List.of(), List.of()))).isSameAs(failure);
        verify(persistence, times(1)).create(any());
    }
}
