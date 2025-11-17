package com.app.backend.dto;
import lombok.Data;

@Data
public class UpdateProfile {
    private String firstName;
    private String lastName;
    private String avatar;
    private String username;
    private String email;
    private String password;

}
