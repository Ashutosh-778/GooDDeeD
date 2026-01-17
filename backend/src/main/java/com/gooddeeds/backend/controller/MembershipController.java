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

    /* ================= JOIN CAUSE ================= */

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
}



