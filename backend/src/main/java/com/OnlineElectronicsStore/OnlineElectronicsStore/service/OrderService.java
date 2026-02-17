package com.OnlineElectronicsStore.OnlineElectronicsStore.service;

import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.OrderCreateDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.*;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.ChatService;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.OrderRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ChatService chatService;
    private final CartService cartService;

    public User get() {
        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if (principal instanceof User user) {
            return user;
        }

        throw new RuntimeException("Пользователь не аутентифицирован");
    }



public OrderService(OrderRepository orderRepository,
                        ChatService chatService, CartService cartService) {
        this.orderRepository = orderRepository;
        this.chatService = chatService;
        this.cartService = cartService;
    }

    public CustomerOrder createOrder(OrderCreateDto dto) {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Cart cart = cartService.getOrCreateCart(username);

        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Корзина пуста");
        }

        User currentUser = cart.getUser();

        CustomerOrder order = new CustomerOrder();
        order.setCustomerName(dto.getCustomerName());
        order.setPhone(dto.getPhone());
        order.setAddress(dto.getAddress());
        order.setStatus(OrderStatus.PAID);
        order.setBuyer(currentUser);

        for (CartItem ci : cart.getItems()) {

            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(ci.getProduct());
            oi.setQuantity(ci.getQuantity());

            // 💎 ВАЖНО: цена фиксируется на момент заказа
            oi.setPriceAtOrder(ci.getProduct().getPrice());

            order.getItems().add(oi);
        }

        orderRepository.save(order);

        cartService.clear(cart);

        chatService.createChatForOrder(order);

        return order;
    }



    public CustomerOrder getById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Заказ не найден: " + orderId)
                );
    }


}
