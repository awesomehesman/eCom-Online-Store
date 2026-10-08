package com.enterprise.fashion.ecommerce.identity.application.loginemail;

import java.util.Objects;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.identity.application.port.out.CurrentLoginEmailSourcePort;
import com.enterprise.fashion.ecommerce.identity.domain.model.CredentialSubject;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/** Dormant source mechanics; no producer of accepted current-email facts exists. */
public final class CurrentLoginEmailSource {
    private final CurrentLoginEmailSourcePort source;
    private final TransactionOperations transactions;

    public CurrentLoginEmailSource(CurrentLoginEmailSourcePort source, TransactionOperations transactions) {
        this.source = Objects.requireNonNull(source);
        this.transactions = Objects.requireNonNull(transactions);
    }

    public LoginEmailPublicationResult publish(AcceptedLoginEmailBinding binding) {
        if (binding == null || TransactionSynchronizationManager.isActualTransactionActive()) {
            return LoginEmailPublicationResult.UNCERTAIN;
        }
        try {
            LoginEmailPublicationResult result = transactions.execute(status -> source.publish(binding));
            return result == null ? LoginEmailPublicationResult.UNCERTAIN : result;
        } catch (RuntimeException failure) {
            return LoginEmailPublicationResult.UNCERTAIN;
        }
    }

    public CurrentLoginEmailObservation observe(CredentialSubject subject, UUID fact) {
        if (subject == null || fact == null || TransactionSynchronizationManager.isActualTransactionActive()) {
            return CurrentLoginEmailObservation.nonConfirming(CurrentLoginEmailObservation.Outcome.UNCERTAIN);
        }
        try {
            CurrentLoginEmailObservation result = transactions.execute(status -> source.observe(subject, fact));
            if (result == null || (result.outcome() == CurrentLoginEmailObservation.Outcome.CURRENT
                    && !result.boundTo(subject, fact))) {
                return CurrentLoginEmailObservation.nonConfirming(CurrentLoginEmailObservation.Outcome.UNCERTAIN);
            }
            return result;
        } catch (RuntimeException failure) {
            return CurrentLoginEmailObservation.nonConfirming(CurrentLoginEmailObservation.Outcome.UNCERTAIN);
        }
    }
}
