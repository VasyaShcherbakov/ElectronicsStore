package com.OnlineElectronicsStore.OnlineElectronicsStore.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Товар, опублікований користувачем")
public class ProductDto {

    @Schema(
            description = "ID товару",
            example = "12"
    )
    private Long id;

    @Schema(
            description = "Назва товару",
            example = "iPhone 13 Pro"
    )
    private String name;

    @Schema(
            description = "Опис товару",
            example = "Стан ідеальний, без подряпин, 128GB"
    )
    private String description;

    @Schema(
            description = "Ціна товару",
            example = "899.99"
    )
    private BigDecimal price;

    @Schema(
            description = "URL зображення товару",
            example = "/uploads/iphone13.jpg"
    )
    private String imageUrl;

    @Schema(
            description = "Категорія товару"
    )
    private CategoryDto category;

    @Schema(
            description = "Продавець (коротка інформація)"
    )
    private UserSummaryDto seller; // хто виклав товар

    public ProductDto() {}

    // ====== Геттери та сеттери ======

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
