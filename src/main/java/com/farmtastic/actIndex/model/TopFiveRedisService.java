package com.farmtastic.actIndex.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TopFiveRedisService {
    private static final String KEY = "hot:act";
    private final StringRedisTemplate redis;
    
    public TopFiveRedisService(StringRedisTemplate redis) {
        this.redis = redis;
    }

    // 點擊 +1
    public void hit(String actId) {
        redis.opsForZSet().incrementScore(KEY, actId, 1D);
    }

    // 取前五名
    public List<String> top5() {
        Set<String> ids = redis.opsForZSet().reverseRange(KEY, 0, 4);
        return (ids == null) ? List.of() : new ArrayList<>(ids);
    }
}
