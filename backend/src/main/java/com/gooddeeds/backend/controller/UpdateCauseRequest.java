package com.gooddeeds.backend.controller;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateCauseRequest {

    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    private boolean restricted;
}
