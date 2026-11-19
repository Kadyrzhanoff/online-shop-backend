package com.example.onlineshop.service;

import com.example.onlineshop.dto.AuthResponse;
import com.example.onlineshop.dto.LoginRequest;
import com.example.onlineshop.dto.RegisterRequest;

public interface AuthService {
    void register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
