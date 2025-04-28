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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/user/home")
public class UserController {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;


    public UserController(UserRepository userRepository, ProductRepository productRepository, ProductService productService) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productService = productService;
    }

   /* @GetMapping("/main")
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
*/

   /* @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product,
                             @RequestParam("imageFile") MultipartFile imageFile) {


        // Здесь ты можешь сохранить product в базу данных
        // А imageFile сохранить на диск или в облачное хранилище

        if (!imageFile.isEmpty()) {
            // например, сохраним картинку на диск
            try {
                String uploadDir = "uploads/"; // Папка для загрузок
                String filePath = uploadDir + imageFile.getOriginalFilename();
                imageFile.transferTo(new File(filePath));
                product.setImagePath(filePath); // сохраняем путь в объект товара
            } catch (IOException e) {
                e.printStackTrace();
                // Можешь добавить обработку ошибки
            }
        }
        // После сохранения, например, отправляем на главную страницу товаров
        // Здесь нужно будет добавить сохранение в базу данных через репозиторий

        productRepository.save(product);
        product.setImageUrl(imageFile.getOriginalFilename());
        return "redirect:/user/home";

    }*/

    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product,
                             @RequestParam("imageFile") MultipartFile imageFile) {
        if (!imageFile.isEmpty()) {
            try {
                String uploadDir = "uploads/";
                String fileName = imageFile.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                product.setImagePath(fileName);  // путь к файлу
                product.setImageUrl(fileName);   // ссылка для отображения на странице
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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



