package com.taskflow.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String email;
    private String fullName;
    private Long userId;

    // === MANUAL CONSTRUCTOR (Fixes the error) ===
    public AuthResponse(String token, String email, String fullName, Long userId) {
        this.token = token;
        this.email = email;
        this.fullName = fullName;
        this.userId = userId;
    }

    // === MANUAL GETTERS ===
    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public Long getUserId() {
        return userId;
    }

    // === MANUAL SETTERS ===
    public void setToken(String token) {
        this.token = token;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}