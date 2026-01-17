package com.gooddeeds.backend.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class UserResponseDTO {

    private UUID id;
    private String name;
    private String email;
    private Instant createdAt;
}
