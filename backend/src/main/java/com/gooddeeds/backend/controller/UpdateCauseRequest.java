package com.gooddeeds.backend.controller;

import lombok.Getter;

@Getter
public class UpdateCauseRequest {
    private String name;
    private String description;
    private boolean restricted;
}
