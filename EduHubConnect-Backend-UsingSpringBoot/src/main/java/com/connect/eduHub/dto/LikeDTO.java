package com.connect.eduHub.dto;

import lombok.Data;

@Data
public class LikeDTO {
    private Long id;
    private Long userId;   // ✅ Replace UserDTO with just userId
    private Long postId;
}
