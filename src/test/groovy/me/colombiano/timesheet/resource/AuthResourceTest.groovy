package me.colombiano.timesheet.resource

import me.colombiano.timesheet.AbstractShiroSpecification
import me.colombiano.timesheet.resource.auth.AuthResource
import org.apache.shiro.authc.UsernamePasswordToken
import org.apache.shiro.subject.Subject

import javax.ws.rs.core.Response

class AuthResourceTest extends AbstractShiroSpecification {
    final resource = new AuthResource()
    final subjectMock = Mock(Subject)

    def setup() {
        setSubject(subjectMock)
    }

    def "authentication should call login on current subject"() {
        when:
        resource.login()

        then:
        1 * subjectMock.login(_ as UsernamePasswordToken)
    }

    def "successful authentication should return 200 OK"() {
        when:
        final response = resource.login()

        then:
        response.status == Response.Status.OK.statusCode
    }
}
