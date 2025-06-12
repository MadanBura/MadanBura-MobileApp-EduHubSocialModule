package com.connect.eduHub.e2ee;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ChatMessageEncrypted {
	private String sender;
	private String recipient;
	private String content;
	private String encryptedAESKey;
	private LocalDateTime timestamp;
}