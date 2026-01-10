package com.gooddeeds.backend.service.impl;

import com.gooddeeds.backend.model.Cause;
import com.gooddeeds.backend.repository.CauseRepository;
import com.gooddeeds.backend.service.CauseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CauseServiceImpl implements CauseService {
    private final CauseRepository causeRepository;

    public CauseServiceImpl(CauseRepository causeRepository) {
        this.causeRepository = causeRepository;
    }

    @Override
    public Cause create(Cause cause) {
        return causeRepository.save(cause);
    }

    @Override
    public List<Cause> list() {
        return causeRepository.findAll();
    }

    @Override
    public Cause findById(UUID id) {
        return causeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Cause not found"));
    }
}
