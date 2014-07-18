package me.colombiano.timesheet.hello

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {

    @Autowired
    HelloService helloService

    @RequestMapping("/hello/greeting")
    String greeting() {
        helloService.greeting()
    }
}
