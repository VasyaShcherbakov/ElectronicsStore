package com.OnlineElectronicsStore.OnlineElectronicsStore.controller;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Cart;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Product;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Role;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.User;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.CartRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.ProductRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.UserRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Controller

public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/register")
    public String showRegisterForm() {
        System.out.println("==========================================");
        return "register";
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
    public String userHome(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            System.out.println("Ошибка: пользователь не аутентифицирован");
            return "redirect:/login";
        }

        User user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
        if (user == null) {
            log.error("Ошибка: пользователь не найден в базе");
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "user-home";
    }
    }


