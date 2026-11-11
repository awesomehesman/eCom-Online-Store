-- Private original-image quarantine only. No public URL, acceptance, security result or publication evidence.
CREATE TABLE product.product_image_quarantine (
    asset_id UUID PRIMARY KEY,
    product_id TEXT COLLATE "C" NOT NULL
        REFERENCES product.product(product_id) ON UPDATE RESTRICT ON DELETE RESTRICT,
    sha256 TEXT COLLATE "C" NOT NULL CHECK (sha256 ~ '^[0-9a-f]{64}$'),
    detected_format TEXT COLLATE "C" NOT NULL CHECK (detected_format IN ('PNG', 'JPEG')),
    byte_length BIGINT NOT NULL CHECK (byte_length > 0),
    pixel_width INTEGER NOT NULL CHECK (pixel_width > 0),
    pixel_height INTEGER NOT NULL CHECK (pixel_height > 0),
    quarantine_status TEXT COLLATE "C" NOT NULL CHECK (quarantine_status = 'QUARANTINED'),
    created_at TIMESTAMPTZ NOT NULL,
    original_bytes BYTEA NOT NULL,
    CONSTRAINT product_image_quarantine_length_check
        CHECK (octet_length(original_bytes) = byte_length)
);

-- Ordinary application DML cannot rewrite identity, bytes, digest, ownership or quarantine status.
CREATE FUNCTION product.protect_product_image_quarantine() RETURNS TRIGGER
LANGUAGE plpgsql AS $$
BEGIN
    RAISE EXCEPTION 'Product image quarantine assets are immutable' USING ERRCODE = '23514';
END;
$$;
CREATE TRIGGER product_image_quarantine_immutable
    BEFORE UPDATE OR DELETE ON product.product_image_quarantine
    FOR EACH ROW EXECUTE FUNCTION product.protect_product_image_quarantine();
