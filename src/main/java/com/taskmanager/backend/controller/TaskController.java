package com.taskmanager.backend.controller;

import com.taskmanager.backend.entity.Task;
import com.taskmanager.backend.service.TaskService;
import com.taskmanager.backend.dto.UpdateTaskStatus;
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
    @PostMapping("/status/{id}")
    public Task updateTaskStatus(
            @PathVariable Long id,
            @RequestBody UpdateTaskStatus request
    ) {
        return service.updateStatus(id, request.getStatus());
    }
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        System.out.println("Próbuję usunąć zadanie o id: " + id);
        service.deleteTask(id);
    }
}
