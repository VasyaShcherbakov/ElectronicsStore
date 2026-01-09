package com.OnlineElectronicsStore.OnlineElectronicsStore.dto;

import java.math.BigDecimal;

public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private CategoryDto category;
    private UserSummaryDto seller; // кто выложил товар

    public ProductDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public CategoryDto getCategory() {
        return category;
    }

    public void setCategory(CategoryDto category) {
        this.category = category;
    }

    public UserSummaryDto getSeller() {
        return seller;
    }

    public void setSeller(UserSummaryDto seller) {
        this.seller = seller;
    }
}
