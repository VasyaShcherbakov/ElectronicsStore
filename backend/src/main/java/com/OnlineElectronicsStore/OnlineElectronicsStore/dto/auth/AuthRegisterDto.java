package com.OnlineElectronicsStore.OnlineElectronicsStore.dto.auth;

import com.OnlineElectronicsStore.OnlineElectronicsStore.model.Gender;

public class AuthRegisterDto {

    private String username;
    private String email;
    private String password;
    private Gender gender;

    // ===== Constructors =====
    public AuthRegisterDto() {}

    public AuthRegisterDto(String username, String email, String password, Gender gender) {
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
