package me.colombiano.timesheet.resource.auth

import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.UsernamePasswordToken

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.Response

@Path("/auth")
class AuthResource {

    @Path("/login")
    @GET
    Response login() {
        SecurityUtils.subject.login(new UsernamePasswordToken("username", "password"))
        Response.ok().build()
    }
}
