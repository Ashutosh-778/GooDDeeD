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

    /* ========== CREATE GOAL (ADMIN ONLY) ========== */

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

    /* ========== GET GOALS OF A CAUSE (PAGINATED) ========== */

    @Override
    public Page<Goal> getGoalsOfCause(UUID causeId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return goalRepository.findByCauseId(causeId, pageable);
    }
}
