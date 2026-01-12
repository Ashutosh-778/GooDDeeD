package com.gooddeeds.backend.service;

import com.gooddeeds.backend.model.CauseMembership;

import java.util.List;
import java.util.UUID;

public interface MembershipService {

    CauseMembership joinCause(UUID userId, UUID causeId);

    List<CauseMembership> getMembersOfCause(UUID causeId);

    // 🔥 ADMIN approval requires admin userId
    CauseMembership approveMembership(UUID adminUserId, UUID membershipId);
}



