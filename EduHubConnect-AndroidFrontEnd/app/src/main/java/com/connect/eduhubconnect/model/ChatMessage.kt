package com.connect.eduhubconnect.model

import java.time.LocalDateTime

data class ChatMessage(
//    val id: Long? = null,                // Unique message ID (nullable and auto-generated on the backend)
    val sender: String,                 // Sender's username
    val recipient: String,              // Recipient's username
    val content: String,                // Message content (could be encrypted)
    val timestamp: String// Timestamp of when the message was sent
)