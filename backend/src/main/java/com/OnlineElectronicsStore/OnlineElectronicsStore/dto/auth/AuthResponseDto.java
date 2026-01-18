package com.OnlineElectronicsStore.OnlineElectronicsStore.dto.auth;

import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "AuthResponseDto",
        description = "Відповідь після успішної аутентифікації або реєстрації"
)
public class AuthResponseDto {

    @Schema(
            description = "JWT токен для доступу до захищених ендпоінтів",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    )
    private String token;

    @Schema(
            description = "Дані авторизованого користувача"
    )
    private UserDto user;

    // ===== Constructors =====
    public AuthResponseDto() {}

    public AuthResponseDto(String token, UserDto user) {
        this.token = token;
        this.user = user;
    }

    // ===== Getters & Setters =====
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}
