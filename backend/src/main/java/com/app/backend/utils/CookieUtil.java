package com.app.backend.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
    @Value("${jwt.refreshTime}")
    private int refreshTime;
    public void addCookie(String refreshToken, HttpServletResponse response){
        Cookie cookie=new Cookie("refreshToken",refreshToken);
        cookie.setValue(refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/**");
        cookie.setSecure(false);
        cookie.setMaxAge(refreshTime);
        cookie.setAttribute("SameSite","Lax");
        response.addCookie(cookie);
    }
    public void clearCookie(HttpServletResponse response){
        Cookie cookie=new Cookie("refreshToken",null);
        cookie.setValue(null);
        cookie.setHttpOnly(true);
        cookie.setPath("/**");
        cookie.setSecure(false);
        cookie.setMaxAge(refreshTime);
        cookie.setAttribute("SameSite","Lax");
        response.addCookie(cookie);
    }
}
