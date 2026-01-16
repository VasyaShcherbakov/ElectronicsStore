package com.OnlineElectronicsStore.OnlineElectronicsStore.controller;

import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Product;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.User;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.ProductRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.UserRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

@Tag(
        name = "User",
        description = "Операції користувача: керування товарами та головна сторінка"
)
@RestController
@RequestMapping("/api/user/home")
@SecurityRequirement(name = "bearerAuth")
public class UserRestController {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductServiceImpl productService;

    public UserRestController(
            UserRepository userRepository,
            ProductRepository productRepository,
            ProductServiceImpl productService
    ) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productService = productService;
    }

    // ================= ADD PRODUCT =================

    @Operation(
            summary = "Додати новий товар",
            description = "Додає новий товар від імені користувача з можливістю завантаження зображення"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Товар успішно створено"),
            @ApiResponse(responseCode = "400", description = "Некоректні дані товару"),
            @ApiResponse(responseCode = "401", description = "Користувач не авторизований"),
            @ApiResponse(responseCode = "500", description = "Помилка збереження файлу")
    })
    @PostMapping(
            value = "/add",
            consumes = { "multipart/form-data" }
    )
    public ResponseEntity<?> addProduct(
            @ModelAttribute Product product,
            @RequestParam("imageFile")
            @Schema(description = "Зображення товару", type = "string", format = "binary")
            MultipartFile imageFile
    ) {
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
                    .body("Помилка збереження файлу: " + e.getMessage());
        }
    }

    // ================= USER HOME =================

    @Operation(
            summary = "Головна сторінка користувача",
            description = "Повертає дані поточного користувача та список товарів (з пошуком або без)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Дані користувача та список товарів",
                    content = @Content(schema = @Schema(implementation = UserHomeResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "Користувач не авторизований"),
            @ApiResponse(responseCode = "404", description = "Користувача не знайдено")
    })
    @GetMapping("/main")
    public ResponseEntity<?> userHome(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(
                    value = "query",
                    required = false
            )
            @Schema(description = "Пошук товарів за назвою")
            String query
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Користувач не авторизований");
        }

        Optional<User> userOpt = userRepository.findByUsername(userDetails.getUsername());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Користувача не знайдено");
        }

        User user = userOpt.get();
        List<Product> products = (query != null && !query.isEmpty())
                ? productRepository.findByNameContainingIgnoreCase(query)
                : productService.getAllProducts();

        return ResponseEntity.ok(new UserHomeResponse(user, products, query));
    }

    // ================= DTO =================

    @Schema(description = "Відповідь головної сторінки користувача")
    static class UserHomeResponse {

        @Schema(description = "Дані користувача")
        private final User user;

        @Schema(description = "Список товарів")
        private final List<Product> products;

        @Schema(description = "Пошуковий запит")
        private final String query;

        public UserHomeResponse(User user, List<Product> products, String query) {
            this.user = user;
            this.products = products;
            this.query = query;
        }

        public User getUser() {
            return user;
        }

        public List<Product> getProducts() {
            return products;
        }

        public String getQuery() {
            return query;
        }
    }
}
