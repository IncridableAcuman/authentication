package com.app.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Username must be required!")
    @Size(min = 3,max = 100,message = "Username must be at between 3 and 100 characters")
    private String username;

    @NotBlank(message = "Email must be required!")
    @Email
    private String email;

    @NotBlank(message = "Password must be required!")
    @Size(min = 8,max = 255,message = "Password must be at least 8 characters long!")
    private String password;
}
