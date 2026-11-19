package com.example.onlineshop.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/me")
    public Map<String, String> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        return Map.of("username", jwt.getSubject());
    }

    @GetMapping("/token-info")
    public Map<String, Object> getTokenInfo(@AuthenticationPrincipal Jwt jwt) {
        return jwt.getClaims();
    }

    @GetMapping("/debug-authorities")
    public Map<String , Object> debugAuthorities(Authentication authentication) {

        return Map.of(
                "username" , authentication.getName(),
                "authorities" , authentication.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        );
    }
}
