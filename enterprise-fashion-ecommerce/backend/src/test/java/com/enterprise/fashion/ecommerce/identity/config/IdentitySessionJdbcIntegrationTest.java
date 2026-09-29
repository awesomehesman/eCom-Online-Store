package com.enterprise.fashion.ecommerce.identity.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.Set;

import javax.sql.DataSource;

import com.enterprise.fashion.ecommerce.identity.adapter.out.session.SpringSessionJdbcRevocationAdapter;
import com.enterprise.fashion.ecommerce.identity.application.SessionRevocationOutcome;
import com.enterprise.fashion.ecommerce.identity.application.usecase.RevokeIdentitySession;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
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
        assertThat(tables).containsExactlyInAnyOrder("spring_session", "spring_session_attributes");

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
