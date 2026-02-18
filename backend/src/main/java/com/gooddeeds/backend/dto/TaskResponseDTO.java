package com.gooddeeds.backend.dto;

import com.gooddeeds.backend.model.TaskStatus;

import java.time.Instant;
import java.util.UUID;

public record TaskResponseDTO(
        UUID id,
        String title,
        String description,
        TaskStatus status,
        UUID causeId,
        String causeName,
        UUID goalId,
        String goalTitle,
        Instant dueDate,
        Instant createdAt
) {}
