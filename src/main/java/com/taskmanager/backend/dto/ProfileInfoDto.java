package com.taskmanager.backend.dto;

import com.taskmanager.backend.entity.User;

public record ProfileInfoDto(
    long id,
    String username,
    String email 
) {
    public static ProfileInfoDto fromEntity(User user) {
        return new ProfileInfoDto(
            user.getId(),
            user.getUsername(),
            user.getEmail()
        );
    }
}