package com.OnlineElectronicsStore.OnlineElectronicsStore.service;

import com.OnlineElectronicsStore.OnlineElectronicsStore.model.*;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       ProductRepository productRepository,
                       UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // Получити або строврити корзину
    public Cart getOrCreateCart(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    // Получити список товарів в корзині
    public List<CartItem> getCartItems(Cart cart) {
        return cartItemRepository.findByCart(cart);
    }

    // Загальна кількість
    public BigDecimal calculateTotal(List<CartItem> items) {
        return items.stream()
                .map(item ->
                        item.getProduct().getPrice()
                                .multiply(BigDecimal.valueOf(item.getQuantity()))
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Добваити товар
    public void addProduct(String username, Long productId) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Щоби не можливо добавити свій же товар
        if (product.getOwner() != null &&
                java.util.Objects.equals(user.getId(), product.getOwner().getId())) {

            throw new RuntimeException("Неможна добавити в корзину свій товар");
        }

        Cart cart = getOrCreateCart(username);

        Optional<CartItem> cartItemOpt =
                cartItemRepository.findByCartAndProduct(cart, product);

        if (cartItemOpt.isPresent()) {
            CartItem cartItem = cartItemOpt.get();
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cartItemRepository.save(cartItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(1);
            cartItemRepository.save(newItem);
        }
    }

    // Видалити товар
    public void removeItem(String username, Long cartItemId) {

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (!item.getCart().getUser().getUsername().equals(username)) {
            throw new RuntimeException("Access denied");
        }

        cartItemRepository.delete(item);
    }

    // Очистити корзину
    public void clear(Cart cart) {
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}