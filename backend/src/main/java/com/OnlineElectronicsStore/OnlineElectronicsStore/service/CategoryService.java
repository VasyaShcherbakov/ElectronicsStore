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

    public ProductCategory addCategory(ProductCategory category) {
        categoryRepository.save(category);
        return category;
    }

    public ProductCategory create(String name) {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setName(name);
        categoryRepository.save(productCategory);
        System.out.println(">>> CATEGORY SAVED: " + name);// <-- вот это сохраняет в БД
        return productCategory;
    }

    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
        System.out.println(">>> CATEGORY DELETED ID: " + id);}


}