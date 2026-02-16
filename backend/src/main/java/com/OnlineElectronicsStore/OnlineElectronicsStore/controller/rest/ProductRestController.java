package com.OnlineElectronicsStore.OnlineElectronicsStore.controller.rest;

import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.ProductDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.mapper.ProductMapper;
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
@Tag(
        name = "Products",
        description = "Операції з товарами: перегляд, пошук, додавання, редагування та видалення"
)
public class ProductRestController {

    private final ProductServiceImpl productService;



    public ProductRestController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    // ================= GET ALL =================

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts()
                .stream()
                .map(ProductMapper::toDto)
                .toList();

        return ResponseEntity.ok(products);
    }

    // ================= GET BY ID =================

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);

        return product != null
                ? ResponseEntity.ok(ProductMapper.toDto(product))
                : ResponseEntity.notFound().build();
    }

    // ================= CREATE =================

    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto dto) {

        Product product = ProductMapper.toEntity(dto);

        Product saved = productService.saveProduct(product);

        return ResponseEntity.ok(ProductMapper.toDto(saved));
    }

    // ================= UPDATE =================

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDto dto
    ) {
        Product product = ProductMapper.toEntity(dto);
        product.setId(id);

        Product updated = productService.saveProduct(product);

        return ResponseEntity.ok(ProductMapper.toDto(updated));
    }

    // ================= DELETE =================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // ================= SEARCH =================

    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> searchProducts(@RequestParam String query) {

        List<ProductDto> results = productService.searchProducts(query)
                .stream()
                .map(ProductMapper::toDto)
                .toList();

        return ResponseEntity.ok(results);
    }
}
