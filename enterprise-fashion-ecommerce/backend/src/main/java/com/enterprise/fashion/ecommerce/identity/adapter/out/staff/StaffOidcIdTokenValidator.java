package com.enterprise.fashion.ecommerce.identity.adapter.out.staff;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import com.enterprise.fashion.ecommerce.identity.application.staff.StaffOidcAuthenticationVerification;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.factories.DefaultJWSVerifierFactory;
import com.nimbusds.jose.proc.JWSVerifierFactory;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

/** Dormant local-key ID-token validation. Performs no discovery, exchange or evidence issuance. */
final class StaffOidcIdTokenValidator {
    record Result(StaffOidcValidatedToken validatedToken,
            StaffOidcAuthenticationVerification.Outcome failure) {
        Result {
            if ((validatedToken == null) == (failure == null)) {
                throw new IllegalArgumentException("Exactly one validation result is required");
            }
        }
        static Result accepted(StaffOidcValidatedToken token) { return new Result(token, null); }
        static Result rejected(StaffOidcAuthenticationVerification.Outcome failure) {
            return new Result(null, Objects.requireNonNull(failure));
        }
        boolean accepted() { return validatedToken != null; }
        @Override public String toString() {
            return accepted() ? "StaffOidcIdTokenValidation[internal-success]"
                    : "StaffOidcIdTokenValidation[" + failure + "]";
        }
    }

    private static final int MAXIMUM_KEY_ID_LENGTH = 128;
    private static final int MAXIMUM_SUBJECT_LENGTH = 255;
    private final StaffOidcTokenTrust trust;
    private final JWSVerifierFactory verifierFactory;

    StaffOidcIdTokenValidator(StaffOidcTokenTrust trust) {
        this(trust, new DefaultJWSVerifierFactory());
    }

    StaffOidcIdTokenValidator(StaffOidcTokenTrust trust, JWSVerifierFactory verifierFactory) {
        this.trust = Objects.requireNonNull(trust, "Token trust is required");
        this.verifierFactory = Objects.requireNonNull(verifierFactory, "Verifier factory is required");
    }

    Result validate(String serializedToken, String expectedNonce) {
        if (serializedToken == null || serializedToken.isBlank()
                || serializedToken.length() > trust.maximumTokenLength()
                || expectedNonce == null || expectedNonce.isBlank()) {
            return rejected(StaffOidcAuthenticationVerification.Outcome.INVALID_TOKEN);
        }
        try {
            SignedJWT token = SignedJWT.parse(serializedToken);
            JWSHeader header = token.getHeader();
            if (!safeHeader(header)) return rejected(StaffOidcAuthenticationVerification.Outcome.INVALID_TOKEN);
            JWSAlgorithm algorithm = header.getAlgorithm();
            if (!trust.allowedAlgorithms().contains(algorithm)) {
                return rejected(StaffOidcAuthenticationVerification.Outcome.INVALID_TOKEN);
            }
            String keyId = header.getKeyID();
            if (!bounded(keyId, MAXIMUM_KEY_ID_LENGTH)) {
                return rejected(StaffOidcAuthenticationVerification.Outcome.VERIFICATION_UNAVAILABLE);
            }
            List<StaffOidcTokenTrust.VerificationKey> keys = trust.verificationKeys().stream()
                    .filter(key -> key.keyId().equals(keyId) && key.algorithm().equals(algorithm)).toList();
            if (keys.size() != 1) {
                return rejected(StaffOidcAuthenticationVerification.Outcome.VERIFICATION_UNAVAILABLE);
            }
            var verifier = verifierFactory.createJWSVerifier(header, keys.getFirst().key());
            if (!token.verify(verifier)) {
                return rejected(StaffOidcAuthenticationVerification.Outcome.INVALID_TOKEN);
            }
            return validateClaims(token.getJWTClaimsSet(), expectedNonce);
        } catch (ParseException | com.nimbusds.jose.JOSEException invalid) {
            return rejected(StaffOidcAuthenticationVerification.Outcome.INVALID_TOKEN);
        } catch (RuntimeException internal) {
            return rejected(StaffOidcAuthenticationVerification.Outcome.INDETERMINATE);
        }
    }

    private Result validateClaims(JWTClaimsSet claims, String expectedNonce) throws ParseException {
        if (!trust.issuer().equals(claims.getIssuer())) {
            return rejected(StaffOidcAuthenticationVerification.Outcome.ISSUER_MISMATCH);
        }
        List<String> audience = claims.getAudience();
        if (audience == null || !audience.contains(trust.clientId())) {
            return rejected(StaffOidcAuthenticationVerification.Outcome.AUDIENCE_MISMATCH);
        }
        String authorizedParty = claims.getStringClaim("azp");
        if (audience.size() > 1 && !trust.clientId().equals(authorizedParty)
                || authorizedParty != null && !trust.clientId().equals(authorizedParty)) {
            return rejected(StaffOidcAuthenticationVerification.Outcome.AUDIENCE_MISMATCH);
        }
        Instant now = trust.clock().instant();
        Instant skewedFuture = now.plus(trust.clockSkew());
        Date expiration = claims.getExpirationTime();
        Date issuedAt = claims.getIssueTime();
        Date notBefore = claims.getNotBeforeTime();
        if (expiration == null || issuedAt == null
                || !expiration.toInstant().isAfter(issuedAt.toInstant())
                || notBefore != null && notBefore.toInstant().isAfter(expiration.toInstant())
                || now.minus(trust.clockSkew()).isAfter(expiration.toInstant())
                || issuedAt.toInstant().isAfter(skewedFuture)
                || notBefore != null && notBefore.toInstant().isAfter(skewedFuture)) {
            return rejected(StaffOidcAuthenticationVerification.Outcome.INVALID_TOKEN);
        }
        String nonce = claims.getStringClaim("nonce");
        if (!expectedNonce.equals(nonce)) {
            return rejected(StaffOidcAuthenticationVerification.Outcome.NONCE_MISMATCH);
        }
        String subject = claims.getSubject();
        if (!bounded(subject, MAXIMUM_SUBJECT_LENGTH)) {
            return rejected(StaffOidcAuthenticationVerification.Outcome.UNTRUSTED_SUBJECT);
        }
        Date authenticationTime = claims.getDateClaim("auth_time");
        if (authenticationTime == null || authenticationTime.toInstant().isAfter(issuedAt.toInstant())
                || authenticationTime.toInstant().isAfter(skewedFuture)
                || authenticationTime.toInstant().isBefore(
                        now.minus(trust.maximumAuthenticationAge()).minus(trust.clockSkew()))) {
            return rejected(StaffOidcAuthenticationVerification.Outcome.STALE_AUTHENTICATION);
        }
        if (!satisfiesAssurance(claims)) {
            return rejected(StaffOidcAuthenticationVerification.Outcome.INSUFFICIENT_ASSURANCE);
        }
        return Result.accepted(new StaffOidcValidatedToken(claims.getIssuer(), subject,
                new LinkedHashSet<>(audience), issuedAt.toInstant(), expiration.toInstant(),
                authenticationTime.toInstant()));
    }

    private boolean satisfiesAssurance(JWTClaimsSet claims) {
        for (StaffOidcTokenTrust.AssuranceRequirement requirement : trust.assuranceRequirements()) {
            Object claim = claims.getClaim(requirement.claimName());
            if (!(claim instanceof String value) || value.isBlank()
                    || !requirement.acceptedValues().contains(value)) return false;
        }
        return true;
    }

    private static boolean safeHeader(JWSHeader header) {
        return header != null && header.getAlgorithm() != null
                && !JWSAlgorithm.NONE.equals(header.getAlgorithm())
                && (header.getCriticalParams() == null || header.getCriticalParams().isEmpty())
                && header.getJWKURL() == null && header.getJWK() == null
                && header.getX509CertURL() == null && header.getX509CertChain() == null;
    }

    private static boolean bounded(String value, int maximum) {
        return value != null && !value.isBlank() && value.length() <= maximum
                && value.codePoints().noneMatch(Character::isISOControl);
    }

    private static Result rejected(StaffOidcAuthenticationVerification.Outcome outcome) {
        return Result.rejected(outcome);
    }
}
