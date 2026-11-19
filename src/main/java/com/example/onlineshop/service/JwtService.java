package com.example.onlineshop.service;

import com.example.onlineshop.entity.User;

public interface JwtService {

    String generateToken(User user);
}
