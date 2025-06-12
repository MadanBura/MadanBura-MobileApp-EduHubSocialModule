package com.connect.eduHub.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageDTO {
    private Long id;
    private String content;
    private Long senderId;
    private Long receiverId;
    private LocalDateTime timestamp;
}
