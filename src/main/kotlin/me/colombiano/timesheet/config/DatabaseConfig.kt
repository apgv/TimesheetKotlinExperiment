package me.colombiano.timesheet.config


trait DatabaseConfig {

    fun maximumPoolSize(): Int

    fun dataSourceClassName(): String

    fun serverName(): String

    fun port(): String

    fun databaseName(): String

    fun username(): String

    fun password(): String
}