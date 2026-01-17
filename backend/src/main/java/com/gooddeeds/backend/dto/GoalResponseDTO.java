package com.gooddeeds.backend.dto;

import java.time.Instant;
import java.util.UUID;

public record GoalResponseDTO(
        UUID id,
        String title,
        String description,
        Instant createdAt
) {}
