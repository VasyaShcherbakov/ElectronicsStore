package com.OnlineElectronicsStore.OnlineElectronicsStore.service;


import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Product;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service

public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public void addProduct(Product product, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            // Указываем путь, куда сохраняем файл
            String uploadDir = "uploads/";
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();

            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            imageFile.transferTo(filePath.toFile());

            // Устанавливаем путь к картинке в объект продукта
            product.setImageUrl("/" + uploadDir + fileName);
        }

        productRepository.save(product);
    }


    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));
    }

    public void saveProduct(Product product) {
        if (product.getImageUrl() == null || product.getImageUrl().trim().isEmpty()) {
            product.setImageUrl("default.jpg"); // Задаем изображение по умолчанию
        }
        productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCase(query);
    }

    public Product addProduct(Product product) {

        return productRepository.save(product);
    }
}
