package me.colombiano.timesheet.dbmigration

import com.google.inject.Inject
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.flywaydb.core.Flyway

import javax.sql.DataSource

class FlywayMigration implements DatabaseMigration {

    @Inject
    private DataSource dataSource

    private final Logger logger = LogManager.getLogger(FlywayMigration.class.name)

    @Override
    void migrate() {
        logger.info("Starting database migration")

        final flyway = new Flyway()
        flyway.dataSource = dataSource

        flyway.migrate()
    }
}
