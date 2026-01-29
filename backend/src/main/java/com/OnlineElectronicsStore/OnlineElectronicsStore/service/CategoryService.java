package com.OnlineElectronicsStore.OnlineElectronicsStore.service;


import com.OnlineElectronicsStore.OnlineElectronicsStore.model.ProductCategory;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    ProductCategory productCategory;
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<ProductCategory> getAll() {
        return categoryRepository.findAll();
    }

    public ProductCategory create(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new IllegalArgumentException("Категория уже существует");
        }
        ProductCategory category = new ProductCategory();
        category.setName(name);
        return categoryRepository.save(category);
    }
}