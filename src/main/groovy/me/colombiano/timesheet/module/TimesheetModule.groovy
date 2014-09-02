package me.colombiano.timesheet.module

import com.google.inject.AbstractModule
import com.google.inject.Inject
import com.google.inject.Provides
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import me.colombiano.timesheet.config.DatabaseConfig
import me.colombiano.timesheet.config.SecurityConfig
import me.colombiano.timesheet.config.StormpathConfig
import me.colombiano.timesheet.dbmigration.DatabaseMigration
import me.colombiano.timesheet.dbmigration.FlywayMigration
import me.colombiano.timesheet.environment.Environment
import me.colombiano.timesheet.config.MysqlConfig
import me.colombiano.timesheet.environment.SystemEnvironment

import javax.sql.DataSource


class TimesheetModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DatabaseMigration).to(FlywayMigration.class)
        bind(Environment.class).to(SystemEnvironment.class)
        bind(SecurityConfig.class).to(StormpathConfig.class)
        bind(DatabaseConfig.class).to(MysqlConfig.class)
        bind(DataSource.class).to(HikariDataSource.class)
    }

    @Provides
    @Inject
    HikariDataSource hikariDataSource(final DatabaseConfig databaseConfig) {
        final hikariConfig = new HikariConfig()
        hikariConfig.with {
            maximumPoolSize = databaseConfig.maximumPoolSize()
            dataSourceClassName = databaseConfig.dataSourceClassName()
            addDataSourceProperty("serverName", databaseConfig.serverName())
            addDataSourceProperty("port", databaseConfig.port())
            addDataSourceProperty("databaseName", databaseConfig.databaseName())
            addDataSourceProperty("user", databaseConfig.user())
            addDataSourceProperty("password", databaseConfig.password())
        }

        new HikariDataSource(hikariConfig)
    }
}
