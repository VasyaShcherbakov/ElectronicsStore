package com.OnlineElectronicsStore.OnlineElectronicsStore.mapper;

import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.CartDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.CartItemDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.UserSummaryDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Cart;

import java.util.stream.Collectors;

public class CartMapper {

    public static CartDto toDto(Cart cart) {
        if (cart == null) return null;
        CartDto dto = new CartDto();
        dto.setId(cart.getId());
        dto.setTotal(cart.getTotal());
        if (cart.getUser() != null) {
            dto.setUser(new UserSummaryDto(cart.getUser().getId(), cart.getUser().getUsername(), cart.getUser().getEmail()));
        }
        dto.setItems(cart.getItems().stream()
                .map(item -> {
                    CartItemDto ci = new CartItemDto();
                    ci.setProductId(item.getProduct().getId());
                    ci.setProductName(item.getProduct().getName());
                    ci.setQuantity(item.getQuantity());
                    ci.setPrice(item.getProduct().getPrice());
                    return ci;
                })
                .collect(Collectors.toList()));
        return dto;
    }

    // при желании добавить обратный mapping: toEntity(...)
}
