package com.enterprise.fashion.ecommerce.identity.application.credential;

import java.text.Normalizer;
import java.util.Objects;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.identity.application.CredentialVerificationOutcome;
import com.enterprise.fashion.ecommerce.identity.application.port.out.CustomerPasswordVerificationPort;
import com.enterprise.fashion.ecommerce.identity.application.port.out.CredentialSourcePort.Observation;
import com.enterprise.fashion.ecommerce.identity.domain.model.CredentialSubject;

/** Bounded credential verification only. No identifier resolution or Authentication orchestration. */
public final class VerifySourceBoundCustomerPassword {
    private final CredentialSource source;
    private final CustomerPasswordVerificationPort passwords;

    public VerifySourceBoundCustomerPassword(CredentialSource source, CustomerPasswordVerificationPort passwords) {
        this.source = Objects.requireNonNull(source);
        this.passwords = Objects.requireNonNull(passwords);
    }

    public SourceBoundPasswordVerificationResult verify(
            CredentialSubject subject, UUID fact, CharSequence submittedPassword) {
        CredentialVerifierObservation observed = source.forVerification(subject, fact);
        if (!observed.boundTo(subject, fact)) {
            return nonConfirming(observed.observation());
        }
        CredentialVerificationOutcome match;
        try {
            String normalized = normalizeForVerification(submittedPassword);
            if (normalized == null) {
                return SourceBoundPasswordVerificationResult.REJECTED;
            }
            // forVerification has completed its short transaction: no DB lock spans Argon2.
            match = passwords.verify(normalized, observed.verifier());
        } catch (RuntimeException rejectedOrUnavailableVerification) {
            return SourceBoundPasswordVerificationResult.REJECTED;
        }
        if (match == CredentialVerificationOutcome.NO_MATCH) {
            return SourceBoundPasswordVerificationResult.NO_MATCH;
        }
        if (match != CredentialVerificationOutcome.MATCH) {
            return SourceBoundPasswordVerificationResult.REJECTED;
        }
        return switch (source.revalidate(observed)) {
            case CONFIRMED_APPLICABLE -> SourceBoundPasswordVerificationResult.VERIFIED;
            case NON_CURRENT -> SourceBoundPasswordVerificationResult.SOURCE_NON_CURRENT;
            case CONFLICT -> SourceBoundPasswordVerificationResult.SOURCE_CONFLICT;
            case UNCERTAIN -> SourceBoundPasswordVerificationResult.SOURCE_UNCERTAIN;
        };
    }

    private SourceBoundPasswordVerificationResult nonConfirming(
            Observation observation) {
        return switch (observation) {
            case SUPERSEDED -> SourceBoundPasswordVerificationResult.SOURCE_NON_CURRENT;
            case INCOMPATIBLE -> SourceBoundPasswordVerificationResult.SOURCE_CONFLICT;
            case INCOMPLETE, BOUND_APPLICABLE -> SourceBoundPasswordVerificationResult.SOURCE_UNCERTAIN;
        };
    }

    private String normalizeForVerification(CharSequence submittedPassword) {
        if (submittedPassword == null) {
            return null;
        }
        String value = submittedPassword.toString();
        for (int index = 0; index < value.length(); index++) {
            char character = value.charAt(index);
            if (Character.isHighSurrogate(character)) {
                if (index + 1 >= value.length() || !Character.isLowSurrogate(value.charAt(index + 1))) {
                    return null;
                }
                index++;
            } else if (Character.isLowSurrogate(character)) {
                return null;
            }
        }
        // Preserve DEC-0007 representation, not prospective length/blocklist acceptance.
        return Normalizer.normalize(value, Normalizer.Form.NFC);
    }
}
