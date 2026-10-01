package com.enterprise.fashion.ecommerce.identity.domain.model;

import java.util.List;

/**
 * Untrusted Customer Login input prepared under DEC-0005, awaiting strict domain validation.
 * Domain labels have not been validated under IDNA2008, including apparent A-labels.
 * This is not a canonical identifier or comparison key and must not be used for lookup.
 * Preparation establishes no uniqueness, mailbox control, current-email verification,
 * credential evidence, Authentication, Principal, Session, association, Customer/Account
 * ownership, Authorization, or HTTP success.
 */
public final class CustomerLoginEmailInput {

    private final String asciiLowercaseLocalPart;
    private final List<String> unvalidatedDomainLabels;

    private CustomerLoginEmailInput(String asciiLowercaseLocalPart, List<String> unvalidatedDomainLabels) {
        this.asciiLowercaseLocalPart = asciiLowercaseLocalPart;
        this.unvalidatedDomainLabels = List.copyOf(unvalidatedDomainLabels);
    }

    /**
     * Prepares a candidate only; successful return still requires strict domain validation.
     * No original input copy is retained and domain-label content is left unchanged.
     *
     * @throws IllegalArgumentException when input fails the preparation rules; the message contains no input
     */
    public static CustomerLoginEmailInput prepareForDomainValidation(String submittedInput) {
        if (submittedInput == null) {
            throw invalidInput();
        }
        int start = 0;
        int end = submittedInput.length();
        while (start < end && isSurroundingCharacter(submittedInput.charAt(start))) {
            start++;
        }
        while (end > start && isSurroundingCharacter(submittedInput.charAt(end - 1))) {
            end--;
        }
        if (start == end) {
            throw invalidInput();
        }
        String candidate = submittedInput.substring(start, end);
        rejectControlsWhitespaceAndMalformedUnicode(candidate);
        int separator = candidate.indexOf('@');
        if (separator <= 0 || separator == candidate.length() - 1
                || candidate.indexOf('@', separator + 1) != -1) {
            throw invalidInput();
        }

        StringBuilder localPart = new StringBuilder(separator);
        boolean previousWasDot = true;
        for (int index = 0; index < separator; index++) {
            char character = candidate.charAt(index);
            if (character == '.') {
                if (previousWasDot) {
                    throw invalidInput();
                }
                previousWasDot = true;
            } else {
                if (!isAsciiAtext(character)) {
                    throw invalidInput();
                }
                previousWasDot = false;
            }
            localPart.append(character >= 'A' && character <= 'Z'
                    ? (char) (character + ('a' - 'A')) : character);
        }
        if (previousWasDot) {
            throw invalidInput();
        }

        String domain = candidate.substring(separator + 1);
        for (int index = 0; index < domain.length(); index++) {
            char character = domain.charAt(index);
            // Reject address syntax and the explicitly prohibited alternative separators.
            // All remaining label validity, including other disallowed Unicode, is deferred.
            if ("()<>[]:;,\"\\".indexOf(character) >= 0
                    || character == '\u3002' || character == '\uff0e' || character == '\uff61') {
                throw invalidInput();
            }
        }
        List<String> labels = List.of(domain.split("\\.", -1));
        if (labels.stream().anyMatch(String::isEmpty)) {
            throw invalidInput();
        }
        return new CustomerLoginEmailInput(localPart.toString(), labels);
    }

    /** Returns sensitive local-part input with only governed ASCII lowercase mapping applied. */
    public String asciiLowercaseLocalPart() {
        return asciiLowercaseLocalPart;
    }

    /** Returns sensitive, unchanged labels; their validity and canonical representation remain unknown. */
    public List<String> unvalidatedDomainLabels() {
        return unvalidatedDomainLabels;
    }

    @Override
    public String toString() {
        return "CustomerLoginEmailInput[awaiting strict domain validation; redacted]";
    }

    private static boolean isSurroundingCharacter(char character) {
        return character == ' ' || character == '\t';
    }

    private static boolean isAsciiAtext(char character) {
        return character >= 'a' && character <= 'z'
                || character >= 'A' && character <= 'Z'
                || character >= '0' && character <= '9'
                || "!#$%&'*+-/=?^_`{|}~".indexOf(character) >= 0;
    }

    private static void rejectControlsWhitespaceAndMalformedUnicode(String candidate) {
        for (int index = 0; index < candidate.length();) {
            char character = candidate.charAt(index);
            if (Character.isLowSurrogate(character)
                    || Character.isHighSurrogate(character)
                    && (index + 1 == candidate.length()
                    || !Character.isLowSurrogate(candidate.charAt(index + 1)))) {
                throw invalidInput();
            }
            int codePoint = candidate.codePointAt(index);
            if (Character.isISOControl(codePoint)
                    || Character.isWhitespace(codePoint) || Character.isSpaceChar(codePoint)) {
                throw invalidInput();
            }
            index += Character.charCount(codePoint);
        }
    }

    private static IllegalArgumentException invalidInput() {
        return new IllegalArgumentException("Customer login email input cannot be prepared");
    }
}
