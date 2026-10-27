package com.ikaansh.taskmanagement.service;

import com.ikaansh.taskmanagement.entity.Task;
import com.ikaansh.taskmanagement.entity.User;
import com.ikaansh.taskmanagement.repository.TaskRepository;
import com.ikaansh.taskmanagement.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void getTasksShouldReturnTasksForUser() {
        Task task = new Task();
        task.setTitle("Learn Java");

        when(taskRepository.findAllByUserId(1L))
                .thenReturn(List.of(task));

        List<Task> result = taskService.getTasks(1L);

        assertEquals(1, result.size());
        assertEquals("Learn Java", result.get(0).getTitle());
        verify(taskRepository).findAllByUserId(1L);
    }

    @Test
    void createTaskShouldSaveTaskForUser() {
        User user = new User();
        user.setName("Akash");
        user.setEmail("akash@example.com");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Task result = taskService.createTask(
                1L, "Learn Spring Boot", "Study REST APIs");

        assertEquals("Learn Spring Boot", result.getTitle());
        assertEquals("Study REST APIs", result.getDescription());
        assertSame(user, result.getUser());

        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void createTaskShouldFailWhenUserDoesNotExist() {
        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                taskService.createTask(99L, "Task", "Description"));

        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void getTaskShouldReturnTaskOwnedByUser() {
        Task task = new Task();
        task.setTitle("My task");

        when(taskRepository.findByIdAndUserId(10L, 1L))
                .thenReturn(Optional.of(task));

        Task result = taskService.getTask(10L, 1L);

        assertEquals("My task", result.getTitle());
    }

    @Test
    void getTaskShouldFailWhenTaskDoesNotBelongToUser() {
        when(taskRepository.findByIdAndUserId(10L, 2L))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                taskService.getTask(10L, 2L));
    }

    @Test
    void updateTaskShouldUpdateTaskDetails() {
        Task task = new Task();
        task.setTitle("Old title");
        task.setDescription("Old description");
        task.setStatus("PENDING");

        when(taskRepository.findByIdAndUserId(10L, 1L))
                .thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Task result = taskService.updateTask(
                10L, 1L, "New title", "New description", "IN_PROGRESS");

        assertEquals("New title", result.getTitle());
        assertEquals("New description", result.getDescription());
        assertEquals("IN_PROGRESS", result.getStatus());

        verify(taskRepository).save(task);
    }

    @Test
    void deleteTaskShouldDeleteTaskOwnedByUser() {
        Task task = new Task();
        task.setTitle("Task to delete");

        when(taskRepository.findByIdAndUserId(10L, 1L))
                .thenReturn(Optional.of(task));

        taskService.deleteTask(10L, 1L);

        verify(taskRepository).delete(task);
    }

    @Test
    void deleteTaskShouldNotDeleteAnotherUsersTask() {
        when(taskRepository.findByIdAndUserId(10L, 2L))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                taskService.deleteTask(10L, 2L));

        verify(taskRepository, never()).delete(any(Task.class));
    }
}
