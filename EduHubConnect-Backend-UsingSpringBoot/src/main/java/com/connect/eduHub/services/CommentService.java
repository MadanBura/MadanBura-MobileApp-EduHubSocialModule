package com.connect.eduHub.services;


import com.connect.eduHub.dto.CommentDTO;
import com.connect.eduHub.mappers.CommentMapper;
import com.connect.eduHub.model.Comment;
import com.connect.eduHub.model.Post;
import com.connect.eduHub.model.User;
import com.connect.eduHub.repository.CommentRepository;
import com.connect.eduHub.repository.PostRepository;
import com.connect.eduHub.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired private CommentRepository commentRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private UserRepo userRepository;
    @Autowired private CommentMapper commentMapper;
    @Autowired private UserService userService;
    @Autowired private NotificationService notificationService;
    // Add a comment to a post
    public CommentDTO addComment(Long userId, Long postId, String content) throws Exception {
        User user = userRepository.findById(userId).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();

//        Comment comment = new Comment();
//        comment.setUser(user);
//        comment.setPost(post);
//        comment.setContent(content);
//        comment.setCreatedAt(LocalDateTime.now());
//        
        //CommentDto
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent(content);
        commentDTO.setPostId(postId);
        commentDTO.setUserId(userId);
        commentDTO.setUserName(user.getName());
        commentDTO.setUserJobTitle(user.getBio());
        commentDTO.setUserProfileImage(user.getProfileImage());
        commentDTO.setCreatedAt(LocalDateTime.now());
        commentDTO.setUpdatedAt(LocalDateTime.now());
      
//        commentDTO.
        
        Comment comment = commentMapper.toEntity(commentDTO);
        
        commentRepository.save(comment);
        notificationService.createCommentNotification(userId, postId, post.getUser().getId(), content);
        return commentDTO;
       
//        return commentMapper.toDTO(commentRepository.save(comment));
    }

    // Get all comments for a post
    public List<CommentDTO> getCommentsForPost(Long postId, Long userId) {
        // Fetch all comments for the given postId
        List<Comment> comments = commentRepository.findByPostId(postId);
        
        // Get the friends of the current user (you need a method or service to get this)
        Set<Long> friends = userService.getFriendsOfUser(userId);  // assuming this service exists

        // Filter comments to include:
        // 1. Friends' comments
        // 2. Updated comments
        // 3. Random comment (if no friends or updated comments)
        List<Comment> filteredComments = comments.stream()
            .filter(comment -> {
                // Check if the commenter is a friend
                boolean isFriend = friends.contains(comment.getUser().getId());
                
                // If the comment is from a friend, include it
                return isFriend || isUpdated(comment);
            })
            .collect(Collectors.toList());

        // If we have no friend or updated comments, pick random comments from the available list
        if (filteredComments.isEmpty()) {
            // Pick a random comment if filtered list is empty
            filteredComments = getRandomComments(comments);
        }

        // Convert to DTO
        return filteredComments.stream()
            .map(commentMapper::toDTO)
            .collect(Collectors.toList());
    }

    // Helper method to determine if a comment is updated
    private boolean isUpdated(Comment comment) {
        // You can check if the comment's `updatedAt` field is after `createdAt` or some other condition.
        return comment.getUpdatedAt() != null && comment.getUpdatedAt().isAfter(comment.getCreatedAt());
    }

    // Helper method to pick random comments
    private List<Comment> getRandomComments(List<Comment> comments) {
        // If there are comments, return a random selection
        if (comments.isEmpty()) {
            return Collections.emptyList();
        }
        
        // Pick a random comment (you can adjust this logic to pick more than one)
        Random rand = new Random();
        return Collections.singletonList(comments.get(rand.nextInt(comments.size())));
    }
    
    
    public List<CommentDTO> getAllCommentsForPost(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                       .map(commentMapper::toDTO)
                       .collect(Collectors.toList());
    }
}
