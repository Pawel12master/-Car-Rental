package com.example.biznes;

import com.example.biznes.controller.ReservationController;
import com.example.biznes.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.AccessType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.biznes.user.User;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class AppControler {
    private static final Logger logger = LogManager.getLogger(AppControler.class);

    private UserService userService;

    public AppControler(UserService userService){
        this.userService = userService;
    }

    @GetMapping("")
    public String homePage(){
        return "base/index";
    }

    @GetMapping("/register")
    public String registerForm(Model model){
        model.addAttribute("user", new User());

        return "base/register_form";
    }
    @PostMapping("/register")
    public String processRegister(User user){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        logger.debug("Zarejestrowano użytkownika: " + user.getEmail());
        userService.register(user);
        return "base/index";
    }
    @GetMapping("/login")
    public String login() {
        return "base/login_form";
    }

}
