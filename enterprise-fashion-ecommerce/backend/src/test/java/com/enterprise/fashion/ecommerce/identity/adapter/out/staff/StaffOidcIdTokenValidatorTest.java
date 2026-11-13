package com.enterprise.fashion.ecommerce.identity.adapter.out.staff;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.enterprise.fashion.ecommerce.identity.application.staff.StaffOidcAuthenticationVerification;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.proc.JWSVerifierFactory;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.PlainJWT;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class StaffOidcIdTokenValidatorTest {
    private static final Instant NOW = Instant.parse("2026-11-13T12:00:00Z");
    private static final String ISSUER = "https://identity.invalid/staff";
    private static final String CLIENT = "staff-client";
    private static final String KEY_ID = "staff-key-1";
    private static final String NONCE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String SUBJECT = "stable-subject";
    private static final String SECRET_MARKER = "secret-token-marker";
    private static KeyPair rsa;
    private static KeyPair ec;

    @BeforeAll static void keys() throws Exception {
        KeyPairGenerator rsaGenerator = KeyPairGenerator.getInstance("RSA");
        rsaGenerator.initialize(2048);
        rsa = rsaGenerator.generateKeyPair();
        KeyPairGenerator ecGenerator = KeyPairGenerator.getInstance("EC");
        ecGenerator.initialize(256);
        ec = ecGenerator.generateKeyPair();
    }

    @Test void validatesSignedRsaTokenIntoImmutableInternalValue() throws Exception {
        var result = validator().validate(token(claims(), JWSAlgorithm.RS256, KEY_ID, rsa), NONCE);

        assertThat(result.accepted()).isTrue();
        assertThat(result.failure()).isNull();
        assertThat(result.validatedToken().issuer()).isEqualTo(ISSUER);
        assertThat(result.validatedToken().subject()).isEqualTo(SUBJECT);
        assertThat(result.validatedToken().audience()).containsExactly(CLIENT);
        assertThatThrownBy(() -> result.validatedToken().audience().add("other"))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThat(result.toString()).doesNotContain(ISSUER, SUBJECT, NONCE);
    }

    @Test void validatesExplicitlyConfiguredEcToken() throws Exception {
        var validator = new StaffOidcIdTokenValidator(trust(JWSAlgorithm.ES256, ec));
        assertThat(validator.validate(token(claims(), JWSAlgorithm.ES256, KEY_ID, ec), NONCE).accepted())
                .isTrue();
    }

    @Test void rejectsTamperedUnsignedMalformedAndOversizedTokens() throws Exception {
        String signed = token(claims(), JWSAlgorithm.RS256, KEY_ID, rsa);
        int signatureStart = signed.lastIndexOf('.') + 1;
        char replacement = signed.charAt(signatureStart) == 'A' ? 'B' : 'A';
        String tampered = signed.substring(0, signatureStart) + replacement
                + signed.substring(signatureStart + 1);
        assertFailure(tampered, NONCE, StaffOidcAuthenticationVerification.Outcome.INVALID_TOKEN);
        assertFailure(new PlainJWT(claims().build()).serialize(), NONCE,
                StaffOidcAuthenticationVerification.Outcome.INVALID_TOKEN);
        assertFailure("not-a-token", NONCE, StaffOidcAuthenticationVerification.Outcome.INVALID_TOKEN);
        assertFailure("x".repeat(4097), NONCE, StaffOidcAuthenticationVerification.Outcome.INVALID_TOKEN);
    }

    @Test void rejectsDisallowedAlgorithmsAndAlgorithmKeyMismatch() throws Exception {
        assertFailure(token(claims(), JWSAlgorithm.ES256, KEY_ID, ec), NONCE,
                StaffOidcAuthenticationVerification.Outcome.INVALID_TOKEN);
        assertThatThrownBy(() -> new StaffOidcTokenTrust.VerificationKey(
                KEY_ID, JWSAlgorithm.ES256, rsa.getPublic()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Verification key is incompatible with its algorithm");
    }

    @Test void rejectsMissingUnknownAndAmbiguousKeySelection() throws Exception {
        assertFailure(token(claims(), JWSAlgorithm.RS256, null, rsa), NONCE,
                StaffOidcAuthenticationVerification.Outcome.VERIFICATION_UNAVAILABLE);
        assertFailure(token(claims(), JWSAlgorithm.RS256, "unknown", rsa), NONCE,
                StaffOidcAuthenticationVerification.Outcome.VERIFICATION_UNAVAILABLE);
        var key = new StaffOidcTokenTrust.VerificationKey(KEY_ID, JWSAlgorithm.RS256, rsa.getPublic());
        assertThatThrownBy(() -> trust(List.of(key, key)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Verification key selection is ambiguous");
        assertThatThrownBy(() -> trust(List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Verification keys are required");
    }

    @Test void validatesExactIssuerAndAudienceIncludingAuthorizedParty() throws Exception {
        assertClaimsFailure(claims().issuer(null), StaffOidcAuthenticationVerification.Outcome.ISSUER_MISMATCH);
        assertClaimsFailure(claims().issuer("https://wrong.invalid"),
                StaffOidcAuthenticationVerification.Outcome.ISSUER_MISMATCH);
        assertClaimsFailure(claims().audience(List.of()),
                StaffOidcAuthenticationVerification.Outcome.AUDIENCE_MISMATCH);
        assertClaimsFailure(claims().audience("other"),
                StaffOidcAuthenticationVerification.Outcome.AUDIENCE_MISMATCH);
        assertClaimsFailure(claims().audience(List.of(CLIENT, "api")),
                StaffOidcAuthenticationVerification.Outcome.AUDIENCE_MISMATCH);
        assertClaimsFailure(claims().audience(List.of(CLIENT, "api")).claim("azp", "other"),
                StaffOidcAuthenticationVerification.Outcome.AUDIENCE_MISMATCH);
        assertThat(validate(claims().audience(List.of(CLIENT, "api")).claim("azp", CLIENT)).accepted())
                .isTrue();
    }

    @Test void validatesNonceExactly() throws Exception {
        assertClaimsFailure(claims().claim("nonce", null),
                StaffOidcAuthenticationVerification.Outcome.NONCE_MISMATCH);
        assertClaimsFailure(claims().claim("nonce", "wrong"),
                StaffOidcAuthenticationVerification.Outcome.NONCE_MISMATCH);
        assertFailure(token(claims(), JWSAlgorithm.RS256, KEY_ID, rsa), "wrong",
                StaffOidcAuthenticationVerification.Outcome.NONCE_MISMATCH);
    }

    @Test void validatesExpirationNotBeforeIssuedAtAndClockSkewBoundaries() throws Exception {
        assertClaimsFailure(claims().expirationTime(null),
                StaffOidcAuthenticationVerification.Outcome.INVALID_TOKEN);
        assertClaimsFailure(claims().expirationTime(Date.from(NOW.minusSeconds(31))),
                StaffOidcAuthenticationVerification.Outcome.INVALID_TOKEN);
        assertThat(validate(claims().issueTime(Date.from(NOW.minusSeconds(60)))
                .notBeforeTime(Date.from(NOW.minusSeconds(60)))
                .claim("auth_time", Date.from(NOW.minusSeconds(60)))
                .expirationTime(Date.from(NOW.minusSeconds(30)))).accepted()).isTrue();
        assertClaimsFailure(claims().notBeforeTime(Date.from(NOW.plusSeconds(31))),
                StaffOidcAuthenticationVerification.Outcome.INVALID_TOKEN);
        assertThat(validate(claims().notBeforeTime(Date.from(NOW.plusSeconds(30)))).accepted()).isTrue();
        assertClaimsFailure(claims().issueTime(Date.from(NOW.plusSeconds(31))),
                StaffOidcAuthenticationVerification.Outcome.INVALID_TOKEN);
        assertThat(validate(claims().issueTime(Date.from(NOW.plusSeconds(30)))).accepted()).isTrue();
    }

    @Test void rejectsInvalidTemporalChronologyIndependentlyOfClockSkew() throws Exception {
        Instant issued = NOW.minusSeconds(10);
        assertClaimsFailure(claims().issueTime(Date.from(issued)).expirationTime(Date.from(issued)),
                StaffOidcAuthenticationVerification.Outcome.INVALID_TOKEN);
        assertClaimsFailure(claims().issueTime(Date.from(issued))
                        .expirationTime(Date.from(issued.minusSeconds(1))),
                StaffOidcAuthenticationVerification.Outcome.INVALID_TOKEN);
        assertClaimsFailure(claims().notBeforeTime(Date.from(NOW.plusSeconds(20)))
                        .expirationTime(Date.from(NOW.plusSeconds(10))),
                StaffOidcAuthenticationVerification.Outcome.INVALID_TOKEN);
        assertClaimsFailure(claims().issueTime(Date.from(issued))
                        .claim("auth_time", Date.from(issued.plusSeconds(1))),
                StaffOidcAuthenticationVerification.Outcome.STALE_AUTHENTICATION);

        assertThat(validate(claims().issueTime(Date.from(issued))
                .expirationTime(Date.from(issued.plusSeconds(1)))
                .notBeforeTime(Date.from(issued.plusSeconds(1)))
                .claim("auth_time", Date.from(issued))).accepted()).isTrue();
    }

    @Test void validatesAuthenticationTimeAndAgeBoundaries() throws Exception {
        assertClaimsFailure(claims().claim("auth_time", null),
                StaffOidcAuthenticationVerification.Outcome.STALE_AUTHENTICATION);
        assertClaimsFailure(claims().claim("auth_time", Date.from(NOW.plusSeconds(31))),
                StaffOidcAuthenticationVerification.Outcome.STALE_AUTHENTICATION);
        assertThat(validate(claims().issueTime(Date.from(NOW.plusSeconds(30)))
                .claim("auth_time", Date.from(NOW.plusSeconds(30)))).accepted()).isTrue();
        assertClaimsFailure(claims().claim("auth_time", Date.from(NOW.minusSeconds(331))),
                StaffOidcAuthenticationVerification.Outcome.STALE_AUTHENTICATION);
        assertThat(validate(claims().claim("auth_time", Date.from(NOW.minusSeconds(330)))).accepted()).isTrue();
    }

    @Test void requiresExplicitAssurance() throws Exception {
        assertThat(validate(claims().claim("acr", "required")).accepted()).isTrue();
        assertClaimsFailure(claims().claim("acr", null),
                StaffOidcAuthenticationVerification.Outcome.INSUFFICIENT_ASSURANCE);
        assertClaimsFailure(claims().claim("acr", " "),
                StaffOidcAuthenticationVerification.Outcome.INSUFFICIENT_ASSURANCE);
        assertClaimsFailure(claims().claim("acr", "lower"),
                StaffOidcAuthenticationVerification.Outcome.INSUFFICIENT_ASSURANCE);
        assertClaimsFailure(claims().claim("acr", List.of("lower", "required")),
                StaffOidcAuthenticationVerification.Outcome.INSUFFICIENT_ASSURANCE);
        assertClaimsFailure(claims().claim("acr", Map.of("value", "required")),
                StaffOidcAuthenticationVerification.Outcome.INSUFFICIENT_ASSURANCE);
        assertClaimsFailure(claims().claim("acr", 1),
                StaffOidcAuthenticationVerification.Outcome.INSUFFICIENT_ASSURANCE);
        assertClaimsFailure(claims().claim("acr", true),
                StaffOidcAuthenticationVerification.Outcome.INSUFFICIENT_ASSURANCE);
    }

    @Test void rejectsUnsupportedCriticalJoseHeaders() throws Exception {
        var header = new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(KEY_ID)
                .criticalParams(Set.of("unsupported")).customParam("unsupported", "value").build();
        var jwt = new SignedJWT(header, claims().build());
        jwt.sign(new RSASSASigner((RSAPrivateKey) rsa.getPrivate()));

        assertFailure(jwt.serialize(), NONCE, StaffOidcAuthenticationVerification.Outcome.INVALID_TOKEN);
    }

    @Test void rejectsMissingOrInvalidSubject() throws Exception {
        assertClaimsFailure(claims().subject(null),
                StaffOidcAuthenticationVerification.Outcome.UNTRUSTED_SUBJECT);
        assertClaimsFailure(claims().subject(" "),
                StaffOidcAuthenticationVerification.Outcome.UNTRUSTED_SUBJECT);
        assertClaimsFailure(claims().subject("x".repeat(256)),
                StaffOidcAuthenticationVerification.Outcome.UNTRUSTED_SUBJECT);
        assertClaimsFailure(claims().subject("subject\nvalue"),
                StaffOidcAuthenticationVerification.Outcome.UNTRUSTED_SUBJECT);
    }

    @Test void internalVerifierFailureIsIndeterminateAndSensitiveValuesStayRedacted() throws Exception {
        JWSVerifierFactory factory = mock(JWSVerifierFactory.class);
        when(factory.createJWSVerifier(any(), any())).thenThrow(new IllegalStateException(SECRET_MARKER));
        String serialized = token(claims().subject(SECRET_MARKER), JWSAlgorithm.RS256, KEY_ID, rsa);
        var result = new StaffOidcIdTokenValidator(trust(), factory).validate(serialized, NONCE);

        assertThat(result.failure()).isEqualTo(StaffOidcAuthenticationVerification.Outcome.INDETERMINATE);
        assertThat(result.toString()).doesNotContain(serialized, SECRET_MARKER, NONCE);
        assertThat(trust().toString()).doesNotContain(ISSUER, CLIENT, KEY_ID);
        assertThat(trust().verificationKeys().getFirst().toString()).doesNotContain(KEY_ID);
    }

    @Test void validationDoesNotMutateSecurityContextOrCreateAuthorities() throws Exception {
        var original = new TestingAuthenticationToken("customer", "redacted", "CUSTOMER");
        SecurityContextHolder.getContext().setAuthentication(original);
        try {
            assertThat(validate(claims()).accepted()).isTrue();
            assertThat(SecurityContextHolder.getContext().getAuthentication()).isSameAs(original);
            assertThat(original.getAuthorities()).extracting(Object::toString).containsExactly("CUSTOMER");
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    private static StaffOidcTokenTrust trust() { return trust(JWSAlgorithm.RS256, rsa); }

    private static StaffOidcTokenTrust trust(JWSAlgorithm algorithm, KeyPair pair) {
        return trust(Set.of(algorithm),
                List.of(new StaffOidcTokenTrust.VerificationKey(KEY_ID, algorithm, pair.getPublic())));
    }

    private static StaffOidcTokenTrust trust(List<StaffOidcTokenTrust.VerificationKey> keys) {
        return trust(Set.of(JWSAlgorithm.RS256), keys);
    }

    private static StaffOidcTokenTrust trust(Set<JWSAlgorithm> algorithms,
            List<StaffOidcTokenTrust.VerificationKey> keys) {
        return new StaffOidcTokenTrust(ISSUER, CLIENT, algorithms, keys, 4096,
                Duration.ofSeconds(30), Duration.ofMinutes(5),
                List.of(new StaffOidcTokenTrust.AssuranceRequirement("acr", Set.of("required"))),
                Clock.fixed(NOW, ZoneOffset.UTC));
    }

    private static JWTClaimsSet.Builder claims() {
        return new JWTClaimsSet.Builder().issuer(ISSUER).subject(SUBJECT).audience(CLIENT)
                .issueTime(Date.from(NOW.minusSeconds(10))).expirationTime(Date.from(NOW.plusSeconds(60)))
                .notBeforeTime(Date.from(NOW.minusSeconds(10))).claim("nonce", NONCE)
                .claim("auth_time", Date.from(NOW.minusSeconds(60))).claim("acr", "required");
    }

    private static StaffOidcIdTokenValidator.Result validate(JWTClaimsSet.Builder claims) throws Exception {
        return validator().validate(token(claims, JWSAlgorithm.RS256, KEY_ID, rsa), NONCE);
    }

    private static void assertClaimsFailure(JWTClaimsSet.Builder claims,
            StaffOidcAuthenticationVerification.Outcome outcome) throws Exception {
        assertThat(validate(claims).failure()).isEqualTo(outcome);
    }

    private static void assertFailure(String token, String nonce,
            StaffOidcAuthenticationVerification.Outcome outcome) {
        assertThat(validator().validate(token, nonce).failure()).isEqualTo(outcome);
    }

    private static StaffOidcIdTokenValidator validator() { return new StaffOidcIdTokenValidator(trust()); }

    private static String token(JWTClaimsSet.Builder claims, JWSAlgorithm algorithm, String keyId,
            KeyPair keyPair) throws Exception {
        var header = new JWSHeader.Builder(algorithm).keyID(keyId).build();
        var jwt = new SignedJWT(header, claims.build());
        if (algorithm.getName().startsWith("ES")) {
            jwt.sign(new ECDSASigner((ECPrivateKey) keyPair.getPrivate()));
        } else {
            jwt.sign(new RSASSASigner((RSAPrivateKey) keyPair.getPrivate()));
        }
        return jwt.serialize();
    }
}
