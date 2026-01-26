
package com.gooddeeds.backend.service;

import com.gooddeeds.backend.controller.CreateUserRequest;
import com.gooddeeds.backend.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    User createUser(CreateUserRequest request);

    Optional<User> getUserById(UUID id);

    Optional<User> getUserByEmail(String email);

    User authenticate(String email, String password);

    User updateUser(UUID id, String name, String email);

    void deleteUser(UUID id);
}

