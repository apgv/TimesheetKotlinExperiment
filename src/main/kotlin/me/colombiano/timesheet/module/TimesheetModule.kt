package me.colombiano.timesheet.module

import com.google.inject.AbstractModule
import me.colombiano.timesheet.dbmigration.FlywayMigration
import com.zaxxer.hikari.HikariConfig
import javax.sql.DataSource
import me.colombiano.timesheet.dbmigration.DatabaseMigration
import me.colombiano.timesheet.environment.Environment
import me.colombiano.timesheet.environment.SystemEnvironment
import com.zaxxer.hikari.HikariDataSource
import com.google.inject.Provides
import com.google.inject.Inject
import me.colombiano.timesheet.config.DatabaseConfig
import me.colombiano.timesheet.config.MysqlConfig
import me.colombiano.timesheet.config.SecurityConfig
import me.colombiano.timesheet.config.StormpathConfig

class TimesheetModule : AbstractModule() {

    override fun configure() {
        bind(javaClass<Environment>())!!.to(javaClass<SystemEnvironment>())
        bind(javaClass<DatabaseConfig>())!!.to(javaClass<MysqlConfig>())
        bind(javaClass<DataSource>())!!.to(javaClass<HikariDataSource>())
        bind(javaClass<DatabaseMigration>())!!.to(javaClass<FlywayMigration>())
        bind(javaClass<SecurityConfig>())!!.to(javaClass<StormpathConfig>())
    }

    Provides
    Inject
    fun hikariDataSource(databaseConfig: DatabaseConfig): HikariDataSource {
        val hikariConfig = HikariConfig()
        hikariConfig.setMaximumPoolSize(databaseConfig.maximumPoolSize())
        hikariConfig.setDataSourceClassName(databaseConfig.dataSourceClassName())
        hikariConfig.addDataSourceProperty("serverName", databaseConfig.serverName())
        hikariConfig.addDataSourceProperty("port", databaseConfig.port())
        hikariConfig.addDataSourceProperty("databaseName", databaseConfig.databaseName())
        hikariConfig.addDataSourceProperty("user", databaseConfig.username())
        hikariConfig.addDataSourceProperty("password", databaseConfig.password())
        return HikariDataSource(hikariConfig)
    }
}