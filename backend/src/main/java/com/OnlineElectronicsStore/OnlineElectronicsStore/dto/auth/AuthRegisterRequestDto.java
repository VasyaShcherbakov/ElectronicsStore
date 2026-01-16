package com.OnlineElectronicsStore.OnlineElectronicsStore.dto.auth;

import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(
        name = "AuthRegisterRequestDto",
        description = "DTO для реєстрації нового користувача"
)
public class AuthRegisterRequestDto {

    @Schema(
            description = "Ім'я користувача (логін)",
            example = "VasyaKot"
    )
    @NotBlank(message = "Ім'я користувача не може бути порожнім")
    @Size(min = 3, max = 50, message = "Ім'я користувача повинно містити від 3 до 50 символів")
    private String username;

    @Schema(
            description = "Email користувача",
            example = "vasya@example.com"
    )
    @NotBlank(message = "Email не може бути порожнім")
    @Email(message = "Некоректний формат email")
    private String email;

    @Schema(
            description = "Пароль користувача",
            example = "strongPassword123"
    )
    @NotBlank(message = "Пароль не може бути порожнім")
    @Size(min = 6, message = "Пароль повинен містити щонайменше 6 символів")
    private String password;

    @Schema(
            description = "Стать користувача",
            example = "MALE"
    )
    @NotNull(message = "Стать обов'язкова")
    private Gender gender;

    // ===== Constructors =====
    public AuthRegisterRequestDto() {}

    public AuthRegisterRequestDto(String username, String email, String password, Gender gender) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.gender = gender;
    }

    // ===== Getters & Setters =====
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
