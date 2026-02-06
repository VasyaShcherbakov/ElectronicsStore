package com.OnlineElectronicsStore.OnlineElectronicsStore.repository;

import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Product;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByCategoryId(Long categoryId);
    List<Product> findByOwner(User owner);
    List<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String name,
            String description
    );
}

