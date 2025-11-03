package com.app.backend.services;

import com.app.backend.dto.AuthResponse;
import com.app.backend.dto.RegisterRequest;
import com.app.backend.entities.User;
import com.app.backend.utils.CookieUtil;
import com.app.backend.utils.MailUtil;
import com.app.backend.utils.TokenUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final TokenService tokenService;
    private final UserService userService;
    private final MailUtil mailUtil;
    private final TokenUtil tokenUtil;
    private final CookieUtil cookieUtil;
    private final CacheService cacheService;

    public AuthResponse authResponse(User user,String accessToken){
        return new AuthResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                accessToken
        );
    }

    @Transactional
    public AuthResponse register(RegisterRequest request, HttpServletResponse response){
        User user=userService.create(request);
        String accessToken=tokenUtil.generateAccessToken(user);
        String refreshToken=tokenUtil.generateRefreshToken(user);
        cacheService.saveToken(accessToken, user.getUsername());
        tokenService.create(user,refreshToken);
        cookieUtil.addCookie(refreshToken,response);
        mailUtil.sendMail(user.getEmail(), "Hi "+user.getUsername(),"Your account was successfully registered");
        return authResponse(user,accessToken);
    }



}
