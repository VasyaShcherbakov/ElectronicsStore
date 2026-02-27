package com.OnlineElectronicsStore.OnlineElectronicsStore.controller.web;

import com.OnlineElectronicsStore.OnlineElectronicsStore.model.User;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.ProductRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.UserRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthWebController {
    private static final Logger log = LoggerFactory.getLogger(AuthWebController.class);
    private final ProductRepository productRepository;

    public AuthWebController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;



    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }



    @GetMapping("/whoami")
    @ResponseBody
    public String whoAmI() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "Не авторизован";
    }



    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid User user,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            userService.registerUser(user);
            return "redirect:/login";
        } catch (Exception e) {
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
