package com.connect.eduHub.dto;

import java.time.LocalDateTime;

import com.connect.eduHub.model.RequestStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class FriendRequestDTO {
	private Long id;
    private Long senderId;
    private Long receiverId;
    private RequestStatus status;
    private LocalDateTime sentAt;
    
    //added
    private String senderName;
    private String userBio;
    private String senderEmail;
    private String senderProfileImage;
}
