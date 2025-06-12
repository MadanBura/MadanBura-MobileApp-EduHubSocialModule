package com.connect.eduHub.services;


import com.connect.eduHub.dto.LikeDTO;
import com.connect.eduHub.mappers.LikeMapper;
import com.connect.eduHub.model.Like;
import com.connect.eduHub.model.Post;
import com.connect.eduHub.model.User;
import com.connect.eduHub.repository.LikeRepository;
import com.connect.eduHub.repository.PostRepository;
import com.connect.eduHub.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeService {

    @Autowired private LikeRepository likeRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private UserRepo userRepository;
    @Autowired private LikeMapper likeMapper;
    
    @Autowired private NotificationService notificationService;

    // Toggle like (like/unlike)
    public String toggleLike(Long userId, Long postId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();

        Optional<Like> existingLike = likeRepository.findByPostAndUser(post, user);

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            return "Unliked the post.";
        } else {
            Like like = new Like();
            like.setUser(user);
            like.setPost(post);
            likeRepository.save(like);
            
            notificationService.createLikeNotification(userId, postId, post.getUser().getId());
//            notificationService.sendNotification(user.getName(), " liked your post.", " ");
            return "Liked the post.";
        }
    }

       
    
    public LikeDTO likePost(Long userId, Long postId) {
        Optional<Like> existingLike = likeRepository.findByUserIdAndPostId(userId, postId);
        if (existingLike.isPresent()) {
            return likeMapper.toDTO(existingLike.get()); // Already liked
        }

        User user = userRepository.findById(userId).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();

        Like like = new Like();
        like.setUser(user);
        like.setPost(post);

        return likeMapper.toDTO(likeRepository.save(like));
    }

    public void unlikePost(Long userId, Long postId) {
        likeRepository.deleteByUserIdAndPostId(userId, postId);
    }

    public int countLikes(Long postId) {
        return likeRepository.countByPostId(postId);
    }
    
}
