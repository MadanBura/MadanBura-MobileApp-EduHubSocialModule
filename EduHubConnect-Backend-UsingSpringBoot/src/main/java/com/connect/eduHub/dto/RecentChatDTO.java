package com.connect.eduHub.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class RecentChatDTO {
    private String username;
    private String userEmail;
    private String profilePicUrl;
    private String lastMessage;
    private LocalDateTime timestamp;
}
