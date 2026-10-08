package com.enterprise.fashion.ecommerce.identity.adapter.out.credential.source;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.identity.adapter.out.credential.GovernedArgon2VerifierProfile;
import com.enterprise.fashion.ecommerce.identity.application.credential.AcceptedCredentialPublication;
import com.enterprise.fashion.ecommerce.identity.application.credential.CredentialPublicationResult;
import com.enterprise.fashion.ecommerce.identity.application.port.out.CredentialSourcePort;
import com.enterprise.fashion.ecommerce.identity.domain.model.CredentialSubject;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Spring Data JDBC owns immutable fact insertion/loading. The complementary JdbcClient use is
 * confined to PostgreSQL coordination-slot insertion and row locking (ADR-0018).
 * Lock order: one subject/context slot, then its facts. Reads never create a slot.
 * Runtime access must remain Identity-owned/least-privileged; out-of-band privileged alteration
 * is outside these controls and cannot be detected from an identically forged row.
 */
@Repository
public class JdbcCredentialSourceAdapter implements CredentialSourcePort {
    private static final String CONTEXT = "LOCAL_CUSTOMER_PASSWORD";
    private final JdbcClient jdbc;
    private final JdbcAggregateTemplate aggregates;
    private final CredentialPublicationRepository publications;

    public JdbcCredentialSourceAdapter(JdbcClient jdbc, JdbcAggregateTemplate aggregates,
            CredentialPublicationRepository publications) {
        this.jdbc = jdbc;
        this.aggregates = aggregates;
        this.publications = publications;
    }

    @Override
    public CredentialPublicationResult publish(AcceptedCredentialPublication publication) {
        requireTransaction();
        Objects.requireNonNull(publication);
        if (!GovernedArgon2VerifierProfile.accepts(publication.verifier())) {
            return CredentialPublicationResult.UNCERTAIN;
        }
        UUID subject = publication.subject().value();
        jdbc.sql("""
                INSERT INTO identity.credential_source_scope(subject, context) VALUES (:subject, :context)
                ON CONFLICT (subject, context) DO NOTHING
                """).param("subject", subject).param("context", CONTEXT).update();
        if (!lock(subject)) {
            throw new IllegalStateException("Credential source coordination unavailable");
        }
        var existing = publications.findById(publication.fact());
        if (existing.isPresent()) {
            CredentialPublicationRow row = existing.get();
            return row.subject().equals(subject) && row.context().equals(CONTEXT)
                    && row.verifier().equals(publication.verifier())
                    && Objects.equals(row.supersedes(), publication.supersedes())
                    ? CredentialPublicationResult.DUPLICATE : CredentialPublicationResult.CONFLICT;
        }
        if (publication.supersedes() != null) {
            var previous = publications.findById(publication.supersedes());
            if (previous.isEmpty()) {
                return CredentialPublicationResult.UNCERTAIN;
            }
            if (!previous.get().subject().equals(subject) || !previous.get().context().equals(CONTEXT)) {
                return CredentialPublicationResult.CONFLICT;
            }
        }
        aggregates.insert(new CredentialPublicationRow(publication.fact(), subject, CONTEXT,
                publication.verifier(), publication.supersedes()));
        return CredentialPublicationResult.PUBLISHED;
    }

    @Override
    public Observation observe(CredentialSubject subject, UUID fact) {
        requireTransaction();
        if (!lock(subject.value())) {
            return Observation.INCOMPLETE;
        }
        // Separate statement AFTER lock acquisition: READ_COMMITTED sees the preceding writer's
        // committed effect even when this transaction waited for its lock. No cached snapshot.
        List<CredentialPublicationRow> rows = publications.forSubject(subject.value(), CONTEXT);
        var requested = rows.stream().filter(row -> row.fact().equals(fact)).findFirst();
        if (requested.isEmpty()) {
            return Observation.INCOMPLETE;
        }
        Set<UUID> facts = new HashSet<>();
        Set<UUID> superseded = new HashSet<>();
        for (CredentialPublicationRow row : rows) {
            if (!facts.add(row.fact()) || !GovernedArgon2VerifierProfile.accepts(row.verifier())
                    || !row.subject().equals(subject.value()) || !CONTEXT.equals(row.context())) {
                return Observation.INCOMPLETE;
            }
            if (row.supersedes() != null) {
                superseded.add(row.supersedes());
            }
        }
        if (!facts.containsAll(superseded)) {
            return Observation.INCOMPLETE;
        }
        if (superseded.contains(fact)) {
            return Observation.SUPERSEDED;
        }
        facts.removeAll(superseded);
        return facts.size() == 1 ? Observation.BOUND_APPLICABLE : Observation.INCOMPATIBLE;
    }

    private boolean lock(UUID subject) {
        return jdbc.sql("""
                SELECT subject FROM identity.credential_source_scope
                WHERE subject = :subject AND context = :context FOR UPDATE
                """).param("subject", subject).param("context", CONTEXT).query(UUID.class)
                .optional().isPresent();
    }

    private void requireTransaction() {
        if (!TransactionSynchronizationManager.isActualTransactionActive()
                || !Integer.valueOf(TransactionDefinition.ISOLATION_READ_COMMITTED).equals(
                        TransactionSynchronizationManager.getCurrentTransactionIsolationLevel())) {
            throw new IllegalStateException("Credential source requires its application transaction");
        }
    }
}
