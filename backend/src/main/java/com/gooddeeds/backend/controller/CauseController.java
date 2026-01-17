package com.gooddeeds.backend.controller;

import com.gooddeeds.backend.dto.CauseResponseDTO;
import com.gooddeeds.backend.mapper.CauseMapper;
import com.gooddeeds.backend.model.Cause;
import com.gooddeeds.backend.service.CauseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/causes")
@RequiredArgsConstructor
public class CauseController {

    private final CauseService causeService;

    /* ========== CREATE CAUSE ========== */
    @PostMapping
    public CauseResponseDTO create(@RequestBody Cause cause) {
        return CauseMapper.toDTO(
                causeService.createCause(cause)
        );
    }

    /* ========== GET ALL CAUSES (PAGINATED) ========== */
    @GetMapping
    public Page<CauseResponseDTO> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return causeService.getAllCauses(page, size)
                .map(CauseMapper::toDTO);
    }

    /* ========== GET CAUSE BY ID ========== */
    @GetMapping("/{id}")
    public CauseResponseDTO getById(@PathVariable UUID id) {
        return CauseMapper.toDTO(
                causeService.getCauseById(id)
        );
    }

    /* ========== SEARCH CAUSES BY GOAL (PAGINATED) ========== */
    @GetMapping("/search")
    public Page<CauseResponseDTO> searchCausesByGoal(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return causeService.searchCausesByGoal(keyword, page, size)
                .map(CauseMapper::toDTO);
    }
}

