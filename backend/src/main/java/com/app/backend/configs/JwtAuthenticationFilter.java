package com.app.backend.configs;

import com.app.backend.services.CustomUserDetailsService;
import com.app.backend.utils.TokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@NoArgsConstructor(force = true)
public class JwtAuthenticationFilter  {
    private final TokenUtil tokenUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        final String authHeader=request.getHeader("Authorization");
        String username;
        String jwt;
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            jwt=authHeader.substring(7);
            assert tokenUtil != null;
            username=tokenUtil.extractSubject(jwt);
        }

    }
}
