package com.enterprise.fashion.ecommerce.identity.adapter.out.staff;

import java.lang.reflect.Modifier;
import java.net.URI;
import java.security.KeyPairGenerator;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import com.nimbusds.jose.JWSAlgorithm;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.*;

class StaffOidcProviderRegistrationResolverTest {
    private static final String ID = "synthetic-provider";
    private static final String ISSUER = "https://identity.invalid/staff";
    private static final String CLIENT = "staff-client";
    private static java.security.PublicKey publicKey;

    @BeforeAll static void key() throws Exception {
        var generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        publicKey = generator.generateKeyPair().getPublic();
    }

    @Test void exactCaseSensitiveIdAndRevisionResolveWithoutFallback() {
        var first = registration(ID, 1, true);
        var second = registration(ID, 2, true);
        var resolver = new StaffOidcProviderRegistrationResolver(List.of(first, second));

        assertResolved(resolver.resolve(ID, 1), first);
        assertResolved(resolver.resolve(ID, 2), second);
        assertMissing(resolver.resolve(ID, 3));
        assertMissing(resolver.resolve(ID.toUpperCase(), 1));
        assertMissing(resolver.resolve("other-provider", 1));
    }

    @Test void invalidLookupInputsAreMissingWithoutThrowing() {
        var resolver = new StaffOidcProviderRegistrationResolver(List.of(registration(ID, 1, true)));

        assertMissing(resolver.resolve(null, 1));
        assertMissing(resolver.resolve(" ", 1));
        assertMissing(resolver.resolve("provider\n", 1));
        assertMissing(resolver.resolve("x".repeat(129), 1));
        assertMissing(resolver.resolve(ID, 0));
        assertMissing(resolver.resolve(ID, -1));
    }

    @Test void sourceRejectsNullsAndSupportsAnEmptyCollection() {
        assertThatThrownBy(() -> new StaffOidcProviderRegistrationResolver(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Provider registrations are required");
        assertThatThrownBy(() -> new StaffOidcProviderRegistrationResolver(java.util.Arrays.asList(
                registration(ID, 1, true), null)))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Provider registration is required");
        assertMissing(new StaffOidcProviderRegistrationResolver(List.of()).resolve(ID, 1));
    }

    @Test void callerMutationCannotChangeTheResolverSnapshot() {
        var first = registration(ID, 1, true);
        var supplied = new ArrayList<>(List.of(first));
        var resolver = new StaffOidcProviderRegistrationResolver(supplied);

        supplied.clear();
        supplied.add(registration(ID, 2, true));

        assertResolved(resolver.resolve(ID, 1), first);
        assertMissing(resolver.resolve(ID, 2));
    }

    @Test void everyDuplicateExactPairIsAmbiguousWhileUnrelatedKeysStillResolve() {
        var first = registration(ID, 1, true);
        var equivalentLooking = registration(ID, 1, true);
        var thirdDuplicate = registration(ID, 1, false);
        var unrelated = registration("other-provider", 1, true);
        var resolver = new StaffOidcProviderRegistrationResolver(
                List.of(first, equivalentLooking, thirdDuplicate, unrelated));

        var ambiguous = resolver.resolve(ID, 1);
        assertThat(ambiguous.outcome())
                .isEqualTo(StaffOidcProviderRegistrationResolver.Outcome.AMBIGUOUS);
        assertThat(ambiguous.registration()).isNull();
        assertResolved(resolver.resolve("other-provider", 1), unrelated);
    }

    @Test void disabledRevisionsNeverFallForwardOrAffectOtherExactRevisions() {
        var disabledOld = registration(ID, 1, false);
        var enabledNew = registration(ID, 2, true);
        var resolver = new StaffOidcProviderRegistrationResolver(List.of(disabledOld, enabledNew));

        var disabled = resolver.resolve(ID, 1);
        assertThat(disabled.outcome())
                .isEqualTo(StaffOidcProviderRegistrationResolver.Outcome.DISABLED);
        assertThat(disabled.registration()).isNull();
        assertResolved(resolver.resolve(ID, 2), enabledNew);

        var enabledOld = registration("another-provider", 1, true);
        var disabledNew = registration("another-provider", 2, false);
        var another = new StaffOidcProviderRegistrationResolver(List.of(enabledOld, disabledNew));
        assertResolved(another.resolve("another-provider", 1), enabledOld);
        assertThat(another.resolve("another-provider", 2).outcome())
                .isEqualTo(StaffOidcProviderRegistrationResolver.Outcome.DISABLED);
    }

    @Test void onlyResolvedResultsMayContainARegistration() {
        var registration = registration(ID, 1, true);
        assertThat(new StaffOidcProviderRegistrationResolver.Resolution(
                StaffOidcProviderRegistrationResolver.Outcome.RESOLVED, registration).registration())
                .isSameAs(registration);
        assertThatThrownBy(() -> new StaffOidcProviderRegistrationResolver.Resolution(
                StaffOidcProviderRegistrationResolver.Outcome.RESOLVED, null))
                .isInstanceOf(IllegalArgumentException.class);
        for (var outcome : StaffOidcProviderRegistrationResolver.Outcome.values()) {
            if (outcome == StaffOidcProviderRegistrationResolver.Outcome.RESOLVED) continue;
            assertThatThrownBy(() -> new StaffOidcProviderRegistrationResolver.Resolution(
                    outcome, registration)).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test void resolutionPreservesTheExactRegistrationAndTrustInstances() {
        StaffOidcTokenTrust trust = trust(ISSUER, CLIENT);
        var registration = registration(ID, 7, true, trust);
        var resolution = new StaffOidcProviderRegistrationResolver(List.of(registration)).resolve(ID, 7);

        assertThat(resolution.registration()).isSameAs(registration);
        assertThat(resolution.registration().tokenTrust()).isSameAs(trust);
        assertThat(resolution.registration().issuer()).isEqualTo(URI.create(ISSUER));
        assertThat(resolution.registration().clientId()).isEqualTo(CLIENT);
        assertThat(resolution.registration().redirectUri())
                .isEqualTo(URI.create("https://store.invalid/login/staff/callback"));
        assertThat(resolution.registration().tokenEndpointUri())
                .isEqualTo(URI.create("https://identity.invalid/token"));
        assertThat(resolution.registration().revision()).isEqualTo(7);
    }

    @Test void typesArePackagePrivateFinalAndDiagnosticsAreRedacted() {
        assertThat(Modifier.isFinal(StaffOidcProviderRegistrationResolver.class.getModifiers())).isTrue();
        assertThat(Modifier.isPublic(StaffOidcProviderRegistrationResolver.class.getModifiers())).isFalse();
        var registration = registration(ID, 1, true);
        var resolver = new StaffOidcProviderRegistrationResolver(List.of(registration));
        var resolution = resolver.resolve(ID, 1);

        assertThat(resolver.toString()).doesNotContain(ID, ISSUER, CLIENT, "identity.invalid");
        assertThat(resolution.toString()).doesNotContain(ID, ISSUER, CLIENT, "identity.invalid");
    }

    @Test void resolutionHasNoAuthenticationOrAuthoritySideEffects() {
        var original = new TestingAuthenticationToken("customer", "redacted", "CUSTOMER");
        SecurityContextHolder.getContext().setAuthentication(original);
        try {
            var registration = registration(ID, 1, true);
            assertResolved(new StaffOidcProviderRegistrationResolver(List.of(registration))
                    .resolve(ID, 1), registration);
            assertThat(SecurityContextHolder.getContext().getAuthentication()).isSameAs(original);
            assertThat(original.getAuthorities()).extracting(Object::toString).containsExactly("CUSTOMER");
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    private static StaffOidcProviderRegistration registration(String id, long revision, boolean enabled) {
        return registration(id, revision, enabled, trust(ISSUER, CLIENT));
    }

    private static StaffOidcProviderRegistration registration(String id, long revision, boolean enabled,
            StaffOidcTokenTrust trust) {
        return new StaffOidcProviderRegistration(id, revision, URI.create(ISSUER), CLIENT,
                URI.create("https://store.invalid/login/staff/callback"),
                URI.create("https://identity.invalid/token"),
                StaffOidcProviderRegistration.ClientAuthenticationMethod.CLIENT_SECRET_BASIC,
                trust, Duration.ofSeconds(5), Duration.ofSeconds(10), 64 * 1024, enabled,
                "synthetic-owner-reference");
    }

    private static StaffOidcTokenTrust trust(String issuer, String client) {
        return new StaffOidcTokenTrust(issuer, client, Set.of(JWSAlgorithm.RS256),
                List.of(new StaffOidcTokenTrust.VerificationKey("test-key", JWSAlgorithm.RS256, publicKey)),
                4096, Duration.ofSeconds(30), Duration.ofMinutes(5),
                List.of(new StaffOidcTokenTrust.AssuranceRequirement("acr", Set.of("required"))),
                Clock.fixed(Instant.parse("2026-11-16T12:00:00Z"), ZoneOffset.UTC));
    }

    private static void assertResolved(StaffOidcProviderRegistrationResolver.Resolution resolution,
            StaffOidcProviderRegistration expected) {
        assertThat(resolution.outcome()).isEqualTo(StaffOidcProviderRegistrationResolver.Outcome.RESOLVED);
        assertThat(resolution.registration()).isSameAs(expected);
    }

    private static void assertMissing(StaffOidcProviderRegistrationResolver.Resolution resolution) {
        assertThat(resolution.outcome()).isEqualTo(StaffOidcProviderRegistrationResolver.Outcome.MISSING);
        assertThat(resolution.registration()).isNull();
    }
}
