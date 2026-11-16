package com.enterprise.fashion.ecommerce.identity.adapter.out.staff;

import java.lang.reflect.Modifier;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import com.enterprise.fashion.ecommerce.identity.application.port.out.StaffOidcAuthenticationVerifier;
import com.enterprise.fashion.ecommerce.identity.application.staff.StaffOidcAuthenticationVerification;
import com.enterprise.fashion.ecommerce.identity.application.staff.StaffOidcAuthorizationTransaction;
import com.enterprise.fashion.ecommerce.identity.application.staff.VerifiedStaffAuthentication;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class StaffOidcRegistrationTokenVerifierTest {
    private static final Instant NOW = Instant.parse("2026-11-16T12:00:00Z");
    private static final String ID = "synthetic-provider";
    private static final String ISSUER = "https://identity.invalid/staff";
    private static final String CLIENT = "staff-client";
    private static final String KEY_ID = "staff-key-1";
    private static final String NONCE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String OTHER_NONCE = "CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC";
    private static final String SUBJECT = "stable-subject";
    private static final String CONTENT_TYPE = "application/json";
    private static KeyPair rsa;
    private static KeyPair otherRsa;
    private static KeyPair ec;

    @BeforeAll static void keys() throws Exception {
        KeyPairGenerator rsaGenerator = KeyPairGenerator.getInstance("RSA");
        rsaGenerator.initialize(2048);
        rsa = rsaGenerator.generateKeyPair();
        otherRsa = rsaGenerator.generateKeyPair();
        KeyPairGenerator ecGenerator = KeyPairGenerator.getInstance("EC");
        ecGenerator.initialize(256);
        ec = ecGenerator.generateKeyPair();
    }

    @Test void exactRegistrationRevisionBindsParserTrustAndTransactionNonce() throws Exception {
        var registration = registration(ID, 7, true, trust(ISSUER, CLIENT, rsa), 64 * 1024);
        var result = verifier(registration).verify(transaction(7, NONCE), 200, CONTENT_TYPE,
                response(token(claims(ISSUER, CLIENT, NONCE), JWSAlgorithm.RS256, rsa)));

        assertThat(result.accepted()).isTrue();
        assertThat(result.failure()).isNull();
        assertThat(result.validatedToken().issuer()).isEqualTo(ISSUER);
        assertThat(result.validatedToken().subject()).isEqualTo(SUBJECT);
        assertThat(result.validatedToken().audience()).containsExactly(CLIENT);
    }

    @Test void wrongOrMissingRegistrationRevisionNeverFallsForward() throws Exception {
        var revisionEight = registration(ID, 8, true, trust(ISSUER, CLIENT, rsa), 64 * 1024);
        assertFailure(verifier(revisionEight).verify(transaction(7, NONCE), 200, CONTENT_TYPE,
                response(token(claims(ISSUER, CLIENT, NONCE), JWSAlgorithm.RS256, rsa))), unavailable());
    }

    @Test void exactRevisionControlsSigningKeysIssuerAndClient() throws Exception {
        String original = token(claims(ISSUER, CLIENT, NONCE), JWSAlgorithm.RS256, rsa);
        assertFailure(verifier(registration(ID, 7, true, trust(ISSUER, CLIENT, otherRsa), 64 * 1024))
                .verify(transaction(7, NONCE), 200, CONTENT_TYPE, response(original)), invalidToken());

        String otherIssuer = "https://other.invalid/staff";
        assertFailure(verifier(registration(ID, 7, true,
                trust(otherIssuer, CLIENT, rsa), 64 * 1024)).verify(
                        transaction(7, NONCE), 200, CONTENT_TYPE, response(original)),
                StaffOidcAuthenticationVerification.Outcome.ISSUER_MISMATCH);

        String otherClient = "other-client";
        assertFailure(verifier(registration(ID, 7, true,
                trust(ISSUER, otherClient, rsa), 64 * 1024)).verify(
                        transaction(7, NONCE), 200, CONTENT_TYPE, response(original)),
                StaffOidcAuthenticationVerification.Outcome.AUDIENCE_MISMATCH);
    }

    @Test void transactionNonceCannotBeSubstituted() throws Exception {
        String idToken = token(claims(ISSUER, CLIENT, OTHER_NONCE), JWSAlgorithm.RS256, rsa);
        assertFailure(verifier(registration()).verify(transaction(7, NONCE), 200, CONTENT_TYPE,
                response(idToken)), StaffOidcAuthenticationVerification.Outcome.NONCE_MISMATCH);
    }

    @Test void missingDisabledAmbiguousAndUnavailableRegistrationsFailClosed() throws Exception {
        byte[] body = response(token(claims(ISSUER, CLIENT, NONCE), JWSAlgorithm.RS256, rsa));
        assertFailure(verifier().verify(transaction(7, NONCE), 200, CONTENT_TYPE, body), unavailable());
        assertFailure(verifier(registration(ID, 7, false, trust(), 64 * 1024))
                .verify(transaction(7, NONCE), 200, CONTENT_TYPE, body), unavailable());
        assertFailure(verifier(registration(), registration())
                .verify(transaction(7, NONCE), 200, CONTENT_TYPE, body), unavailable());

        StaffOidcProviderRegistrationResolver resolver = mock(StaffOidcProviderRegistrationResolver.class);
        when(resolver.resolve(ID, 7)).thenReturn(new StaffOidcProviderRegistrationResolver.Resolution(
                StaffOidcProviderRegistrationResolver.Outcome.UNAVAILABLE, null));
        assertFailure(new StaffOidcRegistrationTokenVerifier(resolver)
                .verify(transaction(7, NONCE), 200, CONTENT_TYPE, body), unavailable());
    }

    @Test void malformedResolverOutcomeIsIndeterminateWithoutTokenValidation() throws Exception {
        StaffOidcProviderRegistrationResolver resolver = mock(StaffOidcProviderRegistrationResolver.class);
        StaffOidcProviderRegistrationResolver.Resolution malformed =
                mock(StaffOidcProviderRegistrationResolver.Resolution.class);
        when(resolver.resolve(ID, 7)).thenReturn(malformed);
        when(malformed.outcome()).thenReturn(null);

        var result = new StaffOidcRegistrationTokenVerifier(resolver).verify(
                transaction(7, NONCE), 200, CONTENT_TYPE,
                response(token(claims(ISSUER, CLIENT, NONCE), JWSAlgorithm.RS256, rsa)));

        assertFailure(result, StaffOidcAuthenticationVerification.Outcome.INDETERMINATE);
        org.mockito.Mockito.verify(malformed, never()).registration();
    }

    @Test void resolvedRegistrationMustMatchTheTransactionsExactPinnedPair() throws Exception {
        byte[] malformedResponse = bytes("{");

        StaffOidcProviderRegistrationResolver wrongIdResolver = resolvedAs(
                registration("other-provider", 7, true, trust(), 64 * 1024));
        assertFailure(new StaffOidcRegistrationTokenVerifier(wrongIdResolver)
                .verify(transaction(7, NONCE), 200, CONTENT_TYPE, malformedResponse),
                StaffOidcAuthenticationVerification.Outcome.INDETERMINATE);

        StaffOidcProviderRegistrationResolver wrongRevisionResolver = resolvedAs(
                registration(ID, 8, true, trust(), 64 * 1024));
        assertFailure(new StaffOidcRegistrationTokenVerifier(wrongRevisionResolver)
                .verify(transaction(7, NONCE), 200, CONTENT_TYPE, malformedResponse),
                StaffOidcAuthenticationVerification.Outcome.INDETERMINATE);
    }

    @Test void invalidTransactionsAreRejectedBeforeRegistrationResolution() {
        StaffOidcProviderRegistrationResolver resolver = mock(StaffOidcProviderRegistrationResolver.class);
        var verifier = new StaffOidcRegistrationTokenVerifier(resolver);

        assertFailure(verifier.verify(null, 200, CONTENT_TYPE, new byte[0]),
                StaffOidcAuthenticationVerification.Outcome.INVALID_TRANSACTION);
        for (var status : StaffOidcAuthorizationTransaction.Status.values()) {
            if (status == StaffOidcAuthorizationTransaction.Status.CONSUMED) continue;
            assertFailure(verifier.verify(transaction(7, NONCE).withStatus(status), 200,
                    CONTENT_TYPE, new byte[0]), StaffOidcAuthenticationVerification.Outcome.INVALID_TRANSACTION);
        }
        verifyNoInteractions(resolver);
    }

    @Test void exactRegistrationResponseLimitAndStrictParserArePreserved() throws Exception {
        String idToken = token(claims(ISSUER, CLIENT, NONCE), JWSAlgorithm.RS256, rsa);
        byte[] valid = response(idToken);
        var tooSmall = verifier(registration(ID, 7, true, trust(), valid.length - 1));
        assertFailure(tooSmall.verify(transaction(7, NONCE), 200, CONTENT_TYPE, valid), invalidToken());

        var verifier = verifier(registration());
        assertFailure(verifier.verify(transaction(7, NONCE), 200, CONTENT_TYPE, bytes("{")), invalidToken());
        assertFailure(verifier.verify(transaction(7, NONCE), 200, CONTENT_TYPE,
                bytes("{\"access_token\":\"a\",\"token_type\":\"Bearer\",\"id_token\":\"one\",\"id_token\":\"two\"}")),
                invalidToken());
        assertFailure(verifier.verify(transaction(7, NONCE), 200, "text/plain", valid), invalidToken());
    }

    @Test void providerUnavailableStatusesOverrideResponseBodyValidation() {
        var verifier = verifier(registration());
        for (int status : new int[] {429, 500, 503, 599}) {
            assertFailure(verifier.verify(transaction(7, NONCE), status, "text/plain", null),
                    StaffOidcAuthenticationVerification.Outcome.PROVIDER_UNAVAILABLE);
        }
    }

    @Test void validatorFailureClassificationsArePreserved() throws Exception {
        var verifier = verifier(registration());
        assertFailure(verify(verifier, token(claims(ISSUER, CLIENT, NONCE), JWSAlgorithm.RS256, otherRsa)),
                invalidToken());
        assertFailure(verify(verifier, token(claims(ISSUER, CLIENT, NONCE), JWSAlgorithm.ES256, ec)),
                invalidToken());
        assertFailure(verify(verifier, token(claims("https://wrong.invalid", CLIENT, NONCE),
                JWSAlgorithm.RS256, rsa)), StaffOidcAuthenticationVerification.Outcome.ISSUER_MISMATCH);
        assertFailure(verify(verifier, token(claims(ISSUER, "wrong-client", NONCE),
                JWSAlgorithm.RS256, rsa)), StaffOidcAuthenticationVerification.Outcome.AUDIENCE_MISMATCH);
        assertFailure(verify(verifier, token(claims(ISSUER, CLIENT, OTHER_NONCE),
                JWSAlgorithm.RS256, rsa)), StaffOidcAuthenticationVerification.Outcome.NONCE_MISMATCH);
        assertFailure(verify(verifier, token(claims(ISSUER, CLIENT, NONCE)
                .expirationTime(Date.from(NOW.minusSeconds(60))), JWSAlgorithm.RS256, rsa)), invalidToken());
        assertFailure(verify(verifier, token(claims(ISSUER, CLIENT, NONCE).claim("acr", "lower"),
                JWSAlgorithm.RS256, rsa)), StaffOidcAuthenticationVerification.Outcome.INSUFFICIENT_ASSURANCE);
        assertFailure(verify(verifier, token(claims(ISSUER, CLIENT, NONCE)
                .claim("auth_time", Date.from(NOW.minusSeconds(331))), JWSAlgorithm.RS256, rsa)),
                StaffOidcAuthenticationVerification.Outcome.STALE_AUTHENTICATION);
        assertFailure(verify(verifier, token(claims(ISSUER, CLIENT, NONCE).subject(" "),
                JWSAlgorithm.RS256, rsa)), StaffOidcAuthenticationVerification.Outcome.UNTRUSTED_SUBJECT);
    }

    @Test void unexpectedFailuresAreIndeterminateAndDiagnosticsStayRedacted() throws Exception {
        StaffOidcProviderRegistrationResolver resolver = mock(StaffOidcProviderRegistrationResolver.class);
        when(resolver.resolve(ID, 7)).thenThrow(new IllegalStateException("private-provider-detail"));
        var failed = new StaffOidcRegistrationTokenVerifier(resolver)
                .verify(transaction(7, NONCE), 200, CONTENT_TYPE, bytes("private-token"));
        assertFailure(failed, StaffOidcAuthenticationVerification.Outcome.INDETERMINATE);
        assertThat(failed.toString()).doesNotContain("private", NONCE, ISSUER, CLIENT);
        assertThat(new StaffOidcRegistrationTokenVerifier(resolver).toString())
                .doesNotContain(ID, ISSUER, CLIENT, NONCE);

        var success = verify(verifier(registration()), token(claims(ISSUER, CLIENT, NONCE),
                JWSAlgorithm.RS256, rsa));
        assertThat(success.toString()).doesNotContain(ISSUER, CLIENT, SUBJECT, NONCE, "eyJ");
    }

    @Test void componentIsDormantPackagePrivateAndHasNoAuthenticationOrSessionSideEffects() throws Exception {
        assertThat(Modifier.isFinal(StaffOidcRegistrationTokenVerifier.class.getModifiers())).isTrue();
        assertThat(Modifier.isPublic(StaffOidcRegistrationTokenVerifier.class.getModifiers())).isFalse();
        assertThat(StaffOidcAuthenticationVerifier.class.isAssignableFrom(
                StaffOidcRegistrationTokenVerifier.class)).isFalse();
        assertThat(StaffOidcRegistrationTokenVerifier.class.getDeclaredMethods())
                .noneMatch(method -> method.getReturnType().equals(VerifiedStaffAuthentication.class));

        var original = new TestingAuthenticationToken("customer", "redacted", "CUSTOMER");
        SecurityContextHolder.getContext().setAuthentication(original);
        try {
            var result = verify(verifier(registration()), token(claims(ISSUER, CLIENT, NONCE),
                    JWSAlgorithm.RS256, rsa));
            assertThat(result.accepted()).isTrue();
            assertThat(SecurityContextHolder.getContext().getAuthentication()).isSameAs(original);
            assertThat(original.getAuthorities()).extracting(Object::toString).containsExactly("CUSTOMER");
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    private static StaffOidcRegistrationTokenVerifier verifier(StaffOidcProviderRegistration... registrations) {
        return new StaffOidcRegistrationTokenVerifier(
                new StaffOidcProviderRegistrationResolver(List.of(registrations)));
    }

    private static StaffOidcProviderRegistrationResolver resolvedAs(
            StaffOidcProviderRegistration registration) {
        StaffOidcProviderRegistrationResolver resolver = mock(StaffOidcProviderRegistrationResolver.class);
        when(resolver.resolve(ID, 7)).thenReturn(new StaffOidcProviderRegistrationResolver.Resolution(
                StaffOidcProviderRegistrationResolver.Outcome.RESOLVED, registration));
        return resolver;
    }

    private static StaffOidcRegistrationTokenVerifier.Result verify(
            StaffOidcRegistrationTokenVerifier verifier, String idToken) {
        return verifier.verify(transaction(7, NONCE), 200, CONTENT_TYPE, response(idToken));
    }

    private static StaffOidcProviderRegistration registration() {
        return registration(ID, 7, true, trust(), 64 * 1024);
    }

    private static StaffOidcProviderRegistration registration(String id, long revision, boolean enabled,
            StaffOidcTokenTrust trust, int maximumResponseSize) {
        return new StaffOidcProviderRegistration(id, revision, URI.create(trust.issuer()), trust.clientId(),
                URI.create("https://store.invalid/login/staff/callback"),
                URI.create("https://identity.invalid/token"),
                StaffOidcProviderRegistration.ClientAuthenticationMethod.CLIENT_SECRET_BASIC,
                trust, Duration.ofSeconds(5), Duration.ofSeconds(10), maximumResponseSize, enabled,
                "synthetic-owner-reference");
    }

    private static StaffOidcTokenTrust trust() { return trust(ISSUER, CLIENT, rsa); }

    private static StaffOidcTokenTrust trust(String issuer, String client, KeyPair pair) {
        return new StaffOidcTokenTrust(issuer, client, Set.of(JWSAlgorithm.RS256),
                List.of(new StaffOidcTokenTrust.VerificationKey(KEY_ID, JWSAlgorithm.RS256, pair.getPublic())),
                4096, Duration.ofSeconds(30), Duration.ofMinutes(5),
                List.of(new StaffOidcTokenTrust.AssuranceRequirement("acr", Set.of("required"))),
                Clock.fixed(NOW, ZoneOffset.UTC));
    }

    private static StaffOidcAuthorizationTransaction transaction(long revision, String nonce) {
        String verifier = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        return new StaffOidcAuthorizationTransaction(UUID.fromString("11111111-1111-1111-1111-111111111111"),
                "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD", nonce, verifier,
                deriveS256(verifier), "synthetic-session", ID, revision,
                NOW.minusSeconds(30), NOW.plusSeconds(60), StaffOidcAuthorizationTransaction.Status.CONSUMED);
    }

    private static JWTClaimsSet.Builder claims(String issuer, String audience, String nonce) {
        return new JWTClaimsSet.Builder().issuer(issuer).subject(SUBJECT).audience(audience)
                .issueTime(Date.from(NOW.minusSeconds(10))).expirationTime(Date.from(NOW.plusSeconds(60)))
                .notBeforeTime(Date.from(NOW.minusSeconds(10))).claim("nonce", nonce)
                .claim("auth_time", Date.from(NOW.minusSeconds(60))).claim("acr", "required");
    }

    private static String token(JWTClaimsSet.Builder claims, JWSAlgorithm algorithm, KeyPair keyPair)
            throws Exception {
        var jwt = new SignedJWT(new JWSHeader.Builder(algorithm).keyID(KEY_ID).build(), claims.build());
        if (algorithm.getName().startsWith("ES")) {
            jwt.sign(new ECDSASigner((ECPrivateKey) keyPair.getPrivate()));
        } else {
            jwt.sign(new RSASSASigner((RSAPrivateKey) keyPair.getPrivate()));
        }
        return jwt.serialize();
    }

    private static byte[] response(String idToken) {
        return bytes("{\"access_token\":\"private-access\",\"token_type\":\"Bearer\",\"id_token\":\""
                + idToken + "\"}");
    }

    private static byte[] bytes(String value) { return value.getBytes(StandardCharsets.UTF_8); }

    private static String deriveS256(String value) {
        try {
            return Base64.getUrlEncoder().withoutPadding().encodeToString(
                    MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.US_ASCII)));
        } catch (java.security.NoSuchAlgorithmException impossibleOnJava21) {
            throw new IllegalStateException(impossibleOnJava21);
        }
    }

    private static void assertFailure(StaffOidcRegistrationTokenVerifier.Result result,
            StaffOidcAuthenticationVerification.Outcome expected) {
        assertThat(result.accepted()).isFalse();
        assertThat(result.validatedToken()).isNull();
        assertThat(result.failure()).isEqualTo(expected);
    }

    private static StaffOidcAuthenticationVerification.Outcome invalidToken() {
        return StaffOidcAuthenticationVerification.Outcome.INVALID_TOKEN;
    }

    private static StaffOidcAuthenticationVerification.Outcome unavailable() {
        return StaffOidcAuthenticationVerification.Outcome.VERIFICATION_UNAVAILABLE;
    }
}
