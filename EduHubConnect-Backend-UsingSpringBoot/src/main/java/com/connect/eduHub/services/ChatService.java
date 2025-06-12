package com.connect.eduHub.services;

import com.connect.eduHub.dto.RecentChatDTO;
import com.connect.eduHub.model.ChatMessage;
import com.connect.eduHub.model.User;
import com.connect.eduHub.repository.ChatMessageRepository;
import com.connect.eduHub.repository.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired private UserRepo userRepository;

    public ChatMessage saveMessage(String sender, String recipient, String encryptedContent) {
        ChatMessage message = ChatMessage.builder()
                .sender(sender)
                .recipient(recipient)
                .content(encryptedContent)
                .timestamp(LocalDateTime.now())
                .build();
        return chatMessageRepository.save(message);
    }

//    public List<ChatMessage> getRecentChats(String userEmail) {
//        // Fetch last 10 recent chats ordered by timestamp
//        Pageable pageable = PageRequest.of(0, 10);
//        return chatMessageRepository.findRecentChatsByEmail(userEmail, pageable);
//    }
//    
    
    public List<RecentChatDTO> getRecentChats(String userEmail) {
        // Step 1: Get last 100 messages involving the user
        Pageable pageable = PageRequest.of(0, 100);
        List<ChatMessage> allRecentMessages = chatMessageRepository.findRecentMessages(userEmail, pageable);

        // Step 2: Keep only latest message per unique other user
        Map<String, ChatMessage> latestMessages = new LinkedHashMap<>();

        for (ChatMessage msg : allRecentMessages) {
            String otherEmail = msg.getSender().equals(userEmail) ? msg.getRecipient() : msg.getSender();

            // Only keep the first occurrence (most recent due to sorting)
            if (!latestMessages.containsKey(otherEmail)) {
                latestMessages.put(otherEmail, msg);
            }
        }

        // Step 3: Convert to DTO
        List<RecentChatDTO> result = new ArrayList<>();

        for (Map.Entry<String, ChatMessage> entry : latestMessages.entrySet()) {
            String otherUserEmail = entry.getKey();
            ChatMessage lastMessage = entry.getValue();

            Optional<User> userOpt = userRepository.findByEmail(otherUserEmail);
            userOpt.ifPresent(otherUser -> {
                RecentChatDTO dto = new RecentChatDTO(
                    otherUser.getName(),
                    otherUser.getEmail(),
                    otherUser.getProfileImage(), // or `getProfilePicUrl()` depending on your model
                    lastMessage.getContent(),
                    lastMessage.getTimestamp()
                );
                result.add(dto);
            });
        }

        return result;
    }


    
    
    public List<ChatMessage> getMessagesBetweenUsers(String user1, String user2) {
        return chatMessageRepository.findMessagesBetweenUsers(user1, user2);
    }
    
    public List<ChatMessage> getRecentChatsOfUser(String user) {
        return chatMessageRepository.findBySenderOrRecipientOrderByTimestampDesc(user, user);
    }
    public List<ChatMessage> getBetween(String u1, String u2) {
        return chatMessageRepository.findBySenderAndRecipientOrRecipientAndSenderOrderByTimestamp(u1,u2,u2,u1);
    }
}
