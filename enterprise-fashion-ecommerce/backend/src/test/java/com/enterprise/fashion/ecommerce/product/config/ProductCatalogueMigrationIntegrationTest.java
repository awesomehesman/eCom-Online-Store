package com.enterprise.fashion.ecommerce.product.config;

import java.util.UUID;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Direct synthetic SQL tests storage safeguards, not lawful publication or production population. */
@Tag("integration")
@SpringBootTest
@Testcontainers
class ProductCatalogueMigrationIntegrationTest {
    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:18");

    @Autowired private JdbcTemplate jdbc;
    @Autowired private Flyway flyway;

    private String id() { return UUID.randomUUID().toString(); }

    private String product(String family, boolean sized, boolean coloured) {
        String id = id();
        jdbc.update("""
                INSERT INTO product.product
                    (product_id, lifecycle, family, attribute_standard_version, size_applicable, colour_applicable)
                VALUES (?, 'DRAFT', ?, '1.0.0', ?, ?)
                """, id, family, sized, coloured);
        return id;
    }

    private void variant(String variant, String owner, String family, boolean sized, boolean coloured,
            String size, String colour) {
        jdbc.update("""
                INSERT INTO product.product_variant
                    (variant_id, product_id, family, attribute_standard_version,
                     size_applicable, colour_applicable, size_code, colour_code)
                VALUES (?, ?, ?, '1.0.0', ?, ?, ?, ?)
                """, variant, owner, family, sized, coloured, size, colour);
    }

    @Test
    void flywayValidatesExistingMigrationsAndAppliesV4() {
        flyway.validate();
        assertThat(flyway.info().applied()).extracting(m -> m.getVersion().getVersion())
                .contains("1", "2", "3", "4");
        assertThat(jdbc.queryForObject("SELECT current_setting('server_version_num')::int", Integer.class))
                .isBetween(180000, 189999);
    }

    @Test
    void identityConstraintsRejectDuplicatesInvalidAndMissingReferences() {
        String owner = product("Apparel", true, true);
        String variant = id();
        variant(variant, owner, "Apparel", true, true, "M", "BLACK");
        assertThatThrownBy(() -> variant(variant, owner, "Apparel", true, true, "L", "BLACK"))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThatThrownBy(() -> jdbc.update("INSERT INTO product.product SELECT * FROM product.product WHERE product_id = ?", owner))
                .isInstanceOf(DataIntegrityViolationException.class);
        for (String bad : new String[]{null, "", "bad/id", "a".repeat(129)}) {
            assertThatThrownBy(() -> variant(bad, owner, "Apparel", true, true, "L", "BLACK"))
                    .isInstanceOf(DataIntegrityViolationException.class);
            assertThatThrownBy(() -> jdbc.update("""
                    INSERT INTO product.product VALUES (?, 'DRAFT', 'Apparel', '1.0.0', true, true)
                    """, bad)).isInstanceOf(DataIntegrityViolationException.class);
        }
        assertThatThrownBy(() -> variant(id(), id(), "Apparel", true, true, "M", "BLACK"))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThatThrownBy(() -> variant(id(), null, "Apparel", true, true, "M", "BLACK"))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void rejectsDuplicateCanonicalConfigurationsEvenWhenDescriptionsDiffer() {
        String owner = product("Apparel", true, true);
        String first = id();
        variant(first, owner, "Apparel", true, true, "M", "BLACK");
        jdbc.update("UPDATE product.product_variant SET fit = 'Regular', material = 'Cotton' WHERE variant_id = ?", first);
        assertThatThrownBy(() -> variant(id(), owner, "Apparel", true, true, "M", "BLACK"))
                .isInstanceOf(DataIntegrityViolationException.class);
        variant(id(), owner, "Apparel", true, true, "L", "BLACK");
        variant(id(), product("Apparel", true, true), "Apparel", true, true, "M", "BLACK");
    }

    @Test
    void nullDimensionsStillEnforceUniquenessForEveryAccessoryApplicabilityCombination() {
        for (boolean sized : new boolean[]{false, true}) {
            for (boolean coloured : new boolean[]{false, true}) {
                String owner = product("Accessories", sized, coloured);
                String size = sized ? "ONE_SIZE" : null;
                String colour = coloured ? "BLACK" : null;
                variant(id(), owner, "Accessories", sized, coloured, size, colour);
                assertThatThrownBy(() -> variant(id(), owner, "Accessories", sized, coloured, size, colour))
                        .isInstanceOf(DataIntegrityViolationException.class);
                variant(id(), product("Accessories", sized, coloured), "Accessories", sized, coloured, size, colour);
            }
        }
    }

    @Test
    void checksExplicitApplicabilityAndParentContext() {
        String owner = product("Apparel", true, true);
        assertThatThrownBy(() -> variant(id(), owner, "Apparel", true, true, null, "BLACK"))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThatThrownBy(() -> variant(id(), owner, "Apparel", true, true, "M", null))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThatThrownBy(() -> variant(id(), owner, "Apparel", false, true, null, "BLACK"))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThatThrownBy(() -> variant(id(), owner, "Footwear", true, true, "3", "BLACK"))
                .isInstanceOf(DataIntegrityViolationException.class);
        String accessory = product("Accessories", false, false);
        assertThatThrownBy(() -> variant(id(), accessory, "Accessories", false, false, "ONE_SIZE", null))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThatThrownBy(() -> variant(id(), accessory, "Accessories", false, false, null, "BLACK"))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void canonicalStorageRejectsAliasesUnsupportedValuesAndNumericAlternateSpellings() {
        String apparel = product("Apparel", true, true);
        for (String size : new String[]{"m", "XXXXL", "ONE_SIZE", "3", " M"}) {
            assertThatThrownBy(() -> variant(id(), apparel, "Apparel", true, true, size, "BLACK"))
                    .isInstanceOf(DataIntegrityViolationException.class);
        }
        for (String colour : new String[]{"Black", "black", "BLUE", "BLACK "}) {
            assertThatThrownBy(() -> variant(id(), apparel, "Apparel", true, true, "M", colour))
                    .isInstanceOf(DataIntegrityViolationException.class);
        }
        String footwear = product("Footwear", true, false);
        for (String size : new String[]{"3.0", "3.50", "3.25", "2.5", "13.5", "UK 3"}) {
            assertThatThrownBy(() -> variant(id(), footwear, "Footwear", true, false, size, null))
                    .isInstanceOf(DataIntegrityViolationException.class);
        }
        for (int half = 6; half <= 26; half++) {
            String size = java.math.BigDecimal.valueOf(half).divide(java.math.BigDecimal.valueOf(2))
                    .stripTrailingZeros().toPlainString();
            variant(id(), footwear, "Footwear", true, false, size, null);
        }
    }

    @Test
    void requiredProductContextAndClosedVocabulariesAreEnforced() {
        String owner = product("Apparel", true, true);
        for (String column : new String[]{"lifecycle", "family", "attribute_standard_version", "size_applicable", "colour_applicable"}) {
            assertThatThrownBy(() -> jdbc.update("UPDATE product.product SET " + column + " = NULL WHERE product_id = ?", owner))
                    .isInstanceOf(DataIntegrityViolationException.class);
        }
        for (String lifecycle : new String[]{"DRAFT", "PUBLISHED", "ARCHIVED"}) {
            jdbc.update("UPDATE product.product SET lifecycle = ? WHERE product_id = ?", lifecycle, owner);
        }
        assertThatThrownBy(() -> jdbc.update("UPDATE product.product SET lifecycle = 'ACTIVE' WHERE product_id = ?", owner))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThatThrownBy(() -> jdbc.update("UPDATE product.product SET family = 'Other' WHERE product_id = ?", owner))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThatThrownBy(() -> jdbc.update("UPDATE product.product SET attribute_standard_version = '2.0.0' WHERE product_id = ?", owner))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void canonicalColumnsUseExplicitCollationAndIdentifiersRemainCaseSensitive() {
        assertThat(jdbc.queryForList("""
                SELECT DISTINCT collation_name FROM information_schema.columns
                WHERE table_schema = 'product'
                  AND column_name IN ('product_id', 'variant_id', 'family',
                      'attribute_standard_version', 'size_code', 'colour_code', 'lifecycle')
                """, String.class)).containsExactly("C");
        String lower = "p" + id();
        String upper = lower.toUpperCase(java.util.Locale.ROOT);
        for (String value : new String[]{lower, upper}) {
            jdbc.update("INSERT INTO product.product VALUES (?, 'DRAFT', 'Accessories', '1.0.0', false, false)", value);
        }
        assertThat(jdbc.queryForObject("SELECT count(*) FROM product.product WHERE product_id IN (?, ?)",
                Integer.class, lower, upper)).isEqualTo(2);
    }

    @Test
    void variantUpdatesCannotBypassUniquenessOrStandardCompatibility() {
        String owner = product("Apparel", true, true);
        String first = id();
        String second = id();
        variant(first, owner, "Apparel", true, true, "M", "BLACK");
        variant(second, owner, "Apparel", true, true, "L", "BLACK");
        assertThatThrownBy(() -> jdbc.update("UPDATE product.product_variant SET size_code = 'M' WHERE variant_id = ?", second))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThatThrownBy(() -> jdbc.update("UPDATE product.product_variant SET attribute_standard_version = '2.0.0' WHERE variant_id = ?", first))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThat(jdbc.queryForObject("SELECT size_code FROM product.product_variant WHERE variant_id = ?", String.class, second))
                .isEqualTo("L");
    }

    @Test
    void preservesIdentityMembershipAndParentReferencesWithoutCascadeDeletion() {
        String owner = product("Apparel", true, true);
        String other = product("Apparel", true, true);
        String variant = id();
        variant(variant, owner, "Apparel", true, true, "M", "BLACK");
        assertThatThrownBy(() -> jdbc.update("UPDATE product.product_variant SET product_id = ? WHERE variant_id = ?", other, variant))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThatThrownBy(() -> jdbc.update("UPDATE product.product_variant SET variant_id = ? WHERE variant_id = ?", id(), variant))
                .isInstanceOf(DataIntegrityViolationException.class);
        String empty = product("Accessories", false, false);
        assertThatThrownBy(() -> jdbc.update("UPDATE product.product SET product_id = ? WHERE product_id = ?", id(), empty))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThatThrownBy(() -> jdbc.update("DELETE FROM product.product WHERE product_id = ?", owner))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThatThrownBy(() -> jdbc.update("UPDATE product.product SET size_applicable = false WHERE product_id = ?", owner))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThat(jdbc.queryForObject("SELECT product_id FROM product.product_variant WHERE variant_id = ?", String.class, variant))
                .isEqualTo(owner);
    }
}
