package com.app.backend.services;

import com.app.backend.dto.UpdateProfile;
import com.app.backend.entities.User;
import com.app.backend.exceptions.UnAuthorizeExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserService userService;

    @Transactional
    public void editProfile(UpdateProfile profile){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")){
            throw new UnAuthorizeExceptionHandler("User not authenticated");
        }
        User user = (User) authentication.getPrincipal();
        userService.updateProfile(user,profile);
    }
}
