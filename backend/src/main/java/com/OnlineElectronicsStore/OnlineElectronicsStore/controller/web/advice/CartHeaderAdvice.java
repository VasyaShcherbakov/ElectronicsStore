package com.OnlineElectronicsStore.OnlineElectronicsStore.controller.web.advice;

import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Cart;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.CartItem;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.User;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.CartItemRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.CartRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ControllerAdvice
public class CartHeaderAdvice {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    public CartHeaderAdvice(CartRepository cartRepository,
                            CartItemRepository cartItemRepository,
                            UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
    }

    @ModelAttribute
    public void addCartInfo(@AuthenticationPrincipal UserDetails userDetails,
                            org.springframework.ui.Model model) {

        if (userDetails == null) {
            model.addAttribute("cartSize", 0);
            model.addAttribute("cartTotal", BigDecimal.ZERO);
            return;
        }

        Optional<User> userOpt = userRepository.findByUsername(userDetails.getUsername());
        if (userOpt.isEmpty()) {
            model.addAttribute("cartSize", 0);
            model.addAttribute("cartTotal", BigDecimal.ZERO);
            return;
        }

        User user = userOpt.get();

        Optional<Cart> cartOpt = cartRepository.findByUser(user);
        if (cartOpt.isEmpty()) {
            model.addAttribute("cartSize", 0);
            model.addAttribute("cartTotal", BigDecimal.ZERO);
            return;
        }

        Cart cart = cartOpt.get();
        List<CartItem> items = cartItemRepository.findByCart(cart);

        int cartSize = items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();

        BigDecimal cartTotal = items.stream()
                .map(item -> item.getProduct()
                        .getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("cartSize", cartSize);
        model.addAttribute("cartTotal", cartTotal);
    }
}
