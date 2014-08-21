package me.colombiano.timesheet.config

import com.google.inject.Inject
import groovy.json.JsonSlurper
import me.colombiano.timesheet.environment.Environment

import static org.apache.commons.lang3.StringUtils.isNotBlank


class MysqlConfig implements DatabaseConfig {

    private def cloudFoundryConfig

    @Inject
    MysqlConfig(final Environment environment) {
        final String cloudFoundryServicesJson = environment.cloudFoundryServices()

        if (isNotBlank(cloudFoundryServicesJson)) {
            final slurper = new JsonSlurper()
            cloudFoundryConfig = slurper.parseText(cloudFoundryServicesJson)
        }
    }

    @Override
    Integer maximumPoolSize() {
        4
    }

    @Override
    String dataSourceClassName() {
        "com.mysql.jdbc.jdbc2.optional.MysqlDataSource"
    }

    @Override
    String serverName() {
        cloudFoundryConfig?.cleardb?.getAt(0)?.credentials?.host ?: "localhost"
    }

    @Override
    String port() {
        cloudFoundryConfig?.cleardb?.getAt(0)?.credentials?.port ?: "3306"
    }

    @Override
    String databaseName() {
        cloudFoundryConfig?.cleardb?.getAt(0)?.credentials?.name ?: "timesheet"
    }

    @Override
    String user() {
        cloudFoundryConfig?.cleardb?.getAt(0)?.credentials?.username ?: "timesheet"
    }

    @Override
    String password() {
        cloudFoundryConfig?.cleardb?.getAt(0)?.credentials?.password ?: "timesheet"
    }

}
