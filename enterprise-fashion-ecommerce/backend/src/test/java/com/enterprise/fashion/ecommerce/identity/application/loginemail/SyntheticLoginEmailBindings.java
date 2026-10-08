package com.enterprise.fashion.ecommerce.identity.application.loginemail;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.UUID;

import com.enterprise.fashion.ecommerce.identity.domain.model.CredentialSubject;

/** Test-only source facts. These never prove lawful production current-email population. */
public final class SyntheticLoginEmailBindings {
    private SyntheticLoginEmailBindings() { }

    public static AcceptedLoginEmailBinding fact(UUID fact, CredentialSubject subject,
            String email, UUID supersedes) {
        try {
            Field issuanceKey = AcceptedLoginEmailBinding.class.getDeclaredField("ISSUANCE_KEY");
            issuanceKey.setAccessible(true);
            Constructor<AcceptedLoginEmailBinding> constructor = AcceptedLoginEmailBinding.class
                    .getDeclaredConstructor(Object.class, UUID.class, CredentialSubject.class,
                            String.class, UUID.class);
            constructor.setAccessible(true);
            return constructor.newInstance(issuanceKey.get(null), fact, subject, email, supersedes);
        } catch (ReflectiveOperationException failure) {
            throw new IllegalArgumentException("Invalid synthetic test binding");
        }
    }
}
