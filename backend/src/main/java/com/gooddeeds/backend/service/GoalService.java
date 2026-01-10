package com.gooddeeds.backend.service;

import com.gooddeeds.backend.model.Goal;

import java.util.List;
import java.util.UUID;

public interface GoalService {
    Goal create(Goal goal);
    List<Goal> findByUser(UUID userId);
    List<Goal> findByCause(UUID causeId);
}
