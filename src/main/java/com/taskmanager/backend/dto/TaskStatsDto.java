package com.taskmanager.backend.dto;

public record TaskStatsDto(
    long todo,
    long inProgress,
    long done,
    long total,
    long overdue
) 
{}