package com.taskmanager.backend.controller;

import com.taskmanager.backend.entity.Task;
import com.taskmanager.backend.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public List<Task> getTasks() {
        return service.getAllTasks();
    }
    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return service.saveTask(task);
    }
}
