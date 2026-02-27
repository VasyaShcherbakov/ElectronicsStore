package com.OnlineElectronicsStore.OnlineElectronicsStore.dto;

import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Gender;
import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Користувач системи (повна інформація)")
public class UserDto {

    @Schema(
            description = "ID користувача",
            example = "5"
    )
    private Long id;

    @Schema(
            description = "Ім'я користувача (логін)",
            example = "ivan_petrov"
    )
    private String username;

    @Schema(
            description = "Email користувача",
            example = "ivan.petrov@gmail.com"
    )
    private String email;

    @Schema(
            description = "Стать користувача",
            example = "MALE"
    )
    private Gender gender;

    @Schema(
            description = "Роль користувача в системі",
            example = "USER"
    )
    private Role role;

    public UserDto() {}

    public UserDto(Long id, String username, String email, Gender gender, Role role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.gender = gender;
        this.role = role;
    }



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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
