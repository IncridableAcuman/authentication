package com.app.backend.dto;

import com.app.backend.enums.Role;

public record AuthResponse(
        Long id,
        String username,
        String email,
        Role role,
        boolean enabled,
        String accessToken
) {
}
