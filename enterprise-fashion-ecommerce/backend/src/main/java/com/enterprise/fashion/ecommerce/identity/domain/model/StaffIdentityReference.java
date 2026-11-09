package com.enterprise.fashion.ecommerce.identity.domain.model;

/** Exact, case-sensitive authority namespace and opaque subject. Construction authenticates nothing. */
public record StaffIdentityReference(String authority, String subject) {
    public StaffIdentityReference {
        if (!bounded(authority, 128) || !bounded(subject, 512)) {
            throw new IllegalArgumentException("Invalid Staff identity reference");
        }
    }
    private static boolean bounded(String value, int maximum) {
        return value != null && !value.isBlank() && value.length() <= maximum
                && value.codePoints().noneMatch(Character::isISOControl);
    }
    @Override public String toString() { return "StaffIdentityReference[redacted]"; }
}
