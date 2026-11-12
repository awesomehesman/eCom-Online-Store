-- Append-only structural binding only. No acceptance, publication evidence or visibility eligibility.
CREATE TABLE product.product_image_association_binding (
    binding_id UUID PRIMARY KEY,
    product_id TEXT COLLATE "C" NOT NULL
        REFERENCES product.product(product_id) ON UPDATE RESTRICT ON DELETE RESTRICT,
    product_revision TEXT COLLATE "C" NOT NULL
        CHECK (product_revision ~ '^v1_[0-9a-f]{64}$'),
    media_association_id TEXT COLLATE "C" NOT NULL
        CHECK (media_association_id ~ '^[A-Za-z0-9_-]{1,128}$'),
    association_revision TEXT COLLATE "C" NOT NULL
        CHECK (association_revision ~ '^v1_[0-9a-f]{64}$'),
    asset_id UUID NOT NULL,
    asset_revision TEXT COLLATE "C" NOT NULL
        CHECK (asset_revision ~ '^sha256_[0-9a-f]{64}$'),
    original_sha256 TEXT COLLATE "C" NOT NULL
        CHECK (original_sha256 ~ '^[0-9a-f]{64}$'),
    variant_id TEXT COLLATE "C",
    bound_at TIMESTAMPTZ NOT NULL,
    authoritative_acceptance_eligible BOOLEAN NOT NULL
        CHECK (NOT authoritative_acceptance_eligible),
    CONSTRAINT piab_matching_revisions_check
        CHECK (product_revision = association_revision),
    CONSTRAINT piab_derived_asset_revision_check
        CHECK (asset_revision = 'sha256_' || original_sha256),
    CONSTRAINT product_image_association_binding_asset_fk
        FOREIGN KEY (asset_id, product_id, original_sha256)
        REFERENCES product.product_image_quarantine(asset_id, product_id, sha256)
        ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT product_image_association_binding_exact_unique
        UNIQUE (product_id, product_revision, media_association_id, asset_id)
);

CREATE INDEX product_image_association_binding_media_history
    ON product.product_image_association_binding
        (product_id, media_association_id, bound_at, binding_id);

-- Serialize binding with Draft representation replacement and verify the exact current association.
CREATE FUNCTION product.validate_product_image_association_binding() RETURNS TRIGGER
LANGUAGE plpgsql AS $$
BEGIN
    PERFORM 1
      FROM product.product
     WHERE product_id = NEW.product_id
       AND lifecycle = 'DRAFT'
       AND representation_revision = NEW.product_revision
     FOR UPDATE;
    IF NOT FOUND THEN
        RAISE EXCEPTION 'Product image binding requires the exact current Draft revision'
            USING ERRCODE = '23514';
    END IF;

    IF NOT EXISTS (
        SELECT 1
          FROM product.product_media
         WHERE media_id = NEW.media_association_id
           AND product_id = NEW.product_id
           AND asset_revision = NEW.asset_revision
           AND variant_id IS NOT DISTINCT FROM NEW.variant_id
    ) THEN
        RAISE EXCEPTION 'Product image binding does not match current media association'
            USING ERRCODE = '23514';
    END IF;
    RETURN NEW;
END;
$$;

CREATE TRIGGER product_image_association_binding_validate
    BEFORE INSERT ON product.product_image_association_binding
    FOR EACH ROW EXECUTE FUNCTION product.validate_product_image_association_binding();

CREATE FUNCTION product.preserve_product_image_association_binding() RETURNS TRIGGER
LANGUAGE plpgsql AS $$
BEGIN
    RAISE EXCEPTION 'Product image association binding history is immutable'
        USING ERRCODE = '23514';
END;
$$;

CREATE TRIGGER product_image_association_binding_immutable
    BEFORE UPDATE OR DELETE ON product.product_image_association_binding
    FOR EACH ROW EXECUTE FUNCTION product.preserve_product_image_association_binding();
CREATE TRIGGER product_image_association_binding_no_truncate
    BEFORE TRUNCATE ON product.product_image_association_binding
    FOR EACH STATEMENT EXECUTE FUNCTION product.preserve_product_image_association_binding();
