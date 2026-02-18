package com.gooddeeds.backend.service.impl;

import com.gooddeeds.backend.dto.CreateTaskRequest;
import com.gooddeeds.backend.dto.TaskStatisticsDTO;
import com.gooddeeds.backend.dto.UpdateTaskRequest;
import com.gooddeeds.backend.model.*;
import com.gooddeeds.backend.repository.*;
import com.gooddeeds.backend.service.TaskService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final CauseRepository causeRepository;
    private final GoalRepository goalRepository;
    private final CauseMembershipRepository membershipRepository;

    // ===================== ADMIN OPERATIONS =====================

    @Override
    public Task createTask(UUID adminUserId, CreateTaskRequest request) {
        Cause cause = causeRepository.findById(request.causeId())
                .orElseThrow(() -> new RuntimeException("Cause not found"));

        verifyAdminRole(adminUserId, request.causeId());

        Goal goal = null;
        if (request.goalId() != null) {
            goal = goalRepository.findById(request.goalId())
                    .orElseThrow(() -> new RuntimeException("Goal not found"));
            // Verify goal belongs to the same cause
            if (!goal.getCause().getId().equals(request.causeId())) {
                throw new RuntimeException("Goal does not belong to the specified cause");
            }
        }

        Task task = Task.builder()
                .title(request.title())
                .description(request.description())
                .status(request.status() != null ? request.status() : TaskStatus.COMING_UP)
                .cause(cause)
                .goal(goal)
                .dueDate(request.dueDate())
                .build();

        return taskRepository.save(task);
    }

    @Override
    public Task getTaskById(UUID userId, UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        
        verifyMembership(userId, task.getCause().getId());
        return task;
    }

    @Override
    public Page<Task> getTasksByCauseId(UUID userId, UUID causeId, int page, int size) {
        verifyMembership(userId, causeId);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return taskRepository.findByCauseId(causeId, pageable);
    }

    @Override
    public Page<Task> getTasksByCauseIdAndStatus(UUID userId, UUID causeId, TaskStatus status, int page, int size) {
        verifyMembership(userId, causeId);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return taskRepository.findByCauseIdAndStatus(causeId, status, pageable);
    }

    @Override
    public Page<Task> getTasksByGoalId(UUID userId, UUID goalId, int page, int size) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found"));
        
        verifyMembership(userId, goal.getCause().getId());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return taskRepository.findByGoalId(goalId, pageable);
    }

    @Override
    public Task updateTask(UUID adminUserId, UUID taskId, UpdateTaskRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        verifyAdminRole(adminUserId, task.getCause().getId());

        if (request.title() != null) {
            task.setTitle(request.title());
        }
        if (request.description() != null) {
            task.setDescription(request.description());
        }
        if (request.status() != null) {
            task.setStatus(request.status());
        }
        // Handle goal assignment: clearGoal=true removes goal, goalId sets new goal
        if (Boolean.TRUE.equals(request.clearGoal())) {
            task.setGoal(null);
        } else if (request.goalId() != null) {
            Goal goal = goalRepository.findById(request.goalId())
                    .orElseThrow(() -> new RuntimeException("Goal not found"));
            if (!goal.getCause().getId().equals(task.getCause().getId())) {
                throw new RuntimeException("Goal does not belong to the task's cause");
            }
            task.setGoal(goal);
        }
        if (request.dueDate() != null) {
            task.setDueDate(request.dueDate());
        }

        return taskRepository.save(task);
    }

    @Override
    public Task updateTaskStatus(UUID adminUserId, UUID taskId, TaskStatus status) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        verifyAdminRole(adminUserId, task.getCause().getId());

        task.setStatus(status);
        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(UUID adminUserId, UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        verifyAdminRole(adminUserId, task.getCause().getId());

        taskRepository.delete(task);
    }

    // ===================== MY TASKS =====================

    @Override
    public Page<Task> getMyTasks(UUID userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return taskRepository.findTasksByUserId(userId, pageable);
    }

    @Override
    public Page<Task> getMyTasksByStatus(UUID userId, TaskStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return taskRepository.findTasksByUserIdAndStatus(userId, status, pageable);
    }

    @Override
    public Page<Task> getMyTasksByCauseId(UUID userId, UUID causeId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return taskRepository.findTasksByUserIdAndCauseId(userId, causeId, pageable);
    }

    @Override
    public Page<Task> getMyTasksByCauseIdAndStatus(UUID userId, UUID causeId, TaskStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return taskRepository.findTasksByUserIdAndCauseIdAndStatus(userId, causeId, status, pageable);
    }

    @Override
    public Page<Task> getMyTasksByGoalId(UUID userId, UUID goalId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return taskRepository.findTasksByUserIdAndGoalId(userId, goalId, pageable);
    }

    // ===================== STATISTICS =====================

    @Override
    public TaskStatisticsDTO getMyTaskStatistics(UUID userId) {
        long total = taskRepository.countTasksByUserId(userId);
        long completed = taskRepository.countTasksByUserIdAndStatus(userId, TaskStatus.COMPLETED);
        long ongoing = taskRepository.countTasksByUserIdAndStatus(userId, TaskStatus.ONGOING);
        long comingUp = taskRepository.countTasksByUserIdAndStatus(userId, TaskStatus.COMING_UP);

        return new TaskStatisticsDTO(total, completed, ongoing, comingUp);
    }

    @Override
    public TaskStatisticsDTO getMyTaskStatisticsByCauseId(UUID userId, UUID causeId) {
        Cause cause = causeRepository.findById(causeId)
                .orElseThrow(() -> new RuntimeException("Cause not found"));

        long total = taskRepository.countTasksByUserIdAndCauseId(userId, causeId);
        long completed = taskRepository.countTasksByUserIdAndCauseIdAndStatus(userId, causeId, TaskStatus.COMPLETED);
        long ongoing = taskRepository.countTasksByUserIdAndCauseIdAndStatus(userId, causeId, TaskStatus.ONGOING);
        long comingUp = taskRepository.countTasksByUserIdAndCauseIdAndStatus(userId, causeId, TaskStatus.COMING_UP);

        return new TaskStatisticsDTO(total, completed, ongoing, comingUp, causeId, cause.getName());
    }

    @Override
    public TaskStatisticsDTO getMyTaskStatisticsByGoalId(UUID userId, UUID goalId) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        long total = taskRepository.countTasksByUserIdAndGoalId(userId, goalId);
        long completed = taskRepository.countTasksByUserIdAndGoalIdAndStatus(userId, goalId, TaskStatus.COMPLETED);
        long ongoing = taskRepository.countTasksByUserIdAndGoalIdAndStatus(userId, goalId, TaskStatus.ONGOING);
        long comingUp = taskRepository.countTasksByUserIdAndGoalIdAndStatus(userId, goalId, TaskStatus.COMING_UP);

        return new TaskStatisticsDTO(
                total, completed, ongoing, comingUp,
                goal.getCause().getId(), goal.getCause().getName(),
                goalId, goal.getTitle()
        );
    }

    // ===================== HELPER METHODS =====================

    private void verifyAdminRole(UUID userId, UUID causeId) {
        CauseMembership membership = membershipRepository.findByUserIdAndCauseId(userId, causeId)
                .orElseThrow(() -> new RuntimeException("Not a member of this cause"));

        if (membership.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only ADMIN can perform this action");
        }
    }

    private void verifyMembership(UUID userId, UUID causeId) {
        CauseMembership membership = membershipRepository.findByUserIdAndCauseId(userId, causeId)
                .orElseThrow(() -> new RuntimeException("Not a member of this cause"));

        if (!membership.isApproved()) {
            throw new RuntimeException("Membership not approved");
        }
    }
}
