package com.gooddeeds.backend.controller;

import com.gooddeeds.backend.dto.LoginRequest;
import com.gooddeeds.backend.model.User;
import com.gooddeeds.backend.repository.UserRepository;
import com.gooddeeds.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        String email = authentication.getName();
        
        // Fetch user to get the ID for the token
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found after authentication"));
        
        String token = jwtService.generateToken(email, user.getId());

        return Map.of("token", token);
    }
}


