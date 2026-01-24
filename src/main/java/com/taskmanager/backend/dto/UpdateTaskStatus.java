package com.taskmanager.backend.dto;

import com.taskmanager.backend.entity.TaskStatus;


public class UpdateTaskStatus {
    private TaskStatus status;

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
