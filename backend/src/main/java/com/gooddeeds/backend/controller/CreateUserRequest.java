package com.gooddeeds.backend.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {
    private String name;
    private String email;
    private String password;
}


