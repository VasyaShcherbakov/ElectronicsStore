package com.OnlineElectronicsStore.OnlineElectronicsStore.controller;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Cart;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Product;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Role;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.User;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.CartRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.ProductRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Controller

public class AuthController {
    private static final Logger log= LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegisterForm() {
        System.out.println("Отработал GET контроллер");
        return "register";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        System.out.println("Отработал GET контроллер");
        try {
            log.info("Регистрация нового пользователя: {}", user.getUsername()); // Логирование
            userService.registerUser(user);
            System.out.println("Юзер зарегистрировался");
            return "redirect:/login";
        } catch (Exception e) {
            log.error("Ошибка при регистрации", e); // Логирует полную ошибку
            model.addAttribute("error", "Ошибка регистрации: " + e.getMessage());
            return "register"; // Вернет пользователя на страницу регистрации
        }

    }

}
