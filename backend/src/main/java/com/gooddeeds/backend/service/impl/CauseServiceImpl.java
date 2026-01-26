package com.gooddeeds.backend.service.impl;

import com.gooddeeds.backend.controller.UpdateCauseRequest;
import com.gooddeeds.backend.model.Cause;
import com.gooddeeds.backend.model.CauseMembership;
import com.gooddeeds.backend.model.Role;
import com.gooddeeds.backend.repository.CauseMembershipRepository;
import com.gooddeeds.backend.repository.CauseRepository;
import com.gooddeeds.backend.repository.GoalRepository;
import com.gooddeeds.backend.service.CauseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CauseServiceImpl implements CauseService {

    private final CauseRepository causeRepository;
    private final GoalRepository goalRepository;
    private final CauseMembershipRepository membershipRepository;

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

    /* ========== UPDATE CAUSE (ADMIN ONLY) ========== */
    @Override
    public Cause updateCause(UUID causeId, UpdateCauseRequest request, UUID adminUserId) {
        Cause cause = causeRepository.findById(causeId)
                .orElseThrow(() -> new RuntimeException("Cause not found"));

        CauseMembership adminMembership = membershipRepository.findByUserIdAndCauseId(adminUserId, causeId)
                .orElseThrow(() -> new RuntimeException("Not a member of this cause"));

        if (adminMembership.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only ADMIN can update cause");
        }

        if (request.getName() != null) {
            cause.setName(request.getName());
        }
        if (request.getDescription() != null) {
            cause.setDescription(request.getDescription());
        }
        cause.setRestricted(request.isRestricted());

        return causeRepository.save(cause);
    }

    /* ========== DELETE CAUSE (ADMIN ONLY) ========== */
    @Override
    public void deleteCause(UUID causeId, UUID adminUserId) {
        Cause cause = causeRepository.findById(causeId)
                .orElseThrow(() -> new RuntimeException("Cause not found"));

        CauseMembership adminMembership = membershipRepository.findByUserIdAndCauseId(adminUserId, causeId)
                .orElseThrow(() -> new RuntimeException("Not a member of this cause"));

        if (adminMembership.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only ADMIN can delete cause");
        }

        causeRepository.delete(cause);
    }
}


