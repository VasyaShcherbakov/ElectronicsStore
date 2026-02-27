package com.OnlineElectronicsStore.OnlineElectronicsStore.controller.rest;

import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.CartDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.mapper.CartMapper;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Cart;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Cart",
        description = "Операції з кошиком користувача"
)
@RestController
@RequestMapping("/api/cart")
@SecurityRequirement(name = "bearerAuth")
public class CartRestController {

    private final CartService cartService;

    public CartRestController(CartService cartService) {
        this.cartService = cartService;
    }

    // ================= GET CART =================

    @Operation(summary = "Отримати кошик користувача")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Кошик успішно отримано"),
            @ApiResponse(responseCode = "401", description = "Користувач не авторизований")
    })
    @GetMapping
    public ResponseEntity<CartDto> getCart(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }

        Cart cart = cartService.getOrCreateCart(userDetails.getUsername());
        CartDto dto = CartMapper.toDto(cart);

        return ResponseEntity.ok(dto);
    }

    // ================= ADD =================

    @Operation(summary = "Додати товар до кошика")
    @PostMapping("/add/{productId}")
    public ResponseEntity<?> addToCart(
            @PathVariable Long productId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }

        cartService.addProduct(userDetails.getUsername(), productId);

        return ResponseEntity.ok("Товар додано");
    }

    // ================= REMOVE =================

    @Operation(summary = "Видалити товар з кошика")
    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<?> removeFromCart(
            @PathVariable Long cartItemId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }

        cartService.removeItem(userDetails.getUsername(), cartItemId);

        return ResponseEntity.ok("Товар видалено");
    }
}