package com.enterprise.fashion.ecommerce.identity.application.staff;

import java.lang.reflect.Modifier;
import java.time.Clock;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import com.enterprise.fashion.ecommerce.identity.application.port.out.StaffAssociationPort;
import com.enterprise.fashion.ecommerce.identity.domain.model.CredentialSubject;
import com.enterprise.fashion.ecommerce.identity.domain.model.StaffAssociation;
import com.enterprise.fashion.ecommerce.identity.domain.model.StaffIdentityReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.support.TransactionOperations;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResolveAuthenticatedStaffTest {
    private final StaffAssociationPort source = mock(StaffAssociationPort.class);
    private final StaffAssociations associations = new StaffAssociations(source, TransactionOperations.withoutTransaction(), Clock.systemUTC());
    private final ResolveAuthenticatedStaff resolver = new ResolveAuthenticatedStaff(associations);
    private final StaffIdentityReference identity = new StaffIdentityReference("synthetic-staff-source", "synthetic-subject");
    private final UUID id = UUID.randomUUID();
    private final Instant created = Instant.parse("2026-11-10T00:00:00Z");

    /** Reflection mirrors existing synthetic credential tests; never an available production issuer. */
    private VerifiedStaffAuthentication syntheticEvidence() throws Exception {
        var constructor = VerifiedStaffAuthentication.class.getDeclaredConstructor(StaffIdentityReference.class);
        constructor.setAccessible(true);
        return constructor.newInstance(identity);
    }

    @Test void activeAssociationResolvesOnlyAnExistingLocalReferenceWithoutSecurityContextEffects() throws Exception {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        when(source.find(identity)).thenReturn(Optional.of(new StaffAssociation(id, identity, created, null)));
        var result = resolver.resolve(syntheticEvidence());
        assertThat(result.outcome()).isEqualTo(ResolveAuthenticatedStaff.Outcome.RESOLVED);
        assertThat(result.staffPrincipalId()).isEqualTo(id);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isSameAs(authentication);
        verify(source).find(identity); verifyNoMoreInteractions(source);
        assertThat(result.toString()).doesNotContain(id.toString(), identity.subject());
    }
    @Test void unknownAssociationIsRejected() throws Exception {
        when(source.find(identity)).thenReturn(Optional.empty());
        var result = resolver.resolve(syntheticEvidence());
        assertThat(result.outcome()).isEqualTo(ResolveAuthenticatedStaff.Outcome.REJECTED);
        assertThat(result.staffPrincipalId()).isNull();
        verify(source).find(identity); verifyNoMoreInteractions(source);
    }
    @Test void revokedAssociationIsRejected() throws Exception {
        when(source.find(identity)).thenReturn(Optional.of(new StaffAssociation(id, identity, created, created)));
        var result = resolver.resolve(syntheticEvidence());
        assertThat(result.outcome()).isEqualTo(ResolveAuthenticatedStaff.Outcome.REJECTED);
        assertThat(result.staffPrincipalId()).isNull();
    }
    @Test void mismatchedAssociationCannotResolve() throws Exception {
        when(source.find(identity)).thenReturn(Optional.of(new StaffAssociation(id,
                new StaffIdentityReference("other-source", identity.subject()), created, null)));
        assertThat(resolver.resolve(syntheticEvidence()).staffPrincipalId()).isNull();
    }
    @Test void persistenceFailureIsUnavailableRatherThanAuthenticatedOrConfirmedAbsent() throws Exception {
        when(source.find(identity)).thenThrow(new IllegalStateException("private persistence diagnostic"));
        var result = resolver.resolve(syntheticEvidence());
        assertThat(result.outcome()).isEqualTo(ResolveAuthenticatedStaff.Outcome.UNAVAILABLE);
        assertThat(result.staffPrincipalId()).isNull();
        assertThat(result.toString()).doesNotContain("private", identity.subject());
    }
    @Test void missingEvidenceDoesNotEvenLookUpAnAssociation() {
        assertThat(resolver.resolve(null).outcome()).isEqualTo(ResolveAuthenticatedStaff.Outcome.REJECTED);
        verifyNoInteractions(source);
    }
    @Test void malformedAndIncompleteReferencesCannotBecomeEvidence() {
        assertThatIllegalArgumentException().isThrownBy(() -> new StaffIdentityReference("", "subject"));
        assertThatIllegalArgumentException().isThrownBy(() -> new StaffIdentityReference("authority", null));
        assertThatIllegalArgumentException().isThrownBy(() -> new StaffIdentityReference("authority", "subject\n"));
        verifyNoInteractions(source);
    }
    @Test void callerJsonClaimsCannotConstructVerifiedStaffEvidence() {
        var mapper = new ObjectMapper();
        for (String json : new String[]{"{}", "{\"identity\":{\"authority\":\"synthetic-staff-source\",\"subject\":\"synthetic-subject\"}}",
                "{\"staffAuthenticated\":true,\"mfa\":true}", "{\"customer\":true}"}) {
            assertThatThrownBy(() -> mapper.readValue(json, VerifiedStaffAuthentication.class))
                    .isInstanceOf(com.fasterxml.jackson.core.JsonProcessingException.class);
        }
        verifyNoInteractions(source);
    }
    @Test void customerReferencesAndGenericClaimsHaveNoAuthenticationConversionApi() throws Exception {
        assertThat(VerifiedStaffAuthentication.class.getConstructors()).isEmpty();
        assertThat(Modifier.isFinal(VerifiedStaffAuthentication.class.getModifiers())).isTrue();
        assertThat(Arrays.stream(VerifiedStaffAuthentication.class.getDeclaredMethods())
                .noneMatch(method -> Modifier.isStatic(method.getModifiers()) && Modifier.isPublic(method.getModifiers()))).isTrue();
        assertThat(ResolveAuthenticatedStaff.class.getMethod("resolve", VerifiedStaffAuthentication.class).getParameterTypes())
                .containsExactly(VerifiedStaffAuthentication.class);
        assertThat(VerifiedStaffAuthentication.class.isAssignableFrom(CredentialSubject.class)).isFalse();
        assertThat(Arrays.stream(ResolveAuthenticatedStaff.class.getMethods()).filter(method -> method.getName().equals("resolve")).count()).isEqualTo(1);
    }
}
