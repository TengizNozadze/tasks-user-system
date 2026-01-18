package com.fullstack.taskservice.controller;

import com.fullstack.taskservice.model.TaskEntity;
import com.fullstack.taskservice.repo.TaskRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class TaskController {

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @PostMapping("/api/tasks")
    public ResponseEntity<TaskEntity> createTask(@RequestBody TaskEntity dto) {
        // Only title and category are expected from frontend
        TaskEntity entity = new TaskEntity(dto.getTitle(), dto.getCategory());
        TaskEntity saved = taskRepository.save(entity);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/api/tasks")
    public ResponseEntity<List<TaskEntity>> listTasks() {
        List<TaskEntity> all = taskRepository.findAll();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/api/tasks/{id}")
    public ResponseEntity<TaskEntity> getTask(@PathVariable Long id) {
        return taskRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}

