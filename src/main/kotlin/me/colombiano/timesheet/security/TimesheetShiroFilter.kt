package me.colombiano.timesheet.security

import javax.servlet.annotation.WebFilter
import org.apache.shiro.web.servlet.ShiroFilter
import javax.servlet.annotation.WebInitParam

WebFilter(urlPatterns = array("/*"),
          initParams = array(WebInitParam(name = "dispatcher", value = "REQUEST"),
                             WebInitParam(name = "dispatcher", value = "FORWARD"),
                             WebInitParam(name = "dispatcher", value = "INCLUDE"),
                             WebInitParam(name = "dispatcher", value = "ERROR")))
class TimesheetShiroFilter : ShiroFilter() {

}