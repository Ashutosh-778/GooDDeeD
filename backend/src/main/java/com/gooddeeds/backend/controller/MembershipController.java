package com.gooddeeds.backend.controller;

import com.gooddeeds.backend.dto.MembershipResponseDTO;
import com.gooddeeds.backend.mapper.MembershipMapper;
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

    //Join cause

    @PostMapping("/join")
    public ResponseEntity<MembershipResponseDTO> joinCause(
            @RequestParam UUID userId,
            @RequestParam UUID causeId
    ) {
        return ResponseEntity.ok(
                MembershipMapper.toDTO(
                        membershipService.joinCause(userId, causeId)
                )
        );
    }

    /* ================= GET MEMBERSHIP BY ID ================= */

    @GetMapping("/{id}")
    public MembershipResponseDTO getMembershipById(@PathVariable UUID id) {
        return MembershipMapper.toDTO(
                membershipService.getMembershipById(id)
        );
    }

    /* ================= GET MEMBERS ================= */

    @GetMapping("/cause/{causeId}")
    public List<MembershipResponseDTO> getMembers(
            @PathVariable UUID causeId
    ) {
        return membershipService.getMembersOfCause(causeId)
                .stream()
                .map(MembershipMapper::toDTO)
                .toList();
    }

    /* ================= APPROVE MEMBER ================= */

    @PostMapping("/{membershipId}/approve")
    public ResponseEntity<MembershipResponseDTO> approveMembership(
            @RequestParam UUID adminUserId,
            @PathVariable UUID membershipId
    ) {
        return ResponseEntity.ok(
                MembershipMapper.toDTO(
                        membershipService.approveMembership(adminUserId, membershipId)
                )
        );
    }

    /* ================= REJECT MEMBER ================= */

    @DeleteMapping("/{membershipId}/reject")
    public void rejectMembership(
            @RequestParam UUID adminUserId,
            @PathVariable UUID membershipId
    ) {
        membershipService.rejectMembership(adminUserId, membershipId);
    }

    /* ================= LEAVE CAUSE ================= */

    @DeleteMapping("/leave")
    public void leaveCause(
            @RequestParam UUID userId,
            @RequestParam UUID causeId
    ) {
        membershipService.leaveCause(userId, causeId);
    }
}



