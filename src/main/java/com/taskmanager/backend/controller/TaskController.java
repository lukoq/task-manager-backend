package com.taskmanager.backend.controller;

import com.taskmanager.backend.entity.Task;
import com.taskmanager.backend.service.TaskService;
import com.taskmanager.backend.service.UserService;
import com.taskmanager.backend.dto.UpdateTaskStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.taskmanager.backend.dto.ProfileInfoDto;
import com.taskmanager.backend.dto.TaskResponseDto;
import com.taskmanager.backend.dto.TaskStatsDto;
import com.taskmanager.backend.dto.UpdateTaskDescription;

import java.util.List;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService service;
    private final UserService userService;

    public TaskController(TaskService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getTasks(Authentication authentication) {
        String email = authentication.getName();
        List<TaskResponseDto> tasks = service.getTasksForUser(email);
    
        return ResponseEntity.ok(tasks); 
    }
    @PostMapping
    public Task createTask(@RequestBody Task task, Authentication authentication) {
        String email = authentication.getName();
        return service.saveTask(task, email);
    }
    @PostMapping("/status/{id}")
    public Task updateTaskStatus(
            @PathVariable Long id,
            @RequestBody UpdateTaskStatus request
    ) {
        return service.updateStatus(id, request.getStatus());
    }
    @PostMapping("/description/{id}")
    public Task updateTaskDescription(
            @PathVariable Long id,
            @RequestBody UpdateTaskDescription request
    ) {
        return service.updateDescription(id, request.getDescription());
    }
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        System.out.println("Removing: " + id);
        service.deleteTask(id);
    }

    @GetMapping("/stats")
    public ResponseEntity<TaskStatsDto> getStats(Authentication authentication) {
        String email = authentication.getName(); 
        return ResponseEntity.ok(service.getTaskStatistics(email));
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileInfoDto> getProfile(Authentication authentication) {
        String email = authentication.getName(); 
        return ResponseEntity.ok(userService.getUserProfile(email));
    }
}
