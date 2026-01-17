package com.gooddeeds.backend.service;

import com.gooddeeds.backend.model.Cause;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface CauseService {

    Cause createCause(Cause cause);

    Page<Cause> getAllCauses(int page, int size);

    Cause getCauseById(UUID id);

    Page<Cause> searchCausesByGoal(String keyword, int page, int size);
}



