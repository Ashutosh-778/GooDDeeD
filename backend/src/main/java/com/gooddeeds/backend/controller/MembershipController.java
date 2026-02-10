package com.gooddeeds.backend.controller;

import com.gooddeeds.backend.dto.MembershipResponseDTO;
import com.gooddeeds.backend.exception.AccessDeniedException;
import com.gooddeeds.backend.mapper.MembershipMapper;
import com.gooddeeds.backend.model.CauseMembership;
import com.gooddeeds.backend.security.SecurityUtils;
import com.gooddeeds.backend.service.MembershipService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/memberships")
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    //Get memberships of the current user
    @GetMapping("/my")
    public List<MembershipResponseDTO> getMyMemberships() {
        UUID userId = SecurityUtils.getCurrentUserId();
        return membershipService.getMembershipsByUserId(userId)
                .stream()
                .map(MembershipMapper::toDTO)
                .toList();
    }

    //Join cause (user derived from JWT)

    @PostMapping("/join")
    public ResponseEntity<MembershipResponseDTO> joinCause(@RequestParam UUID causeId) {
        UUID userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(
                MembershipMapper.toDTO(
                        membershipService.joinCause(userId, causeId)
                )
        );
    }

    //get membership by ID (only if user is part of same cause or owns this membership)

    @GetMapping("/{id}")
    public MembershipResponseDTO getMembershipById(@PathVariable UUID id) {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        CauseMembership membership = membershipService.getMembershipById(id);
        
        // Check if user owns this membership or is a member of the same cause
        boolean isOwner = membership.getUser().getId().equals(currentUserId);
        boolean isCauseMember = membershipService.getMembershipsByUserId(currentUserId)
                .stream()
                .anyMatch(m -> m.getCause().getId().equals(membership.getCause().getId()) && m.isApproved());
        
        if (!isOwner && !isCauseMember) {
            throw new AccessDeniedException("You can only view memberships from causes you belong to");
        }
        
        return MembershipMapper.toDTO(membership);
    }

    //Get members of a cause (only if user is a member of this cause)

    @GetMapping("/cause/{causeId}")
    public List<MembershipResponseDTO> getMembers(@PathVariable UUID causeId) {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        
        // Verify user is an approved member of this cause
        boolean isCauseMember = membershipService.getMembershipsByUserId(currentUserId)
                .stream()
                .anyMatch(m -> m.getCause().getId().equals(causeId) && m.isApproved());
        
        if (!isCauseMember) {
            throw new AccessDeniedException("You can only view members of causes you belong to");
        }
        
        return membershipService.getMembersOfCause(causeId)
                .stream()
                .map(MembershipMapper::toDTO)
                .toList();
    }

    //Approve member by admin (derived from JWT)

    @PostMapping("/{membershipId}/approve")
    public ResponseEntity<MembershipResponseDTO> approveMembership(@PathVariable UUID membershipId) {
        UUID adminUserId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(
                MembershipMapper.toDTO(
                        membershipService.approveMembership(adminUserId, membershipId)
                )
        );
    }

    //Reject member by admin (derived from JWT)

    @DeleteMapping("/{membershipId}/reject")
    public void rejectMembership(@PathVariable UUID membershipId) {
        UUID adminUserId = SecurityUtils.getCurrentUserId();
        membershipService.rejectMembership(adminUserId, membershipId);
    }

    //Leave cause (user derived from JWT)

    @DeleteMapping("/leave")
    public void leaveCause(@RequestParam UUID causeId) {
        UUID userId = SecurityUtils.getCurrentUserId();
        membershipService.leaveCause(userId, causeId);
    }
}



