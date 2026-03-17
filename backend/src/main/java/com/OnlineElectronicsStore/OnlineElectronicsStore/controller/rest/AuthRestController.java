package com.OnlineElectronicsStore.OnlineElectronicsStore.controller.rest;

import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.UserSummaryDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.auth.AuthResponseDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.mapper.UserSummaryMapper;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.User;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.ProductRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.repository.UserRepository;
import com.OnlineElectronicsStore.OnlineElectronicsStore.security.JwtService;
import com.OnlineElectronicsStore.OnlineElectronicsStore.service.UserService;
import com.auth0.jwt.interfaces.DecodedJWT;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

@Tag(
        name = "Authentication",
        description = "Реєстрація, логін та отримання даних поточного користувача"
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
    private final UserSummaryMapper userSummaryMapper;

    public AuthRestController(ProductRepository productRepository,
                              UserService userService,
                              UserRepository userRepository,
                              AuthenticationManager authenticationManager,
                              JwtService jwtService, UserSummaryMapper userSummaryMapper) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userSummaryMapper = userSummaryMapper;
    }

    private static final Logger logger = LoggerFactory.getLogger(AuthRestController.class);

    @Operation(
            summary = "Реєстрація користувача",
            description = "Створює нового користувача та кошик для нього"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Користувач успішно зареєстрований"),
            @ApiResponse(responseCode = "400", description = "Помилка реєстрації")
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid User user) {
        try {
            log.info("Надійшов запит на реєстрацію: {}", user);
            userService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Користувач успішно зареєстрований"));
        } catch (Exception e) {
            log.error("Помилка реєстрації", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Помилка реєстрації: " + e.getMessage()));
        }
    }



    @Operation(
            summary = "Вхід користувача",
            description = "Аутентифікація користувача та видача JWT-токену"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успішний вхід, токен виданий"),
            @ApiResponse(responseCode = "401", description = "Невірні облікові дані")
    })


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {

        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            String authenticatedUsername = authentication.getName();

            // 1. Генерация токенов
            String accessToken = jwtService.generateAccessToken(authenticatedUsername);
            String refreshToken = jwtService.generateRefreshToken(authenticatedUsername);

            // 2. Получаем пользователя из БД
            User user = userRepository.findByUsername(authenticatedUsername)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 3. Маппим в DTO
            UserSummaryDto userDto = userSummaryMapper.toDto(user);

            // 4. Собираем ответ
            AuthResponseDto response = new AuthResponseDto(
                    accessToken,
                    refreshToken,
                    userDto
            );


            return ResponseEntity.ok(response);

        } catch (Exception e) {

            log.error("Помилка входу: {}", e.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Неправильне ім'я користувача або пароль"));
        }
    }



    @Operation(
            summary = "Оновлення access токена",
            description = "Приймає refresh токен та повертає новий access токен"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Access токен успішно оновлено"),
            @ApiResponse(responseCode = "401", description = "Refresh токен недійсний або прострочений")
    })

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {

        logger.info("➡️ Incoming request to /refresh");

        String refreshToken = request.get("refreshToken");
        logger.info("Received refresh token: {}", refreshToken);

        try {
            DecodedJWT decodedJWT = jwtService.validateToken(refreshToken);
            String type = jwtService.extractType(decodedJWT);
            logger.info("Token type: {}", type);

            if (!"refresh".equals(type)) {
                logger.warn("Invalid token type: {}", type);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid refresh token"));
            }

            String username = jwtService.extractUsername(decodedJWT);
            logger.info("Token belongs to user: {}", username);

            String newAccessToken = jwtService.generateAccessToken(username);
            logger.info("Generated new access token for user: {}", username);

            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));

        } catch (Exception e) {
            logger.error("Failed to refresh token", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Refresh token expired or invalid"));
        }
    }


    @Operation(
            summary = "Хто я",
            description = "Повертає ім'я автентифікованого користувача."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Користувач авторизований"),
            @ApiResponse(responseCode = "401", description = "Не авторизовано")
    })

    @GetMapping("/whoami")
    public ResponseEntity<?> whoAmI(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Не авторизовано"));
        }
        return ResponseEntity.ok(Map.of("username", authentication.getName()));
    }


    @Operation(
            summary = "Профіль поточного користувача",
            description = "Повертає дані поточного користувача"
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Дані користувача"),
            @ApiResponse(responseCode = "401", description = "Не авторизовано"),
            @ApiResponse(responseCode = "404", description = "Користувач не знайдено")
    })
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Не авторизовано"));
        }

        return userRepository.findByUsername(userDetails.getUsername())
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Користувач не знайдено")));
    }
}
