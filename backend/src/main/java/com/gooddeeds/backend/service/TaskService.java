package com.gooddeeds.backend.service;

import com.gooddeeds.backend.dto.CreateTaskRequest;
import com.gooddeeds.backend.dto.TaskStatisticsDTO;
import com.gooddeeds.backend.dto.UpdateTaskRequest;
import com.gooddeeds.backend.model.Task;
import com.gooddeeds.backend.model.TaskStatus;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface TaskService {

    // Create task (admin only)
    Task createTask(UUID adminUserId, CreateTaskRequest request);

    // Get task by ID (verifies user is member of the task's cause)
    Task getTaskById(UUID userId, UUID taskId);

    // Get tasks for a cause (paginated, verifies membership)
    Page<Task> getTasksByCauseId(UUID userId, UUID causeId, int page, int size);

    // Get tasks for a cause by status (paginated, verifies membership)
    Page<Task> getTasksByCauseIdAndStatus(UUID userId, UUID causeId, TaskStatus status, int page, int size);

    // Get tasks for a goal (paginated, verifies membership)
    Page<Task> getTasksByGoalId(UUID userId, UUID goalId, int page, int size);

    // Update task (admin only)
    Task updateTask(UUID adminUserId, UUID taskId, UpdateTaskRequest request);

    // Update task status (admin only)
    Task updateTaskStatus(UUID adminUserId, UUID taskId, TaskStatus status);

    // Delete task (admin only)
    void deleteTask(UUID adminUserId, UUID taskId);

    // ===================== MY TASKS =====================

    // Get tasks for user (all causes they're a member of)
    Page<Task> getMyTasks(UUID userId, int page, int size);

    // Get tasks for user filtered by status
    Page<Task> getMyTasksByStatus(UUID userId, TaskStatus status, int page, int size);

    // Get tasks for user filtered by cause
    Page<Task> getMyTasksByCauseId(UUID userId, UUID causeId, int page, int size);

    // Get tasks for user filtered by cause and status
    Page<Task> getMyTasksByCauseIdAndStatus(UUID userId, UUID causeId, TaskStatus status, int page, int size);

    // Get tasks for user filtered by goal
    Page<Task> getMyTasksByGoalId(UUID userId, UUID goalId, int page, int size);

    // ===================== STATISTICS =====================

    // Get task statistics for user (overall)
    TaskStatisticsDTO getMyTaskStatistics(UUID userId);

    // Get task statistics for user filtered by cause
    TaskStatisticsDTO getMyTaskStatisticsByCauseId(UUID userId, UUID causeId);

    // Get task statistics for user filtered by goal
    TaskStatisticsDTO getMyTaskStatisticsByGoalId(UUID userId, UUID goalId);
}
