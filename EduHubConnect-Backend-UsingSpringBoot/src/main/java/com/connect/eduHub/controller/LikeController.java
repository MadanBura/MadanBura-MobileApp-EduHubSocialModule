package com.connect.eduHub.controller;

import com.connect.eduHub.services.LikeService;
import com.connect.eduHub.services.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;
  

    @PostMapping("/toggle")
    public ResponseEntity<String> toggleLike(
        @RequestParam Long userId,
        @RequestParam Long postId
    ) {
        try {
            // Call the service method that will toggle the like for the user and post
            String responseMessage = likeService.toggleLike(userId, postId);
            return ResponseEntity.ok(responseMessage);  // Return success response
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("An error occurred while toggling the like.");  // Handle errors
        }
    }


    @GetMapping("/count/{postId}")
    public int getLikeCount(@PathVariable Long postId) {
        return likeService.countLikes(postId);
    }
}
