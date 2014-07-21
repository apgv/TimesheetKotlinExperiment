package me.colombiano.timesheet.config

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.servlet.GuiceServletContextListener
import com.google.inject.servlet.ServletModule
import com.sun.jersey.guice.JerseyServletModule
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer
import me.colombiano.timesheet.resource.auth.AuthResource
import org.apache.shiro.SecurityUtils
import org.apache.shiro.guice.web.GuiceShiroFilter
import org.apache.shiro.mgt.SecurityManager

import javax.servlet.ServletContext
import javax.servlet.ServletContextEvent

class GuiceServletConfig extends GuiceServletContextListener {

    private ServletContext servletContext

//    https://github.com/google/guice/issues/603
    @Override
    void contextInitialized(final ServletContextEvent servletContextEvent) {
        servletContext = servletContextEvent.servletContext
        super.contextInitialized(servletContextEvent)
    }

    @Override
    protected Injector getInjector() {
        final injector = Guice.createInjector(
                new ServletModule() {
                    @Override
                    protected void configureServlets() {
                        filter("/*").through(GuiceShiroFilter.class)
                    }
                },
                new SecurityWebModule(servletContext),
                new JerseyServletModule() {
                    @Override
                    protected void configureServlets() {
                        bind(AuthResource.class)
                        serve("/rest/*").with(GuiceContainer.class)
                    }
                }
        )

        final securityManager = injector.getInstance(SecurityManager.class)
        SecurityUtils.setSecurityManager(securityManager)
        injector
    }

}
