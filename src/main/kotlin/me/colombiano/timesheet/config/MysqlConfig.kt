package me.colombiano.timesheet.config

import me.colombiano.timesheet.environment.Environment
import org.apache.commons.lang3.StringUtils.*
import com.jayway.jsonpath.JsonPath
import me.colombiano.timesheet.environment.PivotalWebServiceConstants.*
import me.colombiano.timesheet.environment.PivotalWebServiceConstants


class MysqlConfig() : DatabaseConfig {
    
    private var pivotalWebServicesJsonString: String? = ""

    fun environment(environment: Environment?) {
        pivotalWebServicesJsonString = environment?.pivotalWebServicesJsonString()
    }

    private fun pivotalWebServicesConfig(jsonAttributeId: PivotalWebServiceConstants): String? {
        if (isNotBlank(pivotalWebServicesJsonString)) {
            return JsonPath.read<String>(pivotalWebServicesJsonString, "$.cleardb[0].credentials.${jsonAttributeId.value}")
        }

        return null
    }

    override fun maximumPoolSize(): Int {
        return 4
    }

    override fun dataSourceClassName(): String {
        return "com.mysql.jdbc.jdbc2.optional.MysqlDataSource"
    }

    override fun serverName(): String {
        return pivotalWebServicesConfig(HOST) ?: "localhost"
    }

    override fun port(): String {
        return pivotalWebServicesConfig(PORT) ?: "3306"
    }

    override fun databaseName(): String {
        return pivotalWebServicesConfig(NAME) ?: "timesheet"
    }

    override fun username(): String {
        return pivotalWebServicesConfig(USERNAME) ?: "timesheet"
    }

    override fun password(): String {
        return pivotalWebServicesConfig(PASSWORD) ?: "timesheet"
    }
}