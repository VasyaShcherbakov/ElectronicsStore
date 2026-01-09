package com.OnlineElectronicsStore.OnlineElectronicsStore.mapper;

import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.CategoryDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Category;
import java.util.stream.Collectors;

public class CategoryMapper {

    public static CategoryDto toDto(Category category) {
        if (category == null) return null;

        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());



        return dto;
    }

    public static Category toEntity(CategoryDto dto) {
        if (dto == null) return null;

        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        // Продукты не маппим здесь, чтобы не зациклиться
        return category;
    }
}
