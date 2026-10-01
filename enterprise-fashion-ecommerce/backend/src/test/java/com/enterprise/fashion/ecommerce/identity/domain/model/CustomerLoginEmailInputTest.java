package com.enterprise.fashion.ecommerce.identity.domain.model;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CustomerLoginEmailInputTest {

    @ParameterizedTest(name = "rejects invalid preparation input {index}")
    @NullAndEmptySource
    @ValueSource(strings = {
            " ", "\t", " \t \t", "person", "@example.invalid", "person@", "person@@example.invalid",
            "person@example.invalid@other.invalid", ".person@example.invalid", "person.@example.invalid",
            "per..son@example.invalid", "\"person\"@example.invalid", "pérson@example.invalid",
            "per(son@example.invalid", "per,son@example.invalid", "per:son@example.invalid",
            "per;son@example.invalid", "per<son@example.invalid", "per>son@example.invalid",
            "per[son@example.invalid", "per]son@example.invalid", "per\\son@example.invalid",
            "Person <person@example.invalid>", "person@example.invalid,other@other.invalid",
            "person@example.invalid,other", "person@example.invalid;other", "person@example.invalid(comment)",
            "person@[127.0.0.1]", "person@[IPv6:::1]", "person@\"example\".invalid",
            "person@.example.invalid", "person@example..invalid", "person@example.invalid.",
            "person@.", "person@example\u3002invalid", "person@example\uff0einvalid",
            "person@example\uff61invalid", "per son@example.invalid", "person@exam ple.invalid",
            "person@exam\tple.invalid", "person@exam\u00a0ple.invalid", "person@exam\u2003ple.invalid",
            "\nperson@example.invalid", "person@example.invalid\r", "person@exam\u0000ple.invalid",
            "person@exam\u007fple.invalid", "person@exam\u0085ple.invalid",
            "\u000bperson@example.invalid", "person@example.invalid\u000c",
            "\u00a0person@example.invalid", "person@example.invalid\u2003",
            "person@exam\ud800ple.invalid", "person@exam\udc00ple.invalid", "person@example.invalid\ud800"
    })
    void rejectsInputWithoutDisclosingIt(String input) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> CustomerLoginEmailInput.prepareForDomainValidation(input))
                .withMessage("Customer login email input cannot be prepared")
                .withNoCause();
    }

    @ParameterizedTest(name = "trims only governed surrounding characters {index}")
    @ValueSource(strings = {
            "Person@EXAMPLE.invalid", " Person@EXAMPLE.invalid ",
            "\tPerson@EXAMPLE.invalid\t", " \t Person@EXAMPLE.invalid\t \t"
    })
    void removesOnlySurroundingAsciiSpaceAndTab(String input) {
        CustomerLoginEmailInput prepared = CustomerLoginEmailInput.prepareForDomainValidation(input);

        assertThat(prepared.asciiLowercaseLocalPart()).isEqualTo("person");
        assertThat(prepared.unvalidatedDomainLabels()).containsExactly("EXAMPLE", "invalid");
    }

    @Test
    void preservesEveryGovernedAtextCharacterDotsAndPlusTags() {
        CustomerLoginEmailInput prepared = CustomerLoginEmailInput.prepareForDomainValidation(
                "AZaz09.!#$%&'*+-/=?^_`{|}~.Name+Tag@example.invalid");

        assertThat(prepared.asciiLowercaseLocalPart())
                .isEqualTo("azaz09.!#$%&'*+-/=?^_`{|}~.name+tag");
    }

    @Test
    void preservesUnicodeLabelsWithoutNormalizationOrCaseMapping() {
        CustomerLoginEmailInput prepared = CustomerLoginEmailInput.prepareForDomainValidation(
                "Person@BU\u0308CHER.\u0130.\ud83d\ude00");

        assertThat(prepared.unvalidatedDomainLabels()).containsExactly("BU\u0308CHER", "\u0130", "\ud83d\ude00");
    }

    @Test
    void preservesApparentAndMalformedAlabelsAsUnvalidatedCandidates() {
        CustomerLoginEmailInput prepared = CustomerLoginEmailInput.prepareForDomainValidation(
                "person@XN--BCHER-KVA.xn--");

        assertThat(prepared.unvalidatedDomainLabels()).containsExactly("XN--BCHER-KVA", "xn--");
        assertThat(prepared.toString()).contains("awaiting strict domain validation");
    }

    @Test
    void leavesDomainValidityForTheFutureBoundaryEvenForAsciiLabels() {
        CustomerLoginEmailInput prepared = CustomerLoginEmailInput.prepareForDomainValidation("person@-_");

        assertThat(prepared.unvalidatedDomainLabels()).containsExactly("-_");
    }

    @Test
    void exposesImmutableLabelsAndRedactedDiagnostics() {
        CustomerLoginEmailInput prepared = CustomerLoginEmailInput.prepareForDomainValidation(
                "Sensitive.Person+Tag@Private.Example");

        assertThatThrownBy(() -> prepared.unvalidatedDomainLabels().set(0, "replacement"))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThat(prepared.toString())
                .isEqualTo("CustomerLoginEmailInput[awaiting strict domain validation; redacted]");
    }

    @Test
    @ResourceLock("java.util.Locale.default")
    void asciiLowercaseMappingIsIndependentOfTurkishDefaultLocale() {
        Locale original = Locale.getDefault();
        try {
            Locale.setDefault(Locale.forLanguageTag("tr-TR"));
            CustomerLoginEmailInput prepared = CustomerLoginEmailInput.prepareForDomainValidation(
                    "IDENTITY.I+TAG@IDENTITY.Example");

            assertThat(prepared.asciiLowercaseLocalPart()).isEqualTo("identity.i+tag");
            assertThat(prepared.unvalidatedDomainLabels()).containsExactly("IDENTITY", "Example");
        } finally {
            Locale.setDefault(original);
        }
    }
}
