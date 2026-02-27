package com.OnlineElectronicsStore.OnlineElectronicsStore.controller.web;

import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Cart;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.CartService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public String showCart(Model model,
                           @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        Cart cart = cartService.getOrCreateCart(userDetails.getUsername());
        var items = cartService.getCartItems(cart);
        var total = cartService.calculateTotal(items);

        model.addAttribute("cartItems", items);
        model.addAttribute("total", total);
        model.addAttribute("isCartEmpty", items.isEmpty());

        return "cart";
    }



    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId,
                            @AuthenticationPrincipal UserDetails userDetails,
                            RedirectAttributes redirectAttributes) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        try {
            cartService.addProduct(userDetails.getUsername(), productId);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/products";
        }

        return "redirect:/cart";
    }



    @PostMapping("/remove/{cartItemId}")
    public String removeFromCart(@PathVariable Long cartItemId,
                                 @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails != null) {
            cartService.removeItem(userDetails.getUsername(), cartItemId);
        }

        return "redirect:/cart";
    }


}

