package com.server.demo.service;

import com.server.demo.dto.AuthResponse;
import com.server.demo.dto.RegisterRequest;
import com.server.demo.dto.UserResponse;
import com.server.demo.entity.User;
import com.server.demo.enums.Role;
import com.server.demo.exception.BadRequestException;
import com.server.demo.exception.NotFoundException;
import com.server.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
    }

    @Transactional(readOnly = true)
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }

    @Transactional
    public User create(RegisterRequest request) {
        existUser(request.getEmail());

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setEnabled(true);
        user.setAvatar("https://github.com/shadcn.png");
        user.setCreateAt(OffsetDateTime.now()); // DateTimeException tuzatildi

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public void existUser(String email) {
        if (userRepository.existsByEmail(email)) { // findByEmail o'rniga existsByEmail
            throw new BadRequestException("User already exists with email: " + email);
        }
    }

    @Transactional
    public User changeRole(Long id, Role newRole) {
        User user = findUserById(id);
        user.setRole(newRole != null ? newRole : (user.getRole() == Role.USER ? Role.ADMIN : Role.USER));
        return user; // Dirty checking avtomatik update qiladi
    }

    @Transactional
    public void updatePassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> userList() {
        return userRepository.findAll().stream()
                .map(this::userResponse)
                .toList();
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = findUserById(id);
        userRepository.delete(user);
    }

    public UserResponse userResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.isEnabled(),
                user.getAvatar(),
                user.getCreateAt(),
                user.getUpdatedAt()
        );
    }
    public User saveUser(User user){
        return userRepository.save(user);
    }

    public AuthResponse authResponse(User user, String accessToken) {
        return new AuthResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                accessToken
        );
    }
}