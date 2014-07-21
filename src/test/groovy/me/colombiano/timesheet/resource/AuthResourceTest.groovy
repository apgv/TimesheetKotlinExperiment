package me.colombiano.timesheet.resource

import me.colombiano.timesheet.resource.auth.AuthResource
import spock.lang.Specification

import javax.ws.rs.core.Response


class AuthResourceTest extends Specification {
    def resource = new AuthResource()

    def "successful authentication should return 200 OK"() {
        when:
        def response = resource.login()

        then:
        response.status == Response.Status.OK.statusCode
    }
}
