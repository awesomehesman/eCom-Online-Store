package com.enterprise.fashion.ecommerce.pricing.application.query;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import com.enterprise.fashion.ecommerce.product.application.api.ProductVariantEvidence;
import com.enterprise.fashion.ecommerce.pricing.application.persistence.BasePricePersistenceService;
import com.enterprise.fashion.ecommerce.pricing.domain.model.Currency;
import com.enterprise.fashion.ecommerce.pricing.domain.model.Money;
import com.enterprise.fashion.ecommerce.pricing.domain.model.SelectVariantBasePrice;
import com.enterprise.fashion.ecommerce.pricing.domain.model.VariantBasePriceAssignment.State;
import com.enterprise.fashion.ecommerce.pricing.domain.model.VariantBasePriceAssignment.Subject;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/** Real Pricing storage plus an isolated exported-Contract test double; no production Product authority is manufactured. */
@Tag("integration")
@SpringBootTest
@Testcontainers
class ProductAssociatedBasePriceIntegrationTest {
    @Container @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:18");
    @MockitoBean ProductVariantEvidence products;
    @Autowired ObserveProductAssociatedBasePrice query;
    @Autowired BasePricePersistenceService persistence;
    @Autowired JdbcTemplate jdbc;
    private Subject subject() { return new Subject("p-" + UUID.randomUUID(), "v-" + UUID.randomUUID()); }
    private void confirm(Subject subject) {
        when(products.observe(subject.productReference(), subject.variantReference())).thenAnswer(call -> {
            assertThat(TransactionSynchronizationManager.isActualTransactionActive()).isFalse();
            return new ProductVariantEvidence.ConfirmedAssociation(subject.productReference(), subject.variantReference(),
                    ProductVariantEvidence.Lifecycle.DRAFT, "synthetic-Product-revision");
        });
    }

    @Test
    void realStoredSelectionIsConditionalAndQueryDoesNotWrite() {
        var s = subject(); confirm(s);
        var money = new Money(new BigDecimal("1.12345678901234567890"), Currency.ZAR);
        persistence.initialize(s, "price-r1", money);
        var before = persistence.load(s);
        var result = (ObserveProductAssociatedBasePrice.ConditionallyValidatedStoredObservation)
                query.observe(s.productReference(), s.variantReference());
        assertThat(result.pricing().assignment().subject()).isEqualTo(s);
        assertThat(result.pricing().assignment().money().amount()).isEqualTo(money.amount());
        assertThat(result.product().representationRevision()).isEqualTo("synthetic-Product-revision");
        assertThat(persistence.load(s)).isEqualTo(before);
        assertThat(jdbc.queryForObject("SELECT count(*) FROM pricing.base_price_revision WHERE variant_reference = ?",
                Integer.class, s.variantReference())).isEqualTo(1);
        verify(products).observe(s.productReference(), s.variantReference()); verifyNoMoreInteractions(products);
    }

    @Test
    void withdrawalRemainsHistoricalAndMissingPriceIsNotProductAbsence() {
        var s = subject(); confirm(s);
        assertThat(query.observe(s.productReference(), s.variantReference()))
                .isInstanceOfSatisfying(ObserveProductAssociatedBasePrice.PricingNotSelected.class,
                        observation -> assertThat(observation.pricing()).isEqualTo(new SelectVariantBasePrice.Missing()));
        persistence.initialize(s, "one", new Money(BigDecimal.ONE, Currency.ZAR));
        persistence.withdraw(s, "one", "withdrawn");
        assertThat(query.observe(s.productReference(), s.variantReference()))
                .isInstanceOfSatisfying(ObserveProductAssociatedBasePrice.PricingNotSelected.class,
                        observation -> assertThat(observation.pricing())
                                .isEqualTo(new SelectVariantBasePrice.Historical(Set.of(State.OBSOLETE, State.WITHDRAWN))));
        assertThat(persistence.load(s)).hasSize(2).noneMatch(a -> a.state() == State.CURRENT);
    }

    @Test
    void storedOwnershipMismatchCannotProduceConditionalValidation() {
        var s = subject();
        persistence.initialize(s, "one", new Money(BigDecimal.ONE, Currency.ZAR));
        var wrong = new Subject("other-owner", s.variantReference()); confirm(wrong);
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> query.observe(wrong.productReference(), wrong.variantReference()))
                .isInstanceOf(IllegalStateException.class);
    }
}
