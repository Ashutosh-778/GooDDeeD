package com.gooddeeds.backend.controller;

import com.gooddeeds.backend.dto.GoalResponseDTO;
import com.gooddeeds.backend.mapper.GoalMapper;
import com.gooddeeds.backend.service.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    //Create goal (admin only)

    @PostMapping
    public GoalResponseDTO createGoal(
            @RequestParam UUID adminUserId,
            @RequestParam UUID causeId,
            @RequestParam String title,
            @RequestParam(required = false) String description
    ) {
        return GoalMapper.toDTO(
                goalService.createGoal(adminUserId, causeId, title, description)
        );
    }

    //Get goal by ID

    @GetMapping("/{id}")
    public GoalResponseDTO getGoalById(@PathVariable UUID id) {
        return GoalMapper.toDTO(
                goalService.getGoalById(id)
        );
    }

    //Get goals of a cause (paginated)
    @GetMapping("/cause/{causeId}")
    public Page<GoalResponseDTO> getGoals(
            @PathVariable UUID causeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return goalService.getGoalsOfCause(causeId, page, size)
                .map(GoalMapper::toDTO);
    }

    //Update goal (admin only)

    @PutMapping("/{id}")
    public GoalResponseDTO updateGoal(
            @PathVariable UUID id,
            @RequestParam UUID adminUserId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description
    ) {
        return GoalMapper.toDTO(
                goalService.updateGoal(adminUserId, id, title, description)
        );
    }

    //Delete goal (admin only)

    @DeleteMapping("/{id}")
    public void deleteGoal(
            @PathVariable UUID id,
            @RequestParam UUID adminUserId
    ) {
        goalService.deleteGoal(adminUserId, id);
    }
}

