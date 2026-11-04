package com.enterprise.fashion.ecommerce.pricing.application.query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import com.enterprise.fashion.ecommerce.pricing.application.persistence.BasePricePersistenceService;
import com.enterprise.fashion.ecommerce.pricing.domain.model.Currency;
import com.enterprise.fashion.ecommerce.pricing.domain.model.Money;
import com.enterprise.fashion.ecommerce.pricing.domain.model.SelectVariantBasePrice;
import com.enterprise.fashion.ecommerce.pricing.domain.model.VariantBasePriceAssignment;
import com.enterprise.fashion.ecommerce.pricing.domain.model.VariantBasePriceAssignment.State;
import com.enterprise.fashion.ecommerce.pricing.domain.model.VariantBasePriceAssignment.Subject;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class ObserveVariantBasePriceTest {
    private final BasePricePersistenceService persistence = mock(BasePricePersistenceService.class);
    private final ObserveVariantBasePrice query = new ObserveVariantBasePrice(persistence);
    private final Subject subject = new Subject("product-1", "variant-1");
    private VariantBasePriceAssignment assignment(String revision, State state) {
        return new VariantBasePriceAssignment(subject, revision,
                new Money(new BigDecimal("1.12345678901234567890"), Currency.ZAR), state);
    }

    @Test
    void selectedIsTheExactStoredValueAndOnlyOneReadIsPerformed() {
        var current = assignment("a", State.CURRENT);
        when(persistence.load(subject)).thenReturn(List.of(current));
        var result = query.observe(subject);
        assertThat(result).isEqualTo(new SelectVariantBasePrice.Selected(current));
        assertThat(((SelectVariantBasePrice.Selected) result).assignment().money().amount())
                .isEqualTo(new BigDecimal("1.12345678901234567890"));
        verify(persistence).load(subject);
        verifyNoMoreInteractions(persistence);
    }

    @Test
    void missingIsReturnedForConfirmedEmptyHistoryOnly() {
        when(persistence.load(subject)).thenReturn(List.of());
        assertThat(query.observe(subject)).isEqualTo(new SelectVariantBasePrice.Missing());
    }

    @Test
    void obsoleteWithdrawnAndMixedHistoryRemainDistinctWithoutReactivation() {
        when(persistence.load(subject)).thenReturn(List.of(assignment("r1", State.OBSOLETE)));
        assertThat(query.observe(subject)).isEqualTo(new SelectVariantBasePrice.Historical(Set.of(State.OBSOLETE)));
        when(persistence.load(subject)).thenReturn(List.of(assignment("r2", State.WITHDRAWN)));
        assertThat(query.observe(subject)).isEqualTo(new SelectVariantBasePrice.Historical(Set.of(State.WITHDRAWN)));
        when(persistence.load(subject)).thenReturn(List.of(assignment("r1", State.OBSOLETE), assignment("r2", State.WITHDRAWN)));
        assertThat(query.observe(subject)).isEqualTo(new SelectVariantBasePrice.Historical(Set.of(State.OBSOLETE, State.WITHDRAWN)));
    }

    @Test
    void currentDoesNotDependOnRevisionOrderingAndConflictsHaveNoWinner() {
        var current = assignment("a", State.CURRENT);
        var old = assignment("zzz", State.OBSOLETE);
        when(persistence.load(subject)).thenReturn(List.of(old, current));
        assertThat(query.observe(subject)).isEqualTo(new SelectVariantBasePrice.Selected(current));
        when(persistence.load(subject)).thenReturn(List.of(current, old));
        assertThat(query.observe(subject)).isEqualTo(new SelectVariantBasePrice.Selected(current));
        when(persistence.load(subject)).thenReturn(List.of(current, assignment("zzz", State.CURRENT)));
        assertThat(query.observe(subject)).isEqualTo(new SelectVariantBasePrice.Conflict());
    }

    @Test
    void mismatchedReferencesCannotReturnSelectedOrProductFallback() {
        for (Subject wrong : List.of(new Subject("other", "variant-1"), new Subject("product-1", "other"))) {
            when(persistence.load(subject)).thenReturn(List.of(new VariantBasePriceAssignment(wrong, "r",
                    new Money(BigDecimal.ONE, Currency.ZAR), State.CURRENT)));
            assertThat(query.observe(subject)).isEqualTo(new SelectVariantBasePrice.Invalid());
        }

        verify(persistence, org.mockito.Mockito.times(2)).load(subject);
        verifyNoMoreInteractions(persistence);
    }

    @Test
    void duplicateRevisionObservationCannotReturnSelected() {
        // Defensive selector behavior with a synthetic observation, not a claim that
        // the real persistence adapter permits this corrupt representation.
        when(persistence.load(subject)).thenReturn(List.of(assignment("r", State.CURRENT), assignment("r", State.OBSOLETE)));
        assertThat(query.observe(subject)).isEqualTo(new SelectVariantBasePrice.Invalid());
    }

    @Test
    void invalidReferencesAreRejectedBeforeReading() {
        assertThatIllegalArgumentException().isThrownBy(() -> query.observe(null));
        assertThatIllegalArgumentException().isThrownBy(() -> query.observe(new Subject("p", "")));
        assertThatIllegalArgumentException().isThrownBy(() -> query.observe(new Subject("p/1", "v")));
        verifyNoInteractions(persistence);
    }

    @Test
    void databaseTransactionAndCorruptionFailuresAreNotMissing() {
        for (RuntimeException failure : List.of(
                new org.springframework.dao.DataAccessResourceFailureException("synthetic unavailable database"),
                new org.springframework.transaction.CannotCreateTransactionException("synthetic transaction failure"),
                new IllegalStateException("synthetic corrupt persisted chain"))) {
            org.mockito.Mockito.doThrow(failure).when(persistence).load(subject);
            assertThatThrownBy(() -> query.observe(subject)).isSameAs(failure);
        }
    }

    @Test
    void observationHasNoExternalCommercialAuthorityOrActorClaims() {
        // Unverified references are syntax-valid. This observation deliberately cannot
        // establish their existence or a customer's right to purchase the Variant.
        when(persistence.load(subject)).thenReturn(List.of(assignment("r", State.CURRENT)));
        var result = query.observe(subject);
        assertThat(result).isInstanceOf(SelectVariantBasePrice.Selected.class);
        assertThat(result.getClass().getRecordComponents()).extracting("name").containsExactly("assignment");
        verify(persistence).load(subject);
        verifyNoMoreInteractions(persistence);
    }
}
