package com.OnlineElectronicsStore.OnlineElectronicsStore.controller.web;

import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Product;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.User;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.ProductRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.UserRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Controller
@RequestMapping("/user/home")
public class UserController {

    private static final Logger log =
            LoggerFactory.getLogger(UserController.class);

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductServiceImpl productService;


    public UserController(UserRepository userRepository, ProductRepository productRepository, ProductServiceImpl productService) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productService = productService;
    }



    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product,
                             @RequestParam("imageFile") MultipartFile imageFile,
                             @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow();

        product.setUser(user); // 🔥 ВАЖНО

        // загрузка картинки
        if (!imageFile.isEmpty()) {
            try {
                String uploadDir = "uploads/";
                String fileName = imageFile.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Files.copy(imageFile.getInputStream(),
                        uploadPath.resolve(fileName),
                        StandardCopyOption.REPLACE_EXISTING);

                product.setImagePath(fileName);
                product.setImageUrl(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.info("Добавляем товар {} от продавца {}",
                product.getId(),
                product.getUser().getUsername());

        productRepository.save(product);
        return "redirect:/user/home/main";
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
        model.addAttribute("product", new Product()); // <--- ЭТО ДОБАВИТЬ!
        return "user-home";
    }





}
