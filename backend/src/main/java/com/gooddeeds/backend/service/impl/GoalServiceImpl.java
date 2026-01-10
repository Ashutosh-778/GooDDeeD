package com.gooddeeds.backend.service.impl;

import com.gooddeeds.backend.model.Cause;
import com.gooddeeds.backend.model.Goal;
import com.gooddeeds.backend.model.User;
import com.gooddeeds.backend.repository.CauseRepository;
import com.gooddeeds.backend.repository.GoalRepository;
import com.gooddeeds.backend.repository.UserRepository;
import com.gooddeeds.backend.service.GoalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class GoalServiceImpl implements GoalService {
    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final CauseRepository causeRepository;

    public GoalServiceImpl(GoalRepository goalRepository, UserRepository userRepository, CauseRepository causeRepository) {
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
        this.causeRepository = causeRepository;
    }

    @Override
    public Goal create(Goal goal) {
        // Ensure exactly one of ownerUser or cause is set
        if ((goal.getOwnerUser() == null) == (goal.getCause() == null)) {
            throw new IllegalArgumentException("Goal must belong to exactly one of user or cause");
        }
        if (goal.getOwnerUser() != null) {
            UUID uid = goal.getOwnerUser().getId();
            User u = userRepository.findById(uid).orElseThrow(() -> new IllegalArgumentException("Owner user not found"));
            goal.setOwnerUser(u);
        }
        if (goal.getCause() != null) {
            UUID cid = goal.getCause().getId();
            Cause c = causeRepository.findById(cid).orElseThrow(() -> new IllegalArgumentException("Cause not found"));
            goal.setCause(c);
        }
        return goalRepository.save(goal);
    }

    @Override
    public List<Goal> findByUser(UUID userId) {
        return goalRepository.findByOwnerUserId(userId);
    }

    @Override
    public List<Goal> findByCause(UUID causeId) {
        return goalRepository.findByCauseId(causeId);
    }
}
