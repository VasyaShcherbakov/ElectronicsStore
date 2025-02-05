package com.OnlineElectronicsStore.OnlineElectronicsStore.repository;

import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String name);


}

