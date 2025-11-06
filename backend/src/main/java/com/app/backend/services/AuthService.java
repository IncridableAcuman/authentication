package com.app.backend.services;

import com.app.backend.dto.*;
import com.app.backend.entities.User;
import com.app.backend.exceptions.BadRequestExceptionHandler;
import com.app.backend.exceptions.UnAuthorizeExceptionHandler;
import com.app.backend.utils.CookieUtil;
import com.app.backend.utils.MailUtil;
import com.app.backend.utils.TokenUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final TokenService tokenService;
    private final UserService userService;
    private final CacheService cacheService;
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
        cacheService.saveToken(accessToken, user.getUsername());
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
        cacheService.saveToken(accessToken, user.getUsername());
        tokenService.authToken(refreshToken,user);
        cookieUtil.addCookie(refreshToken,response);
        return authResponse(user,accessToken);
    }

    @Transactional
    public AuthResponse refresh(String refreshToken,HttpServletResponse response){
        if(!tokenUtil.validateToken(refreshToken)){
            throw new BadRequestExceptionHandler("Invalid token");
        }
        User user=tokenService.findByRefreshToken(refreshToken).getUser();
        tokenService.deleteToken(refreshToken);
        String newAccessToken=tokenUtil.generateAccessToken(user);
        String newRefreshToken=tokenUtil.generateRefreshToken(user);
        cacheService.saveToken(newAccessToken, user.getUsername());
        tokenService.authToken(newRefreshToken,user);
        cookieUtil.addCookie(newRefreshToken,response);
        return authResponse(user,newAccessToken);
    }

    @Transactional
    public void logout(String refreshToken,HttpServletResponse response){
        if (!tokenUtil.validateToken(refreshToken)) {
            throw new BadRequestExceptionHandler("Invalid refresh token");
        }
        String username=tokenUtil.extractSubject(refreshToken);
        userService.findSubject(username);
        cacheService.removeTokenByUsername(username);
        tokenService.deleteToken(refreshToken);
        cookieUtil.clearCookie(response);
    }
    @Transactional
    public String forgotPassword(ForgotPasswordRequest request){
        User user=userService.findUser(request.getEmail());
        String token=tokenUtil.generateAccessToken(user);
        String url="http://localhost:5173/reset-password?token="+token;
        mailUtil.sendMail(user.getEmail(),"Reset Password",url);
        return "Reset password link sent to email";
    }
    @Transactional
    public String resetPassword(ResetPasswordRequest request){
        if (!tokenUtil.validateToken(request.getToken())){
            throw new UnAuthorizeExceptionHandler("Invalid token");
        }
        String subject=tokenUtil.extractSubject(request.getToken());
        User user=userService.findSubject(subject);
        userService.updatePassword(request.getPassword(),user);
        cacheService.removeToken(request.getToken());
        return "Password updated successfully";
    }
    public CurrentUser getCurrentUser(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")){
            throw new UnAuthorizeExceptionHandler("User not authenticated!");
        }
        User user=(User) authentication.getPrincipal();
        return new CurrentUser(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }
}
