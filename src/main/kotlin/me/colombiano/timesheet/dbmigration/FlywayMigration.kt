package me.colombiano.timesheet.dbmigration

import javax.sql.DataSource
import org.flywaydb.core.Flyway
import org.apache.logging.log4j.LogManager

class FlywayMigration(val dataSource: DataSource?) : DatabaseMigration {

    private val logger = LogManager.getLogger(javaClass<FlywayMigration>().getName())

    override fun migrate() {
        logger?.info("Starting database migration")
        val flyway = Flyway()
        flyway.setDataSource(dataSource)
        flyway.migrate()
    }
}