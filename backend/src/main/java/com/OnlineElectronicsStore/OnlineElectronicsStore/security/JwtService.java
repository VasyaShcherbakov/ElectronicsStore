package com.OnlineElectronicsStore.OnlineElectronicsStore.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    // 🔑 Секретный ключ (в продакшене храни в application.properties)
    private static final String SECRET = "my-secret-key-12345";

    // Срок действия токена — 24 часа
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    // 🧾 Создание токена
    public String generateToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRET));
    }

    // ✅ Проверка токена
    public String validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getSubject(); // Возвращает username
        } catch (Exception e) {
            return null; // Если токен неверный или просрочен
        }
    }
}
