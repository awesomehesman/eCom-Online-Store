package com.enterprise.fashion.ecommerce.identity.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class PreparedProspectiveCustomerPasswordTest {

    @ParameterizedTest(name = "rejects absent or malformed input {index}")
    @NullAndEmptySource
    @ValueSource(strings = {
            "aaaaaaaaaaaaaaa\ud800", "\udc00aaaaaaaaaaaaaaa",
            "aaaaaaa\ud800xaaaaaaaa", "aaaaaaa\udc00\ud800aaaaaaaa",
            "aaaaaaa\ud800\ud800aaaaaaaa"
    })
    void rejectsAbsentOrMalformedInputSafely(String input) {
        assertRejected(input);
    }

    @ParameterizedTest
    @ValueSource(ints = {14, 65, 1024})
    void rejectsOutOfRangeInputWithoutTruncation(int length) {
        assertRejected("a".repeat(length));
    }

    @ParameterizedTest
    @ValueSource(ints = {15, 64})
    void preservesCompleteInputAtInclusiveBounds(int length) {
        String input = "a".repeat(length);
        assertThat(prepare(input).normalizedValue()).isEqualTo(input);
    }

    @ParameterizedTest
    @ValueSource(ints = {15, 64})
    void countsSupplementaryScalarsRatherThanUtf16Units(int length) {
        String input = "\ud83d\ude00".repeat(length);
        assertThat(prepare(input).normalizedValue()).isEqualTo(input);
    }

    @ParameterizedTest
    @ValueSource(ints = {14, 65})
    void appliesScalarBounds(int length) {
        assertRejected("\ud83d\ude00".repeat(length));
    }

    @Test
    void normalizesCanonicallyEquivalentCompleteInputsBeforeCounting() {
        String composed = "\u00e9".repeat(64);
        String decomposed = "e\u0301".repeat(64);
        assertThat(prepare(decomposed).normalizedValue()).isEqualTo(composed);
        assertThat(prepare(composed).normalizedValue()).isEqualTo(composed);
        assertThat(prepare("e\u0301".repeat(15)).normalizedValue()).isEqualTo("\u00e9".repeat(15));
        assertRejected("e\u0301".repeat(14));
        assertRejected("e\u0301".repeat(65));
    }

    @Test
    void preservesWhitespaceCasePunctuationAndCompatibilityCharacters() {
        String input = " \tAa  Bb!?#\uff21\ufb01\u00a0\nZ ";
        assertThat(prepare(input).normalizedValue()).isEqualTo(input);
        assertThat(prepare(" ".repeat(15)).normalizedValue()).isEqualTo(" ".repeat(15));
    }

    @Test
    void preservesOtherwisePermittedUnicodeWithoutInventingCharacterRules() {
        String input = "a".repeat(14) + "\u0000";
        assertThat(prepare(input).normalizedValue()).isEqualTo(input);
    }

    @Test
    void diagnosticsWithholdBothSubmittedAndPreparedSecretsAndRetainLimitedMeaning() {
        String input = "e\u0301".repeat(15);
        PreparedProspectiveCustomerPassword prepared = prepare(input);
        assertThat(prepared.toString())
                .isEqualTo("PreparedProspectiveCustomerPassword[REDACTED; awaiting remaining establishment policy checks]")
                .doesNotContain(input, prepared.normalizedValue());
    }

    private static PreparedProspectiveCustomerPassword prepare(String input) {
        return PreparedProspectiveCustomerPassword.prepareForRemainingPolicyChecks(input);
    }

    private static void assertRejected(String input) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> prepare(input))
                .withMessage("Prospective Customer password cannot be prepared")
                .withNoCause();
    }
}
