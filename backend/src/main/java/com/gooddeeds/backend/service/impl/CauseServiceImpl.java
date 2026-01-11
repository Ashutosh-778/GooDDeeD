package com.gooddeeds.backend.service.impl;

import com.gooddeeds.backend.model.Cause;
import com.gooddeeds.backend.repository.CauseRepository;
import com.gooddeeds.backend.service.CauseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CauseServiceImpl implements CauseService {

    private final CauseRepository causeRepository;

    @Override
    public Cause createCause(Cause cause) {
        return causeRepository.save(cause);
    }

    @Override
    public List<Cause> getAllCauses() {
        return causeRepository.findAll();
    }

    @Override
    public Cause getCauseById(UUID id) {
        return causeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cause not found"));
    }
}

