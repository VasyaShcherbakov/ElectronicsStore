package com.OnlineElectronicsStore.OnlineElectronicsStore.controller.rest;

import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Product;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.User;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.ProductRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.UserRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.ProductServiceImpl;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.UserService;
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

    private final UserService userService;
    private final ProductServiceImpl productService;

    public UserRestController(ProductServiceImpl productService,UserService userService) {
        this.userService = userService;
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
            MultipartFile imageFile,
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("User not authenticated");
        }

        Product savedProduct = productService.addProduct(
                product,
                imageFile,
                userDetails.getUsername()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedProduct);
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
            @RequestParam(value = "query", required = false) String query
    ) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Користувач не авторизований");
        }

        User user = userService.findByUsername(userDetails.getUsername());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Користувача не знайдено");
        }

        List<Product> products = productService.getProductsForUser(user, query);

        return ResponseEntity.ok(
                new UserHomeResponse(user, products, query)
        );
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
