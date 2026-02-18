package com.gooddeeds.backend.controller;

import com.gooddeeds.backend.dto.CreateTaskRequest;
import com.gooddeeds.backend.dto.TaskResponseDTO;
import com.gooddeeds.backend.dto.TaskStatisticsDTO;
import com.gooddeeds.backend.dto.UpdateTaskRequest;
import com.gooddeeds.backend.mapper.TaskMapper;
import com.gooddeeds.backend.model.TaskStatus;
import com.gooddeeds.backend.security.SecurityUtils;
import com.gooddeeds.backend.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    // ===================== ADMIN OPERATIONS =====================

    // Create task (admin only)
    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(
            @Valid @RequestBody CreateTaskRequest request
    ) {
        UUID adminUserId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(
                TaskMapper.toDTO(taskService.createTask(adminUserId, request))
        );
    }

    // Get task by ID (requires cause membership)
    @GetMapping("/{id}")
    public TaskResponseDTO getTaskById(@PathVariable UUID id) {
        UUID userId = SecurityUtils.getCurrentUserId();
        return TaskMapper.toDTO(taskService.getTaskById(userId, id));
    }

    // Get tasks for a cause (paginated, requires membership)
    @GetMapping("/cause/{causeId}")
    public Page<TaskResponseDTO> getTasksByCause(
            @PathVariable UUID causeId,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        UUID userId = SecurityUtils.getCurrentUserId();
        if (status != null) {
            return taskService.getTasksByCauseIdAndStatus(userId, causeId, status, page, size)
                    .map(TaskMapper::toDTO);
        }
        return taskService.getTasksByCauseId(userId, causeId, page, size)
                .map(TaskMapper::toDTO);
    }

    // Get tasks for a goal (paginated, requires membership)
    @GetMapping("/goal/{goalId}")
    public Page<TaskResponseDTO> getTasksByGoal(
            @PathVariable UUID goalId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        UUID userId = SecurityUtils.getCurrentUserId();
        return taskService.getTasksByGoalId(userId, goalId, page, size)
                .map(TaskMapper::toDTO);
    }

    // Update task (admin only)
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTaskRequest request
    ) {
        UUID adminUserId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(
                TaskMapper.toDTO(taskService.updateTask(adminUserId, id, request))
        );
    }

    // Update task status only (admin only) - convenience endpoint
    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponseDTO> updateTaskStatus(
            @PathVariable UUID id,
            @RequestParam TaskStatus status
    ) {
        UUID adminUserId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(
                TaskMapper.toDTO(taskService.updateTaskStatus(adminUserId, id, status))
        );
    }

    // Delete task (admin only)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        UUID adminUserId = SecurityUtils.getCurrentUserId();
        taskService.deleteTask(adminUserId, id);
        return ResponseEntity.noContent().build();
    }

    // ===================== MY TASKS =====================

    // Get tasks for current user (all causes they're a member of)
    @GetMapping("/my")
    public Page<TaskResponseDTO> getMyTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) UUID causeId,
            @RequestParam(required = false) UUID goalId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        UUID userId = SecurityUtils.getCurrentUserId();

        // Filter by goal
        if (goalId != null) {
            return taskService.getMyTasksByGoalId(userId, goalId, page, size)
                    .map(TaskMapper::toDTO);
        }

        // Filter by cause and status
        if (causeId != null && status != null) {
            return taskService.getMyTasksByCauseIdAndStatus(userId, causeId, status, page, size)
                    .map(TaskMapper::toDTO);
        }

        // Filter by cause only
        if (causeId != null) {
            return taskService.getMyTasksByCauseId(userId, causeId, page, size)
                    .map(TaskMapper::toDTO);
        }

        // Filter by status only
        if (status != null) {
            return taskService.getMyTasksByStatus(userId, status, page, size)
                    .map(TaskMapper::toDTO);
        }

        // No filters
        return taskService.getMyTasks(userId, page, size)
                .map(TaskMapper::toDTO);
    }

    // ===================== STATISTICS =====================

    // Get task statistics for current user
    @GetMapping("/my/statistics")
    public TaskStatisticsDTO getMyTaskStatistics(
            @RequestParam(required = false) UUID causeId,
            @RequestParam(required = false) UUID goalId
    ) {
        UUID userId = SecurityUtils.getCurrentUserId();

        // Filter by goal
        if (goalId != null) {
            return taskService.getMyTaskStatisticsByGoalId(userId, goalId);
        }

        // Filter by cause
        if (causeId != null) {
            return taskService.getMyTaskStatisticsByCauseId(userId, causeId);
        }

        // Overall statistics
        return taskService.getMyTaskStatistics(userId);
    }
}
