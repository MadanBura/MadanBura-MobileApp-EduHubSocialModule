package com.connect.eduHub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.connect.eduHub.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // Fetch notifications for a particular user (ordered by timestamp, latest first)
    List<Notification> findByTargetUserIdOrderByTimestampDesc(Long targetUserId);
    
    
    @Query("SELECT n FROM Notification n WHERE n.targetUserId = :userId AND n.isRead = false ORDER BY n.timestamp DESC")
    List<Notification> findUnreadNotificationsByUserId(@Param("userId") Long userId);
}