package com.OnlineElectronicsStore.OnlineElectronicsStore.controller;

import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Cart;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.CartItem;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Product;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.User;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.CartItemRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.CartRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.ProductRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartItemRepository cartItemRepository;





    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartController(CartItemRepository cartItemRepository, CartRepository cartRepository,
                          ProductRepository productRepository,
                          UserRepository userRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String viewCart(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart); // сохраняем, если корзина новая
                });

        model.addAttribute("items", cart.getCartItems());  /*<-здесь метод подсвечиваеться красным напомни зачем мы здесь его делали?*/

        double total = cart.getCartItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        model.addAttribute("total", total);

        return "cart";
    }

    @PostMapping("/add/{id}")
    public String addToCart(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));

        // Получаем корзину пользователя
        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });
        // Проверяем наличие CartItem
        CartItemRepository cartItemRepository;
        Optional<CartItem> existingItem = /*здесь подсвечиваеться ошибка ->*/ cartItemRepository.findByProductIdAndCartUserId(product.getId(), user.getId());

        if (((Optional<?>) existingItem).isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + 1); // увеличиваем количество
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setCart(cart);
            newItem.setQuantity(1);
            cartItemRepository.save(newItem);
        }

        return "redirect:/cart";
    }


}



