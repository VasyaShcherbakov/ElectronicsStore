package com.OnlineElectronicsStore.OnlineElectronicsStore.dto;

import java.math.BigDecimal;
import java.util.List;

public class CartDto {
    private Long id;
    private String username; // имя пользователя, которому принадлежит корзина
    private List<CartItemDto> items; // список товаров
    private BigDecimal total; // общая сумма корзины
    private UserSummaryDto user;



    // ====== Геттеры и сеттеры ======
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<CartItemDto> getItems() {
        return items;
    }

    public void setItems(List<CartItemDto> items) {
        this.items = items;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    public UserSummaryDto getUser() {
        return user;
    }

    public void setUser(UserSummaryDto user) {
        this.user = user;
    }

}
