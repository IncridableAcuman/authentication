package com.app.backend.controllers;

import com.app.backend.dto.UpdateProfile;
import com.app.backend.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @PatchMapping("/update")
    public ResponseEntity<String> editProfile(@RequestBody UpdateProfile profile){
        profileService.editProfile(profile);
        return ResponseEntity.ok("Profile updated successfully");
    }
}
