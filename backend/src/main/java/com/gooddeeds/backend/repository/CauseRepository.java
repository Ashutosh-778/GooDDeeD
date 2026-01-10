package com.gooddeeds.backend.repository;

import com.gooddeeds.backend.model.Cause;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CauseRepository extends JpaRepository<Cause, UUID> {
}
