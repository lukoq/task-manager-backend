package com.taskmanager.backend.dto;

public class UpdateTaskDescription {
    private String description;

    public UpdateTaskDescription() {}

    public UpdateTaskDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
