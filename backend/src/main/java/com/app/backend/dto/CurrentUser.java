package com.app.backend.dto;

import com.app.backend.enums.Role;

public record CurrentUser(Long id, String username, String email, Role role) {
}
