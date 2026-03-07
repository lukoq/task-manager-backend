package com.taskmanager.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ChangePasswordRequestDto(
    @JsonProperty("oldPassword") String oldPassword,
    @JsonProperty("newPassword") String newPassword
)
{}
