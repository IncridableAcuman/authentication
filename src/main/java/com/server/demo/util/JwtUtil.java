package com.server.demo.util;

import com.server.demo.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access_time}")
    private Long accessTime;

    @Value("${jwt.refresh_time}")
    private Long refreshTime;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private String generateToken(User user, Long expiryTime) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("username", user.getUsername())
                .claim("id", user.getId())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiryTime))
                .signWith(key)
                .compact();
    }

    public Map<String, String> getTokens(User user) {
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", generateToken(user, accessTime));
        tokens.put("refreshToken", generateToken(user, refreshTime));
        return tokens;
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("JWT validatsiyadan o'tmadi: {}", e.getMessage());
            return false;
        }
    }

    public String getSubjectFromToken(String token) {
        return extractAllClaims(token).getSubject();
    }
}