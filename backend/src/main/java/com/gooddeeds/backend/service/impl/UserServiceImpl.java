
package com.gooddeeds.backend.service.impl;

import com.gooddeeds.backend.controller.CreateUserRequest;
import com.gooddeeds.backend.model.User;
import com.gooddeeds.backend.repository.UserRepository;
import com.gooddeeds.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(CreateUserRequest request) {

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .passwordHash(request.getPassword()) // ⚠️ plain for now
                .build();

        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}

