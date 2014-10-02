package me.colombiano.timesheet.config

import org.glassfish.jersey.server.ResourceConfig

class JerseyApplication : ResourceConfig() {
    {
        packages("me.colombiano.timesheet.resource")
    }
}