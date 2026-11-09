package com.enterprise.fashion.ecommerce.identity.application.staff;

import java.util.UUID;
import java.time.Clock;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import com.enterprise.fashion.ecommerce.identity.adapter.out.staff.JdbcStaffAssociationAdapter;
import com.enterprise.fashion.ecommerce.identity.application.port.out.StaffAssociationPort;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import static org.mockito.Mockito.*;
import com.enterprise.fashion.ecommerce.identity.domain.model.StaffIdentityReference;
import com.enterprise.fashion.ecommerce.identity.domain.model.CredentialSubject;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.*;

@Tag("integration")
@SpringBootTest(properties = "product.catalogue.cursor-secret=AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
@Testcontainers
class StaffAssociationsIntegrationTest {
    @Container @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:18");
    @Autowired private StaffAssociations service;
    @Autowired private JdbcClient jdbc;
    @Autowired private PlatformTransactionManager manager;
    private StaffIdentityReference reference() { return new StaffIdentityReference("synthetic-authority", UUID.randomUUID().toString()); }

    @Test void explicitAssociationPersistsAndRevocationDeniesWithoutErasingHistory() {
        var reference = reference();
        assertThat(service.active(reference)).isEmpty();
        var created = service.create(reference, "synthetic-provisioning-decision");
        assertThat(service.active(reference).orElseThrow().staffPrincipalId()).isEqualTo(created.staffPrincipalId());
        assertThat(service.revoke(created.staffPrincipalId(), "synthetic-revocation-decision")).isTrue();
        assertThat(service.active(reference)).isEmpty();
        assertThat(service.revoke(created.staffPrincipalId(), "synthetic-repeated-revocation")).isFalse();
        assertThat(jdbc.sql("SELECT revoked_at IS NOT NULL FROM identity.staff_association WHERE staff_principal_id = :id")
                .param("id", created.staffPrincipalId()).query(Boolean.class).single()).isTrue();
        assertThat(jdbc.sql("SELECT action FROM identity.staff_association_event WHERE staff_principal_id = :id ORDER BY action")
                .param("id", created.staffPrincipalId()).query(String.class).list()).containsExactly("CREATED", "REVOKED");
        assertThatThrownBy(() -> service.create(reference, "synthetic-rebinding-attempt")).isInstanceOf(DataIntegrityViolationException.class);
    }
    @Test void duplicateBindingIsRejectedWithoutOverwritingOrOrphanHistory() {
        var reference = reference(); var first = service.create(reference, "synthetic-accepted-decision");
        assertThatThrownBy(() -> service.create(reference, "synthetic-conflicting-decision")).isInstanceOf(DataIntegrityViolationException.class);
        assertThat(service.active(reference).orElseThrow().staffPrincipalId()).isEqualTo(first.staffPrincipalId());
        assertThat(jdbc.sql("SELECT count(*) FROM identity.staff_association_event WHERE staff_principal_id = :id")
                .param("id", first.staffPrincipalId()).query(Integer.class).single()).isEqualTo(1);
    }
    @Test void namespaceAndCustomerCredentialSubjectCannotInferStaffAssociation() {
        var subject = new CredentialSubject(UUID.randomUUID());
        var reference = new StaffIdentityReference("synthetic-staff-source", subject.value().toString());
        assertThat(service.active(reference)).isEmpty();
        service.create(reference, "synthetic-explicit-association");
        assertThat(service.active(new StaffIdentityReference("synthetic-customer-source", reference.subject()))).isEmpty();
        assertThat(service.active(new StaffIdentityReference("SYNTHETIC-STAFF-SOURCE", reference.subject()))).isEmpty();
        assertThat(service.revoke(UUID.randomUUID(), "synthetic-unknown-revocation")).isFalse();
    }
    @Test void databaseRejectsReactivationAndHistoryRewriting() {
        var association = service.create(reference(), "synthetic-create");
        assertThat(service.revoke(association.staffPrincipalId(), "synthetic-revoke")).isTrue();
        assertThatThrownBy(() -> jdbc.sql("UPDATE identity.staff_association SET revoked_at = NULL WHERE staff_principal_id = :id")
                .param("id", association.staffPrincipalId()).update()).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> jdbc.sql("DELETE FROM identity.staff_association_event WHERE staff_principal_id = :id")
                .param("id", association.staffPrincipalId()).update()).isInstanceOf(RuntimeException.class);
        assertThat(service.active(association.identity())).isEmpty();
    }
    @Test void competingCreationAcceptsExactlyOneBinding() throws Exception {
        var reference = reference();
        var start = new CountDownLatch(1);
        try (var workers = Executors.newFixedThreadPool(2)) {
            java.util.concurrent.Callable<Boolean> create = () -> {
                if (!start.await(10, TimeUnit.SECONDS)) throw new IllegalStateException("Test start timed out");
                try { service.create(reference, "synthetic-concurrent-decision"); return true; }
                catch (DataIntegrityViolationException conflict) { return false; }
            };
            var first = workers.submit(create); var second = workers.submit(create); start.countDown();
            assertThat(java.util.List.of(first.get(20, TimeUnit.SECONDS), second.get(20, TimeUnit.SECONDS)))
                    .containsExactlyInAnyOrder(true, false);
        }
        var accepted = service.active(reference).orElseThrow();
        assertThat(jdbc.sql("SELECT count(*) FROM identity.staff_association_event WHERE staff_principal_id = :id")
                .param("id", accepted.staffPrincipalId()).query(Integer.class).single()).isEqualTo(1);
    }
    @Test void lifecycleRecordFailureRollsBackAssociationCreation() {
        var reference = reference();
        var adapter = new JdbcStaffAssociationAdapter(jdbc);
        var failing = mock(StaffAssociationPort.class);
        doAnswer(call -> {
            // Database rejects the overlong journal field after the association insert.
            adapter.create(call.getArgument(0), "x".repeat(257));
            return null;
        }).when(failing).create(any(), anyString());
        var isolated = new StaffAssociations(failing, new TransactionTemplate(manager), Clock.systemUTC());
        assertThatThrownBy(() -> isolated.create(reference, "synthetic-valid-decision"))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThat(service.active(reference)).isEmpty();
        assertThat(jdbc.sql("SELECT count(*) FROM identity.staff_association WHERE authority = :authority AND subject = :subject")
                .param("authority", reference.authority()).param("subject", reference.subject()).query(Integer.class).single()).isZero();
    }

}
