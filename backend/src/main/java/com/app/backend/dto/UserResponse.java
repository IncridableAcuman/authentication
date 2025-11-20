package com.app.backend.dto;

import com.app.backend.enums.Role;

public record UserResponse(
        Long id,
        String username,
        String email,
        String firstName,
        String lastName,
        Role role,
        String avatar,
        boolean enabled
) {
}
