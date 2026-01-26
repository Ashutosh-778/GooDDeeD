package com.gooddeeds.backend.service.impl;

import com.gooddeeds.backend.model.*;
import com.gooddeeds.backend.repository.*;
import com.gooddeeds.backend.service.GoalService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepository;
    private final CauseRepository causeRepository;
    private final CauseMembershipRepository membershipRepository;

    //Create goal (admin only)

    @Override
    public Goal createGoal(UUID adminUserId, UUID causeId, String title, String description) {

        Cause cause = causeRepository.findById(causeId)
                .orElseThrow(() -> new RuntimeException("Cause not found"));

        CauseMembership adminMembership =
                membershipRepository.findByUserIdAndCauseId(adminUserId, causeId)
                        .orElseThrow(() -> new RuntimeException("Not a member of this cause"));

        if (adminMembership.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only ADMIN can create goals");
        }

        Goal goal = Goal.builder()
                .title(title)
                .description(description)
                .cause(cause)
                .build();

        return goalRepository.save(goal);
    }

    //get goal by ID

    @Override
    public Goal getGoalById(UUID goalId) {
        return goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found"));
    }

    //get goals of a cause (paginated)

    @Override
    public Page<Goal> getGoalsOfCause(UUID causeId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return goalRepository.findByCauseId(causeId, pageable);
    }

    //update goal (admin only)

    @Override
    public Goal updateGoal(UUID adminUserId, UUID goalId, String title, String description) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        UUID causeId = goal.getCause().getId();

        CauseMembership adminMembership =
                membershipRepository.findByUserIdAndCauseId(adminUserId, causeId)
                        .orElseThrow(() -> new RuntimeException("Not a member of this cause"));

        if (adminMembership.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only ADMIN can update goals");
        }

        if (title != null) {
            goal.setTitle(title);
        }
        if (description != null) {
            goal.setDescription(description);
        }

        return goalRepository.save(goal);
    }

    //delete goal (admin only)

    @Override
    public void deleteGoal(UUID adminUserId, UUID goalId) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        UUID causeId = goal.getCause().getId();

        CauseMembership adminMembership =
                membershipRepository.findByUserIdAndCauseId(adminUserId, causeId)
                        .orElseThrow(() -> new RuntimeException("Not a member of this cause"));

        if (adminMembership.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only ADMIN can delete goals");
        }

        goalRepository.delete(goal);
    }
}
