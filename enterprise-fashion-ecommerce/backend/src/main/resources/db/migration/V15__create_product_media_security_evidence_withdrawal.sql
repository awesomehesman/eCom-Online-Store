-- Append-only target-specific evidence withdrawal. Non-authoritative. No publication evidence.
-- Does not establish acceptance, publication authority, or positive visibility eligibility.
-- No operational issuance capability is exposed by this migration.

CREATE TABLE product.product_image_evidence_withdrawal (
    withdrawal_id UUID PRIMARY KEY,

    -- Target references (exactly one must be non-null)
    technical_check_id UUID NULL
        REFERENCES product.product_image_security_check(check_id)
        ON UPDATE RESTRICT ON DELETE RESTRICT,
    malware_observation_id UUID NULL
        REFERENCES product.product_image_malware_observation(observation_id)
        ON UPDATE RESTRICT ON DELETE RESTRICT,
    binding_id UUID NULL
        REFERENCES product.product_image_association_binding(binding_id)
        ON UPDATE RESTRICT ON DELETE RESTRICT,

    -- Withdrawal metadata
    withdrawn_at TIMESTAMPTZ NOT NULL,
    withdrawal_reason TEXT COLLATE "C" NOT NULL CHECK (btrim(withdrawal_reason, E' \t\n\r\f') <> ''),
    withdrawal_source TEXT COLLATE "C" NOT NULL CHECK (withdrawal_source IN (
        'SECURITY_FINDING',
        'RIGHTS_REVOCATION',
        'PRODUCER_COMPROMISE',
        'DEFINITION_RETIREMENT',
        'EXPLICIT_REVOCATION',
        'ADVERSE_RECHECK'
    )),

    -- Non-authoritative constraint (matches V10-V14 pattern)
    authoritative_acceptance_eligible BOOLEAN NOT NULL
        CHECK (NOT authoritative_acceptance_eligible),

    -- Exactly-one-target constraint
    CONSTRAINT product_image_evidence_withdrawal_exactly_one_target CHECK (
        (CASE WHEN technical_check_id IS NOT NULL THEN 1 ELSE 0 END
       + CASE WHEN malware_observation_id IS NOT NULL THEN 1 ELSE 0 END
       + CASE WHEN binding_id IS NOT NULL THEN 1 ELSE 0 END) = 1
    )
);

-- Partial unique indexes to prevent duplicate withdrawal of the same target
CREATE UNIQUE INDEX product_image_evidence_withdrawal_technical_unique
    ON product.product_image_evidence_withdrawal (technical_check_id)
    WHERE technical_check_id IS NOT NULL;

CREATE UNIQUE INDEX product_image_evidence_withdrawal_malware_unique
    ON product.product_image_evidence_withdrawal (malware_observation_id)
    WHERE malware_observation_id IS NOT NULL;

CREATE UNIQUE INDEX product_image_evidence_withdrawal_binding_unique
    ON product.product_image_evidence_withdrawal (binding_id)
    WHERE binding_id IS NOT NULL;

-- Audit history index
CREATE INDEX product_image_evidence_withdrawal_history
    ON product.product_image_evidence_withdrawal (withdrawn_at, withdrawal_id);

-- Immutability protection function
CREATE FUNCTION product.protect_product_image_evidence_withdrawal() RETURNS TRIGGER
LANGUAGE plpgsql AS $$
BEGIN
    RAISE EXCEPTION 'Product image evidence withdrawal records are immutable'
        USING ERRCODE = '23514';
END;
$$;

-- Immutability triggers
CREATE TRIGGER product_image_evidence_withdrawal_immutable
    BEFORE UPDATE OR DELETE ON product.product_image_evidence_withdrawal
    FOR EACH ROW EXECUTE FUNCTION product.protect_product_image_evidence_withdrawal();

CREATE TRIGGER product_image_evidence_withdrawal_no_truncate
    BEFORE TRUNCATE ON product.product_image_evidence_withdrawal
    FOR EACH STATEMENT EXECUTE FUNCTION product.protect_product_image_evidence_withdrawal();
