package com.OnlineElectronicsStore.OnlineElectronicsStore.controller;


import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Cart;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Product;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.CartRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

@RequestMapping("/cart")
   // Все маршруты начинаются с /cart
    public class CartController {

        private final CartRepository cartRepository;
        private final ProductRepository productRepository;

        public CartController(CartRepository cartRepository, ProductRepository productRepository) {
            this.cartRepository = cartRepository;
            this.productRepository = productRepository;
        }

        @GetMapping
        public String viewCart(Model model) {
            System.out.println("Зашел в метод viewCart");
            Cart cart = cartRepository.findById(1L).orElse(new Cart());
            model.addAttribute("products", cart.getProducts());
            return "cart";
        }

        @PostMapping("/add/{id}")
        public String addToCart(@PathVariable Long id) {
            System.out.println("Добавление товара в корзину, ID: " + id);
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Товар не найден"));

            Cart cart = cartRepository.findById(1L).orElse(new Cart());
            cart.getProducts().add(product);
            cartRepository.save(cart);

            System.out.println("Товар добавлен в корзину");
            return "redirect:/cart";
        }
    }

