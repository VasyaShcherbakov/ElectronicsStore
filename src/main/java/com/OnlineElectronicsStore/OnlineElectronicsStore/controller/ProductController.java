package com.OnlineElectronicsStore.OnlineElectronicsStore.controller;

import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Cart;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Product;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.CartRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Controller
public class ProductController {


    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/products")
    public String getAllProducts(Model model) {
        model.addAttribute("products", productRepository.findAll()); // Список товаров
        model.addAttribute("product", new Product()); // Пустой объект для формы
        return "products"; // Имя Thymeleaf-шаблона
    }

   /* @PostMapping("/products")
    public String addProduct(@ModelAttribute Product product) {
        productRepository.save(product); // Сохраняем продукт в базу данных
        return "redirect:/products"; // Перенаправляем на список товаров
    }*/
    @PostMapping("/products")
    public String addProduct(@ModelAttribute Product product) {
        if (product.getImageUrl() == null || product.getImageUrl().trim().isEmpty()) {
            product.setImageUrl("default.jpg"); // Задаем изображение по умолчанию
        }
        productRepository.save(product);
        return "redirect:/products";
    }



    @GetMapping("/products/{id}")
    public String getProductDetails(@PathVariable Long id, Model model) {
        System.out.println("PREVET!!!!");
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));
        model.addAttribute("product", product);
        return "product-details"; // Новый шаблон для деталей товара
    }
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
        return "redirect:/products";
    }
    @ExceptionHandler(RuntimeException.class)
    public String handleException(RuntimeException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "error"; // Шаблон для отображения ошибок
    }
    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));
        model.addAttribute("product", product);
        return "edit-product"; // Новый шаблон для редактирования
    }
    @PostMapping("/products/update")
    public String updateProduct(Product product) {
        productRepository.save(product);
        return "redirect:/products";
    }
    @GetMapping("/products/search")
    public String searchProducts(@RequestParam String query, Model model) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(query);
        model.addAttribute("products", products);
        model.addAttribute("query", query);
        return "products";
    }

    /*@GetMapping("/products/search")
    public String searchProducts(@RequestParam(required = false) String query, Model model) {
        if (query == null || query.trim().isEmpty()) {
            model.addAttribute("products", productRepository.findAll());
        } else {
            model.addAttribute("products", productRepository.findByNameContainingIgnoreCase(query));
        }
        model.addAttribute("query", query);
        return "products";
    }*/





    /*Корзина2*/
   /* @RequestMapping("/cart")
    public class CartController {
        private final CartRepository cartRepository;
        private final ProductRepository productRepository;

        public CartController(CartRepository cartRepository, ProductRepository productRepository) {
            this.cartRepository = cartRepository;
            this.productRepository = productRepository;
        }

        // Просмотр корзины
        @GetMapping
        public String viewCart(Model model) {
            System.out.println("Зашёл в метод viewCart");

            Cart cart = cartRepository.findById(1L).orElseGet(() -> {
                System.out.println("Корзина не найдена, создаём новую");
                Cart newCart = new Cart();
                return cartRepository.save(newCart);
            });

            System.out.println("Корзина найдена или создана: " + cart);

            model.addAttribute("products", cart.getProducts());
            return "cart"; // Имя Thymeleaf-шаблона
        }

        // Добавление товара в корзину
        *//*@PostMapping("/add/{id}")
        public String addToCart(@PathVariable("id") Long productId) {
            System.out.println("Получен ID товара: " + productId);

            // Проверяем, есть ли товар в БД
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Товар не найден"));

            System.out.println("Товар найден: " + product);

            // Ищем корзину, если её нет — создаём
            Cart cart = cartRepository.findById(1L).orElseGet(() -> {
                System.out.println("Корзина не найдена, создаём новую");
                Cart newCart = new Cart();
                return cartRepository.save(newCart);
            });

            System.out.println("Корзина найдена: " + cart);

            // Добавляем товар в корзину
            cart.getProducts().add(product);
            cartRepository.save(cart);

            System.out.println("Товар добавлен в корзину");

            return "redirect:/cart";
        }*//*
        @PostMapping("/cart/add/{id}")
        public String addToCart(@PathVariable Long id) {
            System.out.println("Добавление товара в корзину с ID: " + id);
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Товар не найден"));
            Cart cart = cartRepository.findById(1L).orElse(new Cart());
            cart.getProducts().add(product);
            cartRepository.save(cart);
            return "redirect:/cart";
        }




        // Удаление товара из корзины
        @PostMapping("/remove/{id}")
        public String removeFromCart(@PathVariable("id") Long productId) {
            System.out.println("Удаление товара с ID: " + productId);

            Cart cart = cartRepository.findById(1L).orElseThrow(() -> new RuntimeException("Корзина не найдена"));

            Optional<Product> productToRemove = cart.getProducts().stream()
                    .filter(p -> p.getId().equals(productId))
                    .findFirst();

            if (productToRemove.isPresent()) {
                cart.getProducts().remove(productToRemove.get());
                cartRepository.save(cart);
                System.out.println("Товар удалён из корзины");
            } else {
                System.out.println("Товар не найден в корзине");
            }

            return "redirect:/cart";
        }

        // Очистка всей корзины
        @PostMapping("/clear")
        public String clearCart() {
            System.out.println("Очистка корзины");

            Cart cart = cartRepository.findById(1L).orElseThrow(() -> new RuntimeException("Корзина не найдена"));
            cart.getProducts().clear();
            cartRepository.save(cart);

            System.out.println("Корзина очищена");
            return "redirect:/cart";
        }
    }*/



}
