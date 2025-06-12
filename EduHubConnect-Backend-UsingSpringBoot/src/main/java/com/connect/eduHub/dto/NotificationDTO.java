package com.connect.eduHub.dto;

import java.time.LocalDateTime;

import com.connect.eduHub.model.NOTIFICATIONTYPE;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class NotificationDTO {
	    private Long userId;  
	    private Long targetUserId; 
	    private Long postId; 
	    private NOTIFICATIONTYPE type;  
	    private String message; 
	    private String commentContent; 
	    private boolean isRead; 
	    private String profileImage;
	    private String userName;
	    private LocalDateTime timestamp;
}
