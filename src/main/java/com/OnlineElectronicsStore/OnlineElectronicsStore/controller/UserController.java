package com.OnlineElectronicsStore.OnlineElectronicsStore.controller;

import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Product;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.User;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.ProductRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.UserRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/user/home")
public class UserController {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;


    public UserController(UserRepository userRepository, ProductRepository productRepository,ProductService productService) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productService =productService;
    }

    @GetMapping("/main")
    public String userHome(@AuthenticationPrincipal UserDetails userDetails,
                           @RequestParam(value = "query", required = false) String query,
                           Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        User user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }
        List<Product> products = (query != null && !query.isEmpty())
                ? productRepository.findByNameContainingIgnoreCase(query)
                : productService.getAllProducts();

        model.addAttribute("products", products);
        model.addAttribute("user", user);
        model.addAttribute("user_role", user.getUsername());
        model.addAttribute("query", query);
        return "user-home";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product,
                             @RequestParam("imageFile") MultipartFile imageFile,
                             @AuthenticationPrincipal UserDetails userDetails) {
        try {
            productService.addProduct(product, imageFile, userDetails.getUsername());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/user/home/main";
    }







}
