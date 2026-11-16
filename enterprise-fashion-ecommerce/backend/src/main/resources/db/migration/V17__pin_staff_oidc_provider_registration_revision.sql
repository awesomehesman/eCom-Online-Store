-- V16 transactions predate provider-registration revision binding. They are ephemeral,
-- Session-bound protocol state rather than Audit Records, and no table references them.
-- Invalidate them instead of fabricating a revision: any outstanding callback then fails closed.
LOCK TABLE identity.staff_oidc_authorization_transaction IN ACCESS EXCLUSIVE MODE;
DELETE FROM identity.staff_oidc_authorization_transaction;

ALTER TABLE identity.staff_oidc_authorization_transaction
    ADD COLUMN registration_revision BIGINT NOT NULL
        CHECK (registration_revision > 0);

CREATE OR REPLACE FUNCTION identity.protect_staff_oidc_authorization_transaction()
RETURNS trigger LANGUAGE plpgsql AS $$
BEGIN
    IF OLD.status <> 'ACTIVE'
            OR NEW.status NOT IN ('CONSUMED', 'SUPERSEDED', 'EXPIRED', 'CORRUPT') THEN
        RAISE EXCEPTION 'Staff OIDC authorization transaction change rejected';
    END IF;
    IF ROW(NEW.transaction_id, NEW.state_sha256, NEW.nonce, NEW.pkce_verifier,
           NEW.pkce_challenge, NEW.session_id, NEW.registration_id,
           NEW.registration_revision, NEW.created_at, NEW.expires_at)
       IS DISTINCT FROM
       ROW(OLD.transaction_id, OLD.state_sha256, OLD.nonce, OLD.pkce_verifier,
           OLD.pkce_challenge, OLD.session_id, OLD.registration_id,
           OLD.registration_revision, OLD.created_at, OLD.expires_at) THEN
        RAISE EXCEPTION 'Staff OIDC authorization transaction identity is immutable';
    END IF;
    RETURN NEW;
END;
$$;

COMMENT ON COLUMN identity.staff_oidc_authorization_transaction.registration_revision IS
    'Exact immutable provider-registration revision; this does not establish provider admission.';
