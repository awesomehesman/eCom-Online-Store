package com.enterprise.fashion.ecommerce.product.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductLifecycleTest {
    @Test
    void modelsOnlyCanonicalStates() {
        assertThat(ProductLifecycle.values()).containsExactly(
                ProductLifecycle.DRAFT, ProductLifecycle.PUBLISHED, ProductLifecycle.ARCHIVED);
    }

    @Test
    void draftAndArchivedDenyVisibilityAndPublishedAloneGrantsNone() {
        assertThat(ProductLifecycle.DRAFT.grantsCustomerVisibility()).isFalse();
        assertThat(ProductLifecycle.ARCHIVED.grantsCustomerVisibility()).isFalse();
        assertThat(ProductLifecycle.PUBLISHED.grantsCustomerVisibility()).isFalse();
    }
}
