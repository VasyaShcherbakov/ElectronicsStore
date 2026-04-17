package com.OnlineElectronicsStore.OnlineElectronicsStore.mapper;

import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.CartDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.CartItemDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.UserSummaryDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Cart;

public class CartMapper {

    public static CartDto toDto(Cart cart) {

        if (cart == null) return null;

        CartDto dto = new CartDto();
        dto.setId(cart.getId());

        dto.setItems(
                cart.getItems().stream()
                        .map(item -> {
                            CartItemDto ci = new CartItemDto();
                            ci.setId(item.getId());
                            ci.setProductId(item.getProduct().getId());
                            ci.setProductName(item.getProduct().getName());
                            ci.setQuantity(item.getQuantity());
                            ci.setPrice(item.getProduct().getPrice());
                            return ci;
                        })
                        .toList()
        );



        var total = cart.getItems().stream()
                .map(item ->
                        item.getProduct().getPrice()
                                .multiply(java.math.BigDecimal.valueOf(item.getQuantity()))
                )
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        dto.setTotal(total);

        if (cart.getUser() != null) {
            dto.setUser(new UserSummaryDto(
                    cart.getUser().getId(),
                    cart.getUser().getUsername(),
                    cart.getUser().getEmail()
            ));
        }

        return dto;
    }
}
