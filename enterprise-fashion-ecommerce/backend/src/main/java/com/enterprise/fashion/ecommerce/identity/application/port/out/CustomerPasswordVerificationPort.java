package com.enterprise.fashion.ecommerce.identity.application.port.out;

import com.enterprise.fashion.ecommerce.identity.application.CredentialVerificationOutcome;

public interface CustomerPasswordVerificationPort {

    CredentialVerificationOutcome verify(
            CharSequence submittedPassword,
            String authoritativeVerifierRepresentation);
}
