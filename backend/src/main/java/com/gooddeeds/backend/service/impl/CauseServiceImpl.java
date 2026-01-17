package com.gooddeeds.backend.service.impl;

import com.gooddeeds.backend.model.Cause;
import com.gooddeeds.backend.repository.CauseRepository;
import com.gooddeeds.backend.repository.GoalRepository;
import com.gooddeeds.backend.service.CauseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CauseServiceImpl implements CauseService {

    private final CauseRepository causeRepository;
    private final GoalRepository goalRepository;

    @Override
    public Cause createCause(Cause cause) {
        return causeRepository.save(cause);
    }

    /* ========== GET ALL CAUSES (PAGINATED) ========== */
    @Override
    public Page<Cause> getAllCauses(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return causeRepository.findAll(pageable);
    }

    @Override
    public Cause getCauseById(UUID id) {
        return causeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cause not found"));
    }

    /* ========== SEARCH CAUSES BY GOALS (PAGINATED) ========== */
    @Override
    public Page<Cause> searchCausesByGoal(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return goalRepository.findCausesByGoalKeyword(keyword, pageable);
    }
}


