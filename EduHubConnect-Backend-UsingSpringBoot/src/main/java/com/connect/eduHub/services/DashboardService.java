package com.connect.eduHub.services;

import com.connect.eduHub.model.response.UserDashboardResponse;
import com.connect.eduHub.dto.UserDTO;
import com.connect.eduHub.mappers.FriendRequestMapper;
import com.connect.eduHub.mappers.PostMapper;
import com.connect.eduHub.mappers.UserMapper;
import com.connect.eduHub.dto.PostDTO;
import com.connect.eduHub.dto.CommentDTO;
import com.connect.eduHub.dto.FriendRequestDTO;
import com.connect.eduHub.model.Comment;
import com.connect.eduHub.model.FriendRequest;
import com.connect.eduHub.model.Post;
import com.connect.eduHub.model.RequestStatus;
import com.connect.eduHub.model.User;
import com.connect.eduHub.repository.UserRepo;
import com.connect.eduHub.repository.CommentRepository;
import com.connect.eduHub.repository.FriendRequestRepository;
import com.connect.eduHub.repository.LikeRepository;
import com.connect.eduHub.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private UserRepo userRepo;
    
    @Autowired
    private PostRepository postRepo;
    
    @Autowired
    private LikeRepository likeRepository;
    
    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private FriendRequestRepository friendRequestRepo;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private PostMapper postMapper;
    
    @Autowired
    private FriendRequestMapper friendRequestMapper;
    
    @Autowired private NotificationService notificationService;

    public UserDashboardResponse getUserDashboard(Long userId) {
        // Fetch user profile
    	 User user = userRepo.findById(userId).orElseThrow();

    	    UserDTO userProfile = userMapper.toDTO(user);

    	    Set<User> friends = user.getFriends();
    	    List<UserDTO> friendDTOs = friends.stream().map(userMapper::toDTO).toList();

    	    // Friends' posts
    	    Set<User> friendSet = new HashSet<>(friends);
    	    List<Post> friendPosts = postRepo.findByUserIn(friendSet);

    	    // Non-friends' posts
    	    List<Post> allPosts = postRepo.findAllRandomPostsExcludingUser(userId);
    	    List<Post> nonFriendPosts = allPosts.stream()
    	        .filter(p -> !friendSet.contains(p.getUser()))
    	        .limit(10) // limit to avoid overwhelming results
    	        .toList();

    	    // Merge both
    	    List<Post> finalFeed = new ArrayList<>();
    	    finalFeed.addAll(friendPosts);
    	    finalFeed.addAll(nonFriendPosts);

    	    Collections.shuffle(finalFeed); // mix them up

    	    List<PostDTO> feedPostDTOs = finalFeed.stream().map(post -> {
    	        PostDTO dto = postMapper.toDTO(post);

    	        dto.setUserId(post.getUser().getId());
    	        dto.setUserName(post.getUser().getName());
    	        dto.setUserProfilePic(post.getUser().getProfileImage());
    	        
    	        dto.setLikeCount(likeRepository.countByPostId(post.getId()));
    	        dto.setCommentCount(commentRepository.findByPostId(post.getId()).size());
    	        
//    	        if(post.getLikes().size()>0) {
//    	        	dto.setLikedByUser(true);
//    	        }
    	        
    	        dto.setLikedByUser(likeRepository.findByUserIdAndPostId(userId, post.getId()).isPresent());

    	        // ✅ LikedBy users
    	        List<UserDTO> likedByUsers = likeRepository.findAllByPost(post).stream()
    	            .map(like -> userMapper.toDTO(like.getUser()))
    	            .toList();
    	        dto.setLikedBy(likedByUsers);

    	        // ✅ Comments with user info
    	        List<CommentDTO> commentDTOs = commentRepository.findByPost(post).stream()
    	            .map(comment -> {
    	                CommentDTO commentDTO = new CommentDTO();
    	                commentDTO.setId(comment.getId());
    	                commentDTO.setContent(comment.getContent());
    	                commentDTO.setCreatedAt(comment.getCreatedAt());
    	                commentDTO.setUserId(comment.getUser().getId());
    	                commentDTO.setUserName(comment.getUser().getName());
    	                commentDTO.setUserJobTitle(comment.getUser().getBio());
    	                commentDTO.setUserProfileImage(comment.getUser().getProfileImage());
    	                return commentDTO;
    	            }).toList();
    	        dto.setComments(commentDTOs);
    	        
    	        List<Comment> allComments = commentRepository.findByPost(post);
    	        List<CommentDTO> latestCommentDTO = allComments.stream()
    	            .sorted((c1, c2) -> c2.getCreatedAt().compareTo(c1.getCreatedAt())) // newest first
    	            .limit(1)
    	            .map(comment -> {
    	                CommentDTO commentDTO = new CommentDTO();
    	                commentDTO.setId(comment.getId());
    	                commentDTO.setContent(comment.getContent());
    	                commentDTO.setCreatedAt(comment.getCreatedAt());
    	                commentDTO.setUserId(comment.getUser().getId());
    	                commentDTO.setUserName(comment.getUser().getName());
    	                commentDTO.setUserJobTitle(comment.getUser().getBio());
    	                commentDTO.setUserProfileImage(comment.getUser().getProfileImage());
    	                return commentDTO;
    	            })
    	            .collect(Collectors.toList());

    	        dto.setComments(latestCommentDTO);
    	        
    	         return dto;
    	    }).toList();

    	    List<FriendRequest> pendingRequests = friendRequestRepo.findByReceiverAndStatus(user, RequestStatus.PENDING);
    	    List<FriendRequestDTO> requestDTOs = pendingRequests.stream().map(friendRequestMapper::toDTO).toList();

    	    int unreadNotificationCount = notificationService.getUnreadNotification(userId).size();
    	    
    	    return new UserDashboardResponse(userProfile, friendDTOs, feedPostDTOs, requestDTOs, unreadNotificationCount);
    }

    private List<UserDTO> getUserFriends(Long userId) {
        // Implement the logic to fetch friends of the user
        return userRepo.findFriendsByUserId(userId).stream()
            .map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getBio(), user.getProfileImage()))
            .collect(Collectors.toList());
    }

    private List<PostDTO> getUserFeedPosts(Long userId) {
        return postRepo.findPostsByUserId(userId).stream()
            .map(post -> {
                // Convert likes to UserDTO
                List<UserDTO> likedUsers = post.getLikes().stream()
                    .map(like -> new UserDTO(like.getUser().getId(), like.getUser().getName(), like.getUser().getEmail(), null, null))
                    .collect(Collectors.toList());

                // Convert comments to CommentDTO
                List<CommentDTO> commentDTOs = post.getComments().stream()
                    .map(comment -> new CommentDTO(
                            comment.getId(),
                            comment.getContent(),
                            comment.getUser().getId(),
                            comment.getPost().getId(),
                            comment.getUser().getName(),
                            comment.getUser().getBio(),
                            comment.getUser().getProfileImage(),
                            comment.getCreatedAt(),
                            comment.getUpdatedAt()
                    ))
                    .collect(Collectors.toList());

                // Check if the current user liked this post
                boolean isLikedByUser = post.getLikes().stream()
                    .anyMatch(like -> like.getUser().getId().equals(userId));

                return new PostDTO(
                    post.getId(),
                    post.getContent(),
                    post.getImageUrl(),
                    post.getCreatedAt(),
                    post.getUser().getId(),
                    post.getUser().getName(),
                    post.getUser().getProfileImage(),
                    likedUsers.size(),
                    commentDTOs.size(),
                    isLikedByUser,
                    likedUsers,
                    commentDTOs
                );
            })
            .collect(Collectors.toList());
    }

    private List<FriendRequestDTO> getPendingFriendRequests(Long userId) {
        // Implement the logic to fetch pending friend requests for the user
    	
    	User user = userRepo.findById(userId).get();
    	
        return friendRequestRepo.findPendingRequestsByUserId(userId).stream()
            .map(request -> new FriendRequestDTO(request.getId(), request.getSender().getId(), 
            		request.getReceiver().getId(), request.getStatus(), request.getSentAt(),request.getSender().getName(),
            		request.getSender().getBio(), request.getSender().getEmail(), request.getSender().getProfileImage()))
            .collect(Collectors.toList());
    }
    
    

    
}
