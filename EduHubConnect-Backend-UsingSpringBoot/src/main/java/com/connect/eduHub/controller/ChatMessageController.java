package com.connect.eduHub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.connect.eduHub.dto.RecentChatDTO;
import com.connect.eduHub.interceptor.ChatWebSocketHandler;
import com.connect.eduHub.model.ChatMessage;
import com.connect.eduHub.repository.ChatMessageRepository;
import com.connect.eduHub.services.ChatService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatMessageController {
	
	
	@Autowired private ChatMessageRepository chatMessageRepository;
	@Autowired private ChatService chatService;
	@Autowired private ChatWebSocketHandler cuHandler;

	// Endpoint to get recent chats for a user
    @GetMapping("/recent/{userEmail}")
    public List<RecentChatDTO> getRecentChats(@PathVariable String userEmail) {
        return chatService.getRecentChats(userEmail);
    }

    // Endpoint to get messages between two users
    @GetMapping("/messages/{user1}/{user2}")
    public List<ChatMessage> getMessagesBetweenUsers(
            @PathVariable String user1, 
            @PathVariable String user2) {
        return chatService.getMessagesBetweenUsers(user1, user2);
    }
    
    @PostMapping("/send")
    public ChatMessage send(@RequestBody ChatMessage message) throws Exception {
        // Delegate to handler which saves & pushes
        cuHandler.sendAndSave(message);
        return message;
    }
}
