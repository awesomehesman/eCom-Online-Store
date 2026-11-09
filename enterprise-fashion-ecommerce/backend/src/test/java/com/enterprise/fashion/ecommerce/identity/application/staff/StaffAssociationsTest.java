package com.enterprise.fashion.ecommerce.identity.application.staff;

import java.lang.reflect.Modifier;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;
import com.enterprise.fashion.ecommerce.identity.application.port.out.StaffAssociationPort;
import com.enterprise.fashion.ecommerce.identity.domain.model.StaffAssociation;
import com.enterprise.fashion.ecommerce.identity.domain.model.StaffIdentityReference;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.TransactionOperations;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class StaffAssociationsTest {
    private final StaffAssociationPort port = mock(StaffAssociationPort.class);
    private final Instant now = Instant.parse("2026-11-09T12:00:00Z");
    private final StaffIdentityReference reference = new StaffIdentityReference("synthetic-authority", "opaque-subject");
    private final StaffAssociations service = new StaffAssociations(port, TransactionOperations.withoutTransaction(), Clock.fixed(now, ZoneOffset.UTC));

    @Test void explicitCreationGeneratesStableLocalIdentityWithoutAuthentication() {
        var created = service.create(reference, "synthetic-accepted-decision");
        assertThat(created.staffPrincipalId()).isNotNull();
        assertThat(created.createdAt()).isEqualTo(now);
        assertThat(created.status()).isEqualTo(StaffAssociation.Status.ACTIVE);
        verify(port).create(created, "synthetic-accepted-decision");
        verifyNoMoreInteractions(port);
    }
    @Test void unknownAndRevokedAreNonConfirming() {
        when(port.find(reference)).thenReturn(Optional.empty());
        assertThat(service.active(reference)).isEmpty();
        when(port.find(reference)).thenReturn(Optional.of(new StaffAssociation(UUID.randomUUID(), reference, now, now)));
        assertThat(service.active(reference)).isEmpty();
    }
    @Test void mismatchedBindingCannotConfirmAnotherSubject() {
        when(port.find(reference)).thenReturn(Optional.of(new StaffAssociation(UUID.randomUUID(), new StaffIdentityReference("other", "subject"), now, null)));
        assertThat(service.active(reference)).isEmpty();
    }
    @Test void explicitRevocationUsesApplicationClock() {
        var id = UUID.randomUUID(); when(port.revoke(id, now, "synthetic-revocation")).thenReturn(true);
        assertThat(service.revoke(id, "synthetic-revocation")).isTrue();
        verify(port).revoke(id, now, "synthetic-revocation");
    }
    @Test void invalidProvenanceAndReferenceFailBeforePersistence() {
        assertThatIllegalArgumentException().isThrownBy(() -> service.create(reference, " "));
        assertThatIllegalArgumentException().isThrownBy(() -> new StaffIdentityReference("", "subject"));
        assertThatIllegalArgumentException().isThrownBy(() -> new StaffIdentityReference("authority", "bad\nsubject"));
        verifyNoInteractions(port);
    }
    @Test void creationAndRevocationAreNotPublicProvisioningApis() throws Exception {
        assertThat(Modifier.isPublic(StaffAssociations.class.getDeclaredMethod("create", StaffIdentityReference.class, String.class).getModifiers())).isFalse();
        assertThat(Modifier.isPublic(StaffAssociations.class.getDeclaredMethod("revoke", UUID.class, String.class).getModifiers())).isFalse();
        assertThat(reference.toString()).doesNotContain("opaque-subject", "synthetic-authority");
    }
    @Test void unavailablePersistenceCannotProducePositiveEvidence() {
        when(port.find(reference)).thenThrow(new IllegalStateException("unavailable"));
        assertThatThrownBy(() -> service.active(reference)).isInstanceOf(IllegalStateException.class);
    }
}
