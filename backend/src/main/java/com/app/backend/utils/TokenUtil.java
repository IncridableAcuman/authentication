package com.app.backend.utils;

import com.app.backend.entities.User;
import com.app.backend.enums.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenUtil {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.accessTime}")
    private int accessTime;
    @Value("${jwt.refreshTime}")
    private int refreshTime;
    private SecretKey key;

    @PostConstruct
    public void init(){
         this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user, TokenType type){
        int expiration = (type == TokenType.ACCESS ? accessTime : refreshTime);
        return Jwts
                .builder()
                .subject(user.getUsername())
                .claim("email",user.getEmail())
                .claim("role",user.getRole())
                .claim("id",user.getId())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(key)
                .compact();
    }
    public String generateAccessToken(User user){
        return generateToken(user,TokenType.ACCESS);
    }
    public String generateRefreshToken(User user){
        return generateToken(user,TokenType.REFRESH);
    }
    public Claims extractClaim(String token){
        return Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public String extractSubject(String token){
        return  extractClaim(token).getSubject();
    }
    public Date extractExpiryDate(String token){
        return extractClaim(token).getExpiration();
    }
    public boolean validateToken(String token){
        try {
            Date expiration=extractExpiryDate(token);
            return expiration.after(new Date());
        } catch (RuntimeException e) {
            return false;
        }
    }
}
