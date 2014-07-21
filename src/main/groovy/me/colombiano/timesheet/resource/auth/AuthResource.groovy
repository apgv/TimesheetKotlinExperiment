package me.colombiano.timesheet.resource.auth

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.Response

@Path("/auth")
class AuthResource {

    @Path("/login")
    @GET
    Response login() {
        return Response.ok().build()
    }
}
