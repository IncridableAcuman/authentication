package com.app.backend.configs;

import com.app.backend.services.CustomUserDetailsService;
import com.app.backend.utils.TokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenUtil tokenUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        final String authHeader=request.getHeader("Authorization");
        String username = null;
        String jwt = null;
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            jwt=authHeader.substring(7);
            try {
                username=tokenUtil.extractSubject(jwt);
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }
        }
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            assert userDetailsService != null;
            UserDetails userDetails= userDetailsService.loadUserByUsername(username);

            if (tokenUtil.validateToken(jwt)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,null,userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request,response);
    }
}
