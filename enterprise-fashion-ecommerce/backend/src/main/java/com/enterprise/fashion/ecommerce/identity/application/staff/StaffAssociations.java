package com.enterprise.fashion.ecommerce.identity.application.staff;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import com.enterprise.fashion.ecommerce.identity.application.port.out.StaffAssociationPort;
import com.enterprise.fashion.ecommerce.identity.domain.model.StaffAssociation;
import com.enterprise.fashion.ecommerce.identity.domain.model.StaffIdentityReference;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Non-HTTP Identity-internal association lifecycle. Mutation is package-private and has no production caller.
 * Only separately reviewed trusted provisioning code in this package may invoke it. Provenance is an
 * internal accepted decision reference, never a header/claim or proof that an issuer is trusted.
 * Observation supplies no Authentication, Session, Role, Permission or Product Authorization.
 */
public final class StaffAssociations {
    private final StaffAssociationPort source;
    private final TransactionOperations transactions;
    private final Clock clock;
    public StaffAssociations(StaffAssociationPort source, TransactionOperations transactions, Clock clock) {
        this.source = Objects.requireNonNull(source);
        this.transactions = Objects.requireNonNull(transactions);
        this.clock = Objects.requireNonNull(clock);
    }
    StaffAssociation create(StaffIdentityReference identity, String provenance) {
        Objects.requireNonNull(identity);
        validateMutation(provenance);
        var association = new StaffAssociation(UUID.randomUUID(), identity, clock.instant(), null);
        transactions.executeWithoutResult(status -> source.create(association, provenance));
        return association;
    }
    boolean revoke(UUID staffPrincipalId, String provenance) {
        Objects.requireNonNull(staffPrincipalId);
        validateMutation(provenance);
        Instant now = clock.instant();
        return Boolean.TRUE.equals(transactions.execute(status -> source.revoke(staffPrincipalId, now, provenance)));
    }
    public Optional<StaffAssociation> active(StaffIdentityReference identity) {
        Objects.requireNonNull(identity);
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException("Independent Staff association observation required");
        }
        var found = transactions.execute(status -> source.find(identity));
        return found == null ? Optional.empty() : found.filter(item -> item.identity().equals(identity)
                && item.status() == StaffAssociation.Status.ACTIVE);
    }
    private void validateMutation(String provenance) {
        if (provenance == null || provenance.isBlank() || provenance.length() > 256
                || provenance.codePoints().anyMatch(Character::isISOControl)) {
            throw new IllegalArgumentException("Accepted internal provisioning provenance required");
        }
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException("Independent Staff association mutation required");
        }
    }
}
