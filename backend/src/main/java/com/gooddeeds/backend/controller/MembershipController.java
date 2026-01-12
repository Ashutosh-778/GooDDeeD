package com.gooddeeds.backend.controller;

import com.gooddeeds.backend.model.CauseMembership;
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
    public ResponseEntity<CauseMembership> joinCause(
            @RequestParam UUID userId,
            @RequestParam UUID causeId
    ) {
        return ResponseEntity.ok(
                membershipService.joinCause(userId, causeId)
        );
    }

    /* ================= GET MEMBERS ================= */

    @GetMapping("/cause/{causeId}")
    public List<CauseMembership> getMembers(
            @PathVariable UUID causeId
    ) {
        return membershipService.getMembersOfCause(causeId);
    }

    /* ================= APPROVE MEMBER ================= */

    @PostMapping("/{membershipId}/approve")
    public ResponseEntity<CauseMembership> approveMembership(
            @RequestParam UUID adminUserId,
            @PathVariable UUID membershipId
    ) {
        return ResponseEntity.ok(
                membershipService.approveMembership(adminUserId, membershipId)
        );
    }
}


