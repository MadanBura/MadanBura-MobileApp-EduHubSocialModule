package com.connect.eduHub.controller;

import com.connect.eduHub.dto.PostDTO;
import com.connect.eduHub.services.FileStorageService;
import com.connect.eduHub.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private FileStorageService imageService;
    
    // Create a post
//    @PostMapping("/user/{userId}")
//    public ResponseEntity<PostDTO> createPost(@PathVariable Long userId, @RequestBody PostDTO postDTO) {
//        PostDTO createdPost = postService.createPost(userId, postDTO);
//        return ResponseEntity.ok(createdPost);
//    }

    @PostMapping(value = "/user/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPost(
            @PathVariable Long userId,
            @RequestPart("content") String content,
            @RequestPart("image") MultipartFile imageFile) {

        try {
            // Save image to local filesystem and get path/URL
            String imageUrl = imageService.storeFile(imageFile);

            // Prepare DTO
            PostDTO postDTO = new PostDTO();
            postDTO.setContent(content);
            postDTO.setImageUrl(imageUrl);

            // Create post
            PostDTO createdPost = postService.createPost(userId, postDTO);

            return ResponseEntity.ok(createdPost);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to store image");
        }
    }

    
    
    // Get all posts
    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<PostDTO> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }
    
    
    @GetMapping("/user/getSelfPosts/{userId}")
    public ResponseEntity<List<PostDTO>> getPostsByUser(@PathVariable Long userId) {
        List<PostDTO> userPosts = postService.getPostsByUserId(userId);
        return ResponseEntity.ok(userPosts);
    }
    
}
