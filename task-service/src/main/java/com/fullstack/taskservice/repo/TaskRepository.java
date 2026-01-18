package com.fullstack.taskservice.repo;

import com.fullstack.taskservice.model.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findByAssignedUsernamesIsEmpty();
}

