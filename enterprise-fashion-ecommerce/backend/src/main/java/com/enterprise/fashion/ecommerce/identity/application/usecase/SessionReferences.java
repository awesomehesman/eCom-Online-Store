package com.enterprise.fashion.ecommerce.identity.application.usecase;

/** Shared validation for opaque references supported by the existing Session store. */
final class SessionReferences {
    private SessionReferences() {
    }

    static void validate(String sessionIdentifier) {
        // V1 stores opaque Session identifiers in CHAR(36). Do not normalize credentials.
        if (sessionIdentifier == null || sessionIdentifier.isBlank() || sessionIdentifier.length() > 36
                || sessionIdentifier.codePoints().anyMatch(c -> Character.isWhitespace(c)
                        || Character.isSpaceChar(c) || Character.isISOControl(c))) {
            throw new IllegalArgumentException("Invalid Session reference");
        }
    }
}
