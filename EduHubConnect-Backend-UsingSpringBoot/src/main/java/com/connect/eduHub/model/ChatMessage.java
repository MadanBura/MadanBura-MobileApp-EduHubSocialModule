package com.connect.eduHub.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.connect.eduHub.util.LocalDateTimeDeserializer;
import com.connect.eduHub.util.MillisToLocalDateTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;      // Sender's username
    private String recipient;   // Recipient's username

    @Lob
    private String content;
    @Lob
    private String encryptedAESKey;
// Encrypted message content

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime timestamp;
}
