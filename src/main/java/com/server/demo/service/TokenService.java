package com.server.demo.service;

import com.server.demo.entity.Token;
import com.server.demo.entity.User;
import com.server.demo.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    @Transactional(readOnly = true)
    public Optional<Token> findTokenByUser(User user) {
        return tokenRepository.findByUser(user);
    }

    @Transactional
    public void saveToken(User user, String refreshToken) {
        Token token = findTokenByUser(user).orElseGet(Token::new);
        token.setUser(user);
        token.setRefreshToken(refreshToken);
        token.setExpiryDate(OffsetDateTime.now().plusDays(7)); // DateTimeException tuzatildi
        tokenRepository.save(token);
    }

    @Transactional
    public void removeToken(User user) {
        tokenRepository.findByUser(user).ifPresent(tokenRepository::delete);
    }
}