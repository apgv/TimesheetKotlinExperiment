package me.colombiano.timesheet.dbmigration

import javax.sql.DataSource
import org.flywaydb.core.Flyway
import com.google.inject.Inject
import org.apache.logging.log4j.LogManager

class FlywayMigration : DatabaseMigration {

    private var dataSource: DataSource? = Any() as? DataSource
    private val logger = LogManager.getLogger(javaClass<FlywayMigration>().getName())


    Inject
    fun dataSource(dataSource: DataSource) {
        this.dataSource = dataSource
    }

    override fun migrate() {
        logger?.info("Starting database migration")

        val flyway = Flyway()
        flyway.setDataSource(dataSource)

        flyway.migrate()
    }
}