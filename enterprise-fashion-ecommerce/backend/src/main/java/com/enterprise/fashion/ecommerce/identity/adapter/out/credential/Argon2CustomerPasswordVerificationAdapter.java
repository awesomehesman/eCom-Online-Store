package com.enterprise.fashion.ecommerce.identity.adapter.out.credential;

import java.util.Objects;

import com.enterprise.fashion.ecommerce.identity.application.CredentialVerificationOutcome;
import com.enterprise.fashion.ecommerce.identity.application.port.out.CustomerPasswordVerificationPort;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public final class Argon2CustomerPasswordVerificationAdapter implements CustomerPasswordVerificationPort {

    static final int SALT_LENGTH_BYTES = GovernedArgon2VerifierProfile.SALT_LENGTH_BYTES;
    static final int HASH_LENGTH_BYTES = GovernedArgon2VerifierProfile.HASH_LENGTH_BYTES;
    static final int PARALLELISM = GovernedArgon2VerifierProfile.PARALLELISM;
    static final int MEMORY_KIB = GovernedArgon2VerifierProfile.MEMORY_KIB;
    static final int ITERATIONS = GovernedArgon2VerifierProfile.ITERATIONS;

    private final PasswordEncoder passwordEncoder;

    public Argon2CustomerPasswordVerificationAdapter() {
        this(new Argon2PasswordEncoder(
                SALT_LENGTH_BYTES,
                HASH_LENGTH_BYTES,
                PARALLELISM,
                MEMORY_KIB,
                ITERATIONS));
    }

    Argon2CustomerPasswordVerificationAdapter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = Objects.requireNonNull(passwordEncoder, "passwordEncoder");
    }

    @Override
    public CredentialVerificationOutcome verify(
            CharSequence submittedPassword,
            String authoritativeVerifierRepresentation) {
        if (submittedPassword == null
                || !GovernedArgon2VerifierProfile.accepts(authoritativeVerifierRepresentation)) {
            return CredentialVerificationOutcome.REJECTED;
        }

        try {
            return passwordEncoder.matches(submittedPassword, authoritativeVerifierRepresentation)
                    ? CredentialVerificationOutcome.MATCH
                    : CredentialVerificationOutcome.NO_MATCH;
        } catch (RuntimeException unsafeOrUnavailableVerification) {
            return CredentialVerificationOutcome.REJECTED;
        }
    }
}
