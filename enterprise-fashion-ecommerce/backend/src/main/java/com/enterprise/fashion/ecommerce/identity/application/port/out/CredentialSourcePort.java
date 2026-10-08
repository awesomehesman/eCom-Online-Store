package com.enterprise.fashion.ecommerce.identity.application.port.out;

import java.util.UUID;

import com.enterprise.fashion.ecommerce.identity.application.credential.AcceptedCredentialPublication;
import com.enterprise.fashion.ecommerce.identity.application.credential.CredentialPublicationResult;
import com.enterprise.fashion.ecommerce.identity.application.credential.CredentialVerifierObservation;
import com.enterprise.fashion.ecommerce.identity.domain.model.CredentialSubject;

/**
 * Identity-only source access for the fixed local Customer password context.
 * Called inside the application's fresh READ_COMMITTED transaction. Implementations serialize
 * publication and observation for a subject until transaction completion. No email resolution.
 */
public interface CredentialSourcePort {
    CredentialPublicationResult publish(AcceptedCredentialPublication publication);

    Observation observe(CredentialSubject subject, UUID fact);

    /** Coherent, sensitive handoff for Identity verification only; never a separate verifier lookup. */
    CredentialVerifierObservation observeVerifier(CredentialSubject subject, UUID fact);

    /**
     * Trusted Adapter report, not caller-supplied acceptance. The producer never accepts an
     * Observation from its caller. Ordinary evidence carries no verifier.
     */
    enum Observation {
        BOUND_APPLICABLE, SUPERSEDED, INCOMPATIBLE, INCOMPLETE
    }
}
