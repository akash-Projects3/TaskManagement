package com.ikaansh.taskmanagement.controller;

import com.ikaansh.taskmanagement.entity.Task;
import com.ikaansh.taskmanagement.service.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    public record TaskRequest(
            @NotBlank @Size(max = 255) String title,
            @Size(max = 2000) String description,
            String status
    ) {}

    @GetMapping
    public List<Task> getTasks(Authentication authentication) {
        Long userId = getUserId(authentication);
        return taskService.getTasks(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(
            @Valid @RequestBody TaskRequest request,
            Authentication authentication
    ) {
        Long userId = getUserId(authentication);
        return taskService.createTask(
                userId,
                request.title(),
                request.description()
        );
    }

    @GetMapping("/{id}")
    public Task getTask(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return taskService.getTask(id, getUserId(authentication));
    }

    @PutMapping("/{id}")
    public Task updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request,
            Authentication authentication
    ) {
        return taskService.updateTask(
                id,
                getUserId(authentication),
                request.title(),
                request.description(),
                request.status()
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(
            @PathVariable Long id,
            Authentication authentication
    ) {
        taskService.deleteTask(id, getUserId(authentication));
    }

    private Long getUserId(Authentication authentication) {
        return ((Number) authentication.getCredentials()).longValue();
    }
}
