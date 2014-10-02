package me.colombiano.timesheet.bootstrap

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter


class TimesheetFormAuthenticationFilter : FormAuthenticationFilter() {
    {
        setLoginUrl("/")
    }
}