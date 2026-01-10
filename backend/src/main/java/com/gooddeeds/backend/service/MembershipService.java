package com.gooddeeds.backend.service;

import com.gooddeeds.backend.model.CauseMembership;

import java.util.UUID;

public interface MembershipService {
    CauseMembership joinCause(UUID userId, UUID causeId, String role);
    CauseMembership approveMembership(UUID approverUserId, UUID membershipId);
}
