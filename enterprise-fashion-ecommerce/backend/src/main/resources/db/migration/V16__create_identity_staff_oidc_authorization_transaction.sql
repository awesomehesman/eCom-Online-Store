-- Dormant provider-neutral protocol correlation only. These rows establish no Authentication or access.
CREATE TABLE identity.staff_oidc_authorization_transaction (
    transaction_id UUID PRIMARY KEY,
    state_sha256 BYTEA NOT NULL UNIQUE CHECK (octet_length(state_sha256) = 32),
    nonce VARCHAR(128) COLLATE "C" NOT NULL CHECK (nonce ~ '^[A-Za-z0-9_-]{43,128}$'),
    pkce_verifier VARCHAR(128) COLLATE "C" NOT NULL
        CHECK (pkce_verifier ~ '^[A-Za-z0-9._~-]{43,128}$'),
    pkce_challenge VARCHAR(128) COLLATE "C" NOT NULL
        CHECK (pkce_challenge ~ '^[A-Za-z0-9_-]{43,128}$'),
    session_id CHAR(36) NOT NULL REFERENCES identity.spring_session(session_id) ON DELETE CASCADE,
    registration_id VARCHAR(128) COLLATE "C" NOT NULL
        CHECK (length(trim(registration_id)) > 0),
    created_at TIMESTAMPTZ NOT NULL,
    expires_at TIMESTAMPTZ NOT NULL CHECK (expires_at > created_at),
    status VARCHAR(10) NOT NULL
        CHECK (status IN ('ACTIVE', 'CONSUMED', 'SUPERSEDED', 'EXPIRED', 'CORRUPT')),
    consumed_at TIMESTAMPTZ,
    superseded_at TIMESTAMPTZ,
    expired_at TIMESTAMPTZ,
    corrupt_at TIMESTAMPTZ,
    CHECK (
        (status = 'ACTIVE' AND consumed_at IS NULL AND superseded_at IS NULL
            AND expired_at IS NULL AND corrupt_at IS NULL)
        OR (status = 'CONSUMED' AND consumed_at IS NOT NULL
            AND superseded_at IS NULL AND expired_at IS NULL AND corrupt_at IS NULL
            AND consumed_at >= created_at)
        OR (status = 'SUPERSEDED' AND superseded_at IS NOT NULL
            AND consumed_at IS NULL AND expired_at IS NULL AND corrupt_at IS NULL
            AND superseded_at >= created_at)
        OR (status = 'EXPIRED' AND expired_at IS NOT NULL
            AND consumed_at IS NULL AND superseded_at IS NULL AND corrupt_at IS NULL
            AND expired_at >= created_at)
        OR (status = 'CORRUPT' AND corrupt_at IS NOT NULL
            AND consumed_at IS NULL AND superseded_at IS NULL AND expired_at IS NULL
            AND corrupt_at >= created_at)
    )
);

CREATE UNIQUE INDEX uq_staff_oidc_active_session_registration
    ON identity.staff_oidc_authorization_transaction(session_id, registration_id)
    WHERE status = 'ACTIVE';
CREATE INDEX idx_staff_oidc_expiration
    ON identity.staff_oidc_authorization_transaction(expires_at)
    WHERE status = 'ACTIVE';

CREATE FUNCTION identity.protect_staff_oidc_authorization_transaction() RETURNS trigger LANGUAGE plpgsql AS $$
BEGIN
    IF OLD.status <> 'ACTIVE'
            OR NEW.status NOT IN ('CONSUMED', 'SUPERSEDED', 'EXPIRED', 'CORRUPT') THEN
        RAISE EXCEPTION 'Staff OIDC authorization transaction change rejected';
    END IF;
    IF ROW(NEW.transaction_id, NEW.state_sha256, NEW.nonce, NEW.pkce_verifier,
           NEW.pkce_challenge, NEW.session_id, NEW.registration_id, NEW.created_at, NEW.expires_at)
       IS DISTINCT FROM
       ROW(OLD.transaction_id, OLD.state_sha256, OLD.nonce, OLD.pkce_verifier,
           OLD.pkce_challenge, OLD.session_id, OLD.registration_id, OLD.created_at, OLD.expires_at) THEN
        RAISE EXCEPTION 'Staff OIDC authorization transaction identity is immutable';
    END IF;
    RETURN NEW;
END;
$$;

CREATE TRIGGER protect_staff_oidc_authorization_transaction
    BEFORE UPDATE ON identity.staff_oidc_authorization_transaction
    FOR EACH ROW EXECUTE FUNCTION identity.protect_staff_oidc_authorization_transaction();
