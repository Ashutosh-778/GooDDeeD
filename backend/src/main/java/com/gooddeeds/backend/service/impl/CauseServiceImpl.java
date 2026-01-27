package com.gooddeeds.backend.service.impl;

import com.gooddeeds.backend.controller.CreateCauseRequest;
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
    public Cause createCause(CreateCauseRequest request) {
        Cause cause = Cause.builder()
                .name(request.getName())
                .description(request.getDescription())
                .restricted(request.isRestricted())
                .build();
        return causeRepository.save(cause);
    }

    //Getting all causes with pagination
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

    //search causes by goal keyword with pagination
    @Override
    public Page<Cause> searchCausesByGoal(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return goalRepository.findCausesByGoalKeyword(keyword, pageable);
    }

    //update cause (admin only)
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

    //delete cause (admin only)
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


