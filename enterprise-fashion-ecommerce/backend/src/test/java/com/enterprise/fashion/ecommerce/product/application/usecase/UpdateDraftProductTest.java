package com.enterprise.fashion.ecommerce.product.application.usecase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.enterprise.fashion.ecommerce.product.application.port.out.ProductPersistencePort;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductAttributeStandard;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductContent;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductFamily;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductLifecycle;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductRevision;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariant;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariantId;
import com.enterprise.fashion.ecommerce.product.domain.model.VariantConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.transaction.support.TransactionOperations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class UpdateDraftProductTest {
    private final ProductPersistencePort products = mock(ProductPersistencePort.class);
    private final UpdateDraftProduct commands = new UpdateDraftProduct(products, TransactionOperations.withoutTransaction());
    private final Product current = new Product(new ProductId("product"), ProductFamily.ACCESSORIES,
            ProductAttributeStandard.INITIAL, false, false);

    private UpdateDraftProduct.Command command(ProductRevision revision, List<ProductVariant> variants) {
        return new UpdateDraftProduct.Command(current.id(), revision, new ProductContent("Bag", "Canvas bag"), variants, List.of());
    }

    private void rejected(UpdateDraftProduct.Reason reason, Runnable call) {
        assertThatThrownBy(call::run).isInstanceOfSatisfying(UpdateDraftProduct.Rejected.class,
                failure -> assertThat(failure.reason()).isEqualTo(reason));
    }

    @Test
    void buildsValidatedReplacementAndUsesConditionalPersistence() {
        when(products.load(current.id())).thenReturn(Optional.of(current));
        when(products.replaceDraft(any(), eq(current.revision()))).thenReturn(true);
        var result = commands.update(command(current.revision(), List.of()));
        var captured = ArgumentCaptor.forClass(Product.class);
        verify(products).replaceDraft(captured.capture(), eq(current.revision()));
        assertThat(captured.getValue().lifecycle()).isEqualTo(ProductLifecycle.DRAFT);
        assertThat(captured.getValue().isCustomerVisible()).isFalse();
        assertThat(result.id()).isEqualTo(current.id());
        assertThat(result.revision()).isEqualTo(captured.getValue().revision()).isNotEqualTo(current.revision());
    }

    @Test
    void missingAndStaleAreDistinctAndNeverWrite() {
        when(products.load(current.id())).thenReturn(Optional.empty());
        rejected(UpdateDraftProduct.Reason.NOT_FOUND, () -> commands.update(command(current.revision(), List.of())));
        when(products.load(current.id())).thenReturn(Optional.of(current));
        rejected(UpdateDraftProduct.Reason.REVISION_CONFLICT, () -> commands.update(command(new ProductRevision("v1_" + "0".repeat(64)), List.of())));
        verify(products, org.mockito.Mockito.times(2)).load(current.id());
        verifyNoMoreInteractions(products);
    }

    @ParameterizedTest
    @EnumSource(value = ProductLifecycle.class, names = {"PUBLISHED", "ARCHIVED"})
    void rejectsNonDraftWithoutLifecycleTransition(ProductLifecycle lifecycle) {
        Product persisted = Product.reconstruct(current.id(), current.family(), current.standard(), false, false, lifecycle, List.of());
        when(products.load(current.id())).thenReturn(Optional.of(persisted));
        rejected(UpdateDraftProduct.Reason.NOT_DRAFT, () -> commands.update(command(persisted.revision(), List.of())));
        verify(products).load(current.id());
        verifyNoMoreInteractions(products);
    }

    @Test
    void ownershipAndMembershipRemovalAreInvalidRepresentations() {
        var config = new VariantConfiguration(current.family(), current.standard(), false, false, null, null);
        var variant = new ProductVariant(new ProductVariantId("variant"), current.id(), config, null, null);
        when(products.load(current.id())).thenReturn(Optional.of(current.withVariant(variant)));
        rejected(UpdateDraftProduct.Reason.INVALID_REPRESENTATION,
                () -> commands.update(command(current.withVariant(variant).revision(), List.of())));
        when(products.load(current.id())).thenReturn(Optional.of(current));
        var foreign = new ProductVariant(variant.id(), new ProductId("other"), config, null, null);
        rejected(UpdateDraftProduct.Reason.INVALID_REPRESENTATION,
                () -> commands.update(command(current.revision(), List.of(foreign))));
    }

    @Test
    void databaseCasOrSerializationRejectionIsRevisionConflict() {
        when(products.load(current.id())).thenReturn(Optional.of(current));
        when(products.replaceDraft(any(), any())).thenReturn(false);
        rejected(UpdateDraftProduct.Reason.REVISION_CONFLICT, () -> commands.update(command(current.revision(), List.of())));
        when(products.replaceDraft(any(), any())).thenThrow(new PessimisticLockingFailureException("private SQL"));
        rejected(UpdateDraftProduct.Reason.REVISION_CONFLICT, () -> commands.update(command(current.revision(), List.of())));
    }

    @Test
    void persistenceFailureIsNotAnExpectedBusinessRejection() {
        var unavailable = new DataAccessResourceFailureException("private database diagnostic");
        when(products.load(current.id())).thenThrow(unavailable);
        assertThatThrownBy(() -> commands.update(command(current.revision(), List.of()))).isSameAs(unavailable);
    }

    @Test
    void commandCollectionsAreImmutableAndDiagnosticsAreRedacted() {
        var variants = new ArrayList<ProductVariant>();
        var command = command(current.revision(), variants);
        variants.add(null);
        assertThat(command.variants()).isEmpty();
        assertThatThrownBy(() -> command.media().clear()).isInstanceOf(UnsupportedOperationException.class);
        assertThat(command.toString()).doesNotContain("Canvas", current.id().value(), current.revision().value());
        rejected(UpdateDraftProduct.Reason.INVALID_REPRESENTATION,
                () -> new UpdateDraftProduct.Command(null, current.revision(), null, List.of(), List.of()));
    }
}
