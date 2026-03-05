package com.taskmanager.backend.dto;

public record ChangePasswordRequestDto(
    String oldPassword,
    String newPassword
)
{}
