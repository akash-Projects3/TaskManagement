package com.ikaansh.taskmanagement.service;

import com.ikaansh.taskmanagement.entity.Task;
import com.ikaansh.taskmanagement.entity.User;
import com.ikaansh.taskmanagement.repository.TaskRepository;
import com.ikaansh.taskmanagement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository,
                       UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<Task> getTasks(Long userId) {
        return taskRepository.findAllByUserId(userId);
    }

    public Task createTask(Long userId, String title, String description) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setUser(user);

        return taskRepository.save(task);
    }

    public Task getTask(Long taskId, Long userId) {
        return taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Task not found"));
    }

    public Task updateTask(Long taskId, Long userId,
                           String title, String description, String status) {
        Task task = getTask(taskId, userId);

        if (title != null) {
            task.setTitle(title);
        }

        if (description != null) {
            task.setDescription(description);
        }

        if (status != null) {
            task.setStatus(status);
        }

        return taskRepository.save(task);
    }

    public void deleteTask(Long taskId, Long userId) {
        Task task = getTask(taskId, userId);
        taskRepository.delete(task);
    }
}
