package com.gooddeeds.backend.repository;

import com.gooddeeds.backend.model.CauseMembership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CauseMembershipRepository extends JpaRepository<CauseMembership, UUID> {
    Optional<CauseMembership> findByUserIdAndCauseId(java.util.UUID userId, java.util.UUID causeId);
}
