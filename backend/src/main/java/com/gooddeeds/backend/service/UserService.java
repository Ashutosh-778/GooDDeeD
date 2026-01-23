
package com.gooddeeds.backend.service;

import com.gooddeeds.backend.controller.CreateUserRequest;
import com.gooddeeds.backend.model.User;

import java.util.Optional;

public interface UserService {

    User createUser(CreateUserRequest request);

    Optional<User> getUserByEmail(String email);
    User authenticate(String email, String password);

}

