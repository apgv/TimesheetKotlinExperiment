package me.colombiano.timesheet.bootstrap

import javax.servlet.annotation.WebServlet
import org.glassfish.jersey.servlet.ServletContainer
import javax.servlet.annotation.WebInitParam

WebServlet(urlPatterns = array("/rest/*"),
           initParams = array(WebInitParam(name = "javax.ws.rs.Application",
                                           value = "me.colombiano.timesheet.config.JerseyApplication")))
class TimesheetJerseyServlet : ServletContainer() {

}