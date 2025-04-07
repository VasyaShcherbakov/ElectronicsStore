package com.OnlineElectronicsStore.OnlineElectronicsStore.controller;

import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Product;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.User;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.ProductRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.UserRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller

public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final ProductRepository productRepository;

    public AuthController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/register")
    public String showRegisterForm() {
        System.out.println("==========================================");
        return "register";
    }

    @GetMapping("/whoami")
    @ResponseBody
    public String whoAmI() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "Не авторизован";
    }



    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        try {
            System.out.println("Регистрация нового пользователя = Контроллер");
            userService.registerUser(user);
            return "redirect:/login";
        } catch (Exception e) {
            System.out.println("Ошибка при регистрации" + e);
            model.addAttribute("error", "Ошибка регистрации: " + e.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        System.out.println("LOGIN GET");
        return "login";
    }
    @GetMapping("/user/home")
    public String redirectToUserHome() {
        return "redirect:/user/home/main";
    }

}


