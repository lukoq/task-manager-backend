package com.taskmanager.backend;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.taskmanager.backend.dto.TaskStatsDto;
import com.taskmanager.backend.entity.Task;
import com.taskmanager.backend.entity.TaskStatus;
import com.taskmanager.backend.repository.TaskRepository;
import com.taskmanager.backend.repository.UserRepository;
import com.taskmanager.backend.service.TaskService;

import static org.junit.jupiter.api.Assertions.*; 
import static org.mockito.Mockito.when;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;


@ExtendWith(MockitoExtension.class) 
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository; 

    @Mock
    private UserRepository userRepository; 

    @InjectMocks
    private TaskService taskService; 


    @Test
    void shouldReturnCompleteStatistics() {
        String EMAIL = "test@test.pl";
        when(taskRepository.countByStatusAndUserEmail(TaskStatus.DONE, EMAIL)).thenReturn(10L);
        when(taskRepository.countByStatusAndUserEmail(TaskStatus.IN_PROGRESS, EMAIL)).thenReturn(7L);
        when(taskRepository.countByStatusAndUserEmail(TaskStatus.TODO, EMAIL)).thenReturn(5L);
        when(taskRepository.countByUserEmail(EMAIL)).thenReturn(15L);

        TaskStatsDto stats = taskService.getTaskStatistics(EMAIL);

        //then
        assertEquals(10, stats.done());
        assertEquals(7, stats.inProgress());
        assertEquals(5, stats.todo());
        assertEquals(15, stats.total());
    }
    @Test
    void shouldDeleteTask() {
        Long ID = 1L;
        when(taskRepository.existsById(ID)).thenReturn(true);

        taskService.deleteTask(ID);

        verify(taskRepository, times(1)).deleteById(ID);
    }
    @Test
    void shouldThrowExceptionWhenDeletingNonExistentTask() {
        Long ID = 99L;
        when(taskRepository.existsById(ID)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            taskService.deleteTask(ID);
        });

        verify(taskRepository, never()).deleteById(ID);
    }
    @Test
    @DisplayName("Should update a status when a task exists")
    void shouldUpdateStatusWhenTaskExists() {
        Long taskId = 1L;
        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setStatus(TaskStatus.TODO);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(i -> i.getArgument(0));


        Task result = taskService.updateStatus(taskId, TaskStatus.DONE);

        assertNotNull(result);
        assertEquals(TaskStatus.DONE, result.getStatus());
        
        verify(taskRepository, times(1)).save(existingTask);
    }

   
}