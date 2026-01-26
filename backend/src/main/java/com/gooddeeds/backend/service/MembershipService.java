package com.gooddeeds.backend.service;

import com.gooddeeds.backend.model.CauseMembership;

import java.util.List;
import java.util.UUID;

public interface MembershipService {

    CauseMembership joinCause(UUID userId, UUID causeId);

    CauseMembership getMembershipById(UUID membershipId);

    List<CauseMembership> getMembersOfCause(UUID causeId);

    // 🔥 ADMIN approval requires admin userId
    CauseMembership approveMembership(UUID adminUserId, UUID membershipId);

    // 🔥 ADMIN rejection requires admin userId
    void rejectMembership(UUID adminUserId, UUID membershipId);

    // User leaves a cause
    void leaveCause(UUID userId, UUID causeId);
}



