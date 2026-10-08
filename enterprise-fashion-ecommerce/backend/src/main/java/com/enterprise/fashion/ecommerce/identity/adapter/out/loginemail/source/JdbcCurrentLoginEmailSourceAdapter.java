package com.enterprise.fashion.ecommerce.identity.adapter.out.loginemail.source;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.identity.application.loginemail.AcceptedLoginEmailBinding;
import com.enterprise.fashion.ecommerce.identity.application.loginemail.CurrentLoginEmailObservation;
import com.enterprise.fashion.ecommerce.identity.application.loginemail.LoginEmailPublicationResult;
import com.enterprise.fashion.ecommerce.identity.application.port.out.CurrentLoginEmailSourcePort;
import com.enterprise.fashion.ecommerce.identity.domain.model.CredentialSubject;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Identity-owned immutable facts. A subject slot serializes publication and observation.
 * Reads never create a slot. Runtime writes require a future lawful capability issuer;
 * privileged out-of-band modification cannot be authenticated from an identically forged row.
 */
@Repository
public class JdbcCurrentLoginEmailSourceAdapter implements CurrentLoginEmailSourcePort {
    private final JdbcClient jdbc;
    private final JdbcAggregateTemplate aggregates;
    private final LoginEmailBindingRepository bindings;

    public JdbcCurrentLoginEmailSourceAdapter(JdbcClient jdbc, JdbcAggregateTemplate aggregates,
            LoginEmailBindingRepository bindings) {
        this.jdbc = jdbc;
        this.aggregates = aggregates;
        this.bindings = bindings;
    }

    @Override
    public LoginEmailPublicationResult publish(AcceptedLoginEmailBinding binding) {
        requireTransaction();
        Objects.requireNonNull(binding);
        UUID subject = binding.subject().value();
        jdbc.sql("""
                INSERT INTO identity.login_email_binding_scope(subject) VALUES (:subject)
                ON CONFLICT (subject) DO NOTHING
                """).param("subject", subject).update();
        if (!lock(subject)) {
            throw new IllegalStateException("Login-email source coordination unavailable");
        }
        var existing = bindings.findById(binding.fact());
        if (existing.isPresent()) {
            LoginEmailBindingRow row = existing.get();
            return row.subject().equals(subject) && row.email().equals(binding.email())
                    && Objects.equals(row.supersedes(), binding.supersedes())
                    ? LoginEmailPublicationResult.DUPLICATE : LoginEmailPublicationResult.CONFLICT;
        }
        if (binding.supersedes() != null) {
            var previous = bindings.findById(binding.supersedes());
            if (previous.isEmpty()) {
                return LoginEmailPublicationResult.UNCERTAIN;
            }
            if (!previous.get().subject().equals(subject)) {
                return LoginEmailPublicationResult.CONFLICT;
            }
        }
        aggregates.insert(new LoginEmailBindingRow(binding.fact(), subject, binding.email(), binding.supersedes()));
        return LoginEmailPublicationResult.PUBLISHED;
    }

    @Override
    public CurrentLoginEmailObservation observe(CredentialSubject subject, UUID fact) {
        requireTransaction();
        if (!lock(subject.value())) {
            return uncertain();
        }
        // This statement runs after lock acquisition: READ_COMMITTED sees the committed writer effect.
        List<LoginEmailBindingRow> rows = bindings.forSubject(subject.value());
        if (rows.stream().noneMatch(row -> row.fact().equals(fact))) {
            return uncertain();
        }
        Set<UUID> facts = new HashSet<>();
        Set<UUID> superseded = new HashSet<>();
        for (LoginEmailBindingRow row : rows) {
            if (!facts.add(row.fact()) || !row.subject().equals(subject.value())
                    || row.email() == null || row.email().isBlank()) {
                return uncertain();
            }
            if (row.supersedes() != null) {
                superseded.add(row.supersedes());
            }
        }
        if (!facts.containsAll(superseded)) {
            return uncertain();
        }
        facts.removeAll(superseded);
        if (facts.size() != 1) {
            return CurrentLoginEmailObservation.nonConfirming(CurrentLoginEmailObservation.Outcome.CONFLICT);
        }
        if (!facts.contains(fact)) {
            return CurrentLoginEmailObservation.nonConfirming(CurrentLoginEmailObservation.Outcome.SUPERSEDED);
        }
        LoginEmailBindingRow current = rows.stream().filter(row -> row.fact().equals(fact))
                .findFirst().orElseThrow();
        return CurrentLoginEmailObservation.current(subject, current.fact(), current.email());
    }

    private boolean lock(UUID subject) {
        return jdbc.sql("""
                SELECT subject FROM identity.login_email_binding_scope
                WHERE subject = :subject FOR UPDATE
                """).param("subject", subject).query(UUID.class).optional().isPresent();
    }

    private void requireTransaction() {
        if (!TransactionSynchronizationManager.isActualTransactionActive()
                || !Integer.valueOf(TransactionDefinition.ISOLATION_READ_COMMITTED).equals(
                        TransactionSynchronizationManager.getCurrentTransactionIsolationLevel())) {
            throw new IllegalStateException("Login-email source requires its application transaction");
        }
    }

    private static CurrentLoginEmailObservation uncertain() {
        return CurrentLoginEmailObservation.nonConfirming(CurrentLoginEmailObservation.Outcome.UNCERTAIN);
    }
}
