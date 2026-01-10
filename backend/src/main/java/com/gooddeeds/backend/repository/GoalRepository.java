package com.gooddeeds.backend.repository;

import com.gooddeeds.backend.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GoalRepository extends JpaRepository<Goal, UUID> {
    List<Goal> findByOwnerUserId(UUID userId);
    List<Goal> findByCauseId(UUID causeId);
}
