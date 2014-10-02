package me.colombiano.timesheet.bootstrap

import javax.servlet.annotation.WebFilter
import org.apache.shiro.web.servlet.ShiroFilter
import javax.servlet.Filter
import javax.servlet.FilterConfig
import org.apache.shiro.web.filter.mgt.FilterChainResolver
import javax.servlet.annotation.WebInitParam
import javax.servlet.FilterChain
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver
import org.apache.shiro.web.filter.mgt.DefaultFilter

WebFilter(urlPatterns = array("/*"),
          initParams = array(WebInitParam(name = "dispatcher", value = "REQUEST"),
                             WebInitParam(name = "dispatcher", value = "FORWARD"),
                             WebInitParam(name = "dispatcher", value = "INCLUDE"),
                             WebInitParam(name = "dispatcher", value = "ERROR")))
class TimesheetShiroFilter : ShiroFilter() {

}