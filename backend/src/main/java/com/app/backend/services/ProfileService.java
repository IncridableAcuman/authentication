package com.app.backend.services;

import com.app.backend.dto.UpdateProfile;
import com.app.backend.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserService userService;

    @Transactional
    public String editProfile(Long id, UpdateProfile profile){
        User user=userService.findUserById(id);
        userService.updateProfile(profile);
        return "Profile updated successfully";
    }
}
