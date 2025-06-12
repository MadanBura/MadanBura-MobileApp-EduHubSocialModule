package com.connect.eduHub.services;

import com.connect.eduHub.dto.PostDTO;
import com.connect.eduHub.mappers.PostMapper;
import com.connect.eduHub.model.Post;
import com.connect.eduHub.model.User;
import com.connect.eduHub.repository.PostRepository;
import com.connect.eduHub.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired private PostRepository postRepository;
    @Autowired private UserRepo userRepository;
    @Autowired private PostMapper postMapper;

    public PostDTO createPost(Long userId, PostDTO postDTO) {
        User user = userRepository.findById(userId).orElseThrow();
        Post post = postMapper.toEntity(postDTO);
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now());
        return postMapper.toDTO(postRepository.save(post));
    }

    public List<PostDTO> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    
    public List<PostDTO> getFeedPosts(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        
        Set<User> users = new HashSet<>(user.getFriends());
        users.add(user); // include own posts

        return postRepository.findByUserIn(users)
                             .stream()
                             .map(postMapper::toDTO)
                             .collect(Collectors.toList());
    }
    
    
    public List<PostDTO> getPostsByUserId(Long userId) {
        List<Post> posts = postRepository.findPostsByUserId(userId);
        return posts.stream()
                    .map(postMapper::toDTO)
                    .collect(Collectors.toList());
    }

    
}