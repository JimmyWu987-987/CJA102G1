package com.farmtastic.chat.endpoint;

import com.farmtastic.chat.model.ChatMessage;
import com.farmtastic.chat.service.ChatService;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/ws/chat/{userId}")
public class ChatEndpoint {

    private static ChatService chatService;
    private static Map<String, Session> onlineUsers = new ConcurrentHashMap<>();

    @Autowired
    public void setChatService(ChatService service) {
        chatService = service;
    }

    private Session session;
    private String userId;

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;
        onlineUsers.put(userId, session);
        broadcastUserList();
    }

    @OnClose
    public void onClose(Session session) {
        onlineUsers.remove(userId);
        broadcastUserList();
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            // 直接轉 JSON
            ChatMessage msg = parseJson(message);
            chatService.saveMessage(msg);

            // 發送給接收者
            Session toSession = onlineUsers.get(msg.getTo());
            if (toSession != null && toSession.isOpen()) {
                toSession.getBasicRemote().sendText("{\"type\":\"msg\",\"from\":\""+msg.getFrom()+"\",\"to\":\""+msg.getTo()+"\",\"content\":\""+msg.getContent()+"\",\"timestamp\":"+msg.getTimestamp()+"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void broadcastUserList() {
        String list = String.join(",", onlineUsers.keySet());
        onlineUsers.values().forEach(s -> {
            try {
                s.getBasicRemote().sendText("{\"type\":\"users\",\"list\":\""+list+"\"}");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private ChatMessage parseJson(String json) {
        // 簡單解析 JSON，前端發送固定格式
        String from = json.split("\"from\":\"")[1].split("\"")[0];
        String to = json.split("\"to\":\"")[1].split("\"")[0];
        String content = json.split("\"content\":\"")[1].split("\"")[0];
        long timestamp = Long.parseLong(json.split("\"timestamp\":")[1].replaceAll("[^0-9]", ""));
        return new ChatMessage(from, to, content, timestamp);
    }
}

