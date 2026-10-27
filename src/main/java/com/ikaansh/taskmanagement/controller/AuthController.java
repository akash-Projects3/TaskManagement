package com.ikaansh.taskmanagement.controller;

import com.ikaansh.taskmanagement.dto.AuthResponse;
import com.ikaansh.taskmanagement.dto.LoginRequest;
import com.ikaansh.taskmanagement.dto.RegisterRequest;
import com.ikaansh.taskmanagement.entity.User;
import com.ikaansh.taskmanagement.service.JwtService;
import com.ikaansh.taskmanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService,
                          JwtService jwtService,
                          PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        User user = userService.registerUser(
                request.name(), request.email(), request.password());

        String token = jwtService.generateToken(user.getId(), user.getEmail());

        AuthResponse response = new AuthResponse(
                token, "Bearer", user.getId(), user.getName(), user.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        User user = userService.findByEmail(request.email());

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getId(), user.getEmail());

        AuthResponse response = new AuthResponse(
                token, "Bearer", user.getId(), user.getName(), user.getEmail());

        return ResponseEntity.ok(response);
    }
}
