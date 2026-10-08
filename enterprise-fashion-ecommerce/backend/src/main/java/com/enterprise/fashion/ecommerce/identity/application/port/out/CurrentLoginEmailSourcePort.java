package com.enterprise.fashion.ecommerce.identity.application.port.out;

import java.util.UUID;

import com.enterprise.fashion.ecommerce.identity.application.loginemail.AcceptedLoginEmailBinding;
import com.enterprise.fashion.ecommerce.identity.application.loginemail.CurrentLoginEmailObservation;
import com.enterprise.fashion.ecommerce.identity.application.loginemail.LoginEmailPublicationResult;
import com.enterprise.fashion.ecommerce.identity.domain.model.CredentialSubject;

/** Identity-only source. Called inside the application's coherent source transaction. */
public interface CurrentLoginEmailSourcePort {
    LoginEmailPublicationResult publish(AcceptedLoginEmailBinding binding);

    CurrentLoginEmailObservation observe(CredentialSubject subject, UUID fact);
}
