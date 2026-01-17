package com.gooddeeds.backend.mapper;

import com.gooddeeds.backend.dto.CauseResponseDTO;
import com.gooddeeds.backend.model.Cause;

public class CauseMapper {

    public static CauseResponseDTO toDTO(Cause cause) {
        return CauseResponseDTO.builder()
                .id(cause.getId())
                .name(cause.getName())
                .description(cause.getDescription())
                .restricted(cause.isRestricted())
                .createdAt(cause.getCreatedAt())
                .build();
    }
}
