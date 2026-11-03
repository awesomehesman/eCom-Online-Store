package com.enterprise.fashion.ecommerce.pricing.domain.model;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.enterprise.fashion.ecommerce.pricing.domain.model.VariantBasePriceAssignment.State;
import com.enterprise.fashion.ecommerce.pricing.domain.model.VariantBasePriceAssignment.Subject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VariantBasePriceTest {
    private final Subject subject = new Subject("product-1", "variant-1");
    private final SelectVariantBasePrice selector = new SelectVariantBasePrice();

    private VariantBasePriceAssignment assignment(String revision, State state) {
        return new VariantBasePriceAssignment(subject, revision, new Money(new BigDecimal("12.345678901"), Currency.ZAR), state);
    }

    @Test
    void positiveZarPreservesExactRepresentationAndValueSemantics() {
        var first = assignment("revision-A", State.CURRENT);
        assertThat(first).isEqualTo(assignment("revision-A", State.CURRENT));
        assertThat(first.hashCode()).isEqualTo(assignment("revision-A", State.CURRENT).hashCode());
        assertThat(first.money().amount()).isEqualTo(new BigDecimal("12.345678901"));
        assertThat(first.subject()).isEqualTo(subject);
        assertThat(first.revision()).isEqualTo("revision-A");
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-0.00", "-1", "-0.000000001"})
    void rejectsNonpositiveBasePriceWithoutRestrictingMoney(String value) {
        Money money = new Money(new BigDecimal(value), Currency.ZAR);
        assertThat(money.amount()).isEqualTo(new BigDecimal(value));
        assertThatIllegalArgumentException().isThrownBy(
                () -> new VariantBasePriceAssignment(subject, "r", money, State.CURRENT));
    }

    @Test
    void rejectsUnsupportedCurrencyAtExistingCurrencyBoundary() {
        assertThatIllegalArgumentException().isThrownBy(() -> new VariantBasePriceAssignment(
                subject, "r", new Money(BigDecimal.ONE, new Currency("USD")), State.CURRENT));
    }

    @Test
    void rejectsInvalidAssignmentAndReferenceRepresentations() {
        var money = assignment("r", State.CURRENT).money();
        assertThatIllegalArgumentException().isThrownBy(() -> new VariantBasePriceAssignment(null, "r", money, State.CURRENT));
        assertThatIllegalArgumentException().isThrownBy(() -> new VariantBasePriceAssignment(subject, null, money, State.CURRENT));
        assertThatIllegalArgumentException().isThrownBy(() -> new VariantBasePriceAssignment(subject, " ", money, State.CURRENT));
        assertThatIllegalArgumentException().isThrownBy(() -> new VariantBasePriceAssignment(subject, "r", null, State.CURRENT));
        assertThatIllegalArgumentException().isThrownBy(() -> new VariantBasePriceAssignment(subject, "r", money, null));
        assertThatIllegalArgumentException().isThrownBy(() -> new Subject("p", null));
        assertThatIllegalArgumentException().isThrownBy(() -> new Subject(" ", "v"));
        assertThatIllegalArgumentException().isThrownBy(() -> new Subject("p", "v/1"));
        assertThatIllegalArgumentException().isThrownBy(() -> new Subject("p", "v".repeat(129)));
    }

    @Test
    void emptyObservationIsMissingButAbsentOrInvalidObservationIsInvalid() {
        assertThat(selector.select(subject, List.of())).isEqualTo(new SelectVariantBasePrice.Missing());
        assertThat(selector.select(subject, null)).isEqualTo(new SelectVariantBasePrice.Invalid());
        assertThat(selector.select(null, List.of())).isEqualTo(new SelectVariantBasePrice.Invalid());
        assertThat(selector.select(subject, Arrays.asList((VariantBasePriceAssignment) null)))
                .isEqualTo(new SelectVariantBasePrice.Invalid());
    }

    @Test
    void selectsOnlyCurrentAssignmentRegardlessOfRevisionLabelsOrInputOrder() {
        var current = assignment("a", State.CURRENT);
        var old = assignment("zzz", State.OBSOLETE);
        var withdrawn = assignment("newest-looking", State.WITHDRAWN);
        assertThat(selector.select(subject, List.of(old, current, withdrawn)))
                .isEqualTo(new SelectVariantBasePrice.Selected(current));
        assertThat(selector.select(subject, List.of(withdrawn, current, old)))
                .isEqualTo(new SelectVariantBasePrice.Selected(current));
        assertThat(selector.select(subject, List.of(current))).isEqualTo(new SelectVariantBasePrice.Selected(current));
    }

    @Test
    void multipleCurrentAssignmentsConflictWithoutLatestWinsOrDeduplication() {
        var first = assignment("a", State.CURRENT);
        var second = assignment("z", State.CURRENT);
        assertThat(selector.select(subject, List.of(first, second))).isEqualTo(new SelectVariantBasePrice.Conflict());
        assertThat(selector.select(subject, List.of(second, first))).isEqualTo(new SelectVariantBasePrice.Conflict());
        assertThat(selector.select(subject, List.of(first, first))).isEqualTo(new SelectVariantBasePrice.Conflict());
    }

    @Test
    void historyDistinguishesObsoleteWithdrawnAndMixedWithoutFallback() {
        for (State state : List.of(State.OBSOLETE, State.WITHDRAWN)) {
            assertThat(selector.select(subject, List.of(assignment("r", state))))
                    .isEqualTo(new SelectVariantBasePrice.Historical(Set.of(state)));
        }
        var result = (SelectVariantBasePrice.Historical) selector.select(subject,
                List.of(assignment("a", State.OBSOLETE), assignment("b", State.WITHDRAWN)));
        assertThat(result.states()).containsExactlyInAnyOrder(State.OBSOLETE, State.WITHDRAWN);
        assertThatThrownBy(() -> result.states().clear()).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void sameRevisionCannotBeReinterpretedAsCurrentAndHistorical() {
        assertThat(selector.select(subject, List.of(assignment("r", State.CURRENT), assignment("r", State.OBSOLETE))))
                .isEqualTo(new SelectVariantBasePrice.Invalid());
    }

    @Test
    void changedRevisionDoesNotMutateRetainedHistoricalMeaning() {
        var historical = assignment("old", State.OBSOLETE);
        var changed = new VariantBasePriceAssignment(subject, "replacement", new Money(new BigDecimal("99"), Currency.ZAR), State.CURRENT);
        var source = List.of(historical, changed);
        assertThat(selector.select(subject, source)).isEqualTo(new SelectVariantBasePrice.Selected(changed));
        assertThat(historical.money().amount()).isEqualTo(new BigDecimal("12.345678901"));
        assertThat(historical.state()).isEqualTo(State.OBSOLETE);
        assertThat(source).containsExactly(historical, changed);
    }

    @Test
    void rejectsProductAndVariantMismatchEvenInHistoryWithoutProductFallback() {
        var current = assignment("r", State.CURRENT);
        assertThat(selector.select(new Subject("other-product", "variant-1"), List.of(current)))
                .isEqualTo(new SelectVariantBasePrice.Invalid());
        assertThat(selector.select(new Subject("product-1", "other-variant"), List.of(current)))
                .isEqualTo(new SelectVariantBasePrice.Invalid());
        var wrongHistory = new VariantBasePriceAssignment(new Subject("other", "variant-1"), "old", current.money(), State.OBSOLETE);
        assertThat(selector.select(subject, List.of(current, wrongHistory))).isEqualTo(new SelectVariantBasePrice.Invalid());
    }

    @Test
    void syntacticallyValidReferencesProduceNoExternalProductOrSellabilityEvidence() {
        // No Product authority is available to this pure function. Selection can only
        // express consistency of supplied data, even for references with no proven existence.
        var unverified = new Subject("unverified-product", "unverified-variant");
        var candidate = new VariantBasePriceAssignment(unverified, "r", new Money(BigDecimal.ONE, Currency.ZAR), State.CURRENT);
        assertThat(selector.select(unverified, List.of(candidate))).isEqualTo(new SelectVariantBasePrice.Selected(candidate));
        assertThat(SelectVariantBasePrice.Selected.class.getRecordComponents()).extracting("name").containsExactly("assignment");
    }
}
