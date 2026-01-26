package com.gooddeeds.backend.service.impl;

import com.gooddeeds.backend.controller.CreateUserRequest;
import com.gooddeeds.backend.exception.EmailAlreadyExistsException;
import com.gooddeeds.backend.model.User;
import com.gooddeeds.backend.repository.UserRepository;
import com.gooddeeds.backend.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(CreateUserRequest request) {
        // Normalize email: lowercase + trim
        String normalizedEmail = request.getEmail().toLowerCase().trim();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User user = User.builder()
                .name(request.getName().trim())
                .email(normalizedEmail)
                .passwordHash(
                        passwordEncoder.encode(request.getPassword())
                )
                .build();

        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        // Normalize email: lowercase + trim
        String normalizedEmail = email.toLowerCase().trim();
        return userRepository.findByEmail(normalizedEmail);
    }

    @Override
    public User authenticate(String email, String password) {
        // Normalize email: lowercase + trim
        String normalizedEmail = email.toLowerCase().trim();

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            // Generic message to prevent account enumeration
            throw new RuntimeException("Invalid credentials");
        }

        return user;
    }

    @Override
    public User updateUser(UUID id, String name, String email) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (name != null) {
            user.setName(name.trim());
        }
        if (email != null) {
            String normalizedEmail = email.toLowerCase().trim();
            if (!normalizedEmail.equals(user.getEmail()) && userRepository.existsByEmail(normalizedEmail)) {
                throw new EmailAlreadyExistsException("Email already exists");
            }
            user.setEmail(normalizedEmail);
        }

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }
}

