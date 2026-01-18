package com.OnlineElectronicsStore.OnlineElectronicsStore.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthWebController {

    // 👉 страница логина
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // templates/login.html
    }

    // 👉 страница регистрации (позже сделаем)
    @GetMapping("/register")
    public String registerPage() {
        return "register"; // templates/register.html
    }
}
