package me.colombiano.timesheet.resource

import me.colombiano.timesheet.AbstractShiroSpecs
import org.mockito.Mockito.*
import org.mockito.Matchers.*
import org.apache.shiro.subject.Subject
import org.apache.shiro.authc.UsernamePasswordToken
import kotlin.test.*
import javax.ws.rs.core.Response


class AuthResourceSpecs : AbstractShiroSpecs() {{

    given("an auth resouce") {
        val resource = AuthResource()
        val subjectMock = mock(javaClass<Subject>())

        setSubject(subjectMock)

        on("when calling login") {
            val response = resource.login()

            it("should call login on current subject") {
                verify(subjectMock)?.login(any(javaClass<UsernamePasswordToken>()))
            }

            it("should return 200 OK on successful login") {
                assertEquals(Response.Status.OK.getStatusCode(), response?.getStatus())
            }
        }
    }
}}