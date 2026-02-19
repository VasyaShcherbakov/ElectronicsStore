package com.OnlineElectronicsStore.OnlineElectronicsStore.mapper;

import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.CategoryDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.ProductCategory;

public class CategoryMapper {

    public static CategoryDto toDto(ProductCategory productCategory) {
        if (productCategory == null) return null;

        CategoryDto dto = new CategoryDto();
        dto.setId(productCategory.getId());
        dto.setName(productCategory.getName());

        return dto;
    }

    public static ProductCategory toEntity(CategoryDto dto) {
        if (dto == null) return null;

        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(dto.getId());
        productCategory.setName(dto.getName());

        return productCategory;
    }
}
