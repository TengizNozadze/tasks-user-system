package com.fullstack.userservice.controller;

import com.fullstack.shared.utils.dto.UserDto;
import com.fullstack.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/users")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto dto) {
        UserDto created = userService.createUser(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/api/users")
    public ResponseEntity<List<UserDto>> listUsers() {
        List<UserDto> users = userService.getAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/internal/users/by-category/{category}")
    public ResponseEntity<List<UserDto>> getByCategory(@PathVariable String category) {
        List<UserDto> users = userService.getByCategory(category);
        return ResponseEntity.ok(users);
    }
}
