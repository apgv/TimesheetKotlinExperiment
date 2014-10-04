package me.colombiano.timesheet.bootstrap

import javax.servlet.ServletContextEvent
import javax.servlet.annotation.WebListener
import org.apache.shiro.web.env.EnvironmentLoader
import org.apache.shiro.web.env.EnvironmentLoaderListener
import javax.servlet.ServletContext
import com.zaxxer.hikari.HikariDataSource
import com.zaxxer.hikari.HikariConfig
import me.colombiano.timesheet.config.DatabaseConfig
import me.colombiano.timesheet.config.MysqlConfig
import javax.sql.DataSource
import me.colombiano.timesheet.dbmigration.FlywayMigration
import me.colombiano.timesheet.security

WebListener
class TimesheetEnvironmentLoaderListener : security.TimesheetShiroSecurity, EnvironmentLoaderListener() {
    private val dataSource: HikariDataSource = hikariDataSource(MysqlConfig())

    override fun contextInitialized(sce: ServletContextEvent?) {
        secureWebApplication(sce?.getServletContext())
        migrateDatabase(dataSource)
    }

    override fun contextDestroyed(sce: ServletContextEvent?) {
        dataSource.shutdown()
        super<EnvironmentLoaderListener>.contextDestroyed(sce)
    }

    private fun migrateDatabase(dataSource: DataSource) {
        val flywayMigration = FlywayMigration(dataSource)
        flywayMigration.migrate()
    }

    private fun hikariDataSource(databaseConfig: DatabaseConfig): HikariDataSource {
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