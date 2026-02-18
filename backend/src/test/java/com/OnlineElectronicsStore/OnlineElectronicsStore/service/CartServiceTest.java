package com.OnlineElectronicsStore.OnlineElectronicsStore.service;

import com.OnlineElectronicsStore.OnlineElectronicsStore.model.*;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CartService cartService;

    // ================= calculateTotal =================

    @Test
    void shouldCalculateTotalCorrectly() {
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(100));

        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(2);

        BigDecimal total = cartService.calculateTotal(List.of(item));

        assertEquals(BigDecimal.valueOf(200), total);
    }

    @Test
    void shouldReturnZeroWhenNoItems() {
        BigDecimal total = cartService.calculateTotal(List.of());
        assertEquals(BigDecimal.ZERO, total);
    }

    // ================= addProduct =================

    @Test
    void shouldThrowIfUserNotFound() {
        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> cartService.addProduct("john", 1L));
    }

    @Test
    void shouldThrowIfProductNotFound() {
        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(new User()));

        when(productRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> cartService.addProduct("john", 1L));
    }

    @Test
    void shouldNotAllowAddingOwnProduct() {
        User user = new User();
        user.setId(1L);

        Product product = new Product();
        User owner = new User();
        owner.setId(1L);
        product.setOwner(owner);

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        assertThrows(RuntimeException.class,
                () -> cartService.addProduct("john", 1L));
    }

    @Test
    void shouldIncreaseQuantityIfProductAlreadyInCart() {
        User user = new User();
        user.setId(1L);

        Product product = new Product();
        product.setOwner(new User());

        Cart cart = new Cart();
        cart.setUser(user);

        CartItem existingItem = new CartItem();
        existingItem.setQuantity(1);

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(cartRepository.findByUser(user))
                .thenReturn(Optional.of(cart));

        when(cartItemRepository.findByCartAndProduct(cart, product))
                .thenReturn(Optional.of(existingItem));

        cartService.addProduct("john", 1L);

        assertEquals(2, existingItem.getQuantity());
        verify(cartItemRepository).save(existingItem);
    }

    // ================= removeItem =================

    @Test
    void shouldRemoveItemIfOwner() {
        User user = new User();
        user.setUsername("john");

        Cart cart = new Cart();
        cart.setUser(user);

        CartItem item = new CartItem();
        item.setCart(cart);

        when(cartItemRepository.findById(1L))
                .thenReturn(Optional.of(item));

        cartService.removeItem("john", 1L);

        verify(cartItemRepository).delete(item);
    }

    @Test
    void shouldThrowIfRemoving () {
        User user = new User();
        user.setUsername("john");

        Cart cart = new Cart();
        cart.setUser(user);

        CartItem item = new CartItem();
        item.setCart(cart);

        when(cartItemRepository.findById(1L))
                .thenReturn(Optional.of(item));

        assertThrows(RuntimeException.class,
                () -> cartService.removeItem("mike", 1L));
    }

    // ================= clear =================

    @Test
    void shouldClearCart() {
        Cart cart = new Cart();
        cart.setItems(new java.util.ArrayList<>(List.of(new CartItem())));

        cartService.clear(cart);

        assertTrue(cart.getItems().isEmpty());
        verify(cartRepository).save(cart);
    }
}