package com.enterprise.fashion.ecommerce.product.adapter.out.publication;

import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.Availability;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductRevision;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class UnavailableProductPublicationEvidenceAdapterTest {
    private final UnavailableProductPublicationEvidenceAdapter adapter =
            new UnavailableProductPublicationEvidenceAdapter();

    @Test
    void exactProductAndRevisionRequestAlwaysReturnsAnEmptyUnavailableSnapshot() {
        var snapshot = adapter.currentEvidence(new ProductId("product-one"),
                new ProductRevision("v1_" + "a".repeat(64)));

        assertThat(snapshot.availability()).isEqualTo(Availability.UNAVAILABLE);
        assertThat(snapshot.contentAcceptances()).isEmpty();
        assertThat(snapshot.usageRightsAcceptances()).isEmpty();
        assertThat(snapshot.securityChecks()).isEmpty();
        assertThat(snapshot.associationAcceptances()).isEmpty();
        assertThat(snapshot.invalidations()).isEmpty();
    }

    @Test
    void malformedBoundaryCallsCannotBecomeUnavailableEvidenceForAnotherSubject() {
        ProductId productId = new ProductId("product-one");
        ProductRevision revision = new ProductRevision("v1_" + "a".repeat(64));

        assertThatNullPointerException().isThrownBy(() -> adapter.currentEvidence(null, revision));
        assertThatNullPointerException().isThrownBy(() -> adapter.currentEvidence(productId, null));
    }
}
