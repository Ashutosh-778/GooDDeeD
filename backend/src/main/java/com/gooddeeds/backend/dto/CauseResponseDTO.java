package com.gooddeeds.backend.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class CauseResponseDTO {

    private UUID id;
    private String name;
    private String description;
    private boolean restricted;
    private Instant createdAt;
}
