package com.example.onlineshop.service.impl;


import com.example.onlineshop.dto.LoginRequest;
import com.example.onlineshop.dto.RegisterRequest;
import com.example.onlineshop.entity.Role;
import com.example.onlineshop.entity.User;
import com.example.onlineshop.exception.EmailAlreadyExistsException;
import com.example.onlineshop.exception.InvalidCredentialsException;
import com.example.onlineshop.exception.UserAlreadyExistsException;
import com.example.onlineshop.repository.UserRepository;
import com.example.onlineshop.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.onlineshop.dto.AuthResponse;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtServiceImpl jwtService;

    @Override
    public void register(RegisterRequest request){
        if (userRepository.existsByUsername(request.username())) {
            throw new UserAlreadyExistsException(
                    "Username is already in use"
            );
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(
                    "Email is already in use"
            );
        }

        User user = new User();

        user.setUsername(request.username());
        user.setEmail(request.email());

        user.setPassword(
                passwordEncoder.encode(request.password())
        );

        user.setRole(Role.USER);

        userRepository.save(user);
    }

    @Override
    public AuthResponse login(LoginRequest request){
        User user = userRepository
                .findByUsername(request.username())
                .orElseThrow(() -> new InvalidCredentialsException(
                                "Invalid username or password"
                        )
                );

        boolean passwordMatches = passwordEncoder.matches(
                request.password(),
                user.getPassword()
        );

        if (!passwordMatches) {
            throw new InvalidCredentialsException(
                    "Invalid username or password"
            );
        }

        String token = jwtService.generateToken(user);

        return new AuthResponse(token, "Bearer");
    }
}
