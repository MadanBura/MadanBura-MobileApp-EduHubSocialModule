package com.connect.eduhubconnect.dto

data class FriendRequestDTO(
    val id: Long,
    val senderId: Long,
    val receiverId: Long,
    val status: RequestStatus,
    val sentAt: String,
    val senderName : String,
    val userBio : String,
    val senderEmail:String,
    val senderProfileImage:String
)
