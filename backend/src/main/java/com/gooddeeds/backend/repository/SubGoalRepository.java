package com.gooddeeds.backend.repository;

import com.gooddeeds.backend.model.SubGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SubGoalRepository extends JpaRepository<SubGoal, UUID> {
    List<SubGoal> findByGoalId(UUID goalId);
}
