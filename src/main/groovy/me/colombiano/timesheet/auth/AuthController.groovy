package me.colombiano.timesheet.auth

import me.colombiano.timesheet.Urls
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.UsernamePasswordToken
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.view.RedirectView

import javax.servlet.http.HttpServletResponse

import static me.colombiano.timesheet.Urls.LOGIN
import static me.colombiano.timesheet.Urls.LOGIN_SUCCESS

@Controller
@RequestMapping(value = "/auth")
class AuthController {

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    RedirectView login(@RequestParam final String username, @RequestParam final String password, HttpServletResponse httpServletResponse) {
        println("login")
        final UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        SecurityUtils.subject.login(token)
        new RedirectView(LOGIN_SUCCESS.url)
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    RedirectView logout() {
        println("logout")
        SecurityUtils.subject.logout()
        new RedirectView(LOGIN.url)
    }

    @RequestMapping("/loggedin")
    void loggedin() {
        println("logged in: " + SecurityUtils.subject.isAuthenticated())
        SecurityUtils.subject.isAuthenticated()
    }
}
