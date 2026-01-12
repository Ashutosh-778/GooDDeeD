package com.gooddeeds.backend.repository;

import com.gooddeeds.backend.model.Cause;
import com.gooddeeds.backend.model.CauseMembership;
import com.gooddeeds.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import java.util.Optional;

public interface CauseMembershipRepository
        extends JpaRepository<CauseMembership, UUID> {

    boolean existsByUserAndCause(User user, Cause cause);

    List<CauseMembership> findByCauseId(UUID causeId);

    List<CauseMembership> findByUserId(UUID userId);

    // 🔥 REQUIRED for admin approval
    Optional<CauseMembership> findByUserIdAndCauseId(UUID userId, UUID causeId);
}




