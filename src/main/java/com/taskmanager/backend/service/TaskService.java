package com.taskmanager.backend.service;

import com.taskmanager.backend.entity.Task;
import com.taskmanager.backend.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    public Task saveTask(Task task) {
        return repository.save(task);
    }

    public Task getTaskById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
