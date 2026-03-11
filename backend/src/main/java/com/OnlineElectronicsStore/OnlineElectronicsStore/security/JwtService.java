package com.OnlineElectronicsStore.OnlineElectronicsStore.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-expiration}")
    private long accessExpiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;


    public String generateAccessToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withClaim("type", "access")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessExpiration))
                .sign(Algorithm.HMAC256(secret));
    }

    // 🔹 REFRESH TOKEN
    public String generateRefreshToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withClaim("type", "refresh")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshExpiration))
                .sign(Algorithm.HMAC256(secret));
    }

    // 🔹 Проверка токена
    public DecodedJWT validateToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
        return verifier.verify(token);
    }

    public String extractUsername(DecodedJWT decodedJWT) {
        return decodedJWT.getSubject();
    }

    public String extractType(DecodedJWT decodedJWT) {
        return decodedJWT.getClaim("type").asString();
    }
}
