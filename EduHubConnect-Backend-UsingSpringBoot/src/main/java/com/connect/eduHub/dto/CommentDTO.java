package com.connect.eduHub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CommentDTO {
	  private Long id;
	    private String content;
	    private Long userId;
	    private Long postId;
	    private String userName;
	    private String userProfileImage;
	    private String userJobTitle;
	    private LocalDateTime createdAt;
	    private LocalDateTime updatedAt;
}
