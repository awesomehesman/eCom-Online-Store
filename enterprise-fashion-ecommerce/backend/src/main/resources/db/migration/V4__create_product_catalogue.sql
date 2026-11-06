-- Product-owned identity and canonical Attribute storage for DEC-0019 standard 1.0.0.
-- Lifecycle is descriptive state only: no publication, visibility or authorization evidence.
CREATE SCHEMA product;

CREATE TABLE product.product (
    product_id TEXT COLLATE "C" PRIMARY KEY
        CHECK (product_id ~ '^[A-Za-z0-9_-]{1,128}$'),
    lifecycle TEXT COLLATE "C" NOT NULL
        CHECK (lifecycle IN ('DRAFT', 'PUBLISHED', 'ARCHIVED')),
    family TEXT COLLATE "C" NOT NULL
        CHECK (family IN ('Apparel', 'Footwear', 'Accessories')),
    attribute_standard_version TEXT COLLATE "C" NOT NULL
        CHECK (attribute_standard_version = '1.0.0'),
    size_applicable BOOLEAN NOT NULL,
    colour_applicable BOOLEAN NOT NULL,
    CONSTRAINT product_variant_context_unique UNIQUE
        (product_id, family, attribute_standard_version, size_applicable, colour_applicable)
);

CREATE TABLE product.product_variant (
    variant_id TEXT COLLATE "C" PRIMARY KEY
        CHECK (variant_id ~ '^[A-Za-z0-9_-]{1,128}$'),
    product_id TEXT COLLATE "C" NOT NULL,
    family TEXT COLLATE "C" NOT NULL,
    attribute_standard_version TEXT COLLATE "C" NOT NULL,
    size_applicable BOOLEAN NOT NULL,
    colour_applicable BOOLEAN NOT NULL,
    size_code TEXT COLLATE "C",
    colour_code TEXT COLLATE "C",
    fit TEXT,
    material TEXT,
    CONSTRAINT product_variant_owner_fk FOREIGN KEY
        (product_id, family, attribute_standard_version, size_applicable, colour_applicable)
        REFERENCES product.product
        (product_id, family, attribute_standard_version, size_applicable, colour_applicable)
        ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT product_variant_size_applicability_check
        CHECK (size_applicable = (size_code IS NOT NULL)),
    CONSTRAINT product_variant_colour_applicability_check
        CHECK (colour_applicable = (colour_code IS NOT NULL)),
    CONSTRAINT product_variant_size_check CHECK (
        size_code IS NULL OR
        (family = 'Apparel' AND size_code IN ('XS', 'S', 'M', 'L', 'XL', 'XXL', 'XXXL')) OR
        (family = 'Footwear' AND size_code IN
            ('3', '3.5', '4', '4.5', '5', '5.5', '6', '6.5', '7', '7.5', '8', '8.5',
             '9', '9.5', '10', '10.5', '11', '11.5', '12', '12.5', '13')) OR
        (family = 'Accessories' AND size_code = 'ONE_SIZE')
    ),
    CONSTRAINT product_variant_colour_check CHECK (colour_code IS NULL OR colour_code = 'BLACK'),
    -- Canonical values are stored only; labels/aliases and Fit/Material never define uniqueness.
    CONSTRAINT product_variant_configuration_unique
        UNIQUE NULLS NOT DISTINCT (product_id, size_code, colour_code)
);

-- Accepted identity and owner references cannot be changed through an UPDATE/upsert.
-- No cascade deletes, replacement workflow, retention policy or runtime grants are introduced.
CREATE FUNCTION product.preserve_product_identity() RETURNS TRIGGER
LANGUAGE plpgsql AS $$
BEGIN
    IF NEW.product_id IS DISTINCT FROM OLD.product_id THEN
        RAISE EXCEPTION 'Product identity is immutable' USING ERRCODE = '23514';
    END IF;
    RETURN NEW;
END;
$$;
CREATE TRIGGER product_identity_immutable
    BEFORE UPDATE ON product.product
    FOR EACH ROW EXECUTE FUNCTION product.preserve_product_identity();

CREATE FUNCTION product.preserve_variant_membership() RETURNS TRIGGER
LANGUAGE plpgsql AS $$
BEGIN
    IF NEW.variant_id IS DISTINCT FROM OLD.variant_id
            OR NEW.product_id IS DISTINCT FROM OLD.product_id THEN
        RAISE EXCEPTION 'Variant identity and membership are immutable' USING ERRCODE = '23514';
    END IF;
    RETURN NEW;
END;
$$;
CREATE TRIGGER product_variant_membership_immutable
    BEFORE UPDATE ON product.product_variant
    FOR EACH ROW EXECUTE FUNCTION product.preserve_variant_membership();
