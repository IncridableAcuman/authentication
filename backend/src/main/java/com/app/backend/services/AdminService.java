package com.app.backend.services;

import com.app.backend.dto.UserResponse;
import com.app.backend.entities.User;
import com.app.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;

    private UserResponse response(User user){
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getAvatar(),
                user.isEnabled()
        );
    }

    public List<UserResponse> seeAllUsers(){
       List<User> users = userRepository.findAll();
       return users.stream()
               .map(this::response)
               .toList();
    }
}
