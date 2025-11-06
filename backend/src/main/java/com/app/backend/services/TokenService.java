package com.app.backend.services;

import com.app.backend.entities.Token;
import com.app.backend.entities.User;
import com.app.backend.exceptions.NotFoundExceptionHandler;
import com.app.backend.repositories.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenService {
    @Value("${jwt.refreshTime}")
    private int refreshTime;
    private final TokenRepository tokenRepository;

    public void create(User user,String refreshToken){
        Token token=new Token();
        token.setUser(user);
        token.setRefreshToken(refreshToken);
        token.setExpiryDate(LocalDateTime.now().plusSeconds(refreshTime/1000));
         tokenRepository.save(token);
    }
    public void authToken(String refreshToken,User user){
        tokenRepository.findByUser(user).ifPresentOrElse(token -> {
            token.setUser(user);
            token.setRefreshToken(refreshToken);
            token.setExpiryDate(LocalDateTime.now().plusSeconds(refreshTime/1000));
            tokenRepository.save(token);
        },
                ()->create(user,refreshToken)
        );
    }
    public Token findByRefreshToken(String refreshToken){
        return tokenRepository.findByRefreshToken(refreshToken).orElseThrow(()->new NotFoundExceptionHandler("Token not found"));
    }
    public void deleteToken(String refreshToken){
        Token token=findByRefreshToken(refreshToken);
        tokenRepository.delete(token);
    }
}
