package com.gooddeeds.backend.controller;

import com.gooddeeds.backend.model.Cause;
import com.gooddeeds.backend.service.CauseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/causes")
@RequiredArgsConstructor
public class CauseController {

    private final CauseService causeService;

    @PostMapping
    public Cause create(@RequestBody Cause cause) {
        return causeService.createCause(cause);
    }

    @GetMapping
    public List<Cause> getAll() {
        return causeService.getAllCauses();
    }

    @GetMapping("/{id}")
    public Cause getById(@PathVariable UUID id) {
        return causeService.getCauseById(id);
    }
}
