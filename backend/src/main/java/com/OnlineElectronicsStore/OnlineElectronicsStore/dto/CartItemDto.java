package com.OnlineElectronicsStore.OnlineElectronicsStore.dto;

import java.math.BigDecimal;

public class CartItemDto {
    private Long productId;
    private String productName;
    private int quantity;
    private BigDecimal price;

    // ====== Геттеры и сеттеры ======
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
