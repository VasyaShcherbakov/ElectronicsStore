package com.OnlineElectronicsStore.OnlineElectronicsStore.controller.web;

import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Product;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.UserRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.CategoryService;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.ProductServiceImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
public class ProductController {
    private final ProductServiceImpl productService;
    private final UserRepository userRepository;
    private final CategoryService categoryService;

    public ProductController(ProductServiceImpl productService, UserRepository userRepository, CategoryService categoryService) {
        this.productService = productService;
        this.userRepository = userRepository;
        this.categoryService= categoryService;
    }



    @GetMapping("/products")
    public String getAllProducts(
            @RequestParam(required = false) Long categoryId,
            Model model,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        List<Product> products;

        if (categoryId != null) {
            products = productService.getProductsByCategory(categoryId);
        } else {
            products = productService.getAllProducts();
        }

        model.addAttribute("products", products); // ✅ ОДИН раз
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("selectedCategoryId", categoryId); // ✅ из запроса
        model.addAttribute("isProductsPage", true);

        if (userDetails != null) {
            model.addAttribute("user",
                    userRepository
                            .findByUsername(userDetails.getUsername())
                            .orElse(null)
            );
        }

        return "products";
    }




    @PostMapping("/products")
    public String addProduct(@ModelAttribute Product product) {
        productService.saveProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/products/{id}")
    public String getProductDetails(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "product-details";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "edit-product";
    }

    @PostMapping("/products/update")
    public String updateProduct(
            @ModelAttribute Product formProduct,
            @RequestParam("imageFile") MultipartFile imageFile
    ) {
        productService.updateProduct(formProduct, imageFile);
        return "redirect:/user/home";
    }


    @GetMapping("/products/search")
    public String search(
            @RequestParam(required = false) String query,
            Model model, @AuthenticationPrincipal UserDetails userDetails) {

        List<Product> products;

        if (query == null || query.isBlank()) {
            products = productService.getAllProducts();
        } else {
            products = productService.search(query);
        }

        model.addAttribute("products", products);
        model.addAttribute("query", query);
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("user", userDetails);

        return "products";
    }

}
