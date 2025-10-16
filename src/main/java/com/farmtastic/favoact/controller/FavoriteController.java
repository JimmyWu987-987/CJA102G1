package com.farmtastic.favoact.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class FavoriteController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private String getKey(Long userId) {
        return "favorites:user:" + userId;
    }

    private Long getCurrentUserId() {
        return 1L; // 模擬登入
    }

    @PostMapping("/favorite/{activityId}")
    public String addFavorite(@PathVariable Long activityId){
        String key = getKey(getCurrentUserId());
        redisTemplate.opsForSet().add(key, String.valueOf(activityId));
        return "已收藏";
    }

    @DeleteMapping("/favorite/{activityId}")
    public String removeFavorite(@PathVariable Long activityId){
        String key = getKey(getCurrentUserId());
        redisTemplate.opsForSet().remove(key, String.valueOf(activityId));
        return "已移除收藏";
    }

    @GetMapping("/favorites")
    public List<Long> getFavorites(){
        String key = getKey(getCurrentUserId());
        Set<String> ids = redisTemplate.opsForSet().members(key);
        if(ids == null) return List.of();
        return ids.stream().map(Long::parseLong).collect(Collectors.toList());
    }
}
