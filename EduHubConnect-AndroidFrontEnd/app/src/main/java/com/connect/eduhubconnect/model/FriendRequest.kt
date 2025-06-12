package com.connect.eduhubconnect.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "friend_requests")
data class FriendRequest(
    @PrimaryKey val id: Long,
    val senderId: Long,
    val receiverId: Long,
    val status: String,
    val sentAt: String,
    val senderName: String,
    val userBio: String,
    val senderEmail: String,
    val senderProfileImage: String
)
