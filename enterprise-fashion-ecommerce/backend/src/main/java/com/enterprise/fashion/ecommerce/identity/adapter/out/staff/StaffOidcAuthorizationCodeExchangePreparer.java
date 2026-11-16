package com.enterprise.fashion.ecommerce.identity.adapter.out.staff;

import java.net.URI;
import java.time.Duration;
import java.util.Objects;
import com.enterprise.fashion.ecommerce.identity.application.staff.StaffOidcAuthenticationVerification;
import com.enterprise.fashion.ecommerce.identity.application.staff.StaffOidcAuthorizationCallback;
import com.enterprise.fashion.ecommerce.identity.application.staff.StaffOidcAuthorizationTransaction;

/**
 * Dormant preparation of one exact-registration authorization-code exchange request. A CONSUMED
 * status does not independently prove atomic transaction consumption, and the callback carries no
 * state with which this component could independently establish callback provenance. Operational
 * orchestration must preserve those boundaries before invoking any future exchange implementation.
 */
final class StaffOidcAuthorizationCodeExchangePreparer {
    static final class ExchangeRequest {
        private final String registrationId;
        private final long registrationRevision;
        private final URI tokenEndpointUri;
        private final String clientId;
        private final URI redirectUri;
        private final StaffOidcProviderRegistration.ClientAuthenticationMethod clientAuthenticationMethod;
        private final String authorizationCode;
        private final String pkceVerifier;
        private final Duration connectionTimeout;
        private final Duration responseTimeout;
        private final int maximumResponseSize;

        private ExchangeRequest(StaffOidcProviderRegistration registration,
                String authorizationCode, String pkceVerifier) {
            this.registrationId = registration.registrationId();
            this.registrationRevision = registration.revision();
            this.tokenEndpointUri = registration.tokenEndpointUri();
            this.clientId = registration.clientId();
            this.redirectUri = registration.redirectUri();
            this.clientAuthenticationMethod = registration.clientAuthenticationMethod();
            this.authorizationCode = authorizationCode;
            this.pkceVerifier = pkceVerifier;
            this.connectionTimeout = registration.connectionTimeout();
            this.responseTimeout = registration.responseTimeout();
            this.maximumResponseSize = registration.maximumResponseSize();
        }

        String registrationId() { return registrationId; }
        long registrationRevision() { return registrationRevision; }
        URI tokenEndpointUri() { return tokenEndpointUri; }
        String clientId() { return clientId; }
        URI redirectUri() { return redirectUri; }
        StaffOidcProviderRegistration.ClientAuthenticationMethod clientAuthenticationMethod() {
            return clientAuthenticationMethod;
        }
        String authorizationCode() { return authorizationCode; }
        String pkceVerifier() { return pkceVerifier; }
        Duration connectionTimeout() { return connectionTimeout; }
        Duration responseTimeout() { return responseTimeout; }
        int maximumResponseSize() { return maximumResponseSize; }

        @Override public String toString() {
            return "StaffOidcAuthorizationCodeExchangeRequest[redacted]";
        }
    }

    record Result(ExchangeRequest exchangeRequest,
            StaffOidcAuthenticationVerification.Outcome failure) {
        Result {
            if ((exchangeRequest == null) == (failure == null)) {
                throw new IllegalArgumentException("Exactly one exchange-preparation result is required");
            }
        }

        static Result prepared(ExchangeRequest request) {
            return new Result(Objects.requireNonNull(request), null);
        }

        static Result rejected(StaffOidcAuthenticationVerification.Outcome failure) {
            return new Result(null, Objects.requireNonNull(failure));
        }

        boolean prepared() { return exchangeRequest != null; }

        @Override public String toString() {
            return prepared() ? "StaffOidcAuthorizationCodeExchangePreparation[internal-success]"
                    : "StaffOidcAuthorizationCodeExchangePreparation[" + failure + "]";
        }
    }

    private final StaffOidcProviderRegistrationResolver registrations;

    StaffOidcAuthorizationCodeExchangePreparer(StaffOidcProviderRegistrationResolver registrations) {
        this.registrations = Objects.requireNonNull(registrations, "Provider registrations are required");
    }

    Result prepare(StaffOidcAuthorizationTransaction consumedTransaction,
            StaffOidcAuthorizationCallback callback) {
        if (consumedTransaction == null
                || consumedTransaction.status() != StaffOidcAuthorizationTransaction.Status.CONSUMED) {
            return rejected(StaffOidcAuthenticationVerification.Outcome.INVALID_TRANSACTION);
        }
        if (callback == null || callback.kind() != StaffOidcAuthorizationCallback.Kind.AUTHORIZATION_CODE) {
            return rejected(StaffOidcAuthenticationVerification.Outcome.REJECTED_CALLBACK);
        }

        StaffOidcProviderRegistrationResolver.Resolution resolution;
        try {
            resolution = registrations.resolve(consumedTransaction.registrationId(),
                    consumedTransaction.registrationRevision());
        } catch (RuntimeException unavailable) {
            return rejected(StaffOidcAuthenticationVerification.Outcome.INDETERMINATE);
        }
        if (resolution == null || resolution.outcome() == null) {
            return rejected(StaffOidcAuthenticationVerification.Outcome.INDETERMINATE);
        }
        if (resolution.outcome() != StaffOidcProviderRegistrationResolver.Outcome.RESOLVED) {
            return rejected(StaffOidcAuthenticationVerification.Outcome.VERIFICATION_UNAVAILABLE);
        }

        StaffOidcProviderRegistration registration = resolution.registration();
        if (registration == null
                || !registration.registrationId().equals(consumedTransaction.registrationId())
                || registration.revision() != consumedTransaction.registrationRevision()) {
            return rejected(StaffOidcAuthenticationVerification.Outcome.INDETERMINATE);
        }
        try {
            return Result.prepared(new ExchangeRequest(registration, callback.value(),
                    consumedTransaction.pkceVerifier()));
        } catch (RuntimeException unavailable) {
            return rejected(StaffOidcAuthenticationVerification.Outcome.INDETERMINATE);
        }
    }

    @Override public String toString() {
        return "StaffOidcAuthorizationCodeExchangePreparer[redacted]";
    }

    private static Result rejected(StaffOidcAuthenticationVerification.Outcome failure) {
        return Result.rejected(failure);
    }
}
