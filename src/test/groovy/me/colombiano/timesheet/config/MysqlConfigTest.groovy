package me.colombiano.timesheet.config

import me.colombiano.timesheet.environment.Environment
import spock.lang.Specification


class MysqlConfigTest extends Specification {
    private MysqlConfig config
    private Environment environmentMock = Mock(Environment)

    final String cloudFoundryServicesJson = """{
  "cleardb": [
    {
      "name": "cleardb-ebf69",
      "label": "cleardb",
      "plan": "spark",
      "credentials": {
        "jdbcUrl": "jdbc:mysql://cleardb_user:cleardb_password@cf.cleardb.net:3306/cleardb_db_name",
        "uri": "mysql://cleardb_user:cleardb_password@cf.cleardb.net:3306/cleardb_db_name?reconnect=true",
        "name": "cleardb_db_name",
        "hostname": "cf.cleardb.net",
        "host": "cf.cleardb.net",
        "port": "3333",
        "user": "cleardb_user",
        "username": "cleardb_user",
        "password": "cleardb_password"
      }
    }
  ]
}"""

    def setup() {
        config = new MysqlConfig(environmentMock)
    }

    def "should return maximum pool size"() {
        expect:
        config.maximumPoolSize() == 4
    }

    def "should return data source class name"() {
        expect:
        config.dataSourceClassName() == "com.mysql.jdbc.jdbc2.optional.MysqlDataSource"
    }

    def "should return server name for localhost"() {
        expect:
        config.serverName() == "localhost"
    }

    def "should return server name for Cloud Foundry"() {
        given:
        environmentMock.cloudFoundryServices() >> cloudFoundryServicesJson
        config = new MysqlConfig(environmentMock)

        expect:
        config.serverName() == "cf.cleardb.net"
    }

    def "should return port number for localhost"() {
        expect:
        config.port() == "3306"
    }

    def "should return port number for Cloud Foundry"() {
        given:
        environmentMock.cloudFoundryServices() >> cloudFoundryServicesJson
        config = new MysqlConfig(environmentMock)

        expect:
        config.port() == "3333"
    }

    def "should return database name for localhost"() {
        expect:
        config.databaseName() == "timesheet"
    }

    def "should return database name for Cloud Foundry"() {
        given:
        environmentMock.cloudFoundryServices() >> cloudFoundryServicesJson
        config = new MysqlConfig(environmentMock)

        expect:
        config.databaseName() == "cleardb_db_name"
    }

    def "should return username for localhost"() {
        expect:
        config.user() == "timesheet"
    }

    def "should return username for Cloud Foundry"() {
        given:
        environmentMock.cloudFoundryServices() >> cloudFoundryServicesJson
        config = new MysqlConfig(environmentMock)

        expect:
        config.user() == "cleardb_user"
    }

    def "should return password for localhost"() {
        expect:
        config.password() == "timesheet"
    }

    def "should return password for Cloud Foundry"() {
        given:
        environmentMock.cloudFoundryServices() >> cloudFoundryServicesJson
        config = new MysqlConfig(environmentMock)

        expect:
        config.password() == "cleardb_password"
    }
}
