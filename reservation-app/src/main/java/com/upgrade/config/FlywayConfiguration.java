package com.upgrade.config;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import lombok.Setter;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.migration.JavaMigration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfiguration implements ApplicationContextAware {

    @Autowired
    private DataSource dataSource;

    @Setter
    private ApplicationContext applicationContext;

    @PostConstruct
    public void migrate() {
        Flyway.configure().outOfOrder(true).dataSource(dataSource)
                .javaMigrations(applicationContext.getBeansOfType(JavaMigration.class).values().toArray(new JavaMigration[0])).load();
    }
}
