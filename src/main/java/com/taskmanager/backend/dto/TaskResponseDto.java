package com.taskmanager.backend.dto;

import com.taskmanager.backend.entity.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TaskResponseDto {

    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime createdAt;
    private LocalDate dueDate;

    public TaskResponseDto(
            Long id,
            String title,
            String description,
            TaskStatus status,
            LocalDateTime createdAt,
            LocalDate dueDate
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.dueDate = dueDate;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public TaskStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDate getDueDate() { return dueDate; }
}