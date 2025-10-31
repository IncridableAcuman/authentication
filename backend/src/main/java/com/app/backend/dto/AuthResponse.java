package com.app.backend.dto;

import com.app.backend.enums.Role;

import java.util.UUID;

public record AuthResponse(
        UUID id,
        String username,
        String email,
        Role role,
        String accessToken
) {
}
