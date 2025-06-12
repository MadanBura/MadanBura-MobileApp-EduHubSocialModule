package com.connect.eduHub.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;  // The user who performed the action (commented, shared, etc.)
    private Long targetUserId; // The user who will receive the notification (post owner)
    private Long postId; // The post on which the action occurred
    @Enumerated(EnumType.STRING)
    private NOTIFICATIONTYPE type;  // "liked", "commented", "shared"
    private String message; // The message associated with the notification (e.g., "Tony commented on your post")
    private String commentContent; // Store the actual comment content if action is "commented" (can be null if not a comment)
    private boolean isRead; 
    private LocalDateTime timestamp; // Timestamp when the notification was created
    
}
