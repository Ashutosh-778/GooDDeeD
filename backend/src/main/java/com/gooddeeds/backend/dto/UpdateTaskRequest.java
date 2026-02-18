package com.gooddeeds.backend.dto;

import com.gooddeeds.backend.model.TaskStatus;

import java.time.Instant;
import java.util.UUID;

public record UpdateTaskRequest(
        String title,
        String description,
        TaskStatus status,
        UUID goalId,
        Boolean clearGoal,  // Set to true to remove goal assignment
        Instant dueDate
) {}
