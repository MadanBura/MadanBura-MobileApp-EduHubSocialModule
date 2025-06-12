package com.connect.eduhubconnect.dto

import com.connect.eduhubconnect.model.ChatMessage
import java.sql.Timestamp

data class RecentChatRes(
    val username:String,
    val userEmail:String,
    val profilePicUrl:String,
    val lastMessage: String,
    val timestamp: String
)