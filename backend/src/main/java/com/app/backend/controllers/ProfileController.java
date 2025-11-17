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

    @PatchMapping("/update/{id}")
    public ResponseEntity<String> editProfile(@PathVariable Long id, @RequestBody UpdateProfile profile){
        return ResponseEntity.ok(profileService.updateProfile(id,profile));
    }
}
