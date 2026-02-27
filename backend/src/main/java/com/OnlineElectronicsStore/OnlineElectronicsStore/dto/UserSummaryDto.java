package com.OnlineElectronicsStore.OnlineElectronicsStore.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Коротка інформація про користувача (безпечна для публічного використання)")
public class UserSummaryDto {

    @Schema(
            description = "ID користувача",
            example = "3"
    )
    private Long id;

    @Schema(
            description = "Ім'я користувача",
            example = "seller_ivan"
    )
    private String username;

    @Schema(
            description = "Email користувача (публічний)",
            example = "seller@gmail.com"
    )
    private String email;

    public UserSummaryDto() {}

    public UserSummaryDto(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public UserSummaryDto(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    // ====== Геттери та сеттери ======

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}
