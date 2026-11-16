package com.enterprise.fashion.ecommerce.identity.adapter.out.staff;

import java.lang.reflect.Modifier;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import com.enterprise.fashion.ecommerce.identity.application.port.out.StaffOidcAuthenticationVerifier;
import com.enterprise.fashion.ecommerce.identity.application.staff.StaffOidcAuthenticationVerification;
import com.enterprise.fashion.ecommerce.identity.application.staff.StaffOidcAuthorizationCallback;
import com.enterprise.fashion.ecommerce.identity.application.staff.StaffOidcAuthorizationTransaction;
import com.enterprise.fashion.ecommerce.identity.application.staff.VerifiedStaffAuthentication;
import com.nimbusds.jose.JWSAlgorithm;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class StaffOidcAuthorizationCodeExchangePreparerTest {
    private static final Instant NOW = Instant.parse("2026-11-16T12:00:00Z");
    private static final String ID = "synthetic-provider";
    private static final String ISSUER = "https://identity.invalid/staff";
    private static final String CLIENT = "staff-client";
    private static final String CODE = "private-authorization-code";
    private static final String VERIFIER = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String NONCE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static java.security.PublicKey publicKey;

    @BeforeAll static void key() throws Exception {
        var generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        publicKey = generator.generateKeyPair().getPublic();
    }

    @Test void preparesOneImmutableRequestFromTheExactTransactionAndRegistration() {
        var registration = registration(ID, 7, true,
                StaffOidcProviderRegistration.ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
        var result = preparer(registration).prepare(transaction(7), callback());

        assertThat(result.prepared()).isTrue();
        assertThat(result.failure()).isNull();
        var request = result.exchangeRequest();
        assertThat(request.registrationId()).isEqualTo(ID);
        assertThat(request.registrationRevision()).isEqualTo(7);
        assertThat(request.authorizationCode()).isEqualTo(CODE);
        assertThat(request.pkceVerifier()).isEqualTo(VERIFIER);
        assertThat(request.tokenEndpointUri()).isEqualTo(URI.create("https://identity.invalid/token"));
        assertThat(request.clientId()).isEqualTo(CLIENT);
        assertThat(request.redirectUri()).isEqualTo(URI.create("https://store.invalid/login/staff/callback"));
        assertThat(request.clientAuthenticationMethod()).isEqualTo(
                StaffOidcProviderRegistration.ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
        assertThat(request.connectionTimeout()).isEqualTo(Duration.ofSeconds(5));
        assertThat(request.responseTimeout()).isEqualTo(Duration.ofSeconds(10));
        assertThat(request.maximumResponseSize()).isEqualTo(64 * 1024);
    }

    @Test void exactRevisionIsRequiredWithoutFallback() {
        var newer = registration(ID, 8, true,
                StaffOidcProviderRegistration.ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
        assertFailure(preparer(newer).prepare(transaction(7), callback()), unavailable());
    }

    @Test void callbackAndTransactionAreTheOnlyCodeAndPkceSources() {
        String otherCode = "another-private-authorization-code";
        var request = preparer(registration()).prepare(transaction(7),
                StaffOidcAuthorizationCallback.authorizationCode(otherCode)).exchangeRequest();

        assertThat(request.authorizationCode()).isEqualTo(otherCode);
        assertThat(request.pkceVerifier()).isEqualTo(VERIFIER);
        assertThat(request.registrationId()).isEqualTo(ID);
        assertThat(request.registrationRevision()).isEqualTo(7);
    }

    @Test void bothRegistrationOwnedClientAuthenticationMethodsAreRepresentedWithoutCredentials() {
        for (var method : StaffOidcProviderRegistration.ClientAuthenticationMethod.values()) {
            var request = preparer(registration(ID, 7, true, method))
                    .prepare(transaction(7), callback()).exchangeRequest();
            assertThat(request.clientAuthenticationMethod()).isEqualTo(method);
        }
        assertThat(Arrays.stream(StaffOidcAuthorizationCodeExchangePreparer.ExchangeRequest.class
                .getDeclaredFields()).map(field -> field.getName()))
                .doesNotContain("clientSecret", "privateKey", "tokenTrust", "verificationKeys",
                        "headers", "serializedForm", "httpRequest", "authentication");
    }

    @Test void everyNonResolvedRegistrationOutcomeFailsClosed() {
        assertFailure(preparer().prepare(transaction(7), callback()), unavailable());
        assertFailure(preparer(registration(ID, 7, false,
                StaffOidcProviderRegistration.ClientAuthenticationMethod.CLIENT_SECRET_BASIC))
                .prepare(transaction(7), callback()), unavailable());
        assertFailure(preparer(registration(), registration()).prepare(transaction(7), callback()), unavailable());

        StaffOidcProviderRegistrationResolver resolver = mock(StaffOidcProviderRegistrationResolver.class);
        when(resolver.resolve(ID, 7)).thenReturn(new StaffOidcProviderRegistrationResolver.Resolution(
                StaffOidcProviderRegistrationResolver.Outcome.UNAVAILABLE, null));
        assertFailure(new StaffOidcAuthorizationCodeExchangePreparer(resolver)
                .prepare(transaction(7), callback()), unavailable());
    }

    @Test void nullAndMalformedResolverResultsAreIndeterminate() {
        StaffOidcProviderRegistrationResolver nullResolver = mock(StaffOidcProviderRegistrationResolver.class);
        when(nullResolver.resolve(ID, 7)).thenReturn(null);
        assertFailure(new StaffOidcAuthorizationCodeExchangePreparer(nullResolver)
                .prepare(transaction(7), callback()), indeterminate());

        StaffOidcProviderRegistrationResolver.Resolution malformed =
                mock(StaffOidcProviderRegistrationResolver.Resolution.class);
        when(malformed.outcome()).thenReturn(null);
        StaffOidcProviderRegistrationResolver malformedResolver = mock(StaffOidcProviderRegistrationResolver.class);
        when(malformedResolver.resolve(ID, 7)).thenReturn(malformed);
        assertFailure(new StaffOidcAuthorizationCodeExchangePreparer(malformedResolver)
                .prepare(transaction(7), callback()), indeterminate());
        org.mockito.Mockito.verify(malformed, never()).registration();
    }

    @Test void malformedResolvedRegistrationsAreIndeterminate() {
        StaffOidcProviderRegistrationResolver.Resolution missingRegistration =
                mock(StaffOidcProviderRegistrationResolver.Resolution.class);
        when(missingRegistration.outcome()).thenReturn(StaffOidcProviderRegistrationResolver.Outcome.RESOLVED);
        assertFailure(new StaffOidcAuthorizationCodeExchangePreparer(resolverReturning(missingRegistration))
                .prepare(transaction(7), callback()), indeterminate());

        assertFailure(new StaffOidcAuthorizationCodeExchangePreparer(resolvedAs(registration(
                "other-provider", 7, true,
                StaffOidcProviderRegistration.ClientAuthenticationMethod.CLIENT_SECRET_BASIC)))
                .prepare(transaction(7), callback()), indeterminate());
        assertFailure(new StaffOidcAuthorizationCodeExchangePreparer(resolvedAs(registration(
                ID, 8, true, StaffOidcProviderRegistration.ClientAuthenticationMethod.CLIENT_SECRET_BASIC)))
                .prepare(transaction(7), callback()), indeterminate());
    }

    @Test void nullAndEveryNonConsumedTransactionAreRejectedBeforeResolution() {
        StaffOidcProviderRegistrationResolver resolver = mock(StaffOidcProviderRegistrationResolver.class);
        var preparer = new StaffOidcAuthorizationCodeExchangePreparer(resolver);

        assertFailure(preparer.prepare(null, callback()),
                StaffOidcAuthenticationVerification.Outcome.INVALID_TRANSACTION);
        for (var status : StaffOidcAuthorizationTransaction.Status.values()) {
            if (status == StaffOidcAuthorizationTransaction.Status.CONSUMED) continue;
            assertFailure(preparer.prepare(transaction(7).withStatus(status), callback()),
                    StaffOidcAuthenticationVerification.Outcome.INVALID_TRANSACTION);
        }
        verifyNoInteractions(resolver);
    }

    @Test void nullAndProviderRejectedCallbacksNeverResolveARegistration() {
        StaffOidcProviderRegistrationResolver resolver = mock(StaffOidcProviderRegistrationResolver.class);
        var preparer = new StaffOidcAuthorizationCodeExchangePreparer(resolver);

        assertFailure(preparer.prepare(transaction(7), null),
                StaffOidcAuthenticationVerification.Outcome.REJECTED_CALLBACK);
        assertFailure(preparer.prepare(transaction(7), StaffOidcAuthorizationCallback.rejected("access_denied")),
                StaffOidcAuthenticationVerification.Outcome.REJECTED_CALLBACK);
        verifyNoInteractions(resolver);
    }

    @Test void requestAndResultAreImmutableAndDiagnosticsAreRedacted() {
        var result = preparer(registration()).prepare(transaction(7), callback());
        var request = result.exchangeRequest();

        assertThat(Arrays.stream(request.getClass().getDeclaredFields())
                .allMatch(field -> Modifier.isFinal(field.getModifiers()))).isTrue();
        assertThat(Arrays.stream(request.getClass().getDeclaredConstructors())
                .allMatch(constructor -> Modifier.isPrivate(constructor.getModifiers()))).isTrue();
        assertThat(Arrays.stream(request.getClass().getDeclaredMethods()).map(method -> method.getName()))
                .doesNotContain("equals", "hashCode");
        assertThat(request.toString()).doesNotContain(CODE, VERIFIER, CLIENT, "identity.invalid",
                "store.invalid", "synthetic-owner");
        assertThat(result.toString()).doesNotContain(CODE, VERIFIER, CLIENT, "identity.invalid");
        assertThat(new StaffOidcAuthorizationCodeExchangePreparer(
                new StaffOidcProviderRegistrationResolver(List.of())).toString())
                .doesNotContain(CODE, VERIFIER, CLIENT, ID);
    }

    @Test void componentRemainsPackagePrivateDormantAndHasNoAuthenticationOrSessionSideEffects() {
        assertThat(Modifier.isFinal(StaffOidcAuthorizationCodeExchangePreparer.class.getModifiers())).isTrue();
        assertThat(Modifier.isPublic(StaffOidcAuthorizationCodeExchangePreparer.class.getModifiers())).isFalse();
        assertThat(StaffOidcAuthenticationVerifier.class.isAssignableFrom(
                StaffOidcAuthorizationCodeExchangePreparer.class)).isFalse();
        assertThat(Arrays.stream(StaffOidcAuthorizationCodeExchangePreparer.class.getDeclaredMethods()))
                .noneMatch(method -> method.getReturnType().equals(VerifiedStaffAuthentication.class));

        var original = new TestingAuthenticationToken("customer", "redacted", "CUSTOMER");
        SecurityContextHolder.getContext().setAuthentication(original);
        try {
            assertThat(preparer(registration()).prepare(transaction(7), callback()).prepared()).isTrue();
            assertThat(SecurityContextHolder.getContext().getAuthentication()).isSameAs(original);
            assertThat(original.getAuthorities()).extracting(Object::toString).containsExactly("CUSTOMER");
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    private static StaffOidcAuthorizationCodeExchangePreparer preparer(
            StaffOidcProviderRegistration... registrations) {
        return new StaffOidcAuthorizationCodeExchangePreparer(
                new StaffOidcProviderRegistrationResolver(List.of(registrations)));
    }

    private static StaffOidcProviderRegistration registration() {
        return registration(ID, 7, true,
                StaffOidcProviderRegistration.ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
    }

    private static StaffOidcProviderRegistration registration(String id, long revision, boolean enabled,
            StaffOidcProviderRegistration.ClientAuthenticationMethod method) {
        return new StaffOidcProviderRegistration(id, revision, URI.create(ISSUER), CLIENT,
                URI.create("https://store.invalid/login/staff/callback"),
                URI.create("https://identity.invalid/token"), method, trust(),
                Duration.ofSeconds(5), Duration.ofSeconds(10), 64 * 1024, enabled,
                "synthetic-owner-reference");
    }

    private static StaffOidcTokenTrust trust() {
        return new StaffOidcTokenTrust(ISSUER, CLIENT, Set.of(JWSAlgorithm.RS256),
                List.of(new StaffOidcTokenTrust.VerificationKey("test-key", JWSAlgorithm.RS256, publicKey)),
                4096, Duration.ofSeconds(30), Duration.ofMinutes(5),
                List.of(new StaffOidcTokenTrust.AssuranceRequirement("acr", Set.of("required"))),
                Clock.fixed(NOW, ZoneOffset.UTC));
    }

    private static StaffOidcAuthorizationTransaction transaction(long revision) {
        return new StaffOidcAuthorizationTransaction(UUID.fromString("11111111-1111-1111-1111-111111111111"),
                "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD", NONCE, VERIFIER,
                deriveS256(VERIFIER), "synthetic-session", ID, revision,
                NOW.minusSeconds(30), NOW.plusSeconds(60), StaffOidcAuthorizationTransaction.Status.CONSUMED);
    }

    private static StaffOidcAuthorizationCallback callback() {
        return StaffOidcAuthorizationCallback.authorizationCode(CODE);
    }

    private static StaffOidcProviderRegistrationResolver resolvedAs(
            StaffOidcProviderRegistration registration) {
        return resolverReturning(new StaffOidcProviderRegistrationResolver.Resolution(
                StaffOidcProviderRegistrationResolver.Outcome.RESOLVED, registration));
    }

    private static StaffOidcProviderRegistrationResolver resolverReturning(
            StaffOidcProviderRegistrationResolver.Resolution resolution) {
        StaffOidcProviderRegistrationResolver resolver = mock(StaffOidcProviderRegistrationResolver.class);
        when(resolver.resolve(ID, 7)).thenReturn(resolution);
        return resolver;
    }

    private static String deriveS256(String value) {
        try {
            return Base64.getUrlEncoder().withoutPadding().encodeToString(
                    MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.US_ASCII)));
        } catch (java.security.NoSuchAlgorithmException impossibleOnJava21) {
            throw new IllegalStateException(impossibleOnJava21);
        }
    }

    private static void assertFailure(StaffOidcAuthorizationCodeExchangePreparer.Result result,
            StaffOidcAuthenticationVerification.Outcome expected) {
        assertThat(result.prepared()).isFalse();
        assertThat(result.exchangeRequest()).isNull();
        assertThat(result.failure()).isEqualTo(expected);
    }

    private static StaffOidcAuthenticationVerification.Outcome unavailable() {
        return StaffOidcAuthenticationVerification.Outcome.VERIFICATION_UNAVAILABLE;
    }

    private static StaffOidcAuthenticationVerification.Outcome indeterminate() {
        return StaffOidcAuthenticationVerification.Outcome.INDETERMINATE;
    }
}
