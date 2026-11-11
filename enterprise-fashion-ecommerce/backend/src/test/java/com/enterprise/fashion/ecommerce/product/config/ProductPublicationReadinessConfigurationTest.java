package com.enterprise.fashion.ecommerce.product.config;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.enterprise.fashion.ecommerce.product.adapter.out.publication.UnavailableProductPublicationEvidenceAdapter;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductEvidenceEvent;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.Availability;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidence.Snapshot;
import com.enterprise.fashion.ecommerce.product.application.evidence.ProductPublicationEvidencePolicy;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductEvidenceObservationPort;
import com.enterprise.fashion.ecommerce.product.application.port.out.ProductPublicationEvidencePort;
import com.enterprise.fashion.ecommerce.product.application.usecase.AssessProductPublicationReadiness;
import com.enterprise.fashion.ecommerce.product.application.visibility.ProductPublicationReadiness.Outcome;
import com.enterprise.fashion.ecommerce.product.domain.model.Product;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductAttributeStandard;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductContent;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductFamily;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductId;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductMedia;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductSize;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariant;
import com.enterprise.fashion.ecommerce.product.domain.model.ProductVariantId;
import com.enterprise.fashion.ecommerce.product.domain.model.VariantConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class ProductPublicationReadinessConfigurationTest {
    private final ApplicationContextRunner context = new ApplicationContextRunner()
            .withUserConfiguration(ProductPublicationReadinessConfiguration.class);

    @Test
    void defaultCompositionIsAvailableForUseButAlwaysFailsClosed() {
        context.run(application -> {
            assertThat(application).hasSingleBean(ProductPublicationEvidencePort.class)
                    .hasSingleBean(ProductPublicationEvidencePolicy.class)
                    .hasSingleBean(AssessProductPublicationReadiness.class);
            assertThat(application.getBean(ProductPublicationEvidencePort.class))
                    .isInstanceOf(UnavailableProductPublicationEvidenceAdapter.class);

            var result = application.getBean(AssessProductPublicationReadiness.class).assess(product());

            assertThat(result.outcome()).isEqualTo(Outcome.EVIDENCE_UNAVAILABLE);
        });
    }

    @Test
    void separatelySuppliedAuthoritativeBoundaryAndPolicyReplaceOnlyTheFailClosedDefaults() {
        AtomicInteger requests = new AtomicInteger();
        ProductPublicationEvidencePort suppliedPort = (productId, revision) -> {
            requests.incrementAndGet();
            return new Snapshot(Availability.CONFLICTING, List.of(), List.of(), List.of(), List.of(), List.of());
        };
        ProductPublicationEvidencePolicy suppliedPolicy = (kind, source) -> true;

        context.withBean(ProductPublicationEvidencePort.class, () -> suppliedPort)
                .withBean(ProductPublicationEvidencePolicy.class, () -> suppliedPolicy)
                .run(application -> {
                    assertThat(application).hasSingleBean(ProductPublicationEvidencePort.class)
                            .hasSingleBean(ProductPublicationEvidencePolicy.class);
                    assertThat(application.getBean(ProductPublicationEvidencePort.class)).isSameAs(suppliedPort);
                    assertThat(application.getBean(ProductPublicationEvidencePolicy.class)).isSameAs(suppliedPolicy);

                    var result = application.getBean(AssessProductPublicationReadiness.class).assess(product());

                    assertThat(result.outcome()).isEqualTo(Outcome.CONFLICTING_EVIDENCE);
                    assertThat(requests).hasValue(1);
                });
    }

    @Test
    void unconfirmedEvidenceHistoryPortCannotReplaceTheTrustedPublicationEvidencePort() {
        ProductEvidenceObservationPort unconfirmedHistory =
                (productId, purpose) -> List.<ProductEvidenceEvent>of();

        context.withBean(ProductEvidenceObservationPort.class, () -> unconfirmedHistory)
                .run(application -> {
                    assertThat(application.getBean(ProductPublicationEvidencePort.class))
                            .isInstanceOf(UnavailableProductPublicationEvidenceAdapter.class);
                    assertThat(application.getBean(AssessProductPublicationReadiness.class)
                            .assess(product()).outcome()).isEqualTo(Outcome.EVIDENCE_UNAVAILABLE);
                });
    }

    private static Product product() {
        ProductId productId = new ProductId("product-one");
        ProductVariantId variantId = new ProductVariantId("variant-m");
        var configuration = new VariantConfiguration(ProductFamily.APPAREL, ProductAttributeStandard.INITIAL,
                true, false, ProductSize.apparel("M"), null);
        var variant = new ProductVariant(variantId, productId, configuration, null, null);
        var media = new ProductMedia("front", productId, null, "asset-revision-1",
                "https://media.example.invalid/front", "Front view");
        return new Product(productId, ProductFamily.APPAREL, ProductAttributeStandard.INITIAL, true, false)
                .withVariant(variant)
                .withRepresentation(new ProductContent("Black shirt", "A plain black shirt."), List.of(media));
    }
}
