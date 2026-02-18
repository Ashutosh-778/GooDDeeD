package com.gooddeeds.backend.dto;

import com.gooddeeds.backend.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record CreateTaskRequest(
        @NotBlank(message = "Title is required")
        String title,
        String description,
        TaskStatus status,
        @NotNull(message = "Cause ID is required")
        UUID causeId,
        UUID goalId,
        Instant dueDate
) {}
