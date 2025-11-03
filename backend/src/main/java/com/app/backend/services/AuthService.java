package com.app.backend.services;

import com.app.backend.dto.AuthRequest;
import com.app.backend.dto.AuthResponse;
import com.app.backend.dto.RegisterRequest;
import com.app.backend.entities.User;
import com.app.backend.exceptions.BadRequestExceptionHandler;
import com.app.backend.exceptions.UnAuthorizeExceptionHandler;
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
        tokenService.create(user,refreshToken);
        cookieUtil.addCookie(refreshToken,response);
        mailUtil.sendMail(user.getEmail(), "Hi "+user.getUsername(),"Your account was successfully registered");
        return authResponse(user,accessToken);
    }

    @Transactional
    public AuthResponse login(AuthRequest request,HttpServletResponse response){
        User user=userService.findUser(request.getEmail());
        userService.matchesPasswords(request.getPassword(), user.getPassword());
        String accessToken=tokenUtil.generateAccessToken(user);
        String refreshToken=tokenUtil.generateRefreshToken(user);
        tokenService.authToken(refreshToken,user);
        cookieUtil.addCookie(refreshToken,response);
        return authResponse(user,accessToken);
    }

    @Transactional
    public AuthResponse refresh(String refreshToken,HttpServletResponse response){
        if(!tokenUtil.validateToken(refreshToken)){
            throw new BadRequestExceptionHandler("Invalid token");
        }
        String username=tokenUtil.extractSubject(refreshToken);
        User user=userService.findSubject(username);
        String newAccessToken=tokenUtil.generateAccessToken(user);
        String newRefreshToken=tokenUtil.generateRefreshToken(user);
        tokenService.authToken(newRefreshToken,user);
        cookieUtil.addCookie(newRefreshToken,response);
        return authResponse(user,newAccessToken);
    }

    @Transactional
    public void logout(String refreshToken,HttpServletResponse response){
        String username=tokenUtil.extractSubject(refreshToken);
        User user=userService.findSubject(username);

        cookieUtil.clearCookie(response);
    }
}
