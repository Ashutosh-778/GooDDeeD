package com.gooddeeds.backend.repository;

import com.gooddeeds.backend.model.Cause;
import com.gooddeeds.backend.model.Goal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface GoalRepository extends JpaRepository<Goal, UUID> {

    Page<Goal> findByCauseId(UUID causeId, Pageable pageable);

    // 🔍 PAGINATED search by goal keyword
    @Query("""
        SELECT DISTINCT g.cause
        FROM Goal g
        WHERE LOWER(g.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(g.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    Page<Cause> findCausesByGoalKeyword(
            @Param("keyword") String keyword,
            Pageable pageable
    );
}

