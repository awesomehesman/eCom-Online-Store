package com.enterprise.fashion.ecommerce.identity.adapter.out.staff;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import com.enterprise.fashion.ecommerce.identity.application.port.out.StaffAssociationPort;
import com.enterprise.fashion.ecommerce.identity.domain.model.StaffAssociation;
import com.enterprise.fashion.ecommerce.identity.domain.model.StaffIdentityReference;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/** No writes outside an application-owned transaction; unique bindings are retained after revocation. */
public final class JdbcStaffAssociationAdapter implements StaffAssociationPort {
    private final JdbcClient jdbc;
    public JdbcStaffAssociationAdapter(JdbcClient jdbc) { this.jdbc = jdbc; }
    @Override public void create(StaffAssociation association, String provenance) {
        transaction();
        jdbc.sql("INSERT INTO identity.staff_association (staff_principal_id, authority, subject, created_at) VALUES (:id, :authority, :subject, :time)")
                .param("id", association.staffPrincipalId()).param("authority", association.identity().authority())
                .param("subject", association.identity().subject()).param("time", Timestamp.from(association.createdAt())).update();
        event(association.staffPrincipalId(), "CREATED", association.createdAt(), provenance);
    }
    @Override public boolean revoke(UUID id, Instant time, String provenance) {
        transaction();
        int changed = jdbc.sql("UPDATE identity.staff_association SET revoked_at = :time WHERE staff_principal_id = :id AND revoked_at IS NULL")
                .param("id", id).param("time", Timestamp.from(time)).update();
        if (changed == 1) event(id, "REVOKED", time, provenance);
        return changed == 1;
    }
    @Override public Optional<StaffAssociation> find(StaffIdentityReference reference) {
        transaction();
        return jdbc.sql("SELECT staff_principal_id, authority, subject, created_at, revoked_at FROM identity.staff_association WHERE authority = :authority AND subject = :subject")
                .param("authority", reference.authority()).param("subject", reference.subject())
                .query((row, index) -> new StaffAssociation(row.getObject("staff_principal_id", UUID.class),
                        new StaffIdentityReference(row.getString("authority"), row.getString("subject")),
                        row.getTimestamp("created_at").toInstant(), row.getTimestamp("revoked_at") == null ? null : row.getTimestamp("revoked_at").toInstant())).optional();
    }
    private void event(UUID id, String action, Instant time, String provenance) {
        jdbc.sql("INSERT INTO identity.staff_association_event (staff_principal_id, action, occurred_at, provenance) VALUES (:id, :action, :time, :provenance)")
                .param("id", id).param("action", action).param("time", Timestamp.from(time)).param("provenance", provenance).update();
    }
    private void transaction() {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) throw new IllegalStateException("Application transaction required");
    }
}
