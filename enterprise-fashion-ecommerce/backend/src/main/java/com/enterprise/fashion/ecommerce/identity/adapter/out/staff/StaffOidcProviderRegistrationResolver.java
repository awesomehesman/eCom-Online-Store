package com.enterprise.fashion.ecommerce.identity.adapter.out.staff;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Dormant exact-pair lookup over an immutable registration snapshot. Structural resolution is not
 * provider admission, current operational trust or Authentication.
 */
final class StaffOidcProviderRegistrationResolver {
    enum Outcome { RESOLVED, MISSING, DISABLED, AMBIGUOUS, UNAVAILABLE }

    record Resolution(Outcome outcome, StaffOidcProviderRegistration registration) {
        Resolution {
            Objects.requireNonNull(outcome, "Resolution outcome is required");
            if ((outcome == Outcome.RESOLVED) != (registration != null)) {
                throw new IllegalArgumentException("Invalid provider-registration resolution");
            }
        }

        @Override public String toString() {
            return "StaffOidcProviderRegistrationResolution[" + outcome + "]";
        }
    }

    private record Key(String registrationId, long registrationRevision) {}

    private final Map<Key, StaffOidcProviderRegistration> registrations;
    private final Set<Key> ambiguousKeys;

    StaffOidcProviderRegistrationResolver(
            Collection<StaffOidcProviderRegistration> registrations) {
        Objects.requireNonNull(registrations, "Provider registrations are required");
        Map<Key, StaffOidcProviderRegistration> unique = new HashMap<>();
        Set<Key> ambiguous = new HashSet<>();
        for (StaffOidcProviderRegistration registration : registrations) {
            Objects.requireNonNull(registration, "Provider registration is required");
            Key key = new Key(registration.registrationId(), registration.revision());
            if (ambiguous.contains(key)) continue;
            StaffOidcProviderRegistration existing = unique.putIfAbsent(key, registration);
            if (existing != null) {
                unique.remove(key);
                ambiguous.add(key);
            }
        }
        this.registrations = Map.copyOf(unique);
        this.ambiguousKeys = Set.copyOf(ambiguous);
    }

    Resolution resolve(String registrationId, long registrationRevision) {
        if (!validId(registrationId) || registrationRevision <= 0) {
            return result(Outcome.MISSING, null);
        }
        Key key = new Key(registrationId, registrationRevision);
        if (ambiguousKeys.contains(key)) {
            return result(Outcome.AMBIGUOUS, null);
        }
        StaffOidcProviderRegistration registration = registrations.get(key);
        if (registration == null) {
            return result(Outcome.MISSING, null);
        }
        if (!registration.enabled()) {
            return result(Outcome.DISABLED, null);
        }
        return result(Outcome.RESOLVED, registration);
    }

    @Override public String toString() {
        return "StaffOidcProviderRegistrationResolver[redacted]";
    }

    private static boolean validId(String value) {
        return value != null && !value.isBlank() && value.length() <= 128
                && value.codePoints().noneMatch(Character::isISOControl);
    }

    private static Resolution result(Outcome outcome, StaffOidcProviderRegistration registration) {
        return new Resolution(outcome, registration);
    }
}
