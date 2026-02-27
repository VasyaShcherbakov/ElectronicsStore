package com.OnlineElectronicsStore.OnlineElectronicsStore.mapper;

import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.CartItemDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.CartItem;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Product;
import org.springframework.stereotype.Component;

@Component
public class CartItemMapper {


    public CartItemDto toDto(CartItem item) {
        if (item == null) {
            return null;
        }

        CartItemDto dto = new CartItemDto();
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getProduct().getPrice());
        return dto;
    }



    public CartItem toEntity(CartItemDto dto, Product product) {
        if (dto == null) {
            return null;
        }

        CartItem item = new CartItem();
        item.setProduct(product); // product уже должен быть найден в БД по productId
        item.setQuantity(dto.getQuantity());
        return item;
    }
}
