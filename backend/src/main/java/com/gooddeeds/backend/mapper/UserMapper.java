package com.gooddeeds.backend.mapper;

import com.gooddeeds.backend.dto.UserResponseDTO;
import com.gooddeeds.backend.model.User;

public class UserMapper {

    public static UserResponseDTO toDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
