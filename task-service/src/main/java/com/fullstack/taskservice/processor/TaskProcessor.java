package com.fullstack.taskservice.processor;

import com.fullstack.shared.utils.dto.UserDto;
import com.fullstack.taskservice.client.UserServiceClient;
import com.fullstack.taskservice.model.TaskEntity;
import com.fullstack.taskservice.repo.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskProcessor {

    private static final Logger logger = LoggerFactory.getLogger(TaskProcessor.class);

    private final TaskRepository taskRepository;
    private final UserServiceClient userServiceClient;

    public TaskProcessor(TaskRepository taskRepository, UserServiceClient userServiceClient) {
        this.taskRepository = taskRepository;
        this.userServiceClient = userServiceClient;
    }

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void assignUsersToUnassignedTasks() {
        logger.info("TaskProcessor: scanning for unassigned tasks...");
        List<TaskEntity> unassigned = taskRepository.findByAssignedUsernamesIsEmpty();
        for (TaskEntity task : unassigned) {
            try {
                logger.info("Processing task id={} category={}", task.getId(), task.getCategory());
                List<UserDto> users = userServiceClient.getByCategory(task.getCategory());
                if (users != null && !users.isEmpty()) {
                    List<String> usernames = users.stream().map(UserDto::getUsername).collect(Collectors.toList());
                    task.setAssignedUsernames(usernames);
                    taskRepository.save(task);
                    logger.info("Assigned {} users to task id={}", usernames.size(), task.getId());
                } else {
                    logger.info("No users found for category {} for task id={}", task.getCategory(), task.getId());
                }
            } catch (Exception ex) {
                logger.error("Error processing task id=" + task.getId(), ex);
            }
        }
    }
}

