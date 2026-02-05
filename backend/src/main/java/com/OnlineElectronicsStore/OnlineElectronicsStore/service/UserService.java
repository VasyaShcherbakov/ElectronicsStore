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
    private final PasswordEncoder passwordEncoder; // 👈 Изменили тип на PasswordEncoder

    @Autowired
    public UserService(UserRepository userRepository,
                       CartRepository cartRepository,
                       PasswordEncoder passwordEncoder) { // 👈 Этот бин уже есть в SecurityConfig
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ Реализация метода, который Spring Security вызывает при логине
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));
    }

    // ✅ Метод регистрации нового пользователя
    public void registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Пользователь с таким именем уже существует!");
        }

        user.setRole(Role.USER);
        System.out.println("Создаём пользователя с ролью: " + user.getRole());

        // Шифруем пароль перед сохранением
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Сохраняем пользователя
        userRepository.save(user);

        // ✅ Создаём корзину и привязываем к пользователю
        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);
    }

    public User getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Пользователь не авторизован");
        }

        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("Пользователь не найден: " + username)
                );
    }

}
