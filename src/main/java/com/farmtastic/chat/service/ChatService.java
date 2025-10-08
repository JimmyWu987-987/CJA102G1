package com.farmtastic.chat.service;

import com.farmtastic.chat.model.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void saveMessage(ChatMessage msg) {
        String key = "chat:" + msg.getFrom() + ":" + msg.getTo();
        redisTemplate.opsForList().rightPush(key, toJson(msg));

        // 反向 key 保存，方便雙向查詢
        String reverseKey = "chat:" + msg.getTo() + ":" + msg.getFrom();
        redisTemplate.opsForList().rightPush(reverseKey, toJson(msg));
    }

    public List<String> getHistory(String userA, String userB) {
        String key = "chat:" + userA + ":" + userB;
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    private String toJson(ChatMessage msg) {
        return String.format("{\"from\":\"%s\",\"to\":\"%s\",\"content\":\"%s\",\"timestamp\":%d}",
                msg.getFrom(), msg.getTo(), msg.getContent(), msg.getTimestamp());
    }
}
