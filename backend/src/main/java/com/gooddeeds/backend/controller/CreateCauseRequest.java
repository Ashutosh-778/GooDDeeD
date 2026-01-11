package com.gooddeeds.backend.controller;

import lombok.Getter;

@Getter
public class CreateCauseRequest {
    private String name;
    private String description;
    private boolean restricted;
}

