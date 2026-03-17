package com.OnlineElectronicsStore.OnlineElectronicsStore.dto.auth;

import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.UserDto;
import com.OnlineElectronicsStore.OnlineElectronicsStore.dto.UserSummaryDto;
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
    private String accessToken;

    @Schema(
            name = "RefreshToken",
            description = "Токен для оновлення сесії (refresh token). Використовується, коли термін дії access token закінчився.",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    )

    private String refreshToken;


    @Schema(
            description = "Дані авторизованого користувача"
    )
    private UserSummaryDto user;




    // ===== Constructors =====
    public AuthResponseDto(String accessToken, String refreshToken, UserSummaryDto user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.user = user;
    }

    // ===== Getters & Setters =====

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public UserSummaryDto getUser() {
        return user;
    }

    public void setUser(UserSummaryDto user) {
        this.user = user;
    }
}
