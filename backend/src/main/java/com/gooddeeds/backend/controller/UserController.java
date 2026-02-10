package com.gooddeeds.backend.controller;

import com.gooddeeds.backend.dto.UserResponseDTO;
import com.gooddeeds.backend.exception.AccessDeniedException;
import com.gooddeeds.backend.mapper.UserMapper;
import com.gooddeeds.backend.security.SecurityUtils;
import com.gooddeeds.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //Create user
    @PostMapping
    public UserResponseDTO createUser(@Valid @RequestBody CreateUserRequest request) {
        return UserMapper.toDTO(
                userService.createUser(request)
        );
    }

    //Get user by ID (owner only)
    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable UUID id) {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        if (!currentUserId.equals(id)) {
            throw new AccessDeniedException("You can only view your own profile");
        }
        return userService.getUserById(id)
                .map(UserMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    //Get user by email (owner only)
    @GetMapping("/by-email")
    public UserResponseDTO getUserByEmail(@RequestParam String email) {
        String currentEmail = SecurityUtils.getCurrentEmail();
        if (!currentEmail.equalsIgnoreCase(email)) {
            throw new AccessDeniedException("You can only view your own profile");
        }
        return userService.getUserByEmail(email)
                .map(UserMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    //Update user (owner only)
    @PutMapping("/{id}")
    public UserResponseDTO updateUser(
            @PathVariable UUID id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email
    ) {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        if (!currentUserId.equals(id)) {
            throw new AccessDeniedException("You can only update your own profile");
        }
        return UserMapper.toDTO(
                userService.updateUser(id, name, email)
        );
    }

    //Delete user (owner only)
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        if (!currentUserId.equals(id)) {
            throw new AccessDeniedException("You can only delete your own account");
        }
        userService.deleteUser(id);
    }
}




