package me.colombiano.timesheet.config


interface DatabaseConfig {

    Integer maximumPoolSize()

    String dataSourceClassName()

    String serverName()

    String port()

    String databaseName()

    String user()

    String password()
}