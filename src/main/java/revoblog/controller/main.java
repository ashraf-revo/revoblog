package revoblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import revoblog.repository.userRepository;
import revoblog.service.UserSecurity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ashraf on 8/2/15.
 */
@Controller
class main {

    @Autowired
    userRepository userRepository;
    @Autowired
    UserSecurity userSecurity;

    @RequestMapping(value = {"","/"})
    public String index() {
        return "redirect:/index.html";
    }


    @RequestMapping(value = "/signup")
    public String signup(WebRequest request, HttpServletRequest req, HttpServletResponse res) {
        userSecurity.handSignUp(request, req, res);
        return "redirect:/";
    }

    @RequestMapping("/csrf")
    @ResponseBody
    public CsrfToken csrf(CsrfToken token) {
        return token;
    }

}
