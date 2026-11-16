package com.enterprise.fashion.ecommerce.identity.adapter.out.staff;

import java.lang.reflect.Modifier;
import java.net.URI;
import java.security.KeyPairGenerator;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import com.nimbusds.jose.JWSAlgorithm;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.*;

class StaffOidcProviderRegistrationTest {
    private static final String ISSUER = "https://identity.invalid/staff";
    private static final String CLIENT = "staff-client";
    private static java.security.PublicKey publicKey;

    @BeforeAll static void key() throws Exception {
        var generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        publicKey = generator.generateKeyPair().getPublic();
    }

    @Test void preservesAValidSyntheticRegistrationExactly() {
        String registrationId = " staff-registration ";
        var registration = registration(registrationId, true);

        assertThat(registration.registrationId()).isEqualTo(registrationId);
        assertThat(registration.revision()).isEqualTo(1);
        assertThat(registration.issuer()).isEqualTo(URI.create(ISSUER));
        assertThat(registration.clientId()).isEqualTo(CLIENT);
        assertThat(registration.redirectUri()).isEqualTo(URI.create("https://store.invalid/login/staff/callback?flow=oidc"));
        assertThat(registration.tokenEndpointUri()).isEqualTo(URI.create("https://identity.invalid/token"));
        assertThat(registration.clientAuthenticationMethod())
                .isEqualTo(StaffOidcProviderRegistration.ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
        assertThat(registration.connectionTimeout()).isEqualTo(Duration.ofSeconds(5));
        assertThat(registration.responseTimeout()).isEqualTo(Duration.ofSeconds(10));
        assertThat(registration.maximumResponseSize()).isEqualTo(64 * 1024);
        assertThat(registration.enabled()).isTrue();
        assertThat(registration.ownerReference()).isEqualTo("synthetic-owner-reference");
    }

    @Test void isPackagePrivateFinalAndContainsNoCredentialField() {
        assertThat(Modifier.isFinal(StaffOidcProviderRegistration.class.getModifiers())).isTrue();
        assertThat(Modifier.isPublic(StaffOidcProviderRegistration.class.getModifiers())).isFalse();
        assertThat(StaffOidcProviderRegistration.class.getDeclaredFields())
                .filteredOn(field -> !Modifier.isStatic(field.getModifiers()))
                .allMatch(field -> Modifier.isPrivate(field.getModifiers())
                        && Modifier.isFinal(field.getModifiers()));
        assertThat(StaffOidcProviderRegistration.class.getDeclaredFields())
                .extracting(field -> field.getName().toLowerCase())
                .noneMatch(name -> name.contains("secret") || name.contains("credential")
                        || name.contains("password") || name.contains("privatekey"));
    }

    @Test void rejectsInvalidIdentifiersAndRevisionWithoutTrimming() {
        assertInvalid(() -> registration(null, true));
        assertInvalid(() -> registration(" ", true));
        assertInvalid(() -> registration("registration\n", true));
        assertInvalid(() -> registration("x".repeat(129), true));
        assertInvalid(() -> registration(0, ISSUER, CLIENT, trust(), Duration.ofSeconds(5),
                Duration.ofSeconds(10), 1024, true, "owner"));
        assertInvalid(() -> registration(1, ISSUER, " ", trust(), Duration.ofSeconds(5),
                Duration.ofSeconds(10), 1024, true, "owner"));
        assertThatThrownBy(() -> new StaffOidcProviderRegistration("registration", 1, null, CLIENT,
                URI.create("https://store.invalid/callback"), URI.create("https://identity.invalid/token"),
                StaffOidcProviderRegistration.ClientAuthenticationMethod.CLIENT_SECRET_BASIC,
                trust(), Duration.ofSeconds(5), Duration.ofSeconds(10), 1024, true, "owner"))
                .isInstanceOf(NullPointerException.class);
        assertInvalid(() -> registration(1, ISSUER, null, trust(), Duration.ofSeconds(5),
                Duration.ofSeconds(10), 1024, true, "owner"));
        assertInvalid(() -> registration(1, ISSUER, CLIENT, trust(), Duration.ofSeconds(5),
                Duration.ofSeconds(10), 1024, true, "owner\n"));
    }

    @Test void requiresExactIssuerAndClientAgreementWithTrust() {
        assertInvalid(() -> registration(1, "https://other.invalid", CLIENT, trust(),
                Duration.ofSeconds(5), Duration.ofSeconds(10), 1024, true, "owner"));
        assertInvalid(() -> registration(1, ISSUER, "other-client", trust(),
                Duration.ofSeconds(5), Duration.ofSeconds(10), 1024, true, "owner"));
    }

    @Test void rejectsUnsafeOrAmbiguousUris() {
        assertUriInvalid("http://identity.invalid/staff", "https://store.invalid/callback",
                "https://identity.invalid/token");
        assertUriInvalid(ISSUER, "http://store.invalid/callback", "https://identity.invalid/token");
        assertUriInvalid(ISSUER, "https://store.invalid/callback", "http://identity.invalid/token");
        assertUriInvalid("/relative", "https://store.invalid/callback", "https://identity.invalid/token");
        assertUriInvalid("https://user@identity.invalid/staff", "https://store.invalid/callback",
                "https://identity.invalid/token");
        assertUriInvalid(ISSUER, "https://store.invalid/callback#fragment", "https://identity.invalid/token");
        assertUriInvalid("https://identity.invalid/staff?tenant=x", "https://store.invalid/callback",
                "https://identity.invalid/token");
        assertUriInvalid(ISSUER, "https://store.invalid/callback", "https://identity.invalid/token?tenant=x");
        assertUriInvalid("https://identity.invalid:99999/staff", "https://store.invalid/callback",
                "https://identity.invalid/token");
        assertUriInvalid("https://identity.invalid/a/../staff", "https://store.invalid/callback",
                "https://identity.invalid/token");
        assertUriInvalid("https://identity.invalid:/staff", "https://store.invalid/callback",
                "https://identity.invalid/token");
    }

    @Test void requiresServerBasedAuthoritiesAndAcceptsValidHttpsPorts() {
        assertUriInvalid("https://identity.invalid:not-a-port/staff", "https://store.invalid/callback",
                "https://identity.invalid/token");
        assertUriInvalid("https://identity.invalid:65536/staff", "https://store.invalid/callback",
                "https://identity.invalid/token");
        assertUriInvalid("https://identity.invalid:/staff", "https://store.invalid/callback",
                "https://identity.invalid/token");
        assertUriInvalid("https://identity.invalid,other.invalid/staff", "https://store.invalid/callback",
                "https://identity.invalid/token");
        assertUriInvalid("https://identity%2einvalid/staff", "https://store.invalid/callback",
                "https://identity.invalid/token");
        assertUriInvalid("https:///staff", "https://store.invalid/callback",
                "https://identity.invalid/token");

        assertThat(registrationWithIssuer("https://identity.invalid:8443/staff").issuer())
                .isEqualTo(URI.create("https://identity.invalid:8443/staff"));
        assertThat(registrationWithIssuer(ISSUER).issuer()).isEqualTo(URI.create(ISSUER));
    }

    @Test void requiresExplicitClientAuthenticationAndBoundedTransportLimits() {
        assertThatThrownBy(() -> new StaffOidcProviderRegistration("registration", 1,
                URI.create(ISSUER), CLIENT, URI.create("https://store.invalid/callback"),
                URI.create("https://identity.invalid/token"), null, trust(), Duration.ofSeconds(5),
                Duration.ofSeconds(10), 1024, true, "owner")).isInstanceOf(NullPointerException.class);
        assertTimeoutInvalid(Duration.ZERO, Duration.ofSeconds(10), 1024);
        assertTimeoutInvalid(Duration.ofSeconds(61), Duration.ofSeconds(10), 1024);
        assertTimeoutInvalid(Duration.ofSeconds(5), Duration.ZERO, 1024);
        assertTimeoutInvalid(Duration.ofSeconds(5), Duration.ofSeconds(121), 1024);
        assertTimeoutInvalid(Duration.ofSeconds(5), Duration.ofSeconds(10), 0);
        assertTimeoutInvalid(Duration.ofSeconds(5), Duration.ofSeconds(10), 1024 * 1024 + 1);
    }

    @Test void enabledAndDisabledAreMetadataOnlyAndDiagnosticsAreRedacted() {
        var enabled = registration("registration", true);
        var disabled = registration("registration", false);

        assertThat(enabled.enabled()).isTrue();
        assertThat(disabled.enabled()).isFalse();
        assertThat(enabled.toString()).contains("registrationId=registration", "revision=1", "enabled=true")
                .doesNotContain(ISSUER, CLIENT, "identity.invalid", "synthetic-owner-reference", "test-key");
    }

    @Test void constructionHasNoAuthenticationOrNetworkEffect() {
        var original = new TestingAuthenticationToken("customer", "redacted", "CUSTOMER");
        SecurityContextHolder.getContext().setAuthentication(original);
        try {
            assertThat(registration("registration", true)).isNotNull();
            assertThat(SecurityContextHolder.getContext().getAuthentication()).isSameAs(original);
            assertThat(original.getAuthorities()).extracting(Object::toString).containsExactly("CUSTOMER");
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    private static StaffOidcProviderRegistration registration(String registrationId, boolean enabled) {
        return new StaffOidcProviderRegistration(registrationId, 1, URI.create(ISSUER), CLIENT,
                URI.create("https://store.invalid/login/staff/callback?flow=oidc"),
                URI.create("https://identity.invalid/token"),
                StaffOidcProviderRegistration.ClientAuthenticationMethod.CLIENT_SECRET_BASIC,
                trust(), Duration.ofSeconds(5), Duration.ofSeconds(10), 64 * 1024, enabled,
                "synthetic-owner-reference");
    }

    private static StaffOidcProviderRegistration registration(long revision, String issuer,
            String client, StaffOidcTokenTrust trust, Duration connectionTimeout,
            Duration responseTimeout, int maximumResponseSize, boolean enabled, String owner) {
        return new StaffOidcProviderRegistration("registration", revision, URI.create(issuer), client,
                URI.create("https://store.invalid/callback"), URI.create("https://identity.invalid/token"),
                StaffOidcProviderRegistration.ClientAuthenticationMethod.CLIENT_SECRET_BASIC,
                trust, connectionTimeout, responseTimeout, maximumResponseSize, enabled, owner);
    }

    private static StaffOidcTokenTrust trust() {
        return new StaffOidcTokenTrust(ISSUER, CLIENT, Set.of(JWSAlgorithm.RS256),
                List.of(new StaffOidcTokenTrust.VerificationKey("test-key", JWSAlgorithm.RS256, publicKey)),
                4096, Duration.ofSeconds(30), Duration.ofMinutes(5),
                List.of(new StaffOidcTokenTrust.AssuranceRequirement("acr", Set.of("required"))),
                Clock.fixed(Instant.parse("2026-11-16T12:00:00Z"), ZoneOffset.UTC));
    }

    private static StaffOidcProviderRegistration registrationWithIssuer(String issuer) {
        StaffOidcTokenTrust matchingTrust = new StaffOidcTokenTrust(issuer, CLIENT,
                Set.of(JWSAlgorithm.RS256),
                List.of(new StaffOidcTokenTrust.VerificationKey("test-key", JWSAlgorithm.RS256, publicKey)),
                4096, Duration.ofSeconds(30), Duration.ofMinutes(5),
                List.of(new StaffOidcTokenTrust.AssuranceRequirement("acr", Set.of("required"))),
                Clock.fixed(Instant.parse("2026-11-16T12:00:00Z"), ZoneOffset.UTC));
        return new StaffOidcProviderRegistration("registration", 1, URI.create(issuer), CLIENT,
                URI.create("https://store.invalid/callback"), URI.create("https://identity.invalid/token"),
                StaffOidcProviderRegistration.ClientAuthenticationMethod.CLIENT_SECRET_BASIC,
                matchingTrust, Duration.ofSeconds(5), Duration.ofSeconds(10), 1024, true, "owner");
    }

    private static void assertUriInvalid(String issuer, String redirect, String tokenEndpoint) {
        assertThatThrownBy(() -> new StaffOidcProviderRegistration("registration", 1,
                URI.create(issuer), CLIENT, URI.create(redirect), URI.create(tokenEndpoint),
                StaffOidcProviderRegistration.ClientAuthenticationMethod.CLIENT_SECRET_BASIC,
                trust(), Duration.ofSeconds(5), Duration.ofSeconds(10), 1024, true, "owner"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static void assertTimeoutInvalid(Duration connection, Duration response, int size) {
        assertInvalid(() -> registration(1, ISSUER, CLIENT, trust(), connection, response, size, true, "owner"));
    }

    private static void assertInvalid(ThrowingCallable callable) {
        assertThatThrownBy(callable).isInstanceOfAny(IllegalArgumentException.class, NullPointerException.class);
    }
}
