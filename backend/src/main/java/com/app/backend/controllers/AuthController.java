package com.app.backend.controllers;

import com.app.backend.dto.*;
import com.app.backend.services.AuthService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request, HttpServletResponse response) throws MessagingException {
        return ResponseEntity.ok(service.register(request,response));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request, HttpServletResponse response){
        return ResponseEntity.ok(service.login(request,response));
    }
    @GetMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@CookieValue(value = "refreshToken",required = false) String refreshToken,HttpServletResponse response){
        return ResponseEntity.ok(service.refresh(refreshToken,response));
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@CookieValue(value = "refreshToken",required = false) String refreshToken,HttpServletResponse response){
        service.logout(refreshToken,response);
        return ResponseEntity.ok("User successfully logged out");
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) throws MessagingException {
        return ResponseEntity.ok(service.forgotPassword(request));
    }
    @PutMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request){
        return ResponseEntity.ok(service.resetPassword(request));
    }
    @GetMapping("/me")
    public ResponseEntity<CurrentUser> getCurrentUser(){
        return ResponseEntity.ok(service.getCurrentUser());
    }
    @PatchMapping("/role/{id}")
    public ResponseEntity<String> updateRole(@PathVariable Long id){
        return ResponseEntity.ok(service.updateRole(id));
    }
    @GetMapping("/activation/{id}")
    public ResponseEntity<String> activationAccount(@PathVariable Long id){
        return ResponseEntity.ok(service.activation(id));
    }
}
