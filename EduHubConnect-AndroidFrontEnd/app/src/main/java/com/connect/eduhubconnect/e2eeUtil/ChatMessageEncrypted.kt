package com.connect.eduhubconnect.e2eeUtil

data class ChatMessageEncrypted(
    val sender: String,
    val recipient: String,
    val encryptedMessage: String,
    val encryptedAesKey: String,
    val timestamp: String
)