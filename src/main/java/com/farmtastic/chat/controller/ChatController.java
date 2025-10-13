package com.farmtastic.chat.controller;

import com.farmtastic.chat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/history")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/{userA}/{userB}")
    public List<String> getHistory(@PathVariable String userA, @PathVariable String userB) {
        return chatService.getHistory(userA, userB);
    }
}
