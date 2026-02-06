package com.OnlineElectronicsStore.OnlineElectronicsStore.repository;

import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Cart;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.CartItem;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByProductIdAndCartUserId(Long productId, Long userId);
    List<CartItem> findByCart(Cart cart);
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

}
