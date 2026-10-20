package com.enterprise.fashion.ecommerce.identity.adapter.out.session;

import java.time.Clock;
import java.util.Objects;

import com.enterprise.fashion.ecommerce.identity.application.SessionObservation;
import com.enterprise.fashion.ecommerce.identity.application.port.out.IdentitySessionObservationPort;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;

/** SELECT-only observation: unlike findById, never deletes expired Sessions. */
public final class SpringSessionJdbcObservationAdapter implements IdentitySessionObservationPort {
    private final JdbcClient jdbc;
    private final Clock clock;
    private final String query;

    public SpringSessionJdbcObservationAdapter(JdbcClient jdbc, String tableName, Clock clock) {
        this.jdbc = Objects.requireNonNull(jdbc);
        this.clock = Objects.requireNonNull(clock);
        // Identifiers cannot be bound parameters. Support only unquoted table/schema names.
        if (tableName == null || !tableName.matches("[A-Za-z_][A-Za-z0-9_]*(\\.[A-Za-z_][A-Za-z0-9_]*)?")) {
            throw new IllegalArgumentException("Unsupported Session table configuration");
        }
        // MapSession.isExpired: negative intervals never expire; equality is expired.
        // Use last-access + interval, just as the repository does, rather than trusting
        // a potentially inconsistent expiry_time cleanup index. Numeric avoids overflow.
        query = "SELECT EXISTS (SELECT 1 FROM " + tableName
                + " WHERE session_id = :identifier AND (max_inactive_interval < 0"
                + " OR last_access_time > CAST(:now AS NUMERIC) - max_inactive_interval::NUMERIC * 1000))";
    }

    @Override
    public SessionObservation observe(String sessionIdentifier) {
        try {
            return jdbc.sql(query).param("identifier", sessionIdentifier).param("now", clock.millis())
                    .query(Boolean.class).single() ? SessionObservation.PRESENT : SessionObservation.ABSENT;
        }
        catch (DataAccessException exception) {
            return SessionObservation.UNCERTAIN;
        }
    }
}
