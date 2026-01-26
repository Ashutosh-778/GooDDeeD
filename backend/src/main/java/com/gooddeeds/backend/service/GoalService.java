package com.gooddeeds.backend.service;

import com.gooddeeds.backend.model.Goal;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface GoalService {

    Goal createGoal(UUID adminUserId, UUID causeId, String title, String description);

    Goal getGoalById(UUID goalId);

    Page<Goal> getGoalsOfCause(UUID causeId, int page, int size);

    Goal updateGoal(UUID adminUserId, UUID goalId, String title, String description);

    void deleteGoal(UUID adminUserId, UUID goalId);
}
