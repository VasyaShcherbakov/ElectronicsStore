package com.OnlineElectronicsStore.OnlineElectronicsStore.controller.rest;

import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Product;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Products",
        description = "Операції з товарами: перегляд, пошук, додавання, редагування та видалення"
)
@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    private final ProductServiceImpl productService;

    public ProductRestController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    // ================= GET ALL =================

    @Operation(
            summary = "Отримати всі товари",
            description = "Повертає список усіх товарів, доступних у магазині"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список товарів успішно отримано")
    })
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // ================= GET BY ID =================

    @Operation(
            summary = "Отримати товар за ID",
            description = "Повертає детальну інформацію про товар за його ідентифікатором"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Товар знайдено"),
            @ApiResponse(responseCode = "404", description = "Товар не знайдено")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return product != null
                ? ResponseEntity.ok(product)
                : ResponseEntity.notFound().build();
    }

    // ================= CREATE =================

    @Operation(
            summary = "Додати новий товар",
            description = "Створює новий товар. Доступно лише для автентифікованих користувачів"
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Товар успішно створено"),
            @ApiResponse(responseCode = "401", description = "Користувач не авторизований")
    })
    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product saved = productService.saveProduct(product);
        return ResponseEntity.ok(saved);
    }

    // ================= UPDATE =================

    @Operation(
            summary = "Оновити товар",
            description = "Оновлює дані існуючого товару за його ID"
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Товар успішно оновлено"),
            @ApiResponse(responseCode = "401", description = "Користувач не авторизований"),
            @ApiResponse(responseCode = "404", description = "Товар не знайдено")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody Product product
    ) {
        product.setId(id);
        Product updated = productService.saveProduct(product);
        return ResponseEntity.ok(updated);
    }

    // ================= DELETE =================

    @Operation(
            summary = "Видалити товар",
            description = "Видаляє товар за його ID"
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Товар успішно видалено"),
            @ApiResponse(responseCode = "401", description = "Користувач не авторизований"),
            @ApiResponse(responseCode = "404", description = "Товар не знайдено")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // ================= SEARCH =================

    @Operation(
            summary = "Пошук товарів",
            description = "Пошук товарів за назвою (без урахування регістру)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Результати пошуку повернено")
    })
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String query) {
        return ResponseEntity.ok(productService.searchProducts(query));
    }
}
