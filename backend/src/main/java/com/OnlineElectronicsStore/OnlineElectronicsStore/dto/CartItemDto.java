package com.OnlineElectronicsStore.OnlineElectronicsStore.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Елемент кошика (окремий товар у кошику)")
public class CartItemDto {

    @Schema(
            description = "ID товару",
            example = "42"
    )
    private Long productId;

    @Schema(
            description = "Назва товару",
            example = "Ноутбук Lenovo ThinkPad"
    )
    private String productName;

    @Schema(
            description = "Кількість одиниць товару",
            example = "2",
            minimum = "1"
    )
    private int quantity;

    @Schema(
            description = "Ціна товару за одну одиницю",
            example = "999.99"
    )
    private BigDecimal price;

    // ====== Геттери та сеттери ======

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
