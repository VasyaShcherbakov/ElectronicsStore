package com.OnlineElectronicsStore.OnlineElectronicsStore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;

@Schema(description = "DTO кошика користувача")
public class CartDto {

    @Schema(
            description = "Унікальний ідентифікатор кошика",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Ім'я користувача, якому належить кошик",
            example = "VasyaKot"
    )
    private String username;

    @Schema(
            description = "Список товарів у кошику"
    )
    private List<CartItemDto> items;

    @Schema(
            description = "Загальна сума кошика",
            example = "1999.99"
    )
    private BigDecimal total;

    @Schema(
            description = "Коротка інформація про користувача"
    )
    private UserSummaryDto user;



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
