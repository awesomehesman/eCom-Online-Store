-- Append-only technical observations. These rows are not authoritative publication evidence.
ALTER TABLE product.product_image_quarantine
    ADD CONSTRAINT product_image_quarantine_asset_owner_unique UNIQUE (asset_id, product_id);

CREATE TABLE product.product_image_security_check (
    check_id UUID PRIMARY KEY,
    asset_id UUID NOT NULL,
    product_id TEXT COLLATE "C" NOT NULL,
    original_sha256 TEXT COLLATE "C" NOT NULL CHECK (original_sha256 ~ '^[0-9a-f]{64}$'),
    check_definition TEXT COLLATE "C" NOT NULL CHECK (check_definition <> ''),
    implementation_version TEXT COLLATE "C" NOT NULL CHECK (implementation_version <> ''),
    executed_at TIMESTAMPTZ NOT NULL,
    outcome TEXT COLLATE "C" NOT NULL CHECK (outcome IN ('TECHNICAL_CHECKS_PASSED', 'REJECTED')),
    failure_reason TEXT COLLATE "C" NULL CHECK (failure_reason IN
        ('MALFORMED_CONTENT', 'UNSUPPORTED_FORMAT', 'RESOURCE_LIMIT_EXCEEDED')),
    malware_scan_status TEXT COLLATE "C" NOT NULL CHECK (malware_scan_status = 'NOT_PERFORMED'),
    authoritative_acceptance_eligible BOOLEAN NOT NULL CHECK (NOT authoritative_acceptance_eligible),
    CONSTRAINT product_image_security_check_outcome_detail CHECK
        ((outcome = 'REJECTED') = (failure_reason IS NOT NULL)),
    CONSTRAINT product_image_security_check_asset_owner_fk FOREIGN KEY (asset_id, product_id)
        REFERENCES product.product_image_quarantine (asset_id, product_id) ON UPDATE RESTRICT ON DELETE RESTRICT
);
CREATE INDEX product_image_security_check_asset_history
    ON product.product_image_security_check (asset_id, executed_at, check_id);

CREATE FUNCTION product.protect_product_image_security_check() RETURNS TRIGGER
LANGUAGE plpgsql AS $$
BEGIN
    RAISE EXCEPTION 'Product image security check history is immutable' USING ERRCODE = '23514';
END;
$$;
CREATE TRIGGER product_image_security_check_immutable
    BEFORE UPDATE OR DELETE ON product.product_image_security_check
    FOR EACH ROW EXECUTE FUNCTION product.protect_product_image_security_check();
