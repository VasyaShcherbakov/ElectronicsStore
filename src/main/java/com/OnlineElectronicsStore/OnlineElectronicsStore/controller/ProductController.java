package com.OnlineElectronicsStore.OnlineElectronicsStore.controller;

import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Product;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.ProductService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }



    @GetMapping("/products")
    public String getAllProducts(Model model) {
        // Добавляем список товаров
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("product", new Product());

        // Получаем имя пользователя
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            model.addAttribute("username", auth.getName());
        } else {
            model.addAttribute("username", null);
        }

        return "products";
    }

    @PostMapping("/products/add")
    public String addProduct(@RequestParam("name") String name,
                             @RequestParam("description") String description,
                             @RequestParam("price") Double price,
                             @RequestParam("quantity") Integer quantity,
                             @RequestParam("imageFile") MultipartFile imageFile) {

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setQuantity(quantity);

        if (!imageFile.isEmpty()) {
            try {
                String uploadsDir = "uploads/";
                String realPath = new File("src/main/resources/static/" + uploadsDir).getAbsolutePath();
                File uploadDir = new File(realPath);
                if (!uploadDir.exists()) uploadDir.mkdirs();

                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                File dest = new File(uploadDir, fileName);
                imageFile.transferTo(dest);

                product.setImageUrl("/" + uploadsDir + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        productService.addProduct(product);
        return "redirect:/user/home";
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
    public String updateProduct(@ModelAttribute Product product) {
        productService.saveProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/products/search")
    public String searchProducts(@RequestParam String query, Model model) {
        List<Product> products = productService.searchProducts(query);
        model.addAttribute("products", products);
        model.addAttribute("query", query);
        return "products";
    }




}
