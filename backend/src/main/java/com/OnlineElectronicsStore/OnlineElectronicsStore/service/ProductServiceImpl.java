package com.OnlineElectronicsStore.OnlineElectronicsStore.service;


import com.OnlineElectronicsStore.OnlineElectronicsStore.exception.ProductNotFoundException;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Product;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.User;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.ProductRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service

public class ProductServiceImpl {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;


    public ProductServiceImpl(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Product addProduct(Product product,
                              MultipartFile imageFile,
                              String username) {

        // если username null — это уже ошибка бизнес-логики
        if (username == null) {
            throw new RuntimeException("User not authenticated");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // кто выложил товар
        product.setOwner(user);

        // пока не в корзине
        product.setUser(null);

        // загрузка картинки
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String uploadDir = "uploads/";
                Files.createDirectories(Paths.get(uploadDir));

                String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                Path filePath = Paths.get(uploadDir).resolve(fileName);

                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                product.setImagePath(fileName);
                product.setImageUrl(fileName);

            } catch (IOException ex) {
                throw new RuntimeException("Ошибка загрузки изображения", ex);
            }
        }

        return productRepository.save(product);
    }

    public List<Product> getProductsForUser(User user, String query) {
        if (query != null && !query.isEmpty()) {
            return productRepository.findByNameContainingIgnoreCase(query);
        }
        return productRepository.findByOwner(user); // <-- здесь правильный метод
    }


    public List<Product> getProductsByUser(User user) {
        return productRepository.findByOwner(user);
    }


    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Product saveProduct(Product product) {
        if (product.getImageUrl() == null || product.getImageUrl().trim().isEmpty()) {
            product.setImageUrl("default.jpg"); // Задаем изображение по умолчанию
        }
        productRepository.save(product);
        return product;
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCase(query);
    }
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);}

    public List<Product> search(String query) {
        return productRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                        query,
                        query
                );
    }

    @Transactional
    public void updateProduct(Product formProduct, MultipartFile imageFile) {

        Product product = getProductById(formProduct.getId());

        product.setName(formProduct.getName());
        product.setDescription(formProduct.getDescription());
        product.setPrice(formProduct.getPrice());
        product.setQuantity(formProduct.getQuantity());

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String uploadDir = "uploads/";
                Files.createDirectories(Paths.get(uploadDir));

                String fileName = imageFile.getOriginalFilename();
                Path filePath = Paths.get(uploadDir).resolve(fileName);

                Files.copy(imageFile.getInputStream(),
                        filePath,
                        StandardCopyOption.REPLACE_EXISTING);

                product.setImagePath(fileName);
                product.setImageUrl(fileName);

            } catch (IOException e) {
                throw new RuntimeException("Ошибка загрузки файла", e);
            }
        }

        productRepository.save(product);
    }


}
