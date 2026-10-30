-- Internal structural metadata only; no acceptance, visibility, authorization or publication evidence.
ALTER TABLE product.product
    ADD COLUMN content_name TEXT,
    ADD COLUMN content_description TEXT,
    ADD COLUMN representation_revision TEXT,
    ADD CONSTRAINT product_content_pair_check CHECK
        ((content_name IS NULL) = (content_description IS NULL)),
    ADD CONSTRAINT product_content_name_check CHECK (content_name IS NULL OR content_name ~ '[^[:space:]]'),
    ADD CONSTRAINT product_content_description_check CHECK
        (content_description IS NULL OR content_description ~ '[^[:space:]]'),
    ADD CONSTRAINT product_revision_check CHECK
        (representation_revision IS NULL OR representation_revision ~ '^v1_[0-9a-f]{64}$'),
    ADD CONSTRAINT product_content_revision_check CHECK
        (content_name IS NULL OR representation_revision IS NOT NULL);

-- Existing rows retain NULL revision: no invented content, evidence or revision backfill.
-- New adapter inserts always include a computed revision. Legacy reconstruction derives one.
ALTER TABLE product.product_variant
    ADD CONSTRAINT product_variant_owner_identity_unique UNIQUE (product_id, variant_id);

CREATE TABLE product.product_media (
    media_id TEXT COLLATE "C" PRIMARY KEY CHECK (media_id ~ '^[A-Za-z0-9_-]{1,128}$'),
    product_id TEXT COLLATE "C" NOT NULL REFERENCES product.product(product_id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    variant_id TEXT COLLATE "C",
    asset_revision TEXT COLLATE "C" NOT NULL CHECK (asset_revision ~ '^[A-Za-z0-9_-]{1,128}$'),
    url TEXT NOT NULL CHECK (url ~ '^https://'),
    alternative_text TEXT NOT NULL CHECK (alternative_text ~ '[^[:space:]]'),
    presentation_order INTEGER NOT NULL CHECK (presentation_order >= 0),
    CONSTRAINT product_media_order_unique UNIQUE (product_id, presentation_order),
    CONSTRAINT product_media_variant_owner_fk FOREIGN KEY (product_id, variant_id)
        REFERENCES product.product_variant(product_id, variant_id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE FUNCTION product.preserve_media_identity() RETURNS TRIGGER
LANGUAGE plpgsql AS $$
BEGIN
    IF NEW.media_id IS DISTINCT FROM OLD.media_id OR NEW.product_id IS DISTINCT FROM OLD.product_id THEN
        RAISE EXCEPTION 'Media identity and Product ownership are immutable' USING ERRCODE = '23514';
    END IF;
    RETURN NEW;
END;
$$;
CREATE TRIGGER product_media_identity_immutable BEFORE UPDATE ON product.product_media
    FOR EACH ROW EXECUTE FUNCTION product.preserve_media_identity();
