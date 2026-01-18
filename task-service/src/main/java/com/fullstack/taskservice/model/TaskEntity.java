package com.fullstack.taskservice.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tasks")
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String category;

    @ElementCollection
    @CollectionTable(name = "task_assigned_usernames", joinColumns = @JoinColumn(name = "task_id"))
    @Column(name = "username")
    private List<String> assignedUsernames = new ArrayList<>();

    private Instant createdAt = Instant.now();

    public TaskEntity() {}

    public TaskEntity(String title, String category) {
        this.title = title;
        this.category = category;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public List<String> getAssignedUsernames() { return assignedUsernames; }
    public void setAssignedUsernames(List<String> assignedUsernames) { this.assignedUsernames = assignedUsernames; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}

