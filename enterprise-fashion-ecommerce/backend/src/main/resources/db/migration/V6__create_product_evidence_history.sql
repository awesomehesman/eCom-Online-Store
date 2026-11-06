-- Retention of non-authoritative evidence claims only. No accepted/valid/publication outcome.
CREATE TABLE product.evidence_event (
    event_id UUID PRIMARY KEY,
    product_id TEXT COLLATE "C" NOT NULL REFERENCES product.product(product_id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    representation_revision TEXT COLLATE "C" NOT NULL CHECK (representation_revision ~ '^v1_[0-9a-f]{64}$'),
    purpose TEXT NOT NULL CHECK (purpose IN ('CONTENT', 'MEDIA')),
    event_type TEXT NOT NULL CHECK (event_type IN ('RECORD', 'INVALIDATE', 'SUPERSEDE')),
    source_reference TEXT NOT NULL CHECK (source_reference ~ '[^[:space:]]'),
    source_actor_claim TEXT NOT NULL CHECK (source_actor_claim ~ '[^[:space:]]'),
    supporting_references TEXT[] NOT NULL CHECK (array_position(supporting_references, NULL) IS NULL),
    audit_correlation TEXT NOT NULL CHECK (audit_correlation ~ '^[A-Za-z0-9._-]{1,128}$'),
    recorded_at TIMESTAMPTZ NOT NULL,
    recorded_by TEXT NOT NULL CHECK (recorded_by = 'PRODUCT_DATABASE'),
    target_event_id UUID,
    replacement_event_id UUID,
    target_type TEXT GENERATED ALWAYS AS (CASE WHEN target_event_id IS NOT NULL THEN 'RECORD'::TEXT END) STORED,
    replacement_type TEXT GENERATED ALWAYS AS (CASE WHEN replacement_event_id IS NOT NULL THEN 'RECORD'::TEXT END) STORED,
    CONSTRAINT evidence_event_binding_unique UNIQUE (event_id, product_id, purpose, event_type),
    CONSTRAINT evidence_target_fk FOREIGN KEY (target_event_id, product_id, purpose, target_type)
        REFERENCES product.evidence_event(event_id, product_id, purpose, event_type) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT evidence_replacement_fk FOREIGN KEY (replacement_event_id, product_id, purpose, replacement_type)
        REFERENCES product.evidence_event(event_id, product_id, purpose, event_type) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT evidence_event_shape_check CHECK (
        (event_type = 'RECORD' AND target_event_id IS NULL AND replacement_event_id IS NULL) OR
        (event_type = 'INVALIDATE' AND target_event_id IS NOT NULL AND replacement_event_id IS NULL) OR
        (event_type = 'SUPERSEDE' AND target_event_id IS NOT NULL AND replacement_event_id IS NOT NULL
            AND target_event_id <> replacement_event_id)),
    CHECK (event_id IS DISTINCT FROM target_event_id AND event_id IS DISTINCT FROM replacement_event_id)
);
CREATE INDEX evidence_product_purpose_idx ON product.evidence_event(product_id, purpose);

CREATE FUNCTION product.stamp_evidence_recording() RETURNS TRIGGER LANGUAGE plpgsql AS $$
BEGIN
    -- Attribution to the recording system only, never an authenticated Identity/Staff assertion.
    NEW.recorded_at := clock_timestamp();
    NEW.recorded_by := 'PRODUCT_DATABASE';
    IF NEW.event_type <> 'RECORD' AND NOT EXISTS (
        SELECT 1 FROM product.evidence_event
        WHERE event_id = NEW.target_event_id AND product_id = NEW.product_id
          AND purpose = NEW.purpose AND event_type = 'RECORD'
          AND representation_revision = NEW.representation_revision
    ) THEN
        RAISE EXCEPTION 'Evidence history target binding mismatch' USING ERRCODE = '23514';
    END IF;
    RETURN NEW;
END;
$$;
CREATE TRIGGER evidence_recording_stamp BEFORE INSERT ON product.evidence_event
    FOR EACH ROW EXECUTE FUNCTION product.stamp_evidence_recording();

CREATE FUNCTION product.preserve_evidence_history() RETURNS TRIGGER LANGUAGE plpgsql AS $$
BEGIN
    RAISE EXCEPTION 'Product evidence history is append-only' USING ERRCODE = '23514';
END;
$$;
CREATE TRIGGER evidence_history_immutable BEFORE UPDATE OR DELETE ON product.evidence_event
    FOR EACH ROW EXECUTE FUNCTION product.preserve_evidence_history();
CREATE TRIGGER evidence_history_no_truncate BEFORE TRUNCATE ON product.evidence_event
    FOR EACH STATEMENT EXECUTE FUNCTION product.preserve_evidence_history();
