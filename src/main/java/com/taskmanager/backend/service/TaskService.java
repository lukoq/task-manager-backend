package com.taskmanager.backend.service;

import com.taskmanager.backend.entity.Task;
import com.taskmanager.backend.repository.TaskRepository;
import com.taskmanager.backend.repository.UserRepository;
import com.taskmanager.backend.entity.TaskStatus;
import com.taskmanager.backend.entity.User;
import com.taskmanager.backend.dto.TaskResponseDto;
import com.taskmanager.backend.dto.TaskStatsDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private final TaskRepository repository;

    @Autowired
    private UserRepository userRepository;

    public TaskService(TaskRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public Task saveTask(Task task, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        task.setUser(user);
        return repository.save(task);
    }

    public Task getTaskById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Task updateStatus(Long id, TaskStatus status) {
        Task task = repository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        task.setStatus(status);
        return repository.save(task);
    }

    public Task updateDescription(Long id, String newDesc) {
        Task task = repository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        task.setDescription(newDesc);
        return repository.save(task);
    }

    public void deleteTask(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Task not found");
        }
        repository.deleteById(id);
    }

    public List<TaskResponseDto> getTasksForUser(String email) {
        return repository.findByUserEmailOrderByCreatedAtAsc(email)
                .stream()
                .map(task -> new TaskResponseDto(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getStatus(),
                        task.getCreatedAt(),
                        task.getDueDate()
                ))
                .toList();
    }

    public TaskStatsDto getTaskStatistics(String email) {
        LocalDate today = LocalDate.now();

        long todo = repository.countByStatusAndUserEmail(TaskStatus.TODO, email);
        long inProgress = repository.countByStatusAndUserEmail(TaskStatus.IN_PROGRESS, email);
        long done = repository.countByStatusAndUserEmail(TaskStatus.DONE, email);

        long total = repository.countByUserEmail(email);
        long overdue = repository.countByDueDateBeforeAndUserEmail(today, email);;
    
        return new TaskStatsDto(todo, inProgress, done, total, overdue);
    }
}
