package com.enterprise.fashion.ecommerce.identity.application.staff;

import java.lang.reflect.Modifier;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import com.enterprise.fashion.ecommerce.identity.adapter.out.staff.UnavailableStaffOidcAuthenticationVerifier;
import com.enterprise.fashion.ecommerce.identity.application.port.out.StaffOidcAuthenticationVerifier;
import com.enterprise.fashion.ecommerce.identity.domain.model.CredentialSubject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class VerifyStaffOidcAuthenticationTest {
    private static final String STATE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String SESSION = "00000000-0000-0000-0000-000000000001";
    private static final String SECRET_CODE = "opaque-authorization-code";
    private static final String PROVIDER_ERROR = "private-provider-error";

    private final StaffOidcAuthorizationTransactions transactions = mock(StaffOidcAuthorizationTransactions.class);
    private final StaffOidcAuthenticationVerifier verifier = mock(StaffOidcAuthenticationVerifier.class);
    private final VerifyStaffOidcAuthentication service = new VerifyStaffOidcAuthentication(transactions, verifier);

    @AfterEach void clearContext() { SecurityContextHolder.clearContext(); }

    @Test void publicVerificationApiCannotAcceptACallerConstructedTransaction() {
        assertThat(Arrays.stream(VerifyStaffOidcAuthentication.class.getMethods())
                .filter(method -> method.getName().equals("verify")))
                .allMatch(method -> Arrays.stream(method.getParameterTypes())
                        .noneMatch(StaffOidcAuthorizationTransaction.class::equals));
    }

    @Test void onlyTheExactDirectConsumptionResultReachesTheVerifier() {
        var consumed = transaction(StaffOidcAuthorizationTransaction.Status.CONSUMED);
        var callback = StaffOidcAuthorizationCallback.authorizationCode(SECRET_CODE);
        when(transactions.consume(STATE, SESSION)).thenReturn(consumption(
                StaffOidcAuthorizationTransactions.ConsumeOutcome.CONSUMED, consumed));
        when(verifier.verify(consumed, callback)).thenReturn(
                result(StaffOidcAuthenticationVerification.Outcome.EXCHANGE_REJECTED));

        assertThat(service.verify(STATE, SESSION, callback).outcome())
                .isEqualTo(StaffOidcAuthenticationVerification.Outcome.EXCHANGE_REJECTED);
        verify(transactions).consume(STATE, SESSION);
        verify(verifier).verify(same(consumed), same(callback));
    }

    @Test void everyNonConsumedV16OutcomeFailsClosedWithoutVerifierInvocation() {
        Map<StaffOidcAuthorizationTransactions.ConsumeOutcome, StaffOidcAuthenticationVerification.Outcome> expected =
                Map.of(
                        StaffOidcAuthorizationTransactions.ConsumeOutcome.REJECTED,
                        StaffOidcAuthenticationVerification.Outcome.TRANSACTION_REJECTED,
                        StaffOidcAuthorizationTransactions.ConsumeOutcome.REPLAYED,
                        StaffOidcAuthenticationVerification.Outcome.TRANSACTION_REPLAYED,
                        StaffOidcAuthorizationTransactions.ConsumeOutcome.EXPIRED,
                        StaffOidcAuthenticationVerification.Outcome.TRANSACTION_EXPIRED,
                        StaffOidcAuthorizationTransactions.ConsumeOutcome.SUPERSEDED,
                        StaffOidcAuthenticationVerification.Outcome.TRANSACTION_SUPERSEDED,
                        StaffOidcAuthorizationTransactions.ConsumeOutcome.CORRUPT,
                        StaffOidcAuthenticationVerification.Outcome.TRANSACTION_CORRUPT,
                        StaffOidcAuthorizationTransactions.ConsumeOutcome.UNAVAILABLE,
                        StaffOidcAuthenticationVerification.Outcome.TRANSACTION_UNAVAILABLE);
        var callback = StaffOidcAuthorizationCallback.authorizationCode(SECRET_CODE);
        expected.forEach((consumptionOutcome, verificationOutcome) -> {
            reset(transactions, verifier);
            when(transactions.consume(STATE, SESSION)).thenReturn(consumption(consumptionOutcome, null));
            assertThat(service.verify(STATE, SESSION, callback).outcome()).isEqualTo(verificationOutcome);
            verifyNoInteractions(verifier);
        });
    }

    @Test void rejectedProviderCallbackConsumesStateButNeverInvokesVerifier() {
        var consumed = transaction(StaffOidcAuthorizationTransaction.Status.CONSUMED);
        when(transactions.consume(STATE, SESSION)).thenReturn(consumption(
                StaffOidcAuthorizationTransactions.ConsumeOutcome.CONSUMED, consumed));

        assertThat(service.verify(STATE, SESSION,
                StaffOidcAuthorizationCallback.rejected(PROVIDER_ERROR)).outcome())
                .isEqualTo(StaffOidcAuthenticationVerification.Outcome.REJECTED_CALLBACK);
        verify(transactions).consume(STATE, SESSION);
        verifyNoInteractions(verifier);
    }

    @Test void missingCallbackIsRejectedBeforeStateConsumption() {
        assertThat(service.verify(STATE, SESSION, null).outcome())
                .isEqualTo(StaffOidcAuthenticationVerification.Outcome.REJECTED_CALLBACK);
        verifyNoInteractions(transactions, verifier);
    }

    @Test void inconsistentConsumedResultFailsClosedWithoutVerifierInvocation() {
        var invalid = transaction(StaffOidcAuthorizationTransaction.Status.ACTIVE);
        var inconsistent = mock(StaffOidcAuthorizationTransactions.Result.class);
        when(inconsistent.outcome()).thenReturn(StaffOidcAuthorizationTransactions.ConsumeOutcome.CONSUMED);
        when(inconsistent.transaction()).thenReturn(invalid);
        when(transactions.consume(STATE, SESSION)).thenReturn(inconsistent);

        assertThat(service.verify(STATE, SESSION,
                StaffOidcAuthorizationCallback.authorizationCode(SECRET_CODE)).outcome())
                .isEqualTo(StaffOidcAuthenticationVerification.Outcome.INVALID_TRANSACTION);
        verifyNoInteractions(verifier);
    }

    @Test void unavailableConsumptionAndVerifierFailuresFailClosed() {
        var callback = StaffOidcAuthorizationCallback.authorizationCode(SECRET_CODE);
        when(transactions.consume(STATE, SESSION)).thenReturn(null);
        assertThat(service.verify(STATE, SESSION, callback).outcome())
                .isEqualTo(StaffOidcAuthenticationVerification.Outcome.TRANSACTION_UNAVAILABLE);
        when(transactions.consume(STATE, SESSION)).thenThrow(new IllegalStateException("private storage failure"));
        assertThat(service.verify(STATE, SESSION, callback).outcome())
                .isEqualTo(StaffOidcAuthenticationVerification.Outcome.TRANSACTION_UNAVAILABLE);

        var consumed = transaction(StaffOidcAuthorizationTransaction.Status.CONSUMED);
        reset(transactions);
        when(transactions.consume(STATE, SESSION)).thenReturn(consumption(
                StaffOidcAuthorizationTransactions.ConsumeOutcome.CONSUMED, consumed));
        when(verifier.verify(consumed, callback)).thenReturn(null);
        assertThat(service.verify(STATE, SESSION, callback).outcome())
                .isEqualTo(StaffOidcAuthenticationVerification.Outcome.INDETERMINATE);
        when(verifier.verify(consumed, callback)).thenThrow(new IllegalStateException("private failure"));
        assertThat(service.verify(STATE, SESSION, callback).outcome())
                .isEqualTo(StaffOidcAuthenticationVerification.Outcome.INDETERMINATE);
    }

    @Test void unavailableVerifierRemainsFailClosed() {
        var consumed = transaction(StaffOidcAuthorizationTransaction.Status.CONSUMED);
        when(transactions.consume(STATE, SESSION)).thenReturn(consumption(
                StaffOidcAuthorizationTransactions.ConsumeOutcome.CONSUMED, consumed));
        var dormant = new VerifyStaffOidcAuthentication(
                transactions, new UnavailableStaffOidcAuthenticationVerifier());
        assertThat(dormant.verify(STATE, SESSION,
                StaffOidcAuthorizationCallback.authorizationCode(SECRET_CODE)).outcome())
                .isEqualTo(StaffOidcAuthenticationVerification.Outcome.VERIFICATION_UNAVAILABLE);
    }

    @Test void malformedCallbacksAndDiagnosticsDoNotDiscloseSecrets() {
        assertThatThrownBy(() -> StaffOidcAuthorizationCallback.authorizationCode(" \nprivate"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid authorization callback")
                .hasMessageNotContaining("private");
        assertThat(StaffOidcAuthorizationCallback.authorizationCode(SECRET_CODE).toString())
                .doesNotContain(SECRET_CODE);
        assertThat(StaffOidcAuthorizationCallback.rejected(PROVIDER_ERROR).toString())
                .doesNotContain(PROVIDER_ERROR);
    }

    @Test void orchestrationDoesNotMutateAuthenticationOrGrantAuthorities() {
        var original = new TestingAuthenticationToken("customer", "redacted", "CUSTOMER");
        SecurityContextHolder.getContext().setAuthentication(original);
        when(transactions.consume(STATE, SESSION)).thenReturn(consumption(
                StaffOidcAuthorizationTransactions.ConsumeOutcome.REJECTED, null));

        service.verify(STATE, SESSION, StaffOidcAuthorizationCallback.authorizationCode(SECRET_CODE));

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isSameAs(original);
        assertThat(original.getAuthorities()).extracting(Object::toString).containsExactly("CUSTOMER");
    }

    @Test void contractHasNoPositiveEvidenceOrCustomerIdentityPath() {
        assertThat(Arrays.stream(StaffOidcAuthenticationVerification.Outcome.values())
                .map(Enum::name)).noneMatch(name -> name.equals("VERIFIED") || name.equals("AUTHENTICATED"));
        assertThat(StaffOidcAuthenticationVerification.class.getRecordComponents())
                .extracting(component -> component.getType().getName()).containsExactly(
                        StaffOidcAuthenticationVerification.Outcome.class.getName());
        assertThat(VerifiedStaffAuthentication.class.getConstructors()).isEmpty();
        assertThat(Arrays.stream(VerifiedStaffAuthentication.class.getDeclaredMethods())
                .noneMatch(method -> Modifier.isPublic(method.getModifiers())
                        && Modifier.isStatic(method.getModifiers()))).isTrue();
        assertThat(Arrays.stream(VerifyStaffOidcAuthentication.class.getMethods())
                .noneMatch(method -> method.getReturnType() == VerifiedStaffAuthentication.class
                        || Arrays.asList(method.getParameterTypes()).contains(CredentialSubject.class))).isTrue();
    }

    private static StaffOidcAuthorizationTransactions.Result consumption(
            StaffOidcAuthorizationTransactions.ConsumeOutcome outcome,
            StaffOidcAuthorizationTransaction transaction) {
        return new StaffOidcAuthorizationTransactions.Result(outcome, transaction);
    }

    private static StaffOidcAuthenticationVerification result(
            StaffOidcAuthenticationVerification.Outcome outcome) {
        return new StaffOidcAuthenticationVerification(outcome);
    }

    private static StaffOidcAuthorizationTransaction transaction(
            StaffOidcAuthorizationTransaction.Status status) {
        String verifier = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-._~";
        Instant created = Instant.parse("2026-11-13T00:00:00Z");
        return new StaffOidcAuthorizationTransaction(UUID.fromString("c8cfd836-3857-4aae-94f6-c53ca274fd62"),
                STATE, "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB", verifier,
                StaffOidcAuthorizationTransactions.deriveS256(verifier), SESSION, "staff-provider",
                created, created.plusSeconds(300), status);
    }
}
