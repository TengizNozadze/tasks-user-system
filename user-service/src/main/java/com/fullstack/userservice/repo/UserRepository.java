package com.fullstack.userservice.repo;

import com.fullstack.userservice.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findByCategory(String category);
}

