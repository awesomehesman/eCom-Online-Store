package com.enterprise.fashion.ecommerce.identity.application.port.out;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import com.enterprise.fashion.ecommerce.identity.domain.model.StaffAssociation;
import com.enterprise.fashion.ecommerce.identity.domain.model.StaffIdentityReference;

/** Internal persistence handoff; invocation is not evidence of authenticated Staff identity. */
public interface StaffAssociationPort {
    void create(StaffAssociation association, String provenance);
    boolean revoke(UUID staffPrincipalId, Instant time, String provenance);
    Optional<StaffAssociation> find(StaffIdentityReference reference);
}
