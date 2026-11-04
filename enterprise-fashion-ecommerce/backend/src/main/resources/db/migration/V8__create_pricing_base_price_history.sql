CREATE SCHEMA pricing;

-- References are Pricing-owned copies, never cross-module foreign keys or Product proof.
CREATE TABLE pricing.base_price_subject (
    variant_reference TEXT COLLATE "C" PRIMARY KEY CHECK (variant_reference ~ '^[A-Za-z0-9_-]{1,128}$'),
    product_reference TEXT COLLATE "C" NOT NULL CHECK (product_reference ~ '^[A-Za-z0-9_-]{1,128}$'),
    head_revision TEXT COLLATE "C" NOT NULL,
    UNIQUE (variant_reference, product_reference)
);
CREATE TABLE pricing.base_price_revision (
    variant_reference TEXT COLLATE "C" NOT NULL,
    product_reference TEXT COLLATE "C" NOT NULL,
    revision TEXT COLLATE "C" NOT NULL CHECK (btrim(revision) <> ''),
    previous_revision TEXT COLLATE "C",
    kind TEXT NOT NULL CHECK (kind IN ('ASSIGN', 'WITHDRAW')),
    amount NUMERIC NOT NULL CHECK (amount > 0 AND amount::text NOT IN ('NaN', 'Infinity', '-Infinity')),
    currency TEXT NOT NULL CHECK (currency = 'ZAR'),
    PRIMARY KEY (variant_reference, revision),
    FOREIGN KEY (variant_reference, product_reference)
        REFERENCES pricing.base_price_subject(variant_reference, product_reference) ON DELETE RESTRICT ON UPDATE RESTRICT,
    FOREIGN KEY (variant_reference, previous_revision)
        REFERENCES pricing.base_price_revision(variant_reference, revision) ON DELETE RESTRICT ON UPDATE RESTRICT,
    UNIQUE NULLS NOT DISTINCT (variant_reference, previous_revision),
    CHECK (previous_revision IS DISTINCT FROM revision),
    CHECK (kind <> 'WITHDRAW' OR previous_revision IS NOT NULL)
);
ALTER TABLE pricing.base_price_subject ADD CONSTRAINT base_price_head_fk
    FOREIGN KEY (variant_reference, head_revision)
    REFERENCES pricing.base_price_revision(variant_reference, revision)
    DEFERRABLE INITIALLY DEFERRED;

CREATE FUNCTION pricing.preserve_base_price_history() RETURNS TRIGGER LANGUAGE plpgsql AS $$
BEGIN
    RAISE EXCEPTION 'Pricing revision history is immutable' USING ERRCODE = '23514';
END;
$$;
CREATE TRIGGER base_price_history_immutable BEFORE UPDATE OR DELETE ON pricing.base_price_revision
    FOR EACH ROW EXECUTE FUNCTION pricing.preserve_base_price_history();
CREATE TRIGGER base_price_history_no_truncate BEFORE TRUNCATE ON pricing.base_price_revision
    FOR EACH STATEMENT EXECUTE FUNCTION pricing.preserve_base_price_history();

CREATE FUNCTION pricing.validate_base_price_revision() RETURNS TRIGGER LANGUAGE plpgsql AS $$
DECLARE
    head TEXT;
    prior pricing.base_price_revision%ROWTYPE;
BEGIN
    SELECT head_revision INTO head FROM pricing.base_price_subject
        WHERE variant_reference = NEW.variant_reference AND product_reference = NEW.product_reference FOR UPDATE;
    IF NOT FOUND THEN
        RAISE EXCEPTION 'Pricing subject mismatch' USING ERRCODE = '23514';
    END IF;
    IF NEW.previous_revision IS NULL THEN
        IF head <> NEW.revision OR NEW.kind <> 'ASSIGN' THEN
            RAISE EXCEPTION 'Invalid initial Pricing revision' USING ERRCODE = '23514';
        END IF;
    ELSE
        SELECT * INTO prior FROM pricing.base_price_revision
            WHERE variant_reference = NEW.variant_reference AND revision = NEW.previous_revision;
        IF NOT FOUND OR head <> NEW.previous_revision THEN
            RAISE EXCEPTION 'Pricing revision is not based on current head' USING ERRCODE = '23514';
        END IF;
        IF NEW.kind = 'WITHDRAW' AND (prior.kind <> 'ASSIGN' OR NEW.amount <> prior.amount OR NEW.currency <> prior.currency) THEN
            RAISE EXCEPTION 'Invalid Pricing withdrawal' USING ERRCODE = '23514';
        END IF;
    END IF;
    RETURN NEW;
END;
$$;
CREATE TRIGGER base_price_revision_binding BEFORE INSERT ON pricing.base_price_revision
    FOR EACH ROW EXECUTE FUNCTION pricing.validate_base_price_revision();

CREATE FUNCTION pricing.validate_base_price_head() RETURNS TRIGGER LANGUAGE plpgsql AS $$
BEGIN
    IF NEW.variant_reference <> OLD.variant_reference OR NEW.product_reference <> OLD.product_reference
        OR NOT EXISTS (SELECT 1 FROM pricing.base_price_revision
            WHERE variant_reference = NEW.variant_reference AND revision = NEW.head_revision
                AND previous_revision = OLD.head_revision) THEN
        RAISE EXCEPTION 'Invalid Pricing head replacement' USING ERRCODE = '23514';
    END IF;
    RETURN NEW;
END;
$$;
CREATE TRIGGER base_price_head_binding BEFORE UPDATE ON pricing.base_price_subject
    FOR EACH ROW EXECUTE FUNCTION pricing.validate_base_price_head();

-- Insert-before-head-update is valid inside a transaction, never at commit. Both
-- triggers re-read final state: queued NEW.head_revision values may already be stale.
-- Existing row locks and immutable single-successor chains serialize each Variant.
CREATE FUNCTION pricing.validate_terminal_base_price_head() RETURNS TRIGGER LANGUAGE plpgsql AS $$
DECLARE
    head TEXT;
BEGIN
    SELECT head_revision INTO head FROM pricing.base_price_subject
        WHERE variant_reference = NEW.variant_reference AND product_reference = NEW.product_reference FOR UPDATE;
    IF NOT FOUND OR NOT EXISTS (
        SELECT 1 FROM pricing.base_price_revision
        WHERE variant_reference = NEW.variant_reference AND product_reference = NEW.product_reference AND revision = head
    ) OR EXISTS (
        SELECT 1 FROM pricing.base_price_revision
        WHERE variant_reference = NEW.variant_reference AND previous_revision = head
    ) THEN
        RAISE EXCEPTION 'Pricing head must be terminal' USING ERRCODE = '23514';
    END IF;
    RETURN NULL;
END;
$$;
CREATE CONSTRAINT TRIGGER base_price_revision_terminal_head
    AFTER INSERT ON pricing.base_price_revision
    DEFERRABLE INITIALLY DEFERRED
    FOR EACH ROW EXECUTE FUNCTION pricing.validate_terminal_base_price_head();
CREATE CONSTRAINT TRIGGER base_price_subject_terminal_head
    AFTER INSERT OR UPDATE ON pricing.base_price_subject
    DEFERRABLE INITIALLY DEFERRED
    FOR EACH ROW EXECUTE FUNCTION pricing.validate_terminal_base_price_head();
