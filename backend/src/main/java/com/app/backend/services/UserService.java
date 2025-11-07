package com.app.backend.services;

import com.app.backend.dto.RegisterRequest;
import com.app.backend.entities.User;
import com.app.backend.enums.Role;
import com.app.backend.exceptions.BadRequestExceptionHandler;
import com.app.backend.exceptions.NotFoundExceptionHandler;
import com.app.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User create(RegisterRequest request){
        if (userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new BadRequestExceptionHandler("User already exist.");
        }
        User user=new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        return userRepository.save(user);
    }
    @Transactional
    public User findUser(String email){
        return userRepository.findByEmail(email).orElseThrow(()->new NotFoundExceptionHandler("User not found"));
    }
    @Transactional
    public User findSubject(String username){
        return userRepository.findByUsername(username).orElseThrow(()->new NotFoundExceptionHandler("User not found"));
    }
    public void matchesPasswords(String rawPassword,String encodedPassword){
        if(!passwordEncoder.matches(rawPassword,encodedPassword)){
            throw new BadRequestExceptionHandler("Password does not matches!");
        }
    }@Transactional
    public void updatePassword(String password,User user){
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }
    public void updateRole(Long id){
        User user=userRepository.findById(id).orElseThrow(()->new NotFoundExceptionHandler("User not found"));
        if(user.getRole().equals(Role.USER)){
            user.setRole(Role.ADMIN);
        }
        userRepository.save(user);
    }
}
