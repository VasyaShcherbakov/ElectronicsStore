package com.OnlineElectronicsStore.OnlineElectronicsStore.controller.web;

import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Product;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.UserRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.ProductServiceImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
public class ProductController {
    private final ProductServiceImpl productService;
    private final UserRepository userRepository;


    public ProductController(ProductServiceImpl productService, UserRepository userRepository) {
        this.productService = productService;
        this.userRepository = userRepository;
    }



    @GetMapping("/products")
    public String getAllProducts(Model model,
                                 @AuthenticationPrincipal UserDetails userDetails) {

        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("product", new Product());
        model.addAttribute("isProductsPage", true);

        if (userDetails != null) {
            model.addAttribute("user",
                    userRepository
                            .findByUsername(userDetails.getUsername())
                            .orElse(null)
            );
        }


        /*model.addAttribute("cartSize", 0); */// временно

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
        // 1️⃣ Достаем существующий товар из БД
        Product product = productService.getProductById(formProduct.getId());

        // 2️⃣ Обновляем поля
        product.setName(formProduct.getName());
        product.setDescription(formProduct.getDescription());
        product.setPrice(formProduct.getPrice());
        product.setQuantity(formProduct.getQuantity());

        // 3️⃣ Логика загрузки картинки — ТАКАЯ ЖЕ, как в addProduct
        if (!imageFile.isEmpty()) {
            try {
                String uploadDir = "uploads/";
                String fileName = imageFile.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Files.copy(
                        imageFile.getInputStream(),
                        uploadPath.resolve(fileName),
                        StandardCopyOption.REPLACE_EXISTING
                );

                product.setImagePath(fileName);
                product.setImageUrl(fileName);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // ⚠️ если файл НЕ выбран — старая картинка останется

        productService.saveProduct(product);

        return "redirect:/user/home";
    }


    @GetMapping("/products/search")
    public String searchProducts(@RequestParam String query, Model model) {
        List<Product> products = productService.searchProducts(query);
        model.addAttribute("products", products);
        model.addAttribute("query", query);
        return "products";
    }




}
