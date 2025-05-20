package com.abyss.abyss.service;

import com.abyss.abyss.dto.LoginRequest;
import com.abyss.abyss.dto.LoginResponse;
import com.abyss.abyss.dto.UserResponse;
import com.abyss.abyss.model.User;
import com.abyss.abyss.repository.UserRepository;
import com.abyss.abyss.dto.RegisterRequest;
import com.abyss.abyss.security.JwtService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.abyss.abyss.exception.UserNotFoundException;
import com.abyss.abyss.exception.InvalidPasswordException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final JwtService jwtService;

    public boolean register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return false;
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .build();

        userRepository.save(user);
        return true;
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException("user not found"));

        if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException("invalid password");
        }

        String token = jwtService.generateToken(user.getUsername());
        return new LoginResponse(token);
    }

    public UserResponse getUser(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("user not found"));

        return new UserResponse(user.getId(), user.getUsername(), user.getRole());
    }

}
