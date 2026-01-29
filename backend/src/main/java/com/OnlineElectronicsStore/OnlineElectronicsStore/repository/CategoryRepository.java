package com.OnlineElectronicsStore.OnlineElectronicsStore.repository;


import com.OnlineElectronicsStore.OnlineElectronicsStore.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<ProductCategory, Long> {

    boolean existsByName(String name);

}
