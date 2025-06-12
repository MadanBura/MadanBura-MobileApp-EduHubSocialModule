package com.connect.eduHub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.connect.eduHub.model.UserFcmToken;

@Repository
public interface UserFcmTokenRepository extends JpaRepository<UserFcmToken, Long> {

    // Find by userId (optional, to associate tokens with specific users)
    UserFcmToken findByUserId(Long userId);
    void deleteByFcmToken(String fcmToken);

}
