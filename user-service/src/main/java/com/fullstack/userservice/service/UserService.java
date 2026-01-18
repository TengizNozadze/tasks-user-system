package com.fullstack.userservice.service;

import com.fullstack.shared.utils.dto.UserDto;
import com.fullstack.userservice.model.UserEntity;
import com.fullstack.userservice.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserCacheService userCacheService;

    public UserService(UserRepository userRepository, UserCacheService userCacheService) {
        this.userRepository = userRepository;
        this.userCacheService = userCacheService;
    }

    public UserDto createUser(UserDto dto) {
        UserEntity entity = new UserEntity(dto.getUsername(), dto.getFullName(), dto.getCategory());
        entity = userRepository.save(entity);
        UserDto out = new UserDto(entity.getId(), entity.getUsername(), entity.getFullName(), entity.getCategory());
        // update cache for the category by reloading from DB
        updateCacheForCategory(out.getCategory());
        return out;
    }

    public List<UserDto> getByCategory(String category) {
        List<UserDto> cached = userCacheService.get(category);
        if (cached != null) {
            return cached;
        }
        List<UserEntity> users = userRepository.findByCategory(category);
        List<UserDto> dtos = users.stream().map(u -> new UserDto(u.getId(), u.getUsername(), u.getFullName(), u.getCategory())).collect(Collectors.toList());
        userCacheService.put(category, dtos);
        return dtos;
    }

    public List<UserDto> updateCacheForCategory(String category) {
        List<UserEntity> users = userRepository.findByCategory(category);
        List<UserDto> dtos = users.stream().map(u -> new UserDto(u.getId(), u.getUsername(), u.getFullName(), u.getCategory())).collect(Collectors.toList());
        userCacheService.put(category, dtos);
        return dtos;
    }

    public List<UserDto> getAll() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream().map(u -> new UserDto(u.getId(), u.getUsername(), u.getFullName(), u.getCategory())).collect(Collectors.toList());
    }
}
