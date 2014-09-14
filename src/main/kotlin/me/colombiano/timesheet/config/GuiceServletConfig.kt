package me.colombiano.timesheet.config

import com.google.inject.servlet.GuiceServletContextListener
import com.google.inject.Injector
import javax.servlet.ServletContextEvent
import com.google.inject.Guice
import me.colombiano.timesheet.module.TimesheetModule
import me.colombiano.timesheet.module.TimeSheetGuiceServletModule
import me.colombiano.timesheet.module.TimesheetJerseyServletModule
import org.apache.shiro.SecurityUtils
import me.colombiano.timesheet.dbmigration.DatabaseMigration
import me.colombiano.timesheet.module.SecurityWebModule
import javax.servlet.ServletContext

class GuiceServletConfig : GuiceServletContextListener() {

    private var servletContext: ServletContext? = Any() as? ServletContext

    //https://github.com/google/guice/issues/603
    override fun contextInitialized(servletContextEvent: ServletContextEvent?) {
        this.servletContext = servletContextEvent?.getServletContext()
        super<GuiceServletContextListener>.contextInitialized(servletContextEvent)
    }

    override fun getInjector(): Injector? {
        val injector = Guice.createInjector(
                TimeSheetGuiceServletModule(),
                TimesheetModule(),
                SecurityWebModule(servletContext),
                TimesheetJerseyServletModule()
        )

        val securityManager = injector?.getInstance(javaClass<org.apache.shiro.mgt.SecurityManager>())
        SecurityUtils.setSecurityManager(securityManager)

        val databaseMigration = injector?.getInstance(javaClass<DatabaseMigration>())
        databaseMigration?.migrate()

        return injector
    }

}