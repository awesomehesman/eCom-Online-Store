package com.enterprise.fashion.ecommerce.pricing.domain.model;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import com.enterprise.fashion.ecommerce.pricing.domain.model.VariantBasePriceAssignment.State;
import com.enterprise.fashion.ecommerce.pricing.domain.model.VariantBasePriceAssignment.Subject;

/**
 * Pure selection over a complete supplied observation for one subject. The caller must
 * establish current authoritative Product membership, source completeness, authorized
 * commitment and freshness separately. Selected is conditional domain consistency,
 * never external Product authority, Staff authorization, sellability or a Checkout total.
 * Missing input is invalid, not an empty authoritative observation. No Product fallback.
 */
public final class SelectVariantBasePrice {
    public Result select(Subject subject, List<VariantBasePriceAssignment> assignments) {
        if (subject == null || assignments == null) {
            return new Invalid();
        }
        // Validate the entire observation before selecting; never ignore mismatched history.
        for (VariantBasePriceAssignment assignment : assignments) {
            if (assignment == null || !subject.equals(assignment.subject())) {
                return new Invalid();
            }
        }
        List<VariantBasePriceAssignment> current = assignments.stream()
                .filter(assignment -> assignment.state() == State.CURRENT).toList();
        if (current.size() > 1) {
            return new Conflict();
        }
        // One revision cannot simultaneously describe multiple representations or states.
        if (assignments.stream().map(VariantBasePriceAssignment::revision).distinct().count() != assignments.size()) {
            return new Invalid();
        }
        if (current.size() == 1) {
            return new Selected(current.getFirst());
        }
        if (assignments.isEmpty()) {
            return new Missing();
        }
        Set<State> history = EnumSet.noneOf(State.class);
        assignments.forEach(assignment -> history.add(assignment.state()));
        return new Historical(history);
    }

    public sealed interface Result permits Selected, Missing, Historical, Conflict, Invalid { }

    public record Selected(VariantBasePriceAssignment assignment) implements Result {
        public Selected {
            if (assignment == null || assignment.state() != State.CURRENT) {
                throw new IllegalArgumentException("Selection requires a current assignment");
            }
        }
    }
    public record Missing() implements Result { }
    public record Conflict() implements Result { }
    public record Invalid() implements Result { }

    /** No current result. Both obsolete and withdrawn history may be present; neither wins. */
    public record Historical(Set<State> states) implements Result {
        public Historical {
            if (states == null || states.isEmpty() || states.contains(State.CURRENT)) {
                throw new IllegalArgumentException("Historical result requires noncurrent states");
            }
            states = Set.copyOf(states);
        }
    }
}
