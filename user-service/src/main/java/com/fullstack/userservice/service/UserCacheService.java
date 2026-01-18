package com.fullstack.userservice.service;

import com.fullstack.shared.utils.dto.UserDto;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class UserCacheService {

    private static final String MAP_NAME = "usersByCategoryMap";
    private final RedissonClient redissonClient;

    public UserCacheService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public List<UserDto> get(String category) {
        RMapCache<String, List<UserDto>> map = redissonClient.getMapCache(MAP_NAME);
        return map.get(category);
    }

    public void put(String category, List<UserDto> users) {
        RMapCache<String, List<UserDto>> map = redissonClient.getMapCache(MAP_NAME);
        // store with a TTL of 10 minutes
        map.put(category, users, 10, TimeUnit.MINUTES);
    }

    public void evict(String category) {
        RMapCache<String, List<UserDto>> map = redissonClient.getMapCache(MAP_NAME);
        map.remove(category);
    }
}

