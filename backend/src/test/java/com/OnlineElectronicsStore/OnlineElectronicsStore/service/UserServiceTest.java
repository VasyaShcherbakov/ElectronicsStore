package com.OnlineElectronicsStore.OnlineElectronicsStore.service;

import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Cart;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Role;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.User;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.CartRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @AfterEach
    void clearSecurity() {
        SecurityContextHolder.clearContext();
    }

    // ================== registerUser ==================

    @Test
    void shouldRegisterUserSuccessfully() {
        User user = new User();
        user.setUsername("john");
        user.setPassword("plain");

        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plain")).thenReturn("encoded");

        userService.registerUser(user);

        assertEquals(Role.USER, user.getRole());
        assertEquals("encoded", user.getPassword());

        verify(userRepository).save(user);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void shouldThrowIfUsernameAlreadyExists() {
        User user = new User();
        user.setUsername("john");

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.registerUser(user));

        assertEquals("Користвуч з таким імям вже існує", ex.getMessage());
    }

    // ================== findByUsername ==================

    @Test
    void shouldReturnUserIfExists() {
        User user = new User();
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

        assertEquals(user, userService.findByUsername("john"));
    }

    @Test
    void shouldReturnNullIfUserNotFound() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());

        assertNull(userService.findByUsername("john"));
    }

    // ================== loadUserByUsername ==================


    @Test
    void shouldThrowIfUserNotFound() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());

        assertThrows(Exception.class,
                () -> userService.loadUserByUsername("john"));
    }

    // ================== getCurrentUser ==================

    @Test
    void shouldReturnCurrentUser() {
        User user = new User();
        user.setUsername("john");

        // Створюємо токен і ставимо authenticated = true
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        "john",
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );

        SecurityContextHolder.getContext().setAuthentication(authToken);

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

        User result = userService.getCurrentUser();

        assertEquals(user, result);
    }

    @Test
    void shouldThrowIfNotAuthenticated() {
        SecurityContextHolder.clearContext();

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.getCurrentUser());

        assertEquals("Користувач авторизований", ex.getMessage());
    }

    @Test
    void shouldThrowIfUserNotFoundInDb() {
        // Ставимо авторизованого користувача в SecurityContext
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        "john",
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );
        SecurityContextHolder.getContext().setAuthentication(authToken);


        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getCurrentUser();
        });

        assertEquals("Користувача не знайдено: john", exception.getMessage());
    }
}