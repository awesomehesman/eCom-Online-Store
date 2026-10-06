package com.enterprise.fashion.ecommerce.identity.domain.model;

import java.text.Normalizer;

/**
 * Transient Identity-owned prospective password awaiting remaining establishment policy checks.
 * Preparation establishes only valid Unicode, NFC and DEC-0007's structural length bounds.
 * It does not establish blocklist approval, complete policy acceptance, a credential,
 * Authentication, Principal, Session or completed Registration. Not for Login verification.
 * Keep this material within the credential-establishment boundary and only as long as needed;
 * neither submitted nor normalized secrets may be logged, serialized or persisted.
 */
public final class PreparedProspectiveCustomerPassword {

    private final String normalizedValue;

    private PreparedProspectiveCustomerPassword(String normalizedValue) {
        this.normalizedValue = normalizedValue;
    }

    /**
     * Prepares the complete prospective secret without trimming, case folding or truncation.
     *
     * @throws IllegalArgumentException for absent, malformed or structurally invalid input;
     *         the exception contains no secret material
     */
    public static PreparedProspectiveCustomerPassword prepareForRemainingPolicyChecks(String submittedPassword) {
        if (submittedPassword == null) {
            throw invalidInput();
        }
        for (int index = 0; index < submittedPassword.length(); index++) {
            char character = submittedPassword.charAt(index);
            if (Character.isHighSurrogate(character)) {
                if (index + 1 >= submittedPassword.length()
                        || !Character.isLowSurrogate(submittedPassword.charAt(index + 1))) {
                    throw invalidInput();
                }
                index++;
            } else if (Character.isLowSurrogate(character)) {
                throw invalidInput();
            }
        }

        String normalized = Normalizer.normalize(submittedPassword, Normalizer.Form.NFC);
        int codePoints = normalized.codePointCount(0, normalized.length());
        if (codePoints < 15 || codePoints > 64) {
            throw invalidInput();
        }
        return new PreparedProspectiveCustomerPassword(normalized);
    }

    /** Sensitive material for subsequent Identity-owned policy checks; not policy acceptance. */
    public String normalizedValue() {
        return normalizedValue;
    }

    @Override
    public String toString() {
        return "PreparedProspectiveCustomerPassword[REDACTED; awaiting remaining establishment policy checks]";
    }

    private static IllegalArgumentException invalidInput() {
        return new IllegalArgumentException("Prospective Customer password cannot be prepared");
    }
}
