package com.OnlineElectronicsStore.OnlineElectronicsStore.mapper;

import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.*;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.*;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderResponseDto toDto(CustomerOrder order) {

        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId());
        dto.setCustomerName(order.getCustomerName());
        dto.setPhone(order.getPhone());
        dto.setAddress(order.getAddress());
        dto.setStatus(order.getStatus().name());
        dto.setBuyerUsername(order.getBuyer().getUsername());

        dto.setItems(
                order.getItems()
                        .stream()
                        .map(item -> {
                            OrderItemDto itemDto = new OrderItemDto();
                            itemDto.setProductId(item.getProduct().getId());
                            itemDto.setProductName(item.getProduct().getName());
                            itemDto.setQuantity(item.getQuantity());
                            itemDto.setPriceAtOrder(item.getPriceAtOrder());
                            return itemDto;
                        })
                        .collect(Collectors.toList())
        );

        return dto;
    }
}