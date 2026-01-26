package com.OnlineElectronicsStore.OnlineElectronicsStore.controller.web;

import java.math.BigDecimal;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Cart;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.CartItem;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Product;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.User;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.CartItemRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.CartRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.ProductRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    private static final Logger log =
            LoggerFactory.getLogger(CartController.class);

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartController(CartRepository cartRepository,
                          CartItemRepository cartItemRepository,
                          ProductRepository productRepository,
                          UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String showCart(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> userOpt = userRepository.findByUsername(userDetails.getUsername());
        if (userOpt.isEmpty()) return "redirect:/login";

        User user = userOpt.get();
        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);/*Здесь карт подсвечиваеться*/

        // вычисление общей суммы
        java.math.BigDecimal total = cartItems.stream()
                .map(item -> item.getProduct().getPrice().multiply(java.math.BigDecimal.valueOf(item.getQuantity())))
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        return "cart";
    }


    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId,
                            @AuthenticationPrincipal UserDetails userDetails,
                            RedirectAttributes redirectAttributes) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow();

        Product product = productRepository.findById(productId)
                .orElseThrow();

        // 🚫 ЗАПРЕТ добавления своего товара
        if (product.getOwner() != null &&
                product.getOwner().getId().equals(user.getId())) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "❌ Вы не можете добавить в корзину свой собственный товар"
            );

            return "redirect:/products";
        }

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

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

        return "redirect:/cart";
    }


    @PostMapping("/remove/{cartItemId}")
    public String removeFromCart(@PathVariable Long cartItemId,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        Optional<CartItem> cartItemOpt = cartItemRepository.findById(cartItemId);
        if (cartItemOpt.isPresent()) {
            CartItem item = cartItemOpt.get();
            String username = userDetails.getUsername();
            if (item.getCart().getUser().getUsername().equals(username)) {
                cartItemRepository.delete(item);
            }
        }
        return "redirect:/cart";
    }
}

