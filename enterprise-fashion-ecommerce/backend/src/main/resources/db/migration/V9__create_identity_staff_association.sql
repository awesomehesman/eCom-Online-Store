-- Provider-neutral Identity-local association only. No Staff accounts, assignments or trust issuers seeded.
CREATE TABLE identity.staff_association (
    staff_principal_id UUID PRIMARY KEY,
    authority VARCHAR(128) COLLATE "C" NOT NULL CHECK (length(trim(authority)) > 0),
    subject VARCHAR(512) COLLATE "C" NOT NULL CHECK (length(trim(subject)) > 0),
    created_at TIMESTAMPTZ NOT NULL,
    revoked_at TIMESTAMPTZ,
    UNIQUE (authority, subject),
    CHECK (revoked_at IS NULL OR revoked_at >= created_at)
);
CREATE TABLE identity.staff_association_event (
    staff_principal_id UUID NOT NULL REFERENCES identity.staff_association(staff_principal_id),
    action VARCHAR(7) NOT NULL CHECK (action IN ('CREATED', 'REVOKED')),
    occurred_at TIMESTAMPTZ NOT NULL,
    provenance VARCHAR(256) NOT NULL CHECK (length(trim(provenance)) > 0),
    PRIMARY KEY (staff_principal_id, action)
);
-- Ordinary application DML cannot rewrite association identity/history or reactivate a revoked binding.
CREATE FUNCTION identity.protect_staff_association() RETURNS trigger LANGUAGE plpgsql AS $$
BEGIN
    IF TG_OP = 'DELETE' OR TG_TABLE_NAME = 'staff_association_event' THEN
        RAISE EXCEPTION 'Staff association history is immutable';
    END IF;
    IF ROW(NEW.staff_principal_id, NEW.authority, NEW.subject, NEW.created_at)
            IS DISTINCT FROM ROW(OLD.staff_principal_id, OLD.authority, OLD.subject, OLD.created_at)
            OR OLD.revoked_at IS NOT NULL OR NEW.revoked_at IS NULL THEN
        RAISE EXCEPTION 'Staff association change rejected';
    END IF;
    RETURN NEW;
END;
$$;
CREATE TRIGGER protect_staff_association BEFORE UPDATE OR DELETE ON identity.staff_association
    FOR EACH ROW EXECUTE FUNCTION identity.protect_staff_association();
CREATE TRIGGER protect_staff_association_event BEFORE UPDATE OR DELETE ON identity.staff_association_event
    FOR EACH ROW EXECUTE FUNCTION identity.protect_staff_association();
