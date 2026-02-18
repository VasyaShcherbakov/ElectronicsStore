package com.OnlineElectronicsStore.OnlineElectronicsStore.service;

import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.OrderCreateDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.*;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ChatService chatService;

    @Mock
    private CartService cartService;

    @InjectMocks
    private OrderService orderService;

    @AfterEach
    void clearSecurity() {
        SecurityContextHolder.clearContext();
    }

    // ================= createOrder =================

    @Test
    void shouldThrowIfCartIsEmpty() {
        // given
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("john", null)
        );

        Cart cart = new Cart();
        cart.setItems(new ArrayList<>());

        when(cartService.getOrCreateCart("john")).thenReturn(cart);

        OrderCreateDto dto = new OrderCreateDto();

        // when + then
        assertThrows(IllegalStateException.class,
                () -> orderService.createOrder(dto));
    }

    @Test
    void shouldCreateOrderSuccessfully() {
        // given
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("john", null)
        );

        User user = new User();
        user.setUsername("john");

        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(100));

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(2);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(new ArrayList<>());
        cart.getItems().add(cartItem);

        when(cartService.getOrCreateCart("john")).thenReturn(cart);

        OrderCreateDto dto = new OrderCreateDto();
        dto.setCustomerName("John Doe");
        dto.setPhone("123456");
        dto.setAddress("Test Street");

        // when
        CustomerOrder order = orderService.createOrder(dto);

        // then
        assertEquals("John Doe", order.getCustomerName());
        assertEquals(OrderStatus.PAID, order.getStatus());
        assertEquals(user, order.getBuyer());
        assertEquals(1, order.getItems().size());

        OrderItem orderItem = order.getItems().get(0);
        assertEquals(BigDecimal.valueOf(100), orderItem.getPriceAtOrder());
        assertEquals(2, orderItem.getQuantity());

        verify(orderRepository).save(order);
        verify(cartService).clear(cart);
        verify(chatService).createChatForOrder(order);
    }

    // ================= getById =================

    @Test
    void shouldReturnOrderIfExists() {
        CustomerOrder order = new CustomerOrder();
        order.setId(1L);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        CustomerOrder result = orderService.getById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void shouldThrowIfOrderNotFound() {
        when(orderRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> orderService.getById(1L));
    }
}