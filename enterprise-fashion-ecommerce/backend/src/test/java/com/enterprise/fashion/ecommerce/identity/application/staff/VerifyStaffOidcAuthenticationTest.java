package com.enterprise.fashion.ecommerce.identity.application.staff;

import java.lang.reflect.Modifier;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import com.enterprise.fashion.ecommerce.identity.adapter.out.staff.UnavailableStaffOidcAuthenticationVerifier;
import com.enterprise.fashion.ecommerce.identity.application.port.out.StaffOidcAuthenticationVerifier;
import com.enterprise.fashion.ecommerce.identity.domain.model.CredentialSubject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.*;

class VerifyStaffOidcAuthenticationTest {
    private static final String SECRET_CODE = "opaque-authorization-code";
    private static final String PROVIDER_ERROR = "private-provider-error";

    @AfterEach void clearContext() { SecurityContextHolder.clearContext(); }

    @Test void unavailableVerifierIsTheOnlyDefaultAndFailsClosed() {
        var service = new VerifyStaffOidcAuthentication(new UnavailableStaffOidcAuthenticationVerifier());
        assertThat(service.verify(consumed(), StaffOidcAuthorizationCallback.authorizationCode(SECRET_CODE)).outcome())
                .isEqualTo(StaffOidcAuthenticationVerification.Outcome.VERIFICATION_UNAVAILABLE);
    }

    @Test void rejectedAndMissingCallbacksFailClosedWithoutCallingVerifier() {
        var calls = new AtomicInteger();
        var service = new VerifyStaffOidcAuthentication((transaction, callback) -> {
            calls.incrementAndGet();
            return result(StaffOidcAuthenticationVerification.Outcome.INVALID_TOKEN);
        });
        assertThat(service.verify(consumed(), StaffOidcAuthorizationCallback.rejected(PROVIDER_ERROR)).outcome())
                .isEqualTo(StaffOidcAuthenticationVerification.Outcome.REJECTED_CALLBACK);
        assertThat(service.verify(consumed(), null).outcome())
                .isEqualTo(StaffOidcAuthenticationVerification.Outcome.REJECTED_CALLBACK);
        assertThat(calls).hasValue(0);
    }

    @Test void malformedCallbackShapeIsRejectedWithoutEchoingSecret() {
        assertThatThrownBy(() -> StaffOidcAuthorizationCallback.authorizationCode(" \nprivate"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid authorization callback")
                .hasMessageNotContaining("private");
        assertThatThrownBy(() -> StaffOidcAuthorizationCallback.rejected(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageNotContaining("null");
    }

    @Test void onlyConsumedTransactionsReachVerifier() {
        var calls = new AtomicInteger();
        var service = new VerifyStaffOidcAuthentication((transaction, callback) -> {
            calls.incrementAndGet();
            return result(StaffOidcAuthenticationVerification.Outcome.EXCHANGE_REJECTED);
        });
        for (var status : StaffOidcAuthorizationTransaction.Status.values()) {
            if (status != StaffOidcAuthorizationTransaction.Status.CONSUMED) {
                assertThat(service.verify(transaction(status),
                        StaffOidcAuthorizationCallback.authorizationCode(SECRET_CODE)).outcome())
                        .isEqualTo(StaffOidcAuthenticationVerification.Outcome.INVALID_TRANSACTION);
            }
        }
        assertThat(service.verify(null, StaffOidcAuthorizationCallback.authorizationCode(SECRET_CODE)).outcome())
                .isEqualTo(StaffOidcAuthenticationVerification.Outcome.INVALID_TRANSACTION);
        assertThat(calls).hasValue(0);
    }

    @Test void verifierFailureAndMissingResultAreIndeterminate() {
        var callback = StaffOidcAuthorizationCallback.authorizationCode(SECRET_CODE);
        assertThat(new VerifyStaffOidcAuthentication((transaction, ignored) -> null)
                .verify(consumed(), callback).outcome())
                .isEqualTo(StaffOidcAuthenticationVerification.Outcome.INDETERMINATE);
        assertThat(new VerifyStaffOidcAuthentication((transaction, ignored) -> {
            throw new IllegalStateException("private verifier failure");
        }).verify(consumed(), callback).outcome())
                .isEqualTo(StaffOidcAuthenticationVerification.Outcome.INDETERMINATE);
    }

    @Test void callbackAndResultsRedactProtocolSecrets() {
        var callback = StaffOidcAuthorizationCallback.authorizationCode(SECRET_CODE);
        assertThat(callback.toString()).doesNotContain(SECRET_CODE);
        assertThat(StaffOidcAuthorizationCallback.rejected(PROVIDER_ERROR).toString())
                .doesNotContain(PROVIDER_ERROR);
        assertThat(result(StaffOidcAuthenticationVerification.Outcome.INVALID_TOKEN).toString())
                .doesNotContain(SECRET_CODE, PROVIDER_ERROR);
    }

    @Test void orchestrationDoesNotMutateAuthenticationOrGrantAuthorities() {
        var original = new TestingAuthenticationToken("customer", "redacted", "CUSTOMER");
        SecurityContextHolder.getContext().setAuthentication(original);
        var result = new VerifyStaffOidcAuthentication(new UnavailableStaffOidcAuthenticationVerifier())
                .verify(consumed(), StaffOidcAuthorizationCallback.authorizationCode(SECRET_CODE));
        assertThat(result.outcome())
                .isEqualTo(StaffOidcAuthenticationVerification.Outcome.VERIFICATION_UNAVAILABLE);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isSameAs(original);
        assertThat(original.getAuthorities()).extracting(Object::toString).containsExactly("CUSTOMER");
    }

    @Test void contractHasNoPositiveOutcomeOrStaffIdentityPayload() {
        assertThat(Arrays.stream(StaffOidcAuthenticationVerification.Outcome.values())
                .map(Enum::name)).noneMatch(name -> name.equals("VERIFIED") || name.equals("AUTHENTICATED"));
        assertThat(StaffOidcAuthenticationVerification.class.getRecordComponents())
                .extracting(component -> component.getType().getName()).containsExactly(
                        StaffOidcAuthenticationVerification.Outcome.class.getName());
        assertThat(StaffOidcAuthenticationVerifier.class.getMethods())
                .allMatch(method -> method.getReturnType() == StaffOidcAuthenticationVerification.class);
    }

    @Test void verifiedEvidenceAndCustomerIdentityRemainOutsideTheBoundary() {
        assertThat(VerifiedStaffAuthentication.class.getConstructors()).isEmpty();
        assertThat(Arrays.stream(VerifiedStaffAuthentication.class.getDeclaredMethods())
                .noneMatch(method -> Modifier.isPublic(method.getModifiers())
                        && Modifier.isStatic(method.getModifiers()))).isTrue();
        assertThat(Arrays.stream(VerifyStaffOidcAuthentication.class.getMethods())
                .noneMatch(method -> method.getReturnType() == VerifiedStaffAuthentication.class
                        || Arrays.asList(method.getParameterTypes()).contains(CredentialSubject.class))).isTrue();
    }

    private static StaffOidcAuthenticationVerification result(
            StaffOidcAuthenticationVerification.Outcome outcome) {
        return new StaffOidcAuthenticationVerification(outcome);
    }

    private static StaffOidcAuthorizationTransaction consumed() {
        return transaction(StaffOidcAuthorizationTransaction.Status.CONSUMED);
    }

    private static StaffOidcAuthorizationTransaction transaction(
            StaffOidcAuthorizationTransaction.Status status) {
        String verifier = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-._~";
        Instant created = Instant.parse("2026-11-13T00:00:00Z");
        return new StaffOidcAuthorizationTransaction(UUID.fromString("c8cfd836-3857-4aae-94f6-c53ca274fd62"),
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB", verifier,
                StaffOidcAuthorizationTransactions.deriveS256(verifier),
                "00000000-0000-0000-0000-000000000001", "staff-provider",
                created, created.plusSeconds(300), status);
    }
}
