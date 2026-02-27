package com.OnlineElectronicsStore.OnlineElectronicsStore.service;

import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Cart;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Role;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.User;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.CartRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       CartRepository cartRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Метод при логіні
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Користувача не знайдено: " + username));
    }

    // Метод реєстраціх нового користувача
    public void registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Користвуч з таким іменем вже існує");
        }

        user.setRole(Role.USER);
        System.out.println("Створюємо користувача з ролью:" + user.getRole());

        // Шифруем пароль перед зберіганням
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Зберігаєм користувача
        userRepository.save(user);

        // Створюємо корзину і зберігаємо з користувачем
        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public User getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Користувач авторизований");
        }

        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("Користувача не знайдено: " + username)
                );
    }

}
