package me.colombiano.timesheet.resource

import javax.ws.rs.Path
import javax.ws.rs.core.Response
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.UsernamePasswordToken
import javax.ws.rs.GET


Path("/auth")
class AuthResource {

    Path("/login")
    GET
    fun login(): Response? {
        SecurityUtils.getSubject()?.login(UsernamePasswordToken("username", "password"))
        return Response.ok()?.build()
    }

}