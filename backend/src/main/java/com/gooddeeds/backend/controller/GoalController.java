package com.gooddeeds.backend.controller;

import com.gooddeeds.backend.model.Goal;
import com.gooddeeds.backend.service.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    /* ========== CREATE GOAL (ADMIN ONLY) ========== */

    @PostMapping
    public ResponseEntity<Goal> createGoal(
            @RequestParam UUID adminUserId,
            @RequestParam UUID causeId,
            @RequestParam String title,
            @RequestParam(required = false) String description
    ) {
        return ResponseEntity.ok(
                goalService.createGoal(adminUserId, causeId, title, description)
        );
    }

    /* ========== GET GOALS OF A CAUSE (PAGINATED) ========== */

    @GetMapping("/cause/{causeId}")
    public Page<Goal> getGoals(
            @PathVariable UUID causeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return goalService.getGoalsOfCause(causeId, page, size);
    }
}

