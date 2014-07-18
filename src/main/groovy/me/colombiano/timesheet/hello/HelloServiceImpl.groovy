package me.colombiano.timesheet.hello

import org.springframework.stereotype.Service

@Service("HelloService")
class HelloServiceImpl implements HelloService {

    @Override
    String greeting() {
        "Hello world!"
    }
}
