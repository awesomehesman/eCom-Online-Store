package com.enterprise.fashion.ecommerce.identity.application.credential;

import java.lang.reflect.Constructor;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.identity.domain.model.CredentialSubject;

/** Test-only synthetic capability construction. This does NOT establish production authority. */
public final class SyntheticCredentialPublications {
    public static final String VERIFIER = "$argon2id$v=19$m=65536,t=3,p=4$"
            + "MDEyMzQ1Njc4OUFCQ0RFRg$" + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    public static final String OTHER_VERIFIER = VERIFIER.substring(0, VERIFIER.length() - 1) + "E";

    private SyntheticCredentialPublications() { }

    public static AcceptedCredentialPublication fact(UUID fact, CredentialSubject subject,
            String verifier, UUID supersedes) {
        try {
            Constructor<AcceptedCredentialPublication> constructor = AcceptedCredentialPublication.class
                    .getDeclaredConstructor(UUID.class, CredentialSubject.class, String.class, UUID.class);
            constructor.setAccessible(true);
            return constructor.newInstance(fact, subject, verifier, supersedes);
        } catch (ReflectiveOperationException failure) {
            throw new IllegalArgumentException("Invalid synthetic test capability");
        }
    }
}
