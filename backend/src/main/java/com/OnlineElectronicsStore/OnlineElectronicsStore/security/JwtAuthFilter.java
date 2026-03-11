package com.OnlineElectronicsStore.OnlineElectronicsStore.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final List<String> EXCLUDED_PATHS = List.of(
            "/api/auth/login",
            "/api/auth/register",
            "/v3/api-docs",
            "/swagger-ui",
            "/swagger-ui.html",
            "/api/auth/refresh"
    );

    @Autowired
    private JwtService jwtService;

    @Autowired
    @Qualifier("userDetailsServiceImpl")
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        System.out.println("➡️ Incoming request path: " + path);

        // если endpoint публичный — пропускаем
        if (isExcluded(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);

            try {

                // проверяем токен
                DecodedJWT decodedJWT = jwtService.validateToken(token);

                // проверяем тип токена
                String type = jwtService.extractType(decodedJWT);

                // фильтр принимает только ACCESS токены
                if (!"access".equals(type)) {
                    filterChain.doFilter(request, response);
                    return;
                }

                String username = jwtService.extractUsername(decodedJWT);

                if (username != null &&
                        SecurityContextHolder.getContext().getAuthentication() == null) {

                    UserDetails userDetails =
                            userDetailsService.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authentication);
                }

            } catch (Exception e) {
                System.out.println("❌ JWT error: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isExcluded(String path) {
        return EXCLUDED_PATHS.stream().anyMatch(path::startsWith);
    }
}