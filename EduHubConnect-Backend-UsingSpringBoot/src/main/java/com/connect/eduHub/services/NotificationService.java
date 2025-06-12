package com.connect.eduHub.services;

import com.connect.eduHub.dto.NotificationDTO;
import com.connect.eduHub.model.NOTIFICATIONTYPE;
import com.connect.eduHub.model.Notification;
import com.connect.eduHub.model.User;
import com.connect.eduHub.model.UserFcmToken;
import com.connect.eduHub.repository.NotificationRepository;
import com.connect.eduHub.repository.UserFcmTokenRepository;
import com.connect.eduHub.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private FirebaseService firebaseService;
    @Autowired
    private UserFcmTokenRepository userFcmTokenRepository;

    public ResponseEntity<String> createLikeNotification(Long userId, Long postId, Long targetUserId) throws Exception {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Notification notification = buildNotification(userId, postId, targetUserId, NOTIFICATIONTYPE.LIKED, user.getName() + " liked your post.");
        notificationRepository.save(notification);
        sendNotificationToUser(targetUserId, user.getName() + " liked your post.");
        return ResponseEntity.ok("Like notification created");
    }

    public void createCommentNotification(Long userId, Long postId, Long targetUserId, String commentContent) throws Exception {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Notification notification = buildNotification(userId, postId, targetUserId, NOTIFICATIONTYPE.COMMENTED, user.getName() + " commented on your post: \"" + commentContent + "\"");
        notification.setCommentContent(commentContent);
        notificationRepository.save(notification);
        sendNotificationToUser(targetUserId, user.getName() + " commented on your post: " + commentContent);
    }

    public void createShareNotification(Long userId, Long postId, Long targetUserId, String postContent) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Notification notification = buildNotification(userId, postId, targetUserId, NOTIFICATIONTYPE.SHARED, user.getName() + " shared your post: \"" + postContent + "\"");
        notificationRepository.save(notification);
    }

    private Notification buildNotification(Long userId, Long postId, Long targetUserId, NOTIFICATIONTYPE type, String message) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setTargetUserId(targetUserId);
        notification.setPostId(postId);
        notification.setType(type);
        notification.setMessage(message);
        notification.setRead(false);
        notification.setTimestamp(LocalDateTime.now());
        return notification;
    }

    private void sendNotificationToUser(Long targetUserId, String message) throws Exception {
        UserFcmToken userFcmToken = userFcmTokenRepository.findByUserId(targetUserId);
        if (userFcmToken != null && userFcmToken.getFcmToken() != null) {
            firebaseService.sendNotification(userFcmToken.getFcmToken(), "New Notification", message);
        }
    }

    public List<NotificationDTO> getNotifications(Long targetUserId) {
        List<Notification> notificationList = notificationRepository.findByTargetUserIdOrderByTimestampDesc(targetUserId);
        List<NotificationDTO> notificationDto = new ArrayList<>();
        User targetUser = userRepo.findById(targetUserId).orElseThrow(() -> new RuntimeException("User not found"));

        for (Notification notification : notificationList) {
            NotificationDTO dto = new NotificationDTO();
            User user = userRepo.findById(notification.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
            dto.setTargetUserId(notification.getTargetUserId());
            dto.setUserId(targetUser.getId());
            dto.setPostId(notification.getPostId());
            dto.setType(notification.getType());
            dto.setMessage(notification.getMessage());
            dto.setCommentContent(notification.getCommentContent());
            dto.setRead(notification.isRead());
            dto.setProfileImage(user.getProfileImage());
            dto.setUserName(targetUser.getName());
            dto.setTimestamp(notification.getTimestamp());
            notificationDto.add(dto);
        }

        return notificationDto;
    }
    

    public List<Notification> getUnreadNotification(Long userId) {
//      List<Notification> notificationList = notificationRepository.findAll().stream()
//          .filter(notification -> !notification.isRead())  // Correct the filter condition
//          .collect(Collectors.toList());

      return notificationRepository.findUnreadNotificationsByUserId(userId);
  }
  
}
