package com.gooddeeds.backend.service;

import com.gooddeeds.backend.model.Cause;

import java.util.List;
import java.util.UUID;

public interface CauseService {
    Cause create(Cause cause);
    List<Cause> list();
    Cause findById(UUID id);
}
