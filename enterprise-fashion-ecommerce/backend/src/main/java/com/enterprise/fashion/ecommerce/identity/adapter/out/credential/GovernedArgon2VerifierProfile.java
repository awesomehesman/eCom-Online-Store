package com.enterprise.fashion.ecommerce.identity.adapter.out.credential;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/** Shared structural DEC-0006 guard; acceptance here never establishes credential authority. */
public final class GovernedArgon2VerifierProfile {
    static final int SALT_LENGTH_BYTES = 16;
    static final int HASH_LENGTH_BYTES = 32;
    static final int PARALLELISM = 4;
    static final int MEMORY_KIB = 65_536;
    static final int ITERATIONS = 3;

    private static final int ARGON2_VERSION = 19;
    private static final String ALGORITHM = "argon2id";
    private static final int PHC_SECTION_COUNT = 6;
    private static final int MAX_VERIFIER_LENGTH = 128;
    private static final Base64.Decoder BASE64_DECODER = Base64.getDecoder();
    private static final Base64.Encoder CANONICAL_BASE64_ENCODER = Base64.getEncoder().withoutPadding();

    private GovernedArgon2VerifierProfile() { }

    public static boolean accepts(String representation) {
        if (representation == null
                || representation.isEmpty()
                || representation.length() > MAX_VERIFIER_LENGTH) {
            return false;
        }

        String[] sections = representation.split("\\$", -1);
        if (sections.length != PHC_SECTION_COUNT
                || !sections[0].isEmpty()
                || !ALGORITHM.equals(sections[1])
                || !hasExactVersion(sections[2])
                || !hasExactParameters(sections[3])) {
            return false;
        }

        return hasCanonicalBase64Length(sections[4], SALT_LENGTH_BYTES)
                && hasCanonicalBase64Length(sections[5], HASH_LENGTH_BYTES);
    }

    private static boolean hasExactVersion(String versionSection) {
        return parseUnsignedDecimal(versionSection, "v=") == ARGON2_VERSION;
    }

    private static boolean hasExactParameters(String parameterSection) {
        String[] parameters = parameterSection.split(",", -1);
        return parameters.length == 3
                && parseUnsignedDecimal(parameters[0], "m=") == MEMORY_KIB
                && parseUnsignedDecimal(parameters[1], "t=") == ITERATIONS
                && parseUnsignedDecimal(parameters[2], "p=") == PARALLELISM;
    }

    private static long parseUnsignedDecimal(String value, String prefix) {
        if (!value.startsWith(prefix) || value.length() == prefix.length()) {
            return -1;
        }

        long parsed = 0;
        for (int index = prefix.length(); index < value.length(); index++) {
            char character = value.charAt(index);
            if (character < '0' || character > '9') {
                return -1;
            }
            int digit = character - '0';
            if (parsed > (Long.MAX_VALUE - digit) / 10) {
                return -1;
            }
            parsed = parsed * 10 + digit;
        }
        return parsed;
    }

    private static boolean hasCanonicalBase64Length(String value, int requiredBytes) {
        if (value.isEmpty() || !StandardCharsets.US_ASCII.newEncoder().canEncode(value)) {
            return false;
        }

        try {
            byte[] decoded = BASE64_DECODER.decode(value);
            return decoded.length == requiredBytes
                    && CANONICAL_BASE64_ENCODER.encodeToString(decoded).equals(value);
        } catch (IllegalArgumentException malformedBase64) {
            return false;
        }
    }
}
