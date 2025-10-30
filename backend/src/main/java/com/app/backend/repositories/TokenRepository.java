package com.app.backend.repositories;

import com.app.backend.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<String, Token> {
}
