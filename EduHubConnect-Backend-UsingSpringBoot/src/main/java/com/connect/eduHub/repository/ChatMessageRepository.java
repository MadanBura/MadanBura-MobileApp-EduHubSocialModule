package com.connect.eduHub.repository;

import com.connect.eduHub.model.ChatMessage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySenderAndRecipient(String sender, String recipient);
    List<ChatMessage> findByRecipient(String recipient);
    
    @Query("SELECT cm FROM ChatMessage cm WHERE (cm.sender = :email OR cm.recipient = :email) ORDER BY cm.timestamp DESC")
    List<ChatMessage> findRecentChatsByEmail(@Param("email") String email, Pageable pageable);

    @Query("SELECT cm FROM ChatMessage cm WHERE (cm.sender = :user1 AND cm.recipient = :user2) OR (cm.sender = :user2 AND cm.recipient = :user1) ORDER BY cm.timestamp ASC")
    List<ChatMessage> findMessagesBetweenUsers(String user1, String user2);
//    List<ChatMessage> findTop10BySenderOrRecipientOrderByTimestampDesc(String sender, String recipient);
    @Query("SELECT m FROM ChatMessage m WHERE m.sender = :user OR m.recipient = :user ORDER BY m.timestamp DESC")
    List<ChatMessage> findRecentMessages(@Param("user") String user, Pageable pageable);

    
    List<ChatMessage> findBySenderOrRecipientOrderByTimestampDesc(String sender, String recipient);
    List<ChatMessage> findBySenderAndRecipientOrRecipientAndSenderOrderByTimestamp(
        String s1, String r1, String s2, String r2);
    
}