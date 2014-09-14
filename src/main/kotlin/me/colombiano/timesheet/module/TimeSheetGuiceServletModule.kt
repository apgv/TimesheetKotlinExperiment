package me.colombiano.timesheet.module

import com.google.inject.servlet.ServletModule
import org.apache.shiro.guice.web.GuiceShiroFilter

class TimeSheetGuiceServletModule : ServletModule() {

    override fun configureServlets() {
        filter("/*")!!.through(javaClass<GuiceShiroFilter>())
    }
}