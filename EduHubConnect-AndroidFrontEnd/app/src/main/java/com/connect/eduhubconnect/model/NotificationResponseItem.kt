package com.connect.eduhubconnect.model

data class NotificationResponseItem(
    val commentContent: String,
    val id: Int,
    val message: String,
    val postId: Int,
    val read: Boolean,
    val targetUserId: Int,
    val timestamp: String,
    val type: String,
    val userId: Int,
    val profileImage :String,
    val userName:String
)