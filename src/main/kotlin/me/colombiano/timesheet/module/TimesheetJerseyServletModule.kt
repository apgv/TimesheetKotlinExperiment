package me.colombiano.timesheet.module

import com.google.inject.servlet.ServletModule
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer
import me.colombiano.timesheet.resource.AuthResource

class TimesheetJerseyServletModule : ServletModule() {

    override fun configureServlets() {
        bind(javaClass<AuthResource>())
        serve("/rest/*")?.with(javaClass<GuiceContainer>())
    }
}