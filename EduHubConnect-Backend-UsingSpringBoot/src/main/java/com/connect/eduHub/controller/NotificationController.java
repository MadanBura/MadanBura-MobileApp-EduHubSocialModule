package com.connect.eduHub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.connect.eduHub.dto.NotificationDTO;
import com.connect.eduHub.model.Notification;
import com.connect.eduHub.model.UserFcmToken;
import com.connect.eduHub.repository.UserFcmTokenRepository;
import com.connect.eduHub.services.FirebaseService;
import com.connect.eduHub.services.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserFcmTokenRepository userFcmTokenRepository;
    @Autowired
    private FirebaseService firebaseService;


    // Endpoint to create a like notification
    @PostMapping("/like")
    public ResponseEntity<String> likePost(@RequestParam Long userId, @RequestParam Long postId, @RequestParam Long targetUserId) {
        try {
			notificationService.createLikeNotification(userId, postId, targetUserId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return ResponseEntity.ok("Like notification created");
    }

    // Endpoint to create a comment notification
    @PostMapping("/comment")
    public ResponseEntity<String> commentOnPost(@RequestParam Long userId, @RequestParam Long postId, @RequestParam Long targetUserId, @RequestParam String commentContent) {
        try {
			notificationService.createCommentNotification(userId, postId, targetUserId, commentContent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return ResponseEntity.ok("Comment notification created");
    }

    // Endpoint to create a share notification
    @PostMapping("/share")
    public ResponseEntity<String> sharePost(@RequestParam Long userId, @RequestParam Long postId, @RequestParam Long targetUserId, @RequestParam String postContent) throws Exception {
        notificationService.createShareNotification(userId, postId, targetUserId, postContent);
        return ResponseEntity.ok("Share notification created");
    }

    // Endpoint to fetch notifications for a user
    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationDTO>> getNotifications(@PathVariable Long userId) {
        List<NotificationDTO> notifications = notificationService.getNotifications(userId);
        return ResponseEntity.ok(notifications);
    }
    
    
   
   
    @PostMapping("/saveFcmToken")
    public String saveFcmToken(@RequestParam Long userId, @RequestParam String fcmToken) {
        try {
            firebaseService.saveFcmToken(userId, fcmToken);
            return "FCM token saved successfully";
        } catch (Exception e) {
            return "Error saving FCM token: " + e.getMessage();
        }
    }
    
    
}

