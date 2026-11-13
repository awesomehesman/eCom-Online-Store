package com.enterprise.fashion.ecommerce.identity.adapter.out.staff;

import java.security.PublicKey;
import java.time.Clock;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import com.nimbusds.jose.JWSAlgorithm;

/** Dormant, explicitly supplied trust material. It performs no discovery or key retrieval. */
final class StaffOidcTokenTrust {
    private static final Pattern CLAIM_NAME = Pattern.compile("[A-Za-z][A-Za-z0-9_.:-]{0,127}");

    record VerificationKey(String keyId, JWSAlgorithm algorithm, PublicKey key) {
        VerificationKey {
            keyId = bounded(keyId, 128, "key identifier");
            algorithm = requireAsymmetric(algorithm);
            Objects.requireNonNull(key, "Verification key is required");
            if (!compatible(algorithm, key)) {
                throw new IllegalArgumentException("Verification key is incompatible with its algorithm");
            }
        }

        @Override public String toString() { return "VerificationKey[redacted]"; }
    }

    record AssuranceRequirement(String claimName, Set<String> acceptedValues) {
        AssuranceRequirement {
            if (claimName == null || !CLAIM_NAME.matcher(claimName).matches()) {
                throw new IllegalArgumentException("Invalid assurance claim name");
            }
            acceptedValues = Set.copyOf(Objects.requireNonNull(acceptedValues,
                    "Accepted assurance values are required"));
            if (acceptedValues.isEmpty()) {
                throw new IllegalArgumentException("Accepted assurance values are required");
            }
            acceptedValues.forEach(value -> bounded(value, 256, "assurance value"));
        }

        @Override public String toString() { return "AssuranceRequirement[redacted]"; }
    }

    private final String issuer;
    private final String clientId;
    private final Set<JWSAlgorithm> allowedAlgorithms;
    private final List<VerificationKey> verificationKeys;
    private final int maximumTokenLength;
    private final Duration clockSkew;
    private final Duration maximumAuthenticationAge;
    private final List<AssuranceRequirement> assuranceRequirements;
    private final Clock clock;

    StaffOidcTokenTrust(String issuer, String clientId, Set<JWSAlgorithm> allowedAlgorithms,
            List<VerificationKey> verificationKeys, int maximumTokenLength, Duration clockSkew,
            Duration maximumAuthenticationAge, List<AssuranceRequirement> assuranceRequirements,
            Clock clock) {
        this.issuer = bounded(issuer, 2048, "issuer");
        this.clientId = bounded(clientId, 256, "client identifier");
        this.allowedAlgorithms = Set.copyOf(Objects.requireNonNull(allowedAlgorithms,
                "Allowed algorithms are required"));
        if (this.allowedAlgorithms.isEmpty()) {
            throw new IllegalArgumentException("Allowed algorithms are required");
        }
        this.allowedAlgorithms.forEach(StaffOidcTokenTrust::requireAsymmetric);
        this.verificationKeys = List.copyOf(Objects.requireNonNull(verificationKeys,
                "Verification keys are required"));
        if (this.verificationKeys.isEmpty()) {
            throw new IllegalArgumentException("Verification keys are required");
        }
        if (this.verificationKeys.stream().anyMatch(key -> !this.allowedAlgorithms.contains(key.algorithm()))) {
            throw new IllegalArgumentException("Every verification key algorithm must be allowed");
        }
        long distinctSelectors = this.verificationKeys.stream()
                .map(key -> key.keyId() + "\u0000" + key.algorithm().getName()).distinct().count();
        if (distinctSelectors != this.verificationKeys.size()) {
            throw new IllegalArgumentException("Verification key selection is ambiguous");
        }
        if (maximumTokenLength < 256) {
            throw new IllegalArgumentException("Maximum token length is invalid");
        }
        this.maximumTokenLength = maximumTokenLength;
        this.clockSkew = nonNegative(clockSkew, "Clock skew");
        this.maximumAuthenticationAge = positive(maximumAuthenticationAge,
                "Maximum authentication age");
        this.assuranceRequirements = List.copyOf(Objects.requireNonNull(assuranceRequirements,
                "Assurance requirements are required"));
        if (this.assuranceRequirements.isEmpty()) {
            throw new IllegalArgumentException("Assurance requirements are required");
        }
        long distinctClaims = this.assuranceRequirements.stream()
                .map(AssuranceRequirement::claimName).distinct().count();
        if (distinctClaims != this.assuranceRequirements.size()) {
            throw new IllegalArgumentException("Assurance requirements are ambiguous");
        }
        this.clock = Objects.requireNonNull(clock, "Clock is required");
    }

    String issuer() { return issuer; }
    String clientId() { return clientId; }
    Set<JWSAlgorithm> allowedAlgorithms() { return allowedAlgorithms; }
    List<VerificationKey> verificationKeys() { return verificationKeys; }
    int maximumTokenLength() { return maximumTokenLength; }
    Duration clockSkew() { return clockSkew; }
    Duration maximumAuthenticationAge() { return maximumAuthenticationAge; }
    List<AssuranceRequirement> assuranceRequirements() { return assuranceRequirements; }
    Clock clock() { return clock; }

    @Override public String toString() { return "StaffOidcTokenTrust[redacted]"; }

    private static JWSAlgorithm requireAsymmetric(JWSAlgorithm algorithm) {
        Objects.requireNonNull(algorithm, "Signing algorithm is required");
        if (!JWSAlgorithm.Family.RSA.contains(algorithm) && !JWSAlgorithm.Family.EC.contains(algorithm)) {
            throw new IllegalArgumentException("Only explicitly configured asymmetric algorithms are supported");
        }
        return algorithm;
    }

    private static boolean compatible(JWSAlgorithm algorithm, PublicKey key) {
        return JWSAlgorithm.Family.RSA.contains(algorithm) && "RSA".equals(key.getAlgorithm())
                || JWSAlgorithm.Family.EC.contains(algorithm) && "EC".equals(key.getAlgorithm());
    }

    private static String bounded(String value, int maximum, String label) {
        if (value == null || value.isBlank() || value.length() > maximum
                || value.codePoints().anyMatch(Character::isISOControl)) {
            throw new IllegalArgumentException("Invalid " + label);
        }
        return value;
    }

    private static Duration nonNegative(Duration value, String label) {
        Objects.requireNonNull(value, label + " is required");
        if (value.isNegative()) throw new IllegalArgumentException(label + " is invalid");
        return value;
    }

    private static Duration positive(Duration value, String label) {
        Objects.requireNonNull(value, label + " is required");
        if (value.isZero() || value.isNegative()) throw new IllegalArgumentException(label + " is invalid");
        return value;
    }
}
