package com.OnlineElectronicsStore.OnlineElectronicsStore.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(
        name = "AuthLoginDto",
        description = "DTO для входу користувача (логін)"
)
public class AuthLoginDto {

    @Schema(
            description = "Ім'я користувача",
            example = "VasyaKot"
    )
    @NotBlank(message = "Ім'я користувача не може бути порожнім")
    private String username;

    @Schema(
            description = "Пароль користувача",
            example = "strongPassword123"
    )
    @NotBlank(message = "Пароль не може бути порожнім")
    private String password;

    // ===== Constructors =====
    public AuthLoginDto() {}

    public AuthLoginDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // ===== Getters & Setters =====
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
