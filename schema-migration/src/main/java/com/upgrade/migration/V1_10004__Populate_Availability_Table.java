package com.upgrade.migration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Component;

@Component
public class V1_10004__Populate_Availability_Table extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        NamedParameterJdbcTemplate postgresTemplate = new NamedParameterJdbcTemplate(
                new SingleConnectionDataSource(context.getConnection(), true));

        List<MapSqlParameterSource> rows = new ArrayList<>();
        LocalDate day = LocalDate.now();
        IntStream.range(1, 31).forEach(i -> {
            rows.add(new MapSqlParameterSource("day", day.plusDays(i)));
        });

        postgresTemplate.batchUpdate("insert into availability(day) values(:day)", rows.toArray(new MapSqlParameterSource[0]));
    }
}
