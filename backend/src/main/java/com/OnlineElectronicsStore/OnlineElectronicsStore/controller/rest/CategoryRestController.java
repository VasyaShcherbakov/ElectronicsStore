package com.OnlineElectronicsStore.OnlineElectronicsStore.controller.rest;

import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.CategoryDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.mapper.CategoryMapper;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.ProductCategory;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Categories",
        description = "Адміністрування категорій товарів"
)
@RestController
@RequestMapping("/api/admin/categories")
@SecurityRequirement(name = "bearerAuth")
public class CategoryRestController {

    private final CategoryService categoryService;

    public CategoryRestController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // ================= GET ALL =================

    @Operation(summary = "Отримати всі категорії")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список категорій отримано")
    })
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAll() {

        List<CategoryDto> categories = categoryService.getAll()
                .stream()
                .map(CategoryMapper::toDto)
                .toList();

        return ResponseEntity.ok(categories);
    }

    // ================= CREATE =================

    @Operation(summary = "Створити нову категорію")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Категорію створено")
    })
    @PostMapping
    public ResponseEntity<CategoryDto> create(
            @RequestBody CategoryDto dto
    ) {
        ProductCategory category =
                categoryService.create(dto.getName());

        return ResponseEntity.ok(CategoryMapper.toDto(category));
    }

    // ================= DELETE =================

    @Operation(summary = "Видалити категорію")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Категорію видалено")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        categoryService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}