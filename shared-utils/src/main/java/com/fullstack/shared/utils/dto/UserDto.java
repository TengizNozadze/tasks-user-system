package com.fullstack.shared.utils.dto;

import java.io.Serializable;

public class UserDto implements Serializable {
    private Long id;
    private String username;
    private String fullName;
    private String category;

    public UserDto() {}

    public UserDto(Long id, String username, String fullName, String category) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.category = category;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}

