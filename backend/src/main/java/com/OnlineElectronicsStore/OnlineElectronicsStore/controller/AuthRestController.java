package com.OnlineElectronicsStore.OnlineElectronicsStore.controller;

import com.OnlineElectronicsStore.OnlineElectronicsStore.model.User;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.ProductRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.UserRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.security.JwtService;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(
        name = "Authentication",
        description = "Регистрация, логин и получение данных текущего пользователя"
)
@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    private static final Logger log = LoggerFactory.getLogger(AuthRestController.class);

    private final ProductRepository productRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthRestController(ProductRepository productRepository,
                              UserService userService,
                              UserRepository userRepository,
                              AuthenticationManager authenticationManager,
                              JwtService jwtService) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    // ================= REGISTER =================

    @Operation(
            summary = "Регистрация пользователя",
            description = "Создаёт нового пользователя и корзину для него"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Пользователь успешно зарегистрирован"),
            @ApiResponse(responseCode = "400", description = "Ошибка регистрации")
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid User user) {
        try {
            log.info("Пришёл запрос на регистрацию: {}", user);
            userService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Пользователь успешно зарегистрирован"));
        } catch (Exception e) {
            log.error("Ошибка регистрации", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Ошибка регистрации: " + e.getMessage()));
        }
    }

    // ================= LOGIN =================

    @Operation(
            summary = "Вход пользователя",
            description = "Аутентификация пользователя и выдача JWT-токена"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешный вход, токен выдан"),
            @ApiResponse(responseCode = "401", description = "Неверные учетные данные")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {

        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            String token = jwtService.generateToken(username);
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "username", username
            ));

        } catch (Exception e) {
            log.error("Ошибка входа: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Неверное имя пользователя или пароль"));
        }
    }

    // ================= WHO AM I =================

    @Operation(
            summary = "Кто я",
            description = "Возвращает имя текущего аутентифицированного пользователя"
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь авторизован"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @GetMapping("/whoami")
    public ResponseEntity<?> whoAmI(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Не авторизован"));
        }
        return ResponseEntity.ok(Map.of("username", authentication.getName()));
    }

    // ================= ME =================

    @Operation(
            summary = "Профиль текущего пользователя",
            description = "Возвращает данные текущего пользователя"
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Данные пользователя"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Не авторизован"));
        }

        return userRepository.findByUsername(userDetails.getUsername())
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Пользователь не найден")));
    }
}
