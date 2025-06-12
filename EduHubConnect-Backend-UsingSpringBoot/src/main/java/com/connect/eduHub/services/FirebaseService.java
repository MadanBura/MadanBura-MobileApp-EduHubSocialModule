package com.connect.eduHub.services;

import com.connect.eduHub.model.UserFcmToken;
import com.connect.eduHub.repository.UserFcmTokenRepository;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class FirebaseService {

    @Value("${app.firebase-configuration-file}")
    private String firebaseConfigPath;  // Path to the service account JSON file

    @Autowired
    private UserFcmTokenRepository userFcmTokenRepository;

    @PostConstruct
    public void initialize() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            try {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream()))
                        .build();
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase initialized successfully.");
            } catch (IOException e) {
                throw new IOException("Error initializing Firebase SDK: " + e.getMessage(), e);
            }
        }
    }

    public void sendNotification(String token, String title, String body) throws FirebaseMessagingException {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("FCM token is null or empty");
        }

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent message: " + response);
        } catch (FirebaseMessagingException e) {
            handleFirebaseError(token, e);
            throw e;
        }
    }

    private void handleFirebaseError(String token, FirebaseMessagingException e) {
        System.err.println("FCM error code: " + e.getErrorCode());
        if ("registration-token-not-registered".equals(e.getErrorCode()) || "invalid-argument".equals(e.getErrorCode())) {
            System.err.println("Deleting invalid FCM token from DB: " + token);
            userFcmTokenRepository.deleteByFcmToken(token);
        }
    }
    

    public void saveFcmToken(Long userId, String fcmToken) {
        // Check if the token already exists in the database
        UserFcmToken existingToken = userFcmTokenRepository.findByUserId(userId);
        if (existingToken != null) {
            // If the token exists, update it
            existingToken.setFcmToken(fcmToken);
            userFcmTokenRepository.save(existingToken);
        } else {
            // If it's a new token, save it as a new entry
            UserFcmToken newToken = new UserFcmToken();
            newToken.setUserId(userId);
            newToken.setFcmToken(fcmToken);
            userFcmTokenRepository.save(newToken);
        }
    }
}
