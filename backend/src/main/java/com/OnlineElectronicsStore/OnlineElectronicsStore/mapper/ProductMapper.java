package com.OnlineElectronicsStore.OnlineElectronicsStore.mapper;

import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.ProductDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.CategoryDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.UserSummaryDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Product;

public class ProductMapper {

    public static ProductDto toDto(Product product) {
        if (product == null) return null;

        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setImageUrl(product.getImageUrl());


        if (product.getCategory() != null) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(product.getCategory().getId());
            categoryDto.setName(product.getCategory().getName());
            dto.setCategory(categoryDto);
        }


        if (product.getUser() != null) {
            dto.setSeller(new UserSummaryDto(
                    product.getUser().getId(),
                    product.getUser().getUsername(),
                    product.getUser().getEmail()
            ));
        }

        return dto;
    }

    public static Product toEntity(ProductDto dto) {
        if (dto == null) return null;

        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setImageUrl(dto.getImageUrl());

        return product;
    }
}
