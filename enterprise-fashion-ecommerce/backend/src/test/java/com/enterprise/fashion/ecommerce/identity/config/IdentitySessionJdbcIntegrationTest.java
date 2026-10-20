package com.enterprise.fashion.ecommerce.identity.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import com.enterprise.fashion.ecommerce.identity.adapter.out.session.SpringSessionJdbcObservationAdapter;
import com.enterprise.fashion.ecommerce.identity.adapter.out.session.SpringSessionJdbcRevocationAdapter;
import com.enterprise.fashion.ecommerce.identity.application.SessionObservation;
import com.enterprise.fashion.ecommerce.identity.application.SessionRevocationOutcome;
import com.enterprise.fashion.ecommerce.identity.application.usecase.ObserveIdentitySession;
import com.enterprise.fashion.ecommerce.identity.application.usecase.RevokeIdentitySession;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.session.jdbc.JdbcIndexedSessionRepository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Tag("integration")
@SpringBootTest
@Testcontainers
class IdentitySessionJdbcIntegrationTest {

    private static final String TABLE_NAME = "identity.spring_session";

    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:18");

    @Autowired
    private DataSource dataSource;

    @Autowired
    private Environment environment;

    @Autowired
    private Flyway flyway;

    @Autowired
    private JdbcIndexedSessionRepository sessionRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private SpringSessionJdbcRevocationAdapter revocationAdapter;

    @Autowired
    private ObserveIdentitySession observations;

    @Test
    void observesActiveAndRevokedSessionsWithoutChangingStoredStateOrSecurityContext() {
        SessionRepository<Session> repository = sessionRepository(sessionRepository);
        Session created = repository.createSession();
        created.setAttribute("secret", "must-not-escape");
        repository.save(created);
        var before = storedSession(created.getId());
        var attributes = storedAttributes(created.getId());
        var authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        assertThat(observations.observe(created.getId()))
                .isEqualTo(SessionObservation.PRESENT);
        assertThat(storedSession(created.getId())).isEqualTo(before);
        assertThat(storedAttributes(created.getId())).isEqualTo(attributes);
        assertThat(SecurityContextHolder.getContext().getAuthentication())
                .isSameAs(authentication);

        // A separate repository instance writes to the same authoritative store.
        var other = new JdbcIndexedSessionRepository(jdbcTemplate, new TransactionTemplate(transactionManager));
        other.setTableName(TABLE_NAME);
        Session external = other.createSession();
        sessionRepository(other).save(external);
        assertThat(observations.observe(external.getId()))
                .isEqualTo(SessionObservation.PRESENT);

        revocationAdapter.revoke(created.getId());
        assertThat(observations.observe(created.getId()))
                .isEqualTo(SessionObservation.ABSENT);
    }

    @Test
    void observesMissingSessionWithoutCreatingRows() {
        long before = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM identity.spring_session", Long.class);
        assertThat(observations.observe("missing-session"))
                .isEqualTo(SessionObservation.ABSENT);
        assertThat(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM identity.spring_session", Long.class))
                .isEqualTo(before);
    }

    @Test
    void characterizesExpiryBoundaryWithoutDeletingOrModifyingRowsOrAttributes() {
        SessionRepository<Session> repository = sessionRepository(sessionRepository);
        Session created = repository.createSession();
        created.setAttribute("secret", "preserved");
        repository.save(created);
        long now = System.currentTimeMillis();
        jdbcTemplate.update("UPDATE identity.spring_session SET last_access_time = ?, "
                + "max_inactive_interval = 1, expiry_time = ? WHERE session_id = ?",
                now - 1000, now, created.getId());
        var adapter = new SpringSessionJdbcObservationAdapter(
                JdbcClient.create(dataSource), TABLE_NAME,
                Clock.fixed(Instant.ofEpochMilli(now), ZoneOffset.UTC));
        var before = storedSession(created.getId());
        var attributes = storedAttributes(created.getId());
        assertThat(adapter.observe(created.getId()))
                .isEqualTo(SessionObservation.ABSENT);
        assertThat(storedSession(created.getId())).isEqualTo(before);
        assertThat(storedAttributes(created.getId())).isEqualTo(attributes);

        jdbcTemplate.update("UPDATE identity.spring_session SET last_access_time = ? WHERE session_id = ?",
                now - 999, created.getId());
        assertThat(adapter.observe(created.getId()))
                .isEqualTo(SessionObservation.PRESENT);
        jdbcTemplate.update("UPDATE identity.spring_session SET max_inactive_interval = -1, "
                + "last_access_time = 0, expiry_time = 0 WHERE session_id = ?", created.getId());
        assertThat(adapter.observe(created.getId()))
                .isEqualTo(SessionObservation.PRESENT);
        jdbcTemplate.update("UPDATE identity.spring_session SET max_inactive_interval = 0, "
                + "last_access_time = ? WHERE session_id = ?", now, created.getId());
        assertThat(adapter.observe(created.getId()))
                .isEqualTo(SessionObservation.ABSENT);
    }

    @Test
    void databaseFailureIsUncertainRatherThanAbsent() {
        var adapter = new SpringSessionJdbcObservationAdapter(
                JdbcClient.create(dataSource), "identity.missing_session_table",
                Clock.systemUTC());
        assertThat(adapter.observe("opaque-session"))
                .isEqualTo(SessionObservation.UNCERTAIN);
    }

    private Map<String, Object> storedSession(String identifier) {
        return jdbcTemplate.queryForMap("SELECT * FROM identity.spring_session WHERE session_id = ?", identifier);
    }

    private List<String> storedAttributes(String identifier) {
        return jdbcTemplate.queryForList("SELECT attribute_name || ':' || encode(attribute_bytes, 'hex') "
                + "FROM identity.spring_session_attributes WHERE session_primary_id = "
                + "(SELECT primary_id FROM identity.spring_session WHERE session_id = ?) ORDER BY attribute_name",
                String.class, identifier);
    }

    @Test
    void persistsIdentitySessionsThroughFlywayOwnedPostgresqlObjects() throws Exception {
        assertPostgresql18();
        assertFlywayMigrationAndIdentityObjects();
        assertThat(environment.getProperty("spring.session.jdbc.initialize-schema")).isEqualTo("never");
        assertThat(environment.getProperty("spring.session.jdbc.table-name")).isEqualTo(TABLE_NAME);

        SessionRepository<Session> repository = sessionRepository(sessionRepository);
        Session created = repository.createSession();
        created.setAttribute("test_attribute", "shared-database-value");
        repository.save(created);

        assertThat(rowCount("identity.spring_session", "session_id", created.getId())).isOne();
        assertThat(attributeRowCount(created.getId())).isOne();

        JdbcIndexedSessionRepository reconstructedRepository = new JdbcIndexedSessionRepository(
                jdbcTemplate, new TransactionTemplate(transactionManager));
        reconstructedRepository.setTableName(TABLE_NAME);

        SessionRepository<Session> reconstructed = sessionRepository(reconstructedRepository);
        Session restored = reconstructed.findById(created.getId());
        assertThat(restored).isNotNull();
        assertThat(restored.<String>getAttribute("test_attribute")).isEqualTo("shared-database-value");

        repository.deleteById(created.getId());

        assertThat(repository.findById(created.getId())).isNull();
        assertThat(rowCount("identity.spring_session", "session_id", created.getId())).isZero();
        assertThat(attributeRowCount(created.getId())).isZero();
    }

    @Test
    void confirmsAuthoritativeAbsenceAfterRevocationOfAnExistingSession() {
        SessionRepository<Session> repository = sessionRepository(sessionRepository);
        Session created = repository.createSession();
        created.setAttribute("revocation_attribute", "removed-with-session");
        repository.save(created);

        assertThat(rowCount("identity.spring_session", "session_id", created.getId())).isOne();
        assertThat(attributeRowCount(created.getId())).isOne();

        SessionRevocationOutcome outcome = new RevokeIdentitySession(revocationAdapter)
                .revoke(created.getId());

        assertThat(outcome).isEqualTo(SessionRevocationOutcome.CONFIRMED_ABSENT);
        assertThat(repository.findById(created.getId())).isNull();
        assertThat(rowCount("identity.spring_session", "session_id", created.getId())).isZero();
        assertThat(attributeRowCount(created.getId())).isZero();
    }

    @Test
    void confirmsAuthoritativeAbsenceWhenTheSessionWasAlreadyAbsent() {
        String absentSessionIdentifier = "known-absent-session";
        SessionRepository<Session> repository = sessionRepository(sessionRepository);
        assertThat(repository.findById(absentSessionIdentifier)).isNull();

        SessionRevocationOutcome outcome = new RevokeIdentitySession(revocationAdapter)
                .revoke(absentSessionIdentifier);

        assertThat(outcome).isEqualTo(SessionRevocationOutcome.CONFIRMED_ABSENT);
        assertThat(repository.findById(absentSessionIdentifier)).isNull();
        assertThat(rowCount("identity.spring_session", "session_id", absentSessionIdentifier)).isZero();
    }

    private void assertPostgresql18() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metadata = connection.getMetaData();
            assertThat(metadata.getDatabaseProductName()).isEqualTo("PostgreSQL");
            assertThat(metadata.getDatabaseMajorVersion()).isEqualTo(18);
        }
    }

    private void assertFlywayMigrationAndIdentityObjects() {
        assertThat(flyway.info().applied())
                .anySatisfy(migration -> {
                    assertThat(migration.getVersion().getVersion()).isEqualTo("1");
                    assertThat(migration.getDescription()).isEqualTo("create identity session schema");
                });

        assertThat(jdbcTemplate.queryForObject(
                "SELECT EXISTS (SELECT 1 FROM information_schema.schemata WHERE schema_name = 'identity')",
                Boolean.class)).isTrue();

        Set<String> tables = Set.copyOf(jdbcTemplate.queryForList(
                "SELECT table_name FROM information_schema.tables WHERE table_schema = 'identity'",
                String.class));
        assertThat(tables).containsExactlyInAnyOrder("spring_session", "spring_session_attributes",
                "credential_source_scope", "credential_publication", "login_email_binding_scope",
                "login_email_binding");

        Set<String> indexes = Set.copyOf(jdbcTemplate.queryForList(
                "SELECT indexname FROM pg_indexes WHERE schemaname = 'identity'",
                String.class));
        assertThat(indexes).contains(
                "spring_session_pk",
                "spring_session_attributes_pk",
                "idx_spring_session_session_id",
                "idx_spring_session_expiry_time",
                "idx_spring_session_principal_name");

        assertThat(jdbcTemplate.queryForObject("""
                SELECT EXISTS (
                    SELECT 1
                    FROM information_schema.table_constraints
                    WHERE constraint_schema = 'identity'
                      AND table_name = 'spring_session_attributes'
                      AND constraint_name = 'spring_session_attributes_fk'
                      AND constraint_type = 'FOREIGN KEY'
                )
                """, Boolean.class)).isTrue();
    }

    private int rowCount(String table, String column, String value) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM " + table + " WHERE " + column + " = ?", Integer.class, value);
        return count != null ? count : 0;
    }

    private int attributeRowCount(String sessionId) {
        Integer count = jdbcTemplate.queryForObject("""
                SELECT COUNT(*)
                FROM identity.spring_session_attributes attributes
                JOIN identity.spring_session sessions
                  ON sessions.primary_id = attributes.session_primary_id
                WHERE sessions.session_id = ?
                """, Integer.class, sessionId);
        return count != null ? count : 0;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private SessionRepository<Session> sessionRepository(JdbcIndexedSessionRepository repository) {
        return (SessionRepository) repository;
    }
}
