package com.app.backend.services;

import com.app.backend.dto.AuthResponse;
import com.app.backend.dto.RegisterRequest;
import com.app.backend.entities.Token;
import com.app.backend.entities.User;
import com.app.backend.enums.Role;
import com.app.backend.exceptions.BadRequestExceptionHandler;
import com.app.backend.repositories.TokenRepository;
import com.app.backend.repositories.UserRepository;
import com.app.backend.utils.CookieUtil;
import com.app.backend.utils.MailUtil;
import com.app.backend.utils.TokenUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailUtil mailUtil;
    private final TokenUtil tokenUtil;
    private final CookieUtil cookieUtil;

    @Transactional
    public AuthResponse register(RegisterRequest request, HttpServletResponse response){
        if (userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new BadRequestExceptionHandler("User already exist.");
        }
        User user=new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        String accessToken=tokenUtil.generateAccessToken(user);
        String refreshToken=tokenUtil.generateRefreshToken(user);
        Token token=new Token();
        token.setRefreshToken(refreshToken);
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusDays(7));
        tokenRepository.save(token);
        cookieUtil.addCookie(refreshToken,response);
        mailUtil.sendMail(user.getEmail(), "Hi "+user.getUsername(),"Your account successfully registered");
        return new AuthResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                accessToken
                );
    }
}
