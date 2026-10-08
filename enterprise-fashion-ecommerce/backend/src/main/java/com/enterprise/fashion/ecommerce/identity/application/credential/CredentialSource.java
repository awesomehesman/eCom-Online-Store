package com.enterprise.fashion.ecommerce.identity.application.credential;

import java.util.Objects;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.identity.application.port.out.CredentialSourcePort;
import com.enterprise.fashion.ecommerce.identity.domain.model.CredentialSubject;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Application-owned local transaction boundary. No producer of upstream accepted facts exists.
 * No password comparison, establishment, Login, Session or external effect occurs here.
 * Entry from an ambient transaction is non-confirming: no suspended transaction or hidden
 * partial commit is allowed. Transactions complete before results escape; commit failures are non-confirming too.
 */
public final class CredentialSource {
    private final CredentialSourcePort source;
    private final TransactionOperations transactions;

    public CredentialSource(CredentialSourcePort source, TransactionOperations transactions) {
        this.source = Objects.requireNonNull(source);
        this.transactions = Objects.requireNonNull(transactions);
    }

    public CredentialPublicationResult publish(AcceptedCredentialPublication publication) {
        if (publication == null || TransactionSynchronizationManager.isActualTransactionActive()) {
            return CredentialPublicationResult.UNCERTAIN;
        }
        try {
            CredentialPublicationResult result = transactions.execute(status -> source.publish(publication));
            return result == null ? CredentialPublicationResult.UNCERTAIN : result;
        } catch (RuntimeException failure) {
            // Never expose database details, bindings or verifier values through errors.
            return CredentialPublicationResult.UNCERTAIN;
        }
    }

    // Package-private: the secret handoff is unavailable through the ordinary evidence API.
    CredentialVerifierObservation forVerification(CredentialSubject subject, UUID fact) {
        if (subject == null || fact == null || TransactionSynchronizationManager.isActualTransactionActive()) {
            return CredentialVerifierObservation.nonConfirming(CredentialSourcePort.Observation.INCOMPLETE);
        }
        try {
            CredentialVerifierObservation observed = transactions.execute(status -> source.observeVerifier(subject, fact));
            if (observed == null || (observed.observation() == CredentialSourcePort.Observation.BOUND_APPLICABLE
                    && !observed.boundTo(subject, fact))) {
                return CredentialVerifierObservation.nonConfirming(CredentialSourcePort.Observation.INCOMPLETE);
            }
            return observed;
        } catch (RuntimeException unavailableSource) {
            return CredentialVerifierObservation.nonConfirming(CredentialSourcePort.Observation.INCOMPLETE);
        }
    }

    CredentialEvidence revalidate(CredentialVerifierObservation expected) {
        if (expected == null || !expected.boundTo(expected.subject(), expected.fact())
                || TransactionSynchronizationManager.isActualTransactionActive()) {
            return CredentialEvidence.UNCERTAIN;
        }
        try {
            CredentialEvidence result = transactions.execute(status -> {
                CredentialVerifierObservation current = source.observeVerifier(expected.subject(), expected.fact());
                if (current == null) {
                    return CredentialEvidence.UNCERTAIN;
                }
                return switch (current.observation()) {
                    case BOUND_APPLICABLE -> expected.sameBinding(current)
                            ? CredentialEvidence.CONFIRMED_APPLICABLE : CredentialEvidence.UNCERTAIN;
                    case SUPERSEDED -> CredentialEvidence.NON_CURRENT;
                    case INCOMPATIBLE -> CredentialEvidence.CONFLICT;
                    case INCOMPLETE -> CredentialEvidence.UNCERTAIN;
                };
            });
            return result == null ? CredentialEvidence.UNCERTAIN : result;
        } catch (RuntimeException unavailableSource) {
            return CredentialEvidence.UNCERTAIN;
        }
    }

    public CredentialEvidence confirm(CredentialSubject subject, UUID fact) {
        if (subject == null || fact == null || TransactionSynchronizationManager.isActualTransactionActive()) {
            return CredentialEvidence.UNCERTAIN;
        }
        try {
            CredentialEvidence result = transactions.execute(status -> {
                CredentialSourcePort.Observation observation = source.observe(subject, fact);
                if (observation == null) {
                    return CredentialEvidence.UNCERTAIN;
                }
                return switch (observation) {
                    case BOUND_APPLICABLE -> CredentialEvidence.CONFIRMED_APPLICABLE;
                    case SUPERSEDED -> CredentialEvidence.NON_CURRENT;
                    case INCOMPATIBLE -> CredentialEvidence.CONFLICT;
                    case INCOMPLETE -> CredentialEvidence.UNCERTAIN;
                };
            });
            return result == null ? CredentialEvidence.UNCERTAIN : result;
        } catch (RuntimeException failure) {
            return CredentialEvidence.UNCERTAIN;
        }
    }
}
