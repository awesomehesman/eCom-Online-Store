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

    @Test
    void onlyDraftCanPublish() {
        assertThat(ProductLifecycle.DRAFT.canPublish()).isTrue();
        assertThat(ProductLifecycle.PUBLISHED.canPublish()).isFalse();
        assertThat(ProductLifecycle.ARCHIVED.canPublish()).isFalse();
    }

    @Test
    void canPublishAndGrantsVisibilityAreIndependent() {
        // Publication eligibility does not imply visibility
        assertThat(ProductLifecycle.DRAFT.canPublish()).isTrue();
        assertThat(ProductLifecycle.DRAFT.grantsCustomerVisibility()).isFalse();
        // PUBLISHED lifecycle alone still does not grant visibility
        assertThat(ProductLifecycle.PUBLISHED.canPublish()).isFalse();
        assertThat(ProductLifecycle.PUBLISHED.grantsCustomerVisibility()).isFalse();
    }
}
