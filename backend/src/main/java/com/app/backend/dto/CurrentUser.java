package com.app.backend.dto;

import com.app.backend.enums.Role;
import lombok.Data;

@Data
public record CurrentUser(Long id, String username, String email, Role role) {
}
