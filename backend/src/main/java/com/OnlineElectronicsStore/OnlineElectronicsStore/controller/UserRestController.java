package com.OnlineElectronicsStore.OnlineElectronicsStore.controller;

import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Product;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.User;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.ProductRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.UserRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.ProductServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user/home")
public class UserRestController {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductServiceImpl productService;

    public UserRestController(UserRepository userRepository, ProductRepository productRepository, ProductServiceImpl productService) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productService = productService;
    }

    // ✅ Добавление продукта
    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@ModelAttribute Product product,
                                        @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            if (!imageFile.isEmpty()) {
                String uploadDir = "uploads/";
                String fileName = imageFile.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                product.setImagePath(fileName);
                product.setImageUrl("/uploads/" + fileName);
            }

            Product savedProduct = productRepository.save(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при сохранении файла: " + e.getMessage());
        }
    }

    // ✅ Главная страница пользователя (список продуктов + данные юзера)
    @GetMapping("/main")
    public ResponseEntity<?> userHome(@AuthenticationPrincipal UserDetails userDetails,
                                      @RequestParam(value = "query", required = false) String query) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Пользователь не авторизован");
        }

        Optional<User> userOpt = userRepository.findByUsername(userDetails.getUsername());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден");
        }

        User user = userOpt.get();
        List<Product> products = (query != null && !query.isEmpty())
                ? productRepository.findByNameContainingIgnoreCase(query)
                : productService.getAllProducts();

        return ResponseEntity.ok(new UserHomeResponse(user, products, query));
    }

    // 🔹 Вспомогательный DTO (ответ для фронтенда)
    static class UserHomeResponse {
        private final User user;
        private final List<Product> products;
        private final String query;

        public UserHomeResponse(User user, List<Product> products, String query) {
            this.user = user;
            this.products = products;
            this.query = query;
        }

        public User getUser() { return user; }
        public List<Product> getProducts() { return products; }
        public String getQuery() { return query; }
    }
}
