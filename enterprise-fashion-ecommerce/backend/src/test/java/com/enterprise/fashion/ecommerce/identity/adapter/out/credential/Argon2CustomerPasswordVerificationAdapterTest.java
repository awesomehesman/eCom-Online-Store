package com.enterprise.fashion.ecommerce.identity.adapter.out.credential;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicInteger;

import com.enterprise.fashion.ecommerce.identity.application.CredentialVerificationOutcome;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class Argon2CustomerPasswordVerificationAdapterTest {

    private static final String CONTROLLED_PASSWORD = "controlled login passphrase";
    private static final byte[] CONTROLLED_SALT = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);
    private static final String GOVERNED_VERIFIER = governedVerifier(CONTROLLED_PASSWORD);

    private final Argon2CustomerPasswordVerificationAdapter adapter =
            new Argon2CustomerPasswordVerificationAdapter();

    @Test
    void verifiesTheExactGovernedArgon2idProfile() {
        assertThat(GOVERNED_VERIFIER)
                .startsWith("$argon2id$v=19$m=65536,t=3,p=4$");
        assertThat(adapter.verify(CONTROLLED_PASSWORD, GOVERNED_VERIFIER))
                .isEqualTo(CredentialVerificationOutcome.MATCH);
    }

    @Test
    void returnsNoMatchForAnIncorrectPassword() {
        assertThat(adapter.verify("incorrect login input", GOVERNED_VERIFIER))
                .isEqualTo(CredentialVerificationOutcome.NO_MATCH);
    }

    @Test
    void rejectsMalformedAndUnsupportedRepresentations() {
        assertRejected("not-a-phc-representation");
        assertRejected(GOVERNED_VERIFIER.replace("$argon2id$", "$argon2i$"));
        assertRejected(GOVERNED_VERIFIER.replace("$v=19$", "$v=16$"));
        assertRejected(GOVERNED_VERIFIER.replace("m=65536", "m=32768"));
        assertRejected(GOVERNED_VERIFIER.replace("t=3", "t=2"));
        assertRejected(GOVERNED_VERIFIER.replace("p=4", "p=1"));
        assertRejected(GOVERNED_VERIFIER.replace("$MDEyMzQ1Njc4OUFCQ0RFRg$", "$invalid*$"));
        assertRejected(GOVERNED_VERIFIER.substring(0, GOVERNED_VERIFIER.lastIndexOf('$') + 1) + "AA");
        assertRejected("$2a$10$abcdefghijklmnopqrstuuuuuuuuuuuuuuuuuuuuuuuuuuuuu");
    }

    @Test
    void rejectsResourceUnsafeParametersBeforeCryptographicVerification() {
        AtomicInteger matchInvocations = new AtomicInteger();
        PasswordEncoder recordingEncoder = new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                matchInvocations.incrementAndGet();
                return true;
            }
        };
        Argon2CustomerPasswordVerificationAdapter guardedAdapter =
                new Argon2CustomerPasswordVerificationAdapter(recordingEncoder);
        String unsafe = GOVERNED_VERIFIER.replace("m=65536", "m=999999999999999999999999999999999999");

        assertThat(guardedAdapter.verify(CONTROLLED_PASSWORD, unsafe))
                .isEqualTo(CredentialVerificationOutcome.REJECTED);
        assertThat(matchInvocations).hasValue(0);
    }

    @Test
    void doesNotApplyPasswordEstablishmentLengthRulesAtLogin() {
        String shortLoginPassword = "short";
        String verifier = governedVerifier(shortLoginPassword);

        assertThat(adapter.verify(shortLoginPassword, verifier))
                .isEqualTo(CredentialVerificationOutcome.MATCH);
    }

    @Test
    void rejectsNullInputsAndUnsafeVerificationFailure() {
        assertThat(adapter.verify(null, GOVERNED_VERIFIER))
                .isEqualTo(CredentialVerificationOutcome.REJECTED);
        assertThat(adapter.verify(CONTROLLED_PASSWORD, null))
                .isEqualTo(CredentialVerificationOutcome.REJECTED);

        PasswordEncoder failingEncoder = new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                throw new IllegalStateException("verification unavailable");
            }
        };

        assertThat(new Argon2CustomerPasswordVerificationAdapter(failingEncoder)
                        .verify(CONTROLLED_PASSWORD, GOVERNED_VERIFIER))
                .isEqualTo(CredentialVerificationOutcome.REJECTED);
    }

    @Test
    void outcomesContainCredentialEvidenceOnly() {
        assertThat(CredentialVerificationOutcome.values())
                .extracting(Enum::name)
                .containsExactly("MATCH", "NO_MATCH", "REJECTED")
                .noneMatch(name -> name.matches(".*(AUTHENTIC|PRINCIPAL|SESSION|ASSOCIATION|AUTHORI[ZS]).*"));
    }

    private void assertRejected(String representation) {
        assertThat(adapter.verify(CONTROLLED_PASSWORD, representation))
                .isEqualTo(CredentialVerificationOutcome.REJECTED);
    }

    private static String governedVerifier(String password) {
        Argon2Parameters parameters = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withVersion(Argon2Parameters.ARGON2_VERSION_13)
                .withMemoryAsKB(Argon2CustomerPasswordVerificationAdapter.MEMORY_KIB)
                .withIterations(Argon2CustomerPasswordVerificationAdapter.ITERATIONS)
                .withParallelism(Argon2CustomerPasswordVerificationAdapter.PARALLELISM)
                .withSalt(CONTROLLED_SALT)
                .build();
        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(parameters);
        byte[] hash = new byte[Argon2CustomerPasswordVerificationAdapter.HASH_LENGTH_BYTES];
        generator.generateBytes(password.toCharArray(), hash);

        Base64.Encoder encoder = Base64.getEncoder().withoutPadding();
        return "$argon2id$v=19$m=65536,t=3,p=4$"
                + encoder.encodeToString(CONTROLLED_SALT)
                + "$"
                + encoder.encodeToString(hash);
    }
}
