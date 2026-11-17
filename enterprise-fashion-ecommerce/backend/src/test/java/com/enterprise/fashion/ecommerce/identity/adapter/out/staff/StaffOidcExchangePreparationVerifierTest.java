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
import com.enterprise.fashion.ecommerce.identity.adapter.out.staff.StaffOidcProviderRegistration.ClientAuthenticationMethod;
import com.enterprise.fashion.ecommerce.identity.application.port.out.StaffOidcAuthenticationVerifier;
import com.enterprise.fashion.ecommerce.identity.application.staff.StaffOidcAuthenticationVerification;
import com.enterprise.fashion.ecommerce.identity.application.staff.StaffOidcAuthorizationCallback;
import com.enterprise.fashion.ecommerce.identity.application.staff.StaffOidcAuthorizationTransaction;
import com.enterprise.fashion.ecommerce.identity.application.staff.StaffOidcAuthorizationTransactions;
import com.enterprise.fashion.ecommerce.identity.application.staff.VerifiedStaffAuthentication;
import com.enterprise.fashion.ecommerce.identity.application.staff.VerifyStaffOidcAuthentication;
import com.enterprise.fashion.ecommerce.identity.config.IdentityStaffOidcVerificationConfiguration;
import com.nimbusds.jose.JWSAlgorithm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class StaffOidcExchangePreparationVerifierTest {
    private static final Instant NOW = Instant.parse("2026-11-16T12:00:00Z");
    private static final String ID = "synthetic-provider";
    private static final String ISSUER = "https://identity.invalid/staff";
    private static final String CLIENT = "staff-client";
    private static final String STATE = "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD";
    private static final String SESSION = "synthetic-session";
    private static final String CODE = "private-authorization-code";
    private static final String VERIFIER = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String NONCE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static java.security.PublicKey publicKey;

    @BeforeAll static void key() throws Exception {
        var generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        publicKey = generator.generateKeyPair().getPublic();
    }

    @AfterEach void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test void directConsumedV16TransactionReachesExactPreparationAndRemainsUnavailable() {
        var consumed = transaction(7, StaffOidcAuthorizationTransaction.Status.CONSUMED);
        var transactions = mock(StaffOidcAuthorizationTransactions.class);
        when(transactions.consume(STATE, SESSION)).thenReturn(new StaffOidcAuthorizationTransactions.Result(
                StaffOidcAuthorizationTransactions.ConsumeOutcome.CONSUMED, consumed));
        var registrations = mock(StaffOidcProviderRegistrationResolver.class);
        var registration = registration(ID, 7, true);
        when(registrations.resolve(ID, 7)).thenReturn(resolved(registration));
        var service = new VerifyStaffOidcAuthentication(transactions,
                new StaffOidcExchangePreparationVerifier(registrations));

        assertThat(service.verify(STATE, SESSION, callback()).outcome())
                .isEqualTo(StaffOidcAuthenticationVerification.Outcome.VERIFICATION_UNAVAILABLE);
        verify(transactions).consume(STATE, SESSION);
        verify(registrations).resolve(ID, 7);
    }

    @Test void missingDisabledAmbiguousAndWrongRevisionRegistrationsFailClosed() {
        assertOutcome(verifier(), transaction(), unavailable());
        assertOutcome(verifier(registration(ID, 7, false)), transaction(), unavailable());
        assertOutcome(verifier(registration(), registration()), transaction(), unavailable());
        assertOutcome(verifier(registration(ID, 8, true)), transaction(), unavailable());
    }

    @Test void malformedResolverOutputIsIndeterminate() {
        var registrations = mock(StaffOidcProviderRegistrationResolver.class);
        when(registrations.resolve(ID, 7)).thenReturn(null);
        assertOutcome(new StaffOidcExchangePreparationVerifier(registrations), transaction(), indeterminate());

        var malformed = mock(StaffOidcProviderRegistrationResolver.Resolution.class);
        when(malformed.outcome()).thenReturn(null);
        when(registrations.resolve(ID, 7)).thenReturn(malformed);
        assertOutcome(new StaffOidcExchangePreparationVerifier(registrations), transaction(), indeterminate());
    }

    @Test void nonConsumedTransactionsAndRejectedCallbacksPreservePreparationFailures() {
        for (var status : StaffOidcAuthorizationTransaction.Status.values()) {
            if (status == StaffOidcAuthorizationTransaction.Status.CONSUMED) continue;
            assertOutcome(verifier(registration()), transaction(7, status),
                    StaffOidcAuthenticationVerification.Outcome.INVALID_TRANSACTION);
        }
        assertThat(verifier(registration()).verify(transaction(), null).outcome())
                .isEqualTo(StaffOidcAuthenticationVerification.Outcome.REJECTED_CALLBACK);
        assertThat(verifier(registration()).verify(transaction(),
                StaffOidcAuthorizationCallback.rejected("access_denied")).outcome())
                .isEqualTo(StaffOidcAuthenticationVerification.Outcome.REJECTED_CALLBACK);
    }

    @Test void unexpectedRuntimeFailureIsIndeterminateWithoutSecretDisclosure() {
        var preparer = mock(StaffOidcAuthorizationCodeExchangePreparer.class);
        when(preparer.prepare(any(), any())).thenThrow(
                new IllegalStateException("private " + CODE + " " + VERIFIER));
        var verifier = new StaffOidcExchangePreparationVerifier(preparer);

        var result = verifier.verify(transaction(), callback());

        assertThat(result.outcome()).isEqualTo(StaffOidcAuthenticationVerification.Outcome.INDETERMINATE);
        assertThat(result.toString()).doesNotContain(CODE, VERIFIER, CLIENT, "identity.invalid");
        assertThat(verifier.toString()).doesNotContain(CODE, VERIFIER, CLIENT, ID, "identity.invalid");
    }

    @Test void componentHasNoPositiveEvidenceSessionAuthorityOrSecurityContextSideEffects() {
        assertThat(Modifier.isFinal(StaffOidcExchangePreparationVerifier.class.getModifiers())).isTrue();
        assertThat(Modifier.isPublic(StaffOidcExchangePreparationVerifier.class.getModifiers())).isFalse();
        assertThat(StaffOidcAuthenticationVerifier.class)
                .isAssignableFrom(StaffOidcExchangePreparationVerifier.class);
        assertThat(Arrays.stream(StaffOidcExchangePreparationVerifier.class.getDeclaredMethods()))
                .noneMatch(method -> method.getReturnType() == VerifiedStaffAuthentication.class);
        assertThat(Arrays.stream(StaffOidcExchangePreparationVerifier.class.getDeclaredFields())
                .map(field -> field.getType().getName()))
                .containsExactly(StaffOidcAuthorizationCodeExchangePreparer.class.getName());

        var original = new TestingAuthenticationToken("customer", "redacted", "CUSTOMER");
        SecurityContextHolder.getContext().setAuthentication(original);
        assertOutcome(verifier(registration()), transaction(), unavailable());
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isSameAs(original);
        assertThat(original.getAuthorities()).extracting(Object::toString).containsExactly("CUSTOMER");
    }

    @Test void productionCompositionRemainsTheUnavailableVerifierOnly() {
        try (var context = new AnnotationConfigApplicationContext(
                IdentityStaffOidcVerificationConfiguration.class)) {
            assertThat(context.getBeansOfType(StaffOidcAuthenticationVerifier.class)).hasSize(1);
            assertThat(context.getBean(StaffOidcAuthenticationVerifier.class))
                    .isExactlyInstanceOf(UnavailableStaffOidcAuthenticationVerifier.class);
            assertThat(context.getBeansOfType(StaffOidcExchangePreparationVerifier.class)).isEmpty();
        }
    }

    private static StaffOidcExchangePreparationVerifier verifier(
            StaffOidcProviderRegistration... registrations) {
        return new StaffOidcExchangePreparationVerifier(
                new StaffOidcProviderRegistrationResolver(List.of(registrations)));
    }

    private static StaffOidcProviderRegistration registration() {
        return registration(ID, 7, true);
    }

    private static StaffOidcProviderRegistration registration(String id, long revision, boolean enabled) {
        return new StaffOidcProviderRegistration(id, revision, URI.create(ISSUER), CLIENT,
                URI.create("https://store.invalid/login/staff/callback"),
                URI.create("https://identity.invalid/token"), ClientAuthenticationMethod.CLIENT_SECRET_BASIC,
                trust(), Duration.ofSeconds(5), Duration.ofSeconds(10), 64 * 1024, enabled,
                "synthetic-owner-reference");
    }

    private static StaffOidcTokenTrust trust() {
        return new StaffOidcTokenTrust(ISSUER, CLIENT, Set.of(JWSAlgorithm.RS256),
                List.of(new StaffOidcTokenTrust.VerificationKey("test-key", JWSAlgorithm.RS256, publicKey)),
                4096, Duration.ofSeconds(30), Duration.ofMinutes(5),
                List.of(new StaffOidcTokenTrust.AssuranceRequirement("acr", Set.of("required"))),
                Clock.fixed(NOW, ZoneOffset.UTC));
    }

    private static StaffOidcAuthorizationTransaction transaction() {
        return transaction(7, StaffOidcAuthorizationTransaction.Status.CONSUMED);
    }

    private static StaffOidcAuthorizationTransaction transaction(long revision,
            StaffOidcAuthorizationTransaction.Status status) {
        return new StaffOidcAuthorizationTransaction(UUID.fromString("11111111-1111-1111-1111-111111111111"),
                STATE, NONCE, VERIFIER, deriveS256(VERIFIER), SESSION, ID, revision,
                NOW.minusSeconds(30), NOW.plusSeconds(60), status);
    }

    private static StaffOidcAuthorizationCallback callback() {
        return StaffOidcAuthorizationCallback.authorizationCode(CODE);
    }

    private static StaffOidcProviderRegistrationResolver.Resolution resolved(
            StaffOidcProviderRegistration registration) {
        return new StaffOidcProviderRegistrationResolver.Resolution(
                StaffOidcProviderRegistrationResolver.Outcome.RESOLVED, registration);
    }

    private static String deriveS256(String value) {
        try {
            return Base64.getUrlEncoder().withoutPadding().encodeToString(
                    MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.US_ASCII)));
        } catch (java.security.NoSuchAlgorithmException impossibleOnJava21) {
            throw new IllegalStateException(impossibleOnJava21);
        }
    }

    private static void assertOutcome(StaffOidcExchangePreparationVerifier verifier,
            StaffOidcAuthorizationTransaction transaction,
            StaffOidcAuthenticationVerification.Outcome expected) {
        assertThat(verifier.verify(transaction, callback()).outcome()).isEqualTo(expected);
    }

    private static StaffOidcAuthenticationVerification.Outcome unavailable() {
        return StaffOidcAuthenticationVerification.Outcome.VERIFICATION_UNAVAILABLE;
    }

    private static StaffOidcAuthenticationVerification.Outcome indeterminate() {
        return StaffOidcAuthenticationVerification.Outcome.INDETERMINATE;
    }
}
