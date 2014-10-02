package me.colombiano.timesheet.config

import org.junit.Test
import org.junit.Before
import org.mockito.Mockito.*
import me.colombiano.timesheet.environment.Environment
import org.mockito.Mockito
import org.junit.Assert.*
import org.spek.Spek
import com.google.gson.Gson
import me.colombiano.timesheet.environment.PivotalWebServiceConstants
import com.jayway.jsonpath.JsonPath

class MysqlConfigSpecs : Spek() {{

    given("a mysql config") {
        val environmentMock = mock(javaClass<Environment>())
        val mysqlConfig = MysqlConfig()
        mysqlConfig.environment(environmentMock)

        on("calling maximumPoolSize") {
            val maxPoolSize = mysqlConfig.maximumPoolSize()

            it("should return 4") {
                assertEquals(4, maxPoolSize)
            }
        }

        on("calling dataSourceClassName") {
            val dataSourceClassName = mysqlConfig.dataSourceClassName()

            it("should return 'MysqlDataSource'") {
                assertEquals("com.mysql.jdbc.jdbc2.optional.MysqlDataSource", dataSourceClassName)
            }
        }
    }

    given("a mysql config and application running on localhost") {
        val environmentMock = mock(javaClass<Environment>())
        val mysqlConfig = MysqlConfig()
        mysqlConfig.environment(environmentMock)

        on("calling serverName") {
            val serverName = mysqlConfig.serverName()

            it("should return 'localhost'") {
                assertEquals("localhost", serverName)
            }
        }

        on("calling port") {
            val port = mysqlConfig.port()

            it("should return '3306'") {
                assertEquals("3306", port)
            }
        }

        on("calling databaseName") {
            val databaseName = mysqlConfig.databaseName()

            it("should return 'timesheet") {
                assertEquals("timesheet", databaseName)
            }
        }

        on("calling username") {
            val user = mysqlConfig.username()

            it("should return 'timesheet") {
                assertEquals("timesheet", user)
            }
        }

        on("calling password") {
            val password = mysqlConfig.password()

            it("should return 'timesheet") {
                assertEquals("timesheet", password)
            }
        }
    }

    given("a mysql config and application running on Pivotal Web Services") {
        val pivotalWebServicesJsonString = """{
  "cleardb": [
    {
      "name": "cleardb-ebf69",
      "label": "cleardb",
      "plan": "spark",
      "credentials": {
        "jdbcUrl": "jdbc:mysql://cleardb_user:cleardb_password@pws.cleardb.net:3306/cleardb_db_name",
        "uri": "mysql://cleardb_user:cleardb_password@pws.cleardb.net:3306/cleardb_db_name?reconnect=true",
        "name": "cleardb_db_name",
        "hostname": "pws.cleardb.net",
        "host": "pws.cleardb.net",
        "port": "3333",
        "user": "cleardb_user",
        "username": "cleardb_user",
        "password": "cleardb_password"
      }
    }
  ]
}"""

        val environmentMock = mock(javaClass<Environment>())
        `when`(environmentMock?.pivotalWebServicesJsonString())?.thenReturn(pivotalWebServicesJsonString)

        val mysqlConfig = MysqlConfig()
        mysqlConfig.environment(environmentMock)

        on("calling serverName") {
            val serverName = mysqlConfig.serverName()

            it("should return server name from PWS environment") {
                assertEquals("pws.cleardb.net", serverName)
            }
        }

        on("calling port") {
            val port = mysqlConfig.port()

            it("should return port number from PWS environment") {
                assertEquals("3333", port)
            }
        }

        on("calling databaseName") {
            val databaseName = mysqlConfig.databaseName()

            it("should return database name from PWS environment") {
                assertEquals("cleardb_db_name", databaseName)
            }
        }

        on("calling username") {
            val user = mysqlConfig.username()

            it("should return username from PWS environment") {
                assertEquals("cleardb_user", user)
            }
        }

        on("calling password") {
            val password = mysqlConfig.password()

            it("should return password from PWS environment") {
                assertEquals("cleardb_password", password)
            }
        }
    }

}}