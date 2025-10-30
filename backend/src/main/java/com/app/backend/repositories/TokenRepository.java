package com.app.backend.repositories;

import com.app.backend.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface TokenRepository extends JpaRepository<Token, UUID> {
}
