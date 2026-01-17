package com.gooddeeds.backend.service;

import com.gooddeeds.backend.model.Goal;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface GoalService {

    Goal createGoal(UUID adminUserId, UUID causeId, String title, String description);

    Page<Goal> getGoalsOfCause(UUID causeId, int page, int size);
}
