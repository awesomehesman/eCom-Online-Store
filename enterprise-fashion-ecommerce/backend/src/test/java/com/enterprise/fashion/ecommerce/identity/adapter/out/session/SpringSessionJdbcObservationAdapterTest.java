package com.enterprise.fashion.ecommerce.identity.adapter.out.session;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import com.enterprise.fashion.ecommerce.identity.application.SessionObservation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.simple.JdbcClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.RETURNS_SELF;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class SpringSessionJdbcObservationAdapterTest {
    @Test
    void parameterizesReferenceAndTimeAndSelectsOnlyExistenceFromConfiguredTable() {
        var jdbc = mock(JdbcClient.class);
        var statement = mock(JdbcClient.StatementSpec.class, RETURNS_SELF);
        @SuppressWarnings("unchecked")
        JdbcClient.MappedQuerySpec<Boolean> result = mock(JdbcClient.MappedQuerySpec.class);
        when(jdbc.sql(anyString())).thenReturn(statement);
        when(statement.query(Boolean.class)).thenReturn(result);
        when(result.single()).thenReturn(true, false);
        var adapter = new SpringSessionJdbcObservationAdapter(jdbc, "custom.sessions",
                Clock.fixed(Instant.ofEpochMilli(1234), ZoneOffset.UTC));
        assertThat(adapter.observe("opaque-reference")).isEqualTo(SessionObservation.PRESENT);
        assertThat(adapter.observe("opaque-reference")).isEqualTo(SessionObservation.ABSENT);
        verify(jdbc, times(2)).sql("SELECT EXISTS (SELECT 1 FROM custom.sessions"
                + " WHERE session_id = :identifier AND (max_inactive_interval < 0"
                + " OR last_access_time > CAST(:now AS NUMERIC) - max_inactive_interval::NUMERIC * 1000))");
        verify(statement, times(2)).param("identifier", "opaque-reference");
        verify(statement, times(2)).param("now", 1234L);
        verify(statement, times(2)).query(Boolean.class);
        verify(result, times(2)).single();
        verifyNoMoreInteractions(jdbc, statement, result);
    }

    @Test
    void withholdsDatabaseFailureDetails() {
        var jdbc = mock(JdbcClient.class);
        when(jdbc.sql(anyString())).thenThrow(new DataAccessResourceFailureException("private details"));
        assertThat(new SpringSessionJdbcObservationAdapter(jdbc, "identity.spring_session", Clock.systemUTC())
                .observe("reference")).isEqualTo(SessionObservation.UNCERTAIN);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"identity.sessions; DELETE FROM identity.spring_session", "a.b.c", "\"quoted\"", "a-b"})
    void rejectsUnsupportedTableConfigurationBeforeSql(String table) {
        var jdbc = mock(JdbcClient.class);
        assertThatIllegalArgumentException().isThrownBy(() ->
                new SpringSessionJdbcObservationAdapter(jdbc, table, Clock.systemUTC()));
        verifyNoInteractions(jdbc);
    }
}
