package com.app.backend.services;

import com.app.backend.entities.Token;
import com.app.backend.entities.User;
import com.app.backend.repositories.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    public void create(User user,String refreshToken){
        Token token=new Token();
        token.setUser(user);
        token.setRefreshToken(refreshToken);
        token.setExpiryDate(LocalDateTime.now().plusDays(7));
         tokenRepository.save(token);
    }
    public void authToken(String refreshToken,User user){
        tokenRepository.findByUser(user).ifPresentOrElse(token -> {
            token.setUser(user);
            token.setRefreshToken(refreshToken);
            token.setExpiryDate(LocalDateTime.now().plusDays(7));
            tokenRepository.save(token);
        },
                ()->create(user,refreshToken)
        );
    }
}
