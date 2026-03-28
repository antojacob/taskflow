package com.taskflow.backend.controller;

import com.taskflow.backend.dto.AuthResponse;
import com.taskflow.backend.dto.LoginRequest;
import com.taskflow.backend.dto.RegisterRequest;
import com.taskflow.backend.model.User;
import com.taskflow.backend.repository.UserRepository;
import com.taskflow.backend.security.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173") // Allow React frontend
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserRepository userRepository, 
                         PasswordEncoder passwordEncoder, 
                         JwtTokenProvider jwtTokenProvider, 
                         AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Email already registered");
            return ResponseEntity.badRequest().body(error);
        }

        // Create new user
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        // Generate token
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles("USER")
                .build();
        
        String token = jwtTokenProvider.generateToken(userDetails);

        AuthResponse response = new AuthResponse(token, user.getEmail(), user.getFullName(), user.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            UserDetails userDetails = org.springframework.security.core.userdetails.User
                    .withUsername(request.getEmail())
                    .password(userRepository.findByEmail(request.getEmail()).get().getPassword())
                    .roles("USER")
                    .build();

            String token = jwtTokenProvider.generateToken(userDetails);
            User user = userRepository.findByEmail(request.getEmail()).get();

            AuthResponse response = new AuthResponse(token, user.getEmail(), user.getFullName(), user.getId());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Invalid email or password");
            return ResponseEntity.badRequest().body(error);
        }
    }
    
}