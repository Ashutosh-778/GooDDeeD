package com.gooddeeds.backend.mapper;

import com.gooddeeds.backend.dto.TaskResponseDTO;
import com.gooddeeds.backend.model.Task;

public class TaskMapper {

    public static TaskResponseDTO toDTO(Task task) {
        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getCause().getId(),
                task.getCause().getName(),
                task.getGoal() != null ? task.getGoal().getId() : null,
                task.getGoal() != null ? task.getGoal().getTitle() : null,
                task.getDueDate(),
                task.getCreatedAt()
        );
    }
}
