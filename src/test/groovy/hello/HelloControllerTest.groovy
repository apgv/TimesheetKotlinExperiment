package hello

import spock.lang.Specification


class HelloControllerTest extends Specification {
    def controller = new HelloController()
    def helloServiceMock = Mock(HelloService)

    void setup() {
        controller.helloService = helloServiceMock
    }

    def "controller should call service"() {
        when:
        controller.greeting()

        then:
        1 * helloServiceMock.greeting()
    }
}
