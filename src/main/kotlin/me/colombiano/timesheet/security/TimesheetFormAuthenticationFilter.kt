package me.colombiano.timesheet.security

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter

class TimesheetFormAuthenticationFilter : FormAuthenticationFilter() {
    {
        setLoginUrl("/")
    }
}