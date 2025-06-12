package com.connect.eduHub.controller;

import com.connect.eduHub.dto.CommentDTO;
import com.connect.eduHub.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/add")
    public ResponseEntity<CommentDTO> addComment(
        @RequestParam Long userId,
        @RequestParam Long postId,
        @RequestBody String content
    ) {
        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null);  // Return 400 Bad Request for empty content
        }
        try {
            CommentDTO commentDTO = commentService.addComment(userId, postId, content);
            return ResponseEntity.ok(commentDTO);  // Return the created comment with 200 OK
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);  // Return 500 Internal Server Error
        }
    }

    @GetMapping("/post/{postId}")
    public List<CommentDTO> getCommentsForPost(@PathVariable Long postId, @RequestParam(required = false)Long userId) {
    	if (userId != null) {
            return commentService.getCommentsForPost(postId, userId); // filtered by friends/logic
        } else {
            return commentService.getAllCommentsForPost(postId); // return all comments
        }
    }
}
