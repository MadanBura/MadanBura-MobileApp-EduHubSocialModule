package com.connect.eduHub.interceptor;

import com.connect.eduHub.e2ee.ChatMessageEncrypted;
import com.connect.eduHub.model.ChatMessage;
import com.connect.eduHub.repository.ChatMessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

	   private final ChatMessageRepository chatMessageRepository;
	    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

	    public ChatWebSocketHandler(ChatMessageRepository repository) {
	        this.chatMessageRepository = repository;
	    }

	    @Override
	    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
	        String username = (String) session.getAttributes().get("username");
	        if (username != null) {
	            sessions.put(username, session);
	            System.out.println("WebSocket connected: " + username);
	            System.out.println("Currently connected users: " + sessions.keySet());
	        } else {
	            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Unauthorized"));
	        }
	    }

	    @Override
	    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
	        ChatMessage chatMessage = objectMapper.readValue(message.getPayload().toString(), ChatMessage.class);
	        sendAndSave(chatMessage);
//	        
//	    	 ChatMessageEncrypted encryptedMessage = objectMapper.readValue(message.getPayload().toString(), ChatMessageEncrypted.class);
//
//	    	    // Save encrypted data
//	    	    ChatMessage chatMessage = new ChatMessage();
//	    	    chatMessage.setSender(encryptedMessage.getSender());
//	    	    chatMessage.setRecipient(encryptedMessage.getRecipient());
//	    	    chatMessage.setContent(encryptedMessage.getContent());
//	    	    chatMessage.setTimestamp(LocalDateTime.now());
//	    	    
//	    	    sendAndSave(chatMessage);
	    	    
//	    	    chatMessageRepository.save(chatMessage);

	    	    // Forward to recipient
//	    	    WebSocketSession recipientSession = sessions.get(encryptedMessage.getRecipient());
//	    	    if (recipientSession != null && recipientSession.isOpen()) {
//	    	        recipientSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(encryptedMessage)));
//	    	    } else {
//	    	        System.out.println("Recipient offline: " + encryptedMessage.getRecipient());
//	    	    }
//	        
	    }

	    public void sendAndSave(ChatMessage chatMessage) throws IOException {
	        chatMessage.setTimestamp(java.time.LocalDateTime.now());
	        chatMessageRepository.save(chatMessage);

	        WebSocketSession recipientSession = sessions.get(chatMessage.getRecipient());
	        if (recipientSession != null && recipientSession.isOpen()) {
	            recipientSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMessage)));
	            System.out.println("Message sent to: " + chatMessage.getRecipient());
	        } else {
	            System.out.println("Recipient session not open or not found for: " + chatMessage.getRecipient());
	        }
	    }

	    @Override
	    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
	        session.close(CloseStatus.SERVER_ERROR);
	    }

	    @Override
	    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
	        sessions.values().removeIf(s -> s.getId().equals(session.getId()));
	    }

	    @Override
	    public boolean supportsPartialMessages() {
	        return false;
	    }
}
