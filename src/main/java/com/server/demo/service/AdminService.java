package com.server.demo.service;

import com.server.demo.dto.UserResponse;
import com.server.demo.entity.User;
import com.server.demo.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserService userService;

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userService.userList();
    }

    @Transactional
    public void deleteUser(Long id) {
        userService.deleteUser(id);
    }

    @Transactional
    public UserResponse editRole(Long id, Role role) {
        User user = userService.changeRole(id, role);
        return userService.userResponse(user);
    }
}