package com.gooddeeds.backend.service;

import com.gooddeeds.backend.model.Cause;

import java.util.List;
import java.util.UUID;

public interface CauseService {

    Cause createCause(Cause cause);

    List<Cause> getAllCauses();

    Cause getCauseById(UUID id);
}


