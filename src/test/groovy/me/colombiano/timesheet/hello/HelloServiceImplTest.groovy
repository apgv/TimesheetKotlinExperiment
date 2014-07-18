package me.colombiano.timesheet.hello

import spock.lang.Specification


class HelloServiceImplTest extends Specification {
    def helloService = new HelloServiceImpl()

    def "Greeting should return expected greeting"() {
        expect:
        helloService.greeting() == "Hello world!"
    }
}
