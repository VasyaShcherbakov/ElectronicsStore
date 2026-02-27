package com.OnlineElectronicsStore.OnlineElectronicsStore.controller.rest;

import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.OrderCreateDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.OrderResponseDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.mapper.OrderMapper;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.CustomerOrder;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private final OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    // ================= CREATE =================

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(
            @RequestBody OrderCreateDto dto
    ) {
        CustomerOrder order = orderService.createOrder(dto);
        return ResponseEntity.ok(OrderMapper.toDto(order));
    }

    // ================= GET BY ID =================

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrder(
            @PathVariable Long id
    ) {
        CustomerOrder order = orderService.getById(id);
        return ResponseEntity.ok(OrderMapper.toDto(order));
    }
}