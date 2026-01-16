package com.OnlineElectronicsStore.OnlineElectronicsStore.controller;

import com.OnlineElectronicsStore.OnlineElectronicsStore.model.*;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@Tag(
        name = "Cart",
        description = "Операції з кошиком користувача: перегляд, додавання та видалення товарів"
)
@RestController
@RequestMapping("/api/cart")
@SecurityRequirement(name = "bearerAuth")
public class CartRestController {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartRestController(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository
    ) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // ================= GET CART =================

    @Operation(
            summary = "Отримати кошик користувача",
            description = "Повертає вміст кошика поточного автентифікованого користувача та загальну суму"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Кошик успішно отримано"),
            @ApiResponse(responseCode = "401", description = "Користувач не авторизований")
    })
    @GetMapping
    public ResponseEntity<Map<String, Object>> getCart(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Optional<User> userOpt = userRepository.findByUsername(userDetails.getUsername());
        if (userOpt.isEmpty()) return ResponseEntity.status(401).build();

        User user = userOpt.get();
        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        BigDecimal total = cartItems.stream()
                .map(item -> item.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> response = new HashMap<>();
        response.put("items", cartItems);
        response.put("total", total);

        return ResponseEntity.ok(response);
    }

    // ================= ADD TO CART =================

    @Operation(
            summary = "Додати товар до кошика",
            description = "Додає товар до кошика користувача. Якщо товар вже є — збільшує кількість"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Товар додано до кошика"),
            @ApiResponse(responseCode = "400", description = "Користувач або товар не знайдені"),
            @ApiResponse(responseCode = "403", description = "Неможливо додати власний товар"),
            @ApiResponse(responseCode = "401", description = "Користувач не авторизований")
    })
    @PostMapping("/add/{productId}")
    public ResponseEntity<?> addToCart(
            @PathVariable Long productId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Optional<User> userOpt = userRepository.findByUsername(userDetails.getUsername());
        Optional<Product> productOpt = productRepository.findById(productId);

        if (userOpt.isEmpty() || productOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Користувача або товар не знайдено");
        }

        User user = userOpt.get();
        Product product = productOpt.get();

        if (product.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403)
                    .body("Неможливо додати власний товар до кошика");
        }

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        Optional<CartItem> cartItemOpt =
                cartItemRepository.findByProductIdAndCartUserId(productId, user.getId());

        if (cartItemOpt.isPresent()) {
            CartItem item = cartItemOpt.get();
            item.setQuantity(item.getQuantity() + 1);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(1);
            cartItemRepository.save(newItem);
        }

        return ResponseEntity.ok("Товар додано до кошика");
    }

    // ================= REMOVE FROM CART =================

    @Operation(
            summary = "Видалити товар з кошика",
            description = "Видаляє конкретну позицію з кошика користувача"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Товар видалено з кошика"),
            @ApiResponse(responseCode = "403", description = "Немає доступу до цього кошика"),
            @ApiResponse(responseCode = "404", description = "Товар у кошику не знайдено"),
            @ApiResponse(responseCode = "401", description = "Користувач не авторизований")
    })
    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<?> removeFromCart(
            @PathVariable Long cartItemId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Optional<CartItem> cartItemOpt = cartItemRepository.findById(cartItemId);

        if (cartItemOpt.isPresent()) {
            CartItem item = cartItemOpt.get();
            if (item.getCart().getUser().getUsername()
                    .equals(userDetails.getUsername())) {

                cartItemRepository.delete(item);
                return ResponseEntity.ok("Товар видалено з кошика");
            }
            return ResponseEntity.status(403).body("Немає доступу");
        }

        return ResponseEntity.notFound().build();
    }
}
