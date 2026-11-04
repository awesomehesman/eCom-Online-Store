-- Preserve Variant rows/identity while atomically swapping valid canonical configurations.
-- Uniqueness remains immediate by default; the Draft replacement adapter explicitly defers
-- it inside its application-owned transaction and checks it again before returning.
ALTER TABLE product.product_variant
    DROP CONSTRAINT product_variant_configuration_unique,
    ADD CONSTRAINT product_variant_configuration_unique
        UNIQUE NULLS NOT DISTINCT (product_id, size_code, colour_code)
        DEFERRABLE INITIALLY IMMEDIATE;
