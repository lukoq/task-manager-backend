package com.taskmanager.backend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.taskmanager.backend.dto.TaskResponseDto;
import com.taskmanager.backend.dto.TaskStatsDto;
import com.taskmanager.backend.entity.Task;
import com.taskmanager.backend.entity.TaskStatus;
import com.taskmanager.backend.entity.User;
import com.taskmanager.backend.repository.TaskRepository;
import com.taskmanager.backend.repository.UserRepository;
import com.taskmanager.backend.service.TaskService;

import static org.junit.jupiter.api.Assertions.*; 
import static org.mockito.Mockito.when;

import java.util.List;
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
    void shouldReturnCompleteStatistics() { //getTaskStatistics() test
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
    void shouldDeleteTask() { //deleteTask() test 1
        Long ID = 1L;
        when(taskRepository.existsById(ID)).thenReturn(true);

        taskService.deleteTask(ID);

        verify(taskRepository, times(1)).deleteById(ID);
    }
    @Test
    void shouldThrowExceptionWhenDeletingNonExistentTask() { //deleteTask() test 2
        Long ID = 99L;
        when(taskRepository.existsById(ID)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            taskService.deleteTask(ID);
        });

        verify(taskRepository, never()).deleteById(ID);
    }
    @Test 
    void shouldUpdateStatusWhenTaskExists() { //updateStatus() test 1
        Task task = new Task();
        task.setId(1L);
        task.setStatus(TaskStatus.TODO);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(i -> i.getArgument(0));


        Task result = taskService.updateStatus(task.getId(), TaskStatus.DONE);

        assertNotNull(result);
        assertEquals(TaskStatus.DONE, result.getStatus());
        
        verify(taskRepository, times(1)).save(task);
    }
    @Test 
    void shouldThrowExceptionWhenTaskNotFoundInUpdateStatus() { //updateStatus() test 2
        Long taskId = 99L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            taskService.updateStatus(taskId, TaskStatus.TODO);
        });

        verify(taskRepository, never()).save(any(Task.class));
    }
    @Test
    void shouldSaveTaskCorrectly() { //saveTask() test 1
        String EMAIL = "test@test.pl";
        Task task = new Task();
        User user = new User();


        task.setTitle("The new task");
        task.setDescription("Description of the new task");
        user.setEmail(EMAIL);

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task result = taskService.saveTask(task, EMAIL);

        //then
        assertNotNull(result);
        assertEquals("The new task", result.getTitle());
        
        //user == task ?
        assertNotNull(result.getUser());
        assertEquals(EMAIL, result.getUser().getEmail());

        verify(userRepository, times(1)).findByEmail(EMAIL);
        verify(taskRepository, times(1)).save(any(Task.class));
    }
    @Test
    void shouldThrowExceptionWhenUserNotFoundInSaveTask() { //saveTask() test 2
        String EMAIL = "test@test.pl";
        Task task = new Task();
        task.setTitle("The new task");
        task.setDescription("Description of the new task");

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());


        assertThrows(RuntimeException.class, () -> {
            taskService.saveTask(task, EMAIL);
        });

        verify(taskRepository, never()).save(any(Task.class));
    }
    @Test 
    void shouldUpdateDescriptionWhenTaskExists() { //updateDescription() test 1
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        task.setDescription("New description");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(i -> i.getArgument(0));


        Task result = taskService.updateDescription(task.getId(), task.getDescription());

        assertNotNull(result);
        assertEquals("New description", result.getDescription());
        
        verify(taskRepository, times(1)).save(task);
    }
    @Test 
    void shouldThrowExceptionWhenTaskNotFoundInUpdateDescription() { //updateDescription() test 2
        Long taskId = 99L;
        String newDescription = "New description";
        
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            taskService.updateDescription(taskId, newDescription);
        });

        verify(taskRepository, never()).save(any(Task.class));
    }
    @Test
    void shouldReturnTaskResponseDtoListForUser() {
        String EMAIL = "test@test.pl";
        
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");
        task1.setDescription("Desc 1");

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setDescription("Desc 2");

        when(taskRepository.findByUserEmailOrderByCreatedAtAsc(EMAIL)).thenReturn(List.of(task1, task2));

        List<TaskResponseDto> result = taskService.getTasksForUser(EMAIL);

        assertNotNull(result);
        assertEquals(2, result.size());
        
        assertEquals(1L, result.get(0).getId()); 
        assertEquals(2L, result.get(1).getId()); 
        assertEquals("Task 1", result.get(0).getTitle());
        assertEquals("Desc 1", result.get(0).getDescription());
        assertEquals("Task 2", result.get(1).getTitle());
        assertEquals("Desc 2", result.get(1).getDescription());


        verify(taskRepository, times(1)).findByUserEmailOrderByCreatedAtAsc(EMAIL);
    }

   
}