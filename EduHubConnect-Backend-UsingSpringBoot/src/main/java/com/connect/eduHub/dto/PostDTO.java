package com.connect.eduHub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.connect.eduHub.model.Comment;
import com.connect.eduHub.model.Like;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class PostDTO {
   
	private Long id;
    private String content;
    private String imageUrl;
    private LocalDateTime createdAt;
    private Long userId; // Author
    private String userName;
    private String userProfilePic;
    private int likeCount=0;          // total likes
    private int commentCount=0;       // total comments
    private boolean likedByUser;    // did the current user like this post
    
    
    private List<UserDTO> likedBy = new ArrayList<>();        // list of users who liked
    private List<CommentDTO> comments =new ArrayList<>();    // list of comments with user info
    

    

}
