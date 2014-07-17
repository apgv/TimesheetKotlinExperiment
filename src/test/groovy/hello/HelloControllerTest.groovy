package hello

import spock.lang.Specification


class HelloControllerTest extends Specification {
    def controller = new HelloController()

    def "first greeting from REST-ish service"() {
        expect:
        controller.greeting() == "Hello world!"
    }
}
