package com.app.backend.repositories;

import com.app.backend.entities.Token;
import com.app.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByUser(User user);
    Optional<Token> findByRefreshToken(String refreshToken);
}
