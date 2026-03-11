package com.OnlineElectronicsStore.OnlineElectronicsStore.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET = "my-secret-key-12345";

    // 15 минут
    private static final long ACCESS_EXPIRATION = 1000 * 60 * 15;

    // 7 дней
    private static final long REFRESH_EXPIRATION = 1000L * 60 * 60 * 24 * 7;

    // 🔹 ACCESS TOKEN
    public String generateAccessToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withClaim("type", "access")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION))
                .sign(Algorithm.HMAC256(SECRET));
    }

    // 🔹 REFRESH TOKEN
    public String generateRefreshToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withClaim("type", "refresh")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION))
                .sign(Algorithm.HMAC256(SECRET));
    }

    // 🔹 Проверка токена
    public DecodedJWT validateToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        return verifier.verify(token);
    }

    public String extractUsername(DecodedJWT decodedJWT) {
        return decodedJWT.getSubject();
    }

    public String extractType(DecodedJWT decodedJWT) {
        return decodedJWT.getClaim("type").asString();
    }
}
