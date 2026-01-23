package com.gooddeeds.backend.controller;

import com.gooddeeds.backend.dto.LoginRequest;
import com.gooddeeds.backend.model.User;
import com.gooddeeds.backend.security.JwtService;
import com.gooddeeds.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {

        User user = userService.authenticate(
                request.getEmail(),
                request.getPassword()
        );

        String token = jwtService.generateToken(user.getEmail());

        return Map.of("token", token);
    }
}

