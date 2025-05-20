package com.abyss.abyss.controller;

import com.abyss.abyss.dto.LoginRequest;
import com.abyss.abyss.dto.LoginResponse;
import com.abyss.abyss.dto.RegisterRequest;
import com.abyss.abyss.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        boolean success = userService.register(request);

        if (success) {
            return ResponseEntity.ok("successfully registered");
        } else {
            return ResponseEntity.badRequest().body("username already exists");
        }
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

}
